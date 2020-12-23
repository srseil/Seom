package seom.analysis;

import ec.util.MersenneTwisterFast;
import org.reflections.Reflections;
import seom.Configuration;
import seom.Result;
import seom.analysis.parameters.MutationDistance;
import seom.analysis.parameters.MutationProbability;
import seom.analysis.parameters.Parameter;
import seom.analysis.parameters.PopulationSize;
import seom.games.BargainingSubgame;
import seom.games.PrisonersDilemma;
import seom.games.StagHunt;
import seom.games.UltimatumSubgame;
import seom.networks.*;
import seom.utils.Log;
import seom.utils.RecordTable;
import seom.utils.SimulationRunner;
import seom.utils.Timer;

import java.io.File;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class SensitivityAnalysis {
    private final static DecimalFormat decimalFormat = new DecimalFormat(
        "#0.0###", DecimalFormatSymbols.getInstance(Locale.ENGLISH));

    public final static long BASE_SEED = 19561831;
    public final static int NUM_REPETITIONS = 10;
    public final static boolean CYCLE_DETECTION_ENABLED = true;
    public final static int MAX_NUM_GENERATIONS = 1000;

    private final GameType gameType;
    private final NetworkType networkType;
    private final ArrayList<Parameter<?>> parameters;
    private final Map<Parameter<?>, RecordTable> resultTables;

    public SensitivityAnalysis(GameType gameType, NetworkType networkType) {
        this.gameType = gameType;
        this.networkType = networkType;
        resultTables = new HashMap<>();

        var reflections = new Reflections("seom.analysis.parameters");
        var parameterClasses = reflections.getSubTypesOf(Parameter.class);
        parameters = new ArrayList<>();
        for (var parameterClass : parameterClasses) {
            try {
                Parameter<?> parameter = parameterClass.getConstructor().newInstance();
                if (parameter.isApplicable(gameType, networkType)) {
                    parameters.add(parameter);
                    setupResultTable(parameter);
                }
            } catch (Exception e) {
                e.printStackTrace();
                assert false;
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public Parameter[] getParameters() {
        return parameters.toArray(Parameter[]::new);
    }

    public void writeResultTables(String baseDirectory) {
        String directory = Path.of(baseDirectory, gameType.toString(), networkType.toString()).toString();
        var dir = new File(directory);
        assert dir.exists() || dir.mkdirs() : "Could not create directories: " + directory;

        for (var entry : resultTables.entrySet()) {
            if (entry.getValue().isEmpty()) continue;
            String fileName = entry.getKey().getName().replaceAll("\\s+", "_") + ".csv";
            entry.getValue().writeCSVTo(Path.of(directory, fileName).toString());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void simulate() {
        Log.info("Nominal parameter values:");
        for (Parameter parameter : parameters) {
            Log.info(parameter.getName() + " = " + parameter.getValueName(parameter.getNominal()));
        }
        Log.info("");

        Timer.start();
        for (Parameter parameter : parameters) {
            simulate(parameter);
            Log.info("");
        }
        Timer.stop();

        Log.info("");
        Log.info("Simulation of all parameters finished ... " + Timer.duration().toMillis() + "ms");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void simulate(String parameterName) {
        Log.info("Nominal parameter values:");
        for (Parameter parameter : parameters) {
            Log.info(parameter.getName() + " = " + parameter.getValueName(parameter.getNominal()));
        }
        Log.info("");

        Parameter chosen = null;
        for (Parameter parameter : parameters) {
            if (parameter.getName().equalsIgnoreCase(parameterName)) {
                chosen = parameter;
                break;
            }
        }
        assert chosen != null : "Could not find parameter with name '" + parameterName + "'";

        Timer.start();
        simulate(chosen);
        Timer.stop();

        Log.info("");
        Log.info("Simulation parameter '" + parameterName + "' finished ... " + Timer.duration().toMillis() + "ms");
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void simulate(Parameter parameter) {
        var config = new Configuration();
        config.setCycleDetectionEnabled(CYCLE_DETECTION_ENABLED);
        config.setMaxNumGenerations(MAX_NUM_GENERATIONS);
        setConfigGame(config, gameType);
        setConfigNetworkBuilder(config, networkType);

        // Set all parameters to nominal values
        for (Parameter other : parameters) {
            other.apply(other.getNominal(), config);
        }

        if (!applySpecialParameterCases(parameter, config)) return;

        // Set individual parameter values
        for (var value : parameter.getValues()) {
            if (!applySpecialValueCases(parameter, value, config)) continue;

            parameter.apply(value, config);
            Log.info(parameter.getName() + " = " + parameter.getValueName(value));
            Result[] results = simulate(config);

            for (int i = 0; i < NUM_REPETITIONS; i++) {
                storeResults(parameter, value, results[i], BASE_SEED + i);
            }
        }
    }

    private Result[] simulate(Configuration config) {
        Result[] results = new Result[NUM_REPETITIONS];
        for (int i = 0; i < NUM_REPETITIONS; i++) {
            var random = new MersenneTwisterFast(BASE_SEED + i);
            config.setRandom(random);

            Timer.start();
            config.setNetwork(config.getNetworkBuilder().create());
            var generationTime = Timer.stop();

            Timer.start();
            Result result = SimulationRunner.run(config);
            var simulationTime = Timer.stop();

            Log.info("(stability: " + decimalFormat.format(result.getStability())
                + ", morality: " + decimalFormat.format(result.getMorality()) + ")"
                + " [" + (i+1) + "/" + NUM_REPETITIONS + "]"
                + " ... " + (generationTime.toMillis() + simulationTime.toMillis()) + "ms"
                + " (network: " + generationTime.toMillis() + "ms, simulation: " + simulationTime.toMillis() + "ms)");

            results[i] = result;
        }
        return results;
    }

    private boolean applySpecialParameterCases(Parameter<?> parameter, Configuration config) {
        // Mutation probability has different nominal value for mutation distance parameter
        if (parameter instanceof MutationDistance) {
            Optional<Parameter<?>> param = parameters.stream()
                .filter(x -> x instanceof MutationProbability).findFirst();
            if (param.isPresent()) {
                MutationProbability mutationProbability = (MutationProbability) param.get();
                mutationProbability.apply(0.01, config);
                return true;
            }
        }

        return true;
    }

    private boolean applySpecialValueCases(Parameter<?> parameter, Object value, Configuration config) {
        // RandomNetwork's edge probabilities are too small for population count of 100
        // Additionally, a population size of 10000 consumes too much memory
        if (parameter instanceof PopulationSize && networkType == NetworkType.RandomNetwork) {
            PopulationSize populationSize = (PopulationSize) parameter;
            if ((int) value != populationSize.getNominal()) return false;
        }

        // For the FullyConnectedNetwork, a population size of 10000 consumes too much memory
        if (parameter instanceof PopulationSize && networkType == NetworkType.FullyConnected) {
            PopulationSize populationSize = (PopulationSize) parameter;
            if ((int) value == 10000) return false;
        }

        return true;
    }

    private void setupResultTable(Parameter<?> parameter) {
        var table = new RecordTable();
        table.addColumn(parameter.getName());
        table.addColumn("stability");
        table.addColumn("morality");
        table.addColumn("cycle length");
        table.addColumn("generation count");
        table.addColumn("seed");
        resultTables.put(parameter, table);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void storeResults(Parameter parameter, Object value, Result result, long seed) {
        RecordTable table = resultTables.get(parameter);
        table.addRecord(
            parameter.getValueName(value),
            decimalFormat.format(result.getStability()),
            decimalFormat.format(result.getMorality()),
            result.isCyclic() ? result.getCycleLength() : 0,
            result.getGenerationCount(),
            seed
        );

    }

    private void setConfigGame(Configuration config, GameType gameType) {
        switch (gameType) {
            case PrisonersDilemma: config.setGame(new PrisonersDilemma()); break;
            case StagHunt: config.setGame(new StagHunt()); break;
            case BargainingSubgame: config.setGame(new BargainingSubgame()); break;
            case UltimatumGame: config.setGame(new UltimatumSubgame()); break;
        }
    }

    private void setConfigNetworkBuilder(Configuration config, NetworkType networkType) {
        switch (networkType) {
            case FullyConnected: config.setNetworkBuilder(new FullyConnectedNetworkBuilder()); break;
            case Lattice1D: config.setNetworkBuilder(new Lattice1DBuilder()); break;
            case Lattice2D: config.setNetworkBuilder(new Lattice2DBuilder()); break;
            case SmallWorld1D: config.setNetworkBuilder(new SmallWorld1DBuilder()); break;
            case SmallWorld2D: config.setNetworkBuilder(new SmallWorld2DBuilder()); break;
            case BoundedDegree: config.setNetworkBuilder(new BoundedDegreeNetworkBuilder()); break;
            case RandomNetwork: config.setNetworkBuilder(new RandomNetworkBuilder()); break;
        }
    }
}
