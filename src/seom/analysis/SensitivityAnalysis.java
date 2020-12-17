package seom.analysis;

import ec.util.MersenneTwisterFast;
import org.reflections.Reflections;
import seom.Configuration;
import seom.Result;
import seom.analysis.parameters.MutationDistance;
import seom.analysis.parameters.MutationProbability;
import seom.analysis.parameters.Parameter;
import seom.games.BargainingSubgame;
import seom.games.PrisonersDilemma;
import seom.games.StagHunt;
import seom.games.UltimatumSubgame;
import seom.networks.*;
import seom.utils.Log;
import seom.utils.SimulationRunner;
import seom.utils.Timer;

import java.util.ArrayList;

public class SensitivityAnalysis {
    public final static long BASE_SEED = 19561831;
    public final static int NUM_REPETITIONS = 10;
    public final static boolean CYCLE_DETECTION_ENABLED = true;
    public final static int MAX_NUM_GENERATIONS = 1000;

    private final GameType gameType;
    private final NetworkType networkType;
    private final ArrayList<Parameter<?>> parameters;
    private MutationProbability mutationProbability;

    public SensitivityAnalysis(GameType gameType, NetworkType networkType) {
        this.gameType = gameType;
        this.networkType = networkType;

        var reflections = new Reflections("seom.analysis.parameters");
        var parameterClasses = reflections.getSubTypesOf(Parameter.class);
        parameters = new ArrayList<>();
        for (var parameterClass : parameterClasses) {
            try {
                Parameter<?> parameter = parameterClass.getConstructor().newInstance();
                if (parameter.isApplicable(gameType, networkType)) {
                    parameters.add(parameter);
                }
            } catch (Exception e) {
                e.printStackTrace();
                assert false;
            }
        }

        mutationProbability = null;
        for (Parameter<?> parameter : parameters) {
            if (parameter instanceof MutationProbability) {
                mutationProbability = (MutationProbability) parameter;
                break;
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public Parameter[] getParameters() {
        return parameters.toArray(Parameter[]::new);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void simulate() {
        Timer.start();
        for (Parameter parameter : parameters) {
            simulate(parameter);
        }
        Timer.stop();
        Log.info("Simulation of all parameters finished ... " + Timer.duration().toMillis() + "ms");
    }

    @SuppressWarnings("rawtypes")
    public void simulate(String parameterName) {
        for (Parameter parameter : parameters) {
            if (parameter.getName().equalsIgnoreCase(parameterName)) {
                simulate(parameter);
                return;
            }
        }
        assert false : "Could not find parameter with name '" + parameterName + "'";
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

        // Mutation probability has different nominal value for mutation distance parameter
        if (parameter instanceof MutationDistance && mutationProbability != null) {
            mutationProbability.apply(0.01, config);
        }

        // Set individual parameter values
        for (var value : parameter.getValues()) {
            parameter.apply(value, config);
            Log.info(parameter.getName() + " = " + parameter.getValueName(value));
            simulate(config);
        }
    }

    private void simulate(Configuration config) {
        for (int i = 0; i < NUM_REPETITIONS; i++) {
            var random = new MersenneTwisterFast(BASE_SEED + i);
            config.setRandom(random);

            Timer.start();
            config.setNetwork(config.getNetworkBuilder().create());
            var generationTime = Timer.stop();

            Timer.start();
            Result result = SimulationRunner.run(config);
            var simulationTime = Timer.stop();

            Log.info("(stability: " + result.getStability() + ", morality: " + result.getMorality() + ")"
                + " [" + (i+1) + "/" + NUM_REPETITIONS + "]"
                + " ... " + (generationTime.toMillis() + simulationTime.toMillis()) + "ms"
                + " (network: " + generationTime.toMillis() + "ms, simulation: " + simulationTime.toMillis() + "ms)");
        }
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
