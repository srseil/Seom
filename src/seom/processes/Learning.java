package seom.processes;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import seom.Agent;
import seom.Configuration;
import seom.networks.LearningEdge;
import seom.networks.NetworkUtils;
import sim.engine.SimState;
import sim.engine.Steppable;

public class Learning implements Steppable {
    private final Configuration config;
    private final UndirectedSparseGraph<Agent, LearningEdge> learningGraph;

    public Learning(Configuration config) {
        this.config = config;
        learningGraph = NetworkUtils.getLearningGraph(config.getNetwork());
    }

    @Override
    public void step(SimState simState) {
        System.out.println("Learning");

        for (Agent agent : learningGraph.getVertices()) {
            config.getLearningRule().updateStrategy(agent, learningGraph.getNeighbors(agent), config.getGame());
        }

        for (Agent agent : learningGraph.getVertices()) {
            agent.resetScore();
        }
    }
}
