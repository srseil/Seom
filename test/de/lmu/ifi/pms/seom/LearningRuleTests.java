package de.lmu.ifi.pms.seom;

import de.lmu.ifi.pms.seom.games.*;
import ec.util.MersenneTwisterFast;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import de.lmu.ifi.pms.seom.learning.BestResponse;
import de.lmu.ifi.pms.seom.learning.ImitateAverage;
import de.lmu.ifi.pms.seom.learning.ImitateBest;
import de.lmu.ifi.pms.seom.learning.ImitateProbability;

import java.util.Arrays;

public class LearningRuleTests {
    @Test
    public void useImitateBest() {
        var learningRule = new ImitateBest();
        var game = new PrisonersDilemma();

        var agent = new Agent();
        agent.setStrategy(PrisonersDilemma.Strategy.Cooperate);

        var neighbors = new Agent[] {
            new Agent(),
            new Agent()
        };

        neighbors[0].setStrategy(PrisonersDilemma.Strategy.Defect);
        neighbors[0].increaseScore(2.0);

        neighbors[1].setStrategy(PrisonersDilemma.Strategy.Cooperate);
        neighbors[1].increaseScore(1.0);

        Strategy strategy = learningRule.getUpdatedStrategy(agent, Arrays.asList(neighbors.clone()), game);

        Assertions.assertEquals(PrisonersDilemma.Strategy.Defect, strategy);
    }

    @Test
    public void useImitateProbability() {
        var learningRule = new ImitateProbability();
        // This seed results in .nextDouble() producing ~0.248
        learningRule.setRandom(new MersenneTwisterFast(12345678));
        var game = new StagHunt();

        var agent = new Agent();
        agent.setStrategy(StagHunt.Strategy.Hare);

        var neighbors = new Agent[] {
            new Agent(),
            new Agent()
        };

        neighbors[0].setStrategy(StagHunt.Strategy.Stag);
        neighbors[0].increaseScore(1.0);

        neighbors[1].setStrategy(StagHunt.Strategy.Hare);
        neighbors[1].increaseScore(3.0);

        Strategy strategy = learningRule.getUpdatedStrategy(agent, Arrays.asList(neighbors.clone()), game);

        Assertions.assertEquals(StagHunt.Strategy.Stag, strategy);
    }

    @Test
    public void useImitateAverage() {
        var learningRule = new ImitateAverage();
        var game = new BargainingSubgame();

        var agent = new Agent();
        agent.setStrategy(BargainingSubgame.Strategy.Demand4);

        var neighbors = new Agent[] {
            new Agent(),
            new Agent(),
            new Agent(),
            new Agent()
        };

        neighbors[0].setStrategy(BargainingSubgame.Strategy.Demand4);
        neighbors[0].increaseScore(2.0);

        neighbors[1].setStrategy(BargainingSubgame.Strategy.Demand5);
        neighbors[1].increaseScore(1.0);

        neighbors[2].setStrategy(BargainingSubgame.Strategy.Demand5);
        neighbors[2].increaseScore(5.0);

        neighbors[3].setStrategy(BargainingSubgame.Strategy.Demand6);
        neighbors[3].increaseScore(3.0);

        Strategy strategy = learningRule.getUpdatedStrategy(agent, Arrays.asList(neighbors.clone()), game);

        // Tie-break in order of strategies as defined in the enum
        Assertions.assertEquals(BargainingSubgame.Strategy.Demand5, strategy);
    }

    @Test
    public void useBestResponse() {
        var learningRule = new BestResponse();
        var game = new UltimatumSubgame();

        var agent = new Agent();
        agent.setStrategy(UltimatumSubgame.Strategy.S7Fairman);
        agent.increaseScore(15);

        var neighbors = new Agent[] {
            new Agent(),
            new Agent()
        };

        neighbors[0].setStrategy(UltimatumSubgame.Strategy.S1Gamesman);
        neighbors[1].setStrategy(UltimatumSubgame.Strategy.S5EasyRider);

        Strategy strategy = learningRule.getUpdatedStrategy(agent, Arrays.asList(neighbors.clone()), game);

        // Counterfactual total payoffs are (S7Fairman: 15, S1Gamesman: 32, S5EasyRider: 16)
        Assertions.assertEquals(UltimatumSubgame.Strategy.S1Gamesman, strategy);
    }
}
