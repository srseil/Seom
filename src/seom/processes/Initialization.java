package seom.processes;

import seom.Agent;
import seom.Configuration;
import seom.games.Strategy;
import sim.engine.SimState;

import java.util.ArrayList;
import java.util.Comparator;

public class Initialization implements SimulationProcess {
    private final Configuration config;

    public Initialization(Configuration config) {
        this.config = config;
    }

    @Override
    public boolean isStochastic() {
        // Even though it is randomized, the initial distribution of
        // strategies is not considered to be a stochastic process.
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public void step(SimState simState) {
        System.out.println("Initialization");

        Strategy[] strategies = config.getGame().getStrategies();
        var sortedAgents = new ArrayList<>(config.getNetwork().getVertices());
        sortedAgents.sort(Comparator.comparingInt(Agent::getId));

        for (Agent agent : sortedAgents) {
            int rand = config.getRandom().nextInt(strategies.length);
            agent.setStrategy(strategies[rand]);
        }
    }
}
