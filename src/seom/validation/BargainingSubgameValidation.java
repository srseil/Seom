package seom.validation;

import ec.util.MersenneTwisterFast;
import seom.Configuration;
import seom.Result;
import seom.games.BargainingSubgame;
import seom.learning.ImitateAverage;
import seom.learning.ImitateBest;
import seom.learning.ImitateProbability;
import seom.learning.LearningRule;
import seom.networks.Lattice2DBuilder;
import seom.utils.*;
import seom.utils.tuples.Pair;
import seom.utils.tuples.Triple;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// This class replicates the experiment mentioned in The Structural Evolution of Morality, p. 170-173.
// It is unclear how many agents were chosen. Here, a lattice of 32x32 with wrap around was chosen.
public class BargainingSubgameValidation {
    public final static long BASE_SEED = 19561831;
    public final static int NUM_REPETITIONS = 10000;

    private final Configuration config;
    private final Map<Triple<Lattice2DBuilder.Neighborhood, Integer, LearningRule>, Integer> resultMap;

    List<Pair<Lattice2DBuilder.Neighborhood, Integer>> neighborhoods = List.of(
        Pair.of(Lattice2DBuilder.Neighborhood.VonNeumann, 1),
        Pair.of(Lattice2DBuilder.Neighborhood.Moore, 1),
        Pair.of(Lattice2DBuilder.Neighborhood.Moore, 2)
    );
    List<LearningRule> learningRules = List.of(
        new ImitateProbability(),
        new ImitateBest(),
        new ImitateAverage()
    );

    public BargainingSubgameValidation() {
        config = new Configuration();
        config.setGame(new BargainingSubgame());
        config.setMutationProbability(0.0);
        config.setInitialMoralMean(0.5);
        config.setMaxNumGenerations(1000);
        config.setCycleDetectionEnabled(true);
        resultMap = new HashMap<>();
    }

    public void simulate() {
        for (Pair<Lattice2DBuilder.Neighborhood, Integer> neighborhood : neighborhoods) {
            for (LearningRule learningRule : learningRules) {
                simulate(neighborhood.getFirst(), neighborhood.getSecond(), learningRule);
            }
        }
    }

    public void writeResultsToCSV(String filePath) {
        try (var writer = new PrintWriter(filePath)) {
            writer.println("Neighborhood;Interaction Distance;Learning Rule;5-Polymorphism;Other;");
            for (Pair<Lattice2DBuilder.Neighborhood, Integer> neighborhood : neighborhoods) {
                for (LearningRule learningRule : learningRules) {
                    var key = Triple.of(neighborhood.getFirst(), neighborhood.getSecond(), learningRule);
                    writer.print(neighborhood.getFirst() + ";");
                    writer.print(neighborhood.getSecond() + ";");
                    writer.print(learningRule + ";");
                    writer.print(resultMap.get(key) + ";");
                    writer.print(NUM_REPETITIONS - resultMap.get(key) + ";");
                    writer.println();
                }
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public Map<Triple<Lattice2DBuilder.Neighborhood, Integer, LearningRule>, Integer> getResultMap() {
        return resultMap;
    }

    private void simulate(Lattice2DBuilder.Neighborhood neighborhood,
                          int interactionDistance, LearningRule learningRule) {
        for (int i = 0; i < NUM_REPETITIONS; i++) {
            var random = new MersenneTwisterFast(BASE_SEED + i);

            Timer.start();
            var graph = new Lattice2DBuilder()
                .setNeighborhood(neighborhood)
                .setInteractionDistance(interactionDistance)
                .setLearningDistance(1)
                .setWrapAround(true)
                .setWidth(32)
                .setHeight(32)
                .create();
            config.setRandom(random);
            config.setNetwork(graph);
            config.setLearningRule(learningRule);
            Result result = SimulationRunner.run(config);
            Timer.stop();

            if (result.getMorality() == 1.0) {
                var key = Triple.of(neighborhood, interactionDistance, learningRule);
                int count = resultMap.getOrDefault(key, 0);
                resultMap.put(key, count + 1);
            }

            Log.info(
                "(neighborhood: " + neighborhood + ", interaction distance: " + interactionDistance
                    + ", learning rule: " + learningRule + ") [" + (i + 1) + "/" + NUM_REPETITIONS + "]"
                    + " ... " + Timer.duration().toMillis() + "ms"
            );
        }
    }
}
