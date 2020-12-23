package seom.processes;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import seom.Agent;
import seom.Configuration;
import seom.games.Strategy;
import seom.learning.ImitateProbability;
import seom.networks.LearningEdge;
import seom.networks.NetworkUtils;
import seom.utils.Log;
import sim.engine.SimState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Learning implements SimulationProcess {
    private final Configuration config;
    private final UndirectedSparseGraph<Agent, LearningEdge> learningGraph;

    public Learning(Configuration config) {
        this.config = config;
        learningGraph = NetworkUtils.getLearningGraph(config.getNetwork());
    }

    @Override
    public boolean isStochastic() {
        return config.getLearningRule() instanceof ImitateProbability;
    }

    @Override
    public void reset() {
    }

    @Override
    public void step(SimState simState) {
        Log.fine("Learning");

        var newStrategies = new HashMap<Agent, Strategy>();
        var sortedAgents = new ArrayList<>(config.getNetwork().getVertices());
        Collections.sort(sortedAgents);
        for (Agent agent : sortedAgents) {
            Strategy newStrategy = config.getLearningRule().getUpdatedStrategy(
                agent, learningGraph.getNeighbors(agent), config.getGame());
            newStrategies.put(agent, newStrategy);
        }

        for (Agent agent : learningGraph.getVertices()) {
            agent.resetScore();
            agent.setStrategy(newStrategies.get(agent));
        }
    }
}
