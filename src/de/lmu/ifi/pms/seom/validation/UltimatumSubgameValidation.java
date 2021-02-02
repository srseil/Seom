package de.lmu.ifi.pms.seom.validation;

import de.lmu.ifi.pms.seom.utils.Log;
import de.lmu.ifi.pms.seom.utils.SimulationRunner;
import de.lmu.ifi.pms.seom.utils.Timer;
import ec.util.MersenneTwisterFast;
import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.Result;
import de.lmu.ifi.pms.seom.games.Strategy;
import de.lmu.ifi.pms.seom.games.UltimatumSubgame;
import de.lmu.ifi.pms.seom.learning.ImitateBest;
import de.lmu.ifi.pms.seom.networks.BoundedDegreeBuilder;
import de.lmu.ifi.pms.seom.utils.tuples.Triple;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

// This class replicates the experiment mentioned in The Structural Evolution of Morality, p. 231-233.
public class UltimatumSubgameValidation {
    public final static long BASE_SEED = 19561831;
    public final static int NUM_REPETITIONS = 1000;

    private final Configuration config;
    private final Map<Triple<Integer, Integer, Boolean>, Integer> resultMap;

    public UltimatumSubgameValidation() {
        config = new Configuration();
        config.setGame(new UltimatumSubgame());
        config.setLearningRule(new ImitateBest());
        config.setInitialMoralMean(0.5);
        config.setMutationDistance(0);
        config.setMaxNumGenerations(1000);
        config.setCycleDetectionEnabled(true);
        resultMap = new HashMap<>();
    }

    public void simulate(double mutationProbability) {
        resultMap.clear();
        for (int min = 2; min <= 9; min++) {
            for (int max = 3; max <= 10; max++) {
                if (min >= max) continue;
                simulate(min, max, mutationProbability);
            }
        }
    }

    public void writeResultsToCSV(String filePath) {
        try (var writer = new PrintWriter(filePath)) {
            writer.print(";");
            for (int max = 3; max <= 10; max++) {
                writer.print(max + ";");
            }
            writer.println();

            for (int min = 2; min <= 9; min++) {
                writer.print(min + ";");
                for (int max = 3; max <= 10; max++) {
                    if (min >= max) {
                        writer.print("-;");
                    } else {
                        var moralCount = resultMap.getOrDefault(Triple.of(min, max, true), 0);
                        var immoralCount = resultMap.getOrDefault(Triple.of(min, max, false), 0);
                        writer.print("(" + immoralCount + " : " + moralCount + ");");
                    }
                }
                writer.println();
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }
    }

    public Map<Triple<Integer, Integer, Boolean>, Integer> getResultMap() {
        return resultMap;
    }

    private void simulate(int minDegree, int maxDegree, double mutationProbability) {
        for (int i = 0; i < NUM_REPETITIONS; i++) {
            var random = new MersenneTwisterFast(BASE_SEED + i);

            Timer.start();
            var graph = new BoundedDegreeBuilder(random)
                .setLearningDistance(1)
                .setNumAgents(50)
                .setMinDegree(minDegree)
                .setMaxDegree(maxDegree)
                .create();
            config.setRandom(random);
            config.setNetwork(graph);
            config.setMutationProbability(mutationProbability);
            Result result = SimulationRunner.run(config);
            Timer.stop();

            boolean moralPolymorphism = true;
            boolean immoralPolymorphism = true;
            Map<Strategy, Integer> strategyMap = result.getStrategyMap();
            for (var entry : strategyMap.entrySet()) {
                var strategy = entry.getKey();
                if (entry.getValue() > 0 && strategy != UltimatumSubgame.Strategy.S7Fairman
                    && strategy != UltimatumSubgame.Strategy.S5EasyRider) {
                    moralPolymorphism = false;
                }
                if (entry.getValue() > 0 && strategy != UltimatumSubgame.Strategy.S1Gamesman
                    && strategy != UltimatumSubgame.Strategy.S4MadDog) {
                    immoralPolymorphism = false;
                }
            }

            if (moralPolymorphism) {
                var key = Triple.of(minDegree, maxDegree, true);
                int count = resultMap.getOrDefault(key, 0);
                resultMap.put(key, count + 1);
            } else if (immoralPolymorphism) {
                var key = Triple.of(minDegree, maxDegree, false);
                int count = resultMap.getOrDefault(key, 0);
                resultMap.put(key, count + 1);
            }

            Log.info(
                "(min degree: " + minDegree + ", max degree: " + maxDegree
                    + ", mutation probability: " + mutationProbability
                    + ") [" + (i + 1) + "/" + NUM_REPETITIONS + "]"
                    + " ... " + Timer.duration().toMillis() + "ms"
            );
        }
    }
}
