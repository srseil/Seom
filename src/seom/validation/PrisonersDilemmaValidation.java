package seom.validation;

import ec.util.MersenneTwisterFast;
import seom.Configuration;
import seom.Result;
import seom.games.Payoffs;
import seom.games.PrisonersDilemma;
import seom.learning.ImitateBest;
import seom.networks.BoundedDegreeBuilder;
import seom.utils.Log;
import seom.utils.SimulationRunner;
import seom.utils.Timer;
import seom.utils.tuples.Vector3;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This class replicates the experiment mentioned in The Structural Evolution of Morality, p. 86-87.
// It is unclear which learning rule was used. Here, ImitateBest was chosen.
public class PrisonersDilemmaValidation {
    public final static long BASE_SEED = 19561831;
    public final static int NUM_REPETITIONS = 10000;

    private final Configuration config;
    private final Map<Vector3<Integer>, Integer> resultMap;

    public PrisonersDilemmaValidation() {
        config = new Configuration();
        config.setGame(new PrisonersDilemma(
            new Payoffs(0.666, 0.666),
            new Payoffs(0.0, 1.0),
            new Payoffs(1.0, 0.0),
            new Payoffs(0.333, 0.333)
        ));
        config.setLearningRule(new ImitateBest());
        config.setMutationProbability(0.0);
        config.setInitialMoralMean(0.5);
        config.setMaxNumGenerations(1000);
        config.setCycleDetectionEnabled(true);
        resultMap = new HashMap<>();
    }

    public void simulate() {
        resultMap.clear();
        for (int n : List.of(15, 30, 60)) {
            for (int min = 1; min <= 9; min++) {
                for (int max = 3; max <= 10; max++) {
                    if (min >= max) continue;
                    resultMap.put(Vector3.of(n, min, max), 0);
                    simulate(n, min, max);
                }
            }
        }
    }

    public void writeResultsToCSV(String filePath) {
        try (var writer = new PrintWriter(filePath)) {
            writer.print(";;");
            for (int max = 3; max <= 10; max++) {
                writer.print(max + ";");
            }
            writer.println();

            for (int n : List.of(15, 30, 60)) {
                for (int min = 1; min <= 9; min++) {
                    writer.print(n + ";" + min + ";");
                    for (int max = 3; max <= 10; max++) {
                        if (min >= max) {
                            writer.print("-;");
                        } else {
                            writer.print(resultMap.get(Vector3.of(n, min, max)) + ";");
                        }
                    }
                    writer.println();
                }
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public Map<Vector3<Integer>, Integer> getResultMap() {
        return resultMap;
    }

    private void simulate(int numAgents, int minDegree, int maxDegree) {
        for (int i = 0; i < NUM_REPETITIONS; i++) {
            var random = new MersenneTwisterFast(BASE_SEED + i);

            Timer.start();
            var graph = new BoundedDegreeBuilder(random)
                .setLearningDistance(1)
                .setNumAgents(numAgents)
                .setMinDegree(minDegree)
                .setMaxDegree(maxDegree)
                .create();
            config.setRandom(random);
            config.setNetwork(graph);
            Result result = SimulationRunner.run(config);
            Timer.stop();

            if (result.getMorality() == 0.0) {
                var key = Vector3.of(numAgents, minDegree, maxDegree);
                int allDefectCount = resultMap.get(key);
                resultMap.put(key, allDefectCount + 1);
            }

            Log.info(
                "(num agents: " + numAgents + ", min degree: " + minDegree + ", max degree: " + maxDegree
                    + ") [" + (i + 1) + "/" + NUM_REPETITIONS + "]"
                    + " ... " + Timer.duration().toMillis() + "ms"
            );
        }
    }
}
