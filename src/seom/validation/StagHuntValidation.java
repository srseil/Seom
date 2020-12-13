package seom.validation;

import ec.util.MersenneTwisterFast;
import seom.Configuration;
import seom.Result;
import seom.games.Payoffs;
import seom.games.StagHunt;
import seom.learning.ImitateBest;
import seom.networks.BoundedDegreeNetworkBuilder;
import seom.utils.Log;
import seom.utils.Range;
import seom.utils.SimulationRunner;
import seom.utils.Timer;

import java.util.HashMap;
import java.util.Map;

// This class replicates the experiment mentioned in The Structural Evolution of Morality, p. 137-138.
// It is unclear how many agents were used. Here, the number 100 was chosen.
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
            var graph = new BoundedDegreeNetworkBuilder(random)
                .setLearningDistance(learningDistance)
                .setNumAgents(100)
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
