package de.lmu.ifi.pms.seom.validation;

import de.lmu.ifi.pms.seom.utils.Log;
import de.lmu.ifi.pms.seom.utils.Range;
import de.lmu.ifi.pms.seom.utils.SimulationRunner;
import de.lmu.ifi.pms.seom.utils.Timer;
import ec.util.MersenneTwisterFast;
import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.Result;
import de.lmu.ifi.pms.seom.games.Payoffs;
import de.lmu.ifi.pms.seom.games.StagHunt;
import de.lmu.ifi.pms.seom.learning.ImitateBest;
import de.lmu.ifi.pms.seom.networks.BoundedDegreeBuilder;

import java.util.HashMap;
import java.util.Map;

// This class replicates the experiment mentioned in The Structural Evolution of Morality, p. 137-138.
// The experiment should be executed for the learning distances 1 and 2.
public class StagHuntValidation {
    public final static long BASE_SEED = 19561831;
    public final static int NUM_REPETITIONS = 10000;

    private final Configuration config;
    private final Map<Range, Integer> resultMap;

    public StagHuntValidation() {
        config = new Configuration();
        config.setGame(new StagHunt(
            new Payoffs(3.0, 3.0),
            new Payoffs(0.0, 2.0),
            new Payoffs(2.0, 0.0),
            new Payoffs(2.0, 2.0)
        ));
        config.setLearningRule(new ImitateBest());
        config.setMutationProbability(0.0);
        config.setInitialMoralMean(0.5);
        config.setMaxNumGenerations(1000);
        config.setCycleDetectionEnabled(true);
        resultMap = new HashMap<>();
    }

    public void simulate(int learningDistance) {
        resultMap.clear();
        resultMap.put(Range.from(1.0).to(1.0), 0);
        resultMap.put(Range.from(0.9).toExclusive(1.0), 0);
        resultMap.put(Range.from(0.8).toExclusive(0.9), 0);
        resultMap.put(Range.from(0.7).toExclusive(0.8), 0);
        resultMap.put(Range.from(0.6).toExclusive(0.7), 0);
        resultMap.put(Range.from(0.5).toExclusive(0.6), 0);
        resultMap.put(Range.from(0.4).toExclusive(0.5), 0);
        resultMap.put(Range.from(0.3).toExclusive(0.4), 0);
        resultMap.put(Range.from(0.2).toExclusive(0.3), 0);
        resultMap.put(Range.from(0.1).toExclusive(0.2), 0);
        resultMap.put(Range.fromExclusive(0.0).toExclusive(0.1), 0);
        resultMap.put(Range.from(0.0).to(0.0), 0);

        for (int i = 0; i < NUM_REPETITIONS; i++) {
            var random = new MersenneTwisterFast(BASE_SEED + i);

            Timer.start();
            var graph = new BoundedDegreeBuilder(random)
                .setLearningDistance(learningDistance)
                .setNumAgents(40)
                .setMinDegree(2)
                .setMaxDegree(4)
                .create();
            config.setRandom(random);
            config.setNetwork(graph);
            Result result = SimulationRunner.run(config);
            Timer.stop();

            for (Range range : resultMap.keySet()) {
                if (range.contains(result.getMorality())) {
                    int numResults = resultMap.get(range);
                    resultMap.put(range, numResults + 1);
                    break;
                }
            }

            Log.info("(learning distance: " + learningDistance + ") [" + (i + 1) + "/" + NUM_REPETITIONS + "]" + " ... " + Timer.duration().toMillis() + "ms");
        }
    }

    public Map<Range, Integer> getResultMap() {
        return resultMap;
    }
}
