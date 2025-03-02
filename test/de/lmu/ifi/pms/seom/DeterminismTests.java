package de.lmu.ifi.pms.seom;

import de.lmu.ifi.pms.seom.games.PrisonersDilemma;
import de.lmu.ifi.pms.seom.networks.BoundedDegreeBuilder;
import de.lmu.ifi.pms.seom.utils.SimulationRunner;
import ec.util.MersenneTwisterFast;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import de.lmu.ifi.pms.seom.learning.ImitateBest;

public class DeterminismTests {
    private final static int MAX_NUM_GENERATIONS = 100;

    @Test
    public void simulateDeterministically() {
        Configuration config1 = createConfig(new MersenneTwisterFast(12345678));
        Configuration config2 = createConfig(new MersenneTwisterFast(12345678));

        Result result1 = SimulationRunner.run(config1);
        Result result2 = SimulationRunner.run(config2);

        Assertions.assertEquals(result1.getStrategyHistory().size(), result2.getStrategyHistory().size());
        for (int i = 0; i < MAX_NUM_GENERATIONS; i++) {
            Assertions.assertArrayEquals(
                result1.getStrategyHistory().get(i),
                result2.getStrategyHistory().get(i)
            );
        }
    }

    private Configuration createConfig(MersenneTwisterFast random) {
        var config = new Configuration();
        config.setGame(new PrisonersDilemma());
        config.setLearningRule(new ImitateBest());
        config.setNetwork(new BoundedDegreeBuilder(random)
            .setNumAgents(20)
            .setLearningDistance(2)
            .setMinDegree(2)
            .setMaxDegree(4)
            .create()
        );
        config.setInitialMoralMean(0.5);
        config.setMutationProbability(1.0);
        config.setMutationDistance(0);
        config.setHomogeneityDetectionEnabled(false);
        config.setCycleDetectionEnabled(false);
        config.setMaxNumGenerations(MAX_NUM_GENERATIONS);
        config.setRandom(random);
        return config;
    }
}
