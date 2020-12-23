package seom;

import ec.util.MersenneTwisterFast;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seom.games.PrisonersDilemma;
import seom.learning.ImitateBest;
import seom.networks.Lattice1DBuilder;
import seom.processes.Stability;

import java.util.ArrayList;
import java.util.Comparator;

public class StabilityTests {
    @Test
    public void detectHomogenous() {
        Configuration config = createBasicConfig();
        config.setMaxNumGenerations(1000);
        config.setCycleDetectionEnabled(false);
        var simulation = new Simulation(config);
        var stability = new Stability(simulation);
        stability.setHomogeneityDetectionEnabled(true);
        simulation.start();
        var sortedAgents = new ArrayList<>(config.getNetwork().getVertices());
        sortedAgents.sort(Comparator.comparingInt(Agent::getId));

        sortedAgents.get(0).setStrategy(PrisonersDilemma.Strategy.Cooperate);
        sortedAgents.get(1).setStrategy(PrisonersDilemma.Strategy.Defect);
        sortedAgents.get(2).setStrategy(PrisonersDilemma.Strategy.Cooperate);
        stability.step(null);
        Assertions.assertFalse(simulation.schedule.scheduleComplete());

        sortedAgents.get(0).setStrategy(PrisonersDilemma.Strategy.Cooperate);
        sortedAgents.get(1).setStrategy(PrisonersDilemma.Strategy.Cooperate);
        sortedAgents.get(2).setStrategy(PrisonersDilemma.Strategy.Cooperate);
        stability.step(null);
        Assertions.assertTrue(simulation.schedule.scheduleComplete());

        Assertions.assertTrue(simulation.getResult().isCyclic());
        Assertions.assertEquals(1, simulation.getResult().getCycleLength());
        Assertions.assertEquals(2, simulation.getResult().getCycleStart());
        Assertions.assertEquals(2, simulation.getResult().getCycleEnd());
    }

    @Test
    public void detectGenerationLimit() {
        Configuration config = createBasicConfig();
        config.setMaxNumGenerations(2);
        config.setCycleDetectionEnabled(false);
        var simulation = new Simulation(config);
        var stability = new Stability(simulation);
        simulation.start();
        var sortedAgents = new ArrayList<>(config.getNetwork().getVertices());
        sortedAgents.sort(Comparator.comparingInt(Agent::getId));

        sortedAgents.get(0).setStrategy(PrisonersDilemma.Strategy.Cooperate);
        sortedAgents.get(1).setStrategy(PrisonersDilemma.Strategy.Defect);
        sortedAgents.get(2).setStrategy(PrisonersDilemma.Strategy.Cooperate);
        stability.step(null);
        Assertions.assertFalse(simulation.schedule.scheduleComplete());

        sortedAgents.get(0).setStrategy(PrisonersDilemma.Strategy.Defect);
        sortedAgents.get(1).setStrategy(PrisonersDilemma.Strategy.Cooperate);
        sortedAgents.get(2).setStrategy(PrisonersDilemma.Strategy.Defect);
        stability.step(null);
        Assertions.assertTrue(simulation.schedule.scheduleComplete());

        Assertions.assertFalse(simulation.getResult().isCyclic());
    }

    @Test
    public void detectCycle() {
        Configuration config = createBasicConfig();
        config.setMaxNumGenerations(1000);
        config.setCycleDetectionEnabled(true);
        var simulation = new Simulation(config);
        var stability = new Stability(simulation);
        stability.setCycleDetectionEnabled(true);
        simulation.start();
        var sortedAgents = new ArrayList<>(config.getNetwork().getVertices());
        sortedAgents.sort(Comparator.comparingInt(Agent::getId));

        sortedAgents.get(0).setStrategy(PrisonersDilemma.Strategy.Cooperate);
        sortedAgents.get(1).setStrategy(PrisonersDilemma.Strategy.Defect);
        sortedAgents.get(2).setStrategy(PrisonersDilemma.Strategy.Cooperate);
        stability.step(null);
        Assertions.assertFalse(simulation.schedule.scheduleComplete());

        sortedAgents.get(0).setStrategy(PrisonersDilemma.Strategy.Defect);
        sortedAgents.get(1).setStrategy(PrisonersDilemma.Strategy.Cooperate);
        sortedAgents.get(2).setStrategy(PrisonersDilemma.Strategy.Defect);
        stability.step(null);
        Assertions.assertFalse(simulation.schedule.scheduleComplete());

        sortedAgents.get(0).setStrategy(PrisonersDilemma.Strategy.Cooperate);
        sortedAgents.get(1).setStrategy(PrisonersDilemma.Strategy.Defect);
        sortedAgents.get(2).setStrategy(PrisonersDilemma.Strategy.Cooperate);
        stability.step(null);
        Assertions.assertTrue(simulation.schedule.scheduleComplete());

        Assertions.assertTrue(simulation.getResult().isCyclic());
        Assertions.assertEquals(2, simulation.getResult().getCycleLength());
        Assertions.assertEquals(1, simulation.getResult().getCycleStart());
        Assertions.assertEquals(2, simulation.getResult().getCycleEnd());
    }

    private Configuration createBasicConfig() {
        var random = new MersenneTwisterFast(12345678);
        var config = new Configuration();
        config.setRandom(random);
        config.setGame(new PrisonersDilemma());
        config.setLearningRule(new ImitateBest());
        config.setMutationProbability(0.0);
        var graph = new Lattice1DBuilder()
            .setLength(3)
            .setLearningDistance(1)
            .setInteractionDistance(1)
            .setWrapAround(true)
            .create();
        config.setNetwork(graph);
        return config;
    }
}
