package seom;

import ec.util.MersenneTwisterFast;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seom.games.BargainingSubgame;
import seom.games.PrisonersDilemma;
import seom.games.StagHunt;
import seom.games.UltimatumSubgame;
import seom.learning.ImitateBest;
import seom.networks.Lattice1DBuilder;
import seom.processes.Initialization;
import seom.processes.Interactions;
import seom.processes.Learning;
import seom.processes.Mutation;

import java.util.ArrayList;
import java.util.Collections;

public class ProcessTests {
    @Test
    public void executeInitialization() {
        var random = new MersenneTwisterFast(12345678);
        var config = new Configuration();
        config.setRandom(random);
        config.setGame(new BargainingSubgame());
        var graph = new Lattice1DBuilder()
            .setLength(100)
            .setLearningDistance(1)
            .setInteractionDistance(1)
            .setWrapAround(true)
            .create();
        config.setNetwork(graph);
        config.setInitialMoralMean(0.5);

        var initialization = new Initialization(config);
        initialization.step(null);
        for (Agent agent : graph.getVertices()) {
            Assertions.assertTrue(agent.getStrategy() == BargainingSubgame.Strategy.Demand4
                || agent.getStrategy() == BargainingSubgame.Strategy.Demand5
                || agent.getStrategy() == BargainingSubgame.Strategy.Demand6);
        }
    }

    @Test
    public void executeInteractions() {
        var config = new Configuration();
        var graph = new Lattice1DBuilder()
            .setLength(3)
            .setLearningDistance(1)
            .setInteractionDistance(1)
            .setWrapAround(true)
            .create();
        config.setNetwork(graph);
        config.setGame(new PrisonersDilemma());

        Agent[] agents = graph.getVertices().toArray(Agent[]::new);
        agents[0].setStrategy(PrisonersDilemma.Strategy.Cooperate);
        agents[1].setStrategy(PrisonersDilemma.Strategy.Defect);
        agents[2].setStrategy(PrisonersDilemma.Strategy.Cooperate);

        var interactions = new Interactions(config);
        interactions.step(null);
        Assertions.assertEquals(2, agents[0].getScore());
        Assertions.assertEquals(6, agents[1].getScore());
        Assertions.assertEquals(2, agents[2].getScore());
    }

    @Test
    public void executeLearning() {
        var config = new Configuration();
        config.setGame(new StagHunt());
        var graph = new Lattice1DBuilder()
            .setLength(3)
            .setLearningDistance(1)
            .setInteractionDistance(1)
            .setWrapAround(true)
            .create();
        config.setNetwork(graph);
        config.setLearningRule(new ImitateBest());

        Agent[] agents = graph.getVertices().toArray(Agent[]::new);
        agents[0].setStrategy(StagHunt.Strategy.Hare);
        agents[0].increaseScore(2);
        agents[1].setStrategy(StagHunt.Strategy.Stag);
        agents[1].increaseScore(0);
        agents[2].setStrategy(StagHunt.Strategy.Hare);
        agents[2].increaseScore(2);

        var learning = new Learning(config);
        learning.step(null);
        Assertions.assertEquals(StagHunt.Strategy.Hare, agents[0].getStrategy());
        Assertions.assertEquals(StagHunt.Strategy.Hare, agents[1].getStrategy());
        Assertions.assertEquals(StagHunt.Strategy.Hare, agents[2].getStrategy());
    }

    @Test
    public void executeMutation() {
        // one in the middle below 0.2
        var random = new MersenneTwisterFast(12345678);
        var config = new Configuration();
        config.setRandom(random);
        config.setGame(new UltimatumSubgame());
        var graph = new Lattice1DBuilder()
            .setLength(5)
            .setLearningDistance(1)
            .setInteractionDistance(1)
            .setWrapAround(true)
            .create();
        config.setNetwork(graph);
        config.setMutationProbability(0.1);
        config.setMutationDistance(1);

        var sortedAgents = new ArrayList<>(config.getNetwork().getVertices());
        Collections.sort(sortedAgents);
        sortedAgents.get(0).setStrategy(UltimatumSubgame.Strategy.S7Fairman);
        sortedAgents.get(1).setStrategy(UltimatumSubgame.Strategy.S7Fairman);
        sortedAgents.get(2).setStrategy(UltimatumSubgame.Strategy.S7Fairman);
        sortedAgents.get(3).setStrategy(UltimatumSubgame.Strategy.S7Fairman);
        sortedAgents.get(4).setStrategy(UltimatumSubgame.Strategy.S7Fairman);

        var mutation = new Mutation(config);
        mutation.step(null);
        Assertions.assertNotEquals(UltimatumSubgame.Strategy.S7Fairman, sortedAgents.get(0).getStrategy());
        Assertions.assertEquals(UltimatumSubgame.Strategy.S7Fairman, sortedAgents.get(1).getStrategy());
        Assertions.assertEquals(UltimatumSubgame.Strategy.S7Fairman, sortedAgents.get(2).getStrategy());
        Assertions.assertNotEquals(UltimatumSubgame.Strategy.S7Fairman, sortedAgents.get(3).getStrategy());
        Assertions.assertNotEquals(UltimatumSubgame.Strategy.S7Fairman, sortedAgents.get(4).getStrategy());
    }
}
