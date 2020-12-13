package seom.processes;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.Agent;
import seom.Configuration;
import seom.games.Payoffs;
import seom.networks.InteractionEdge;
import seom.networks.NetworkUtils;
import seom.utils.Log;
import sim.engine.SimState;

public class Interactions implements SimulationProcess {
    private final Configuration config;
    private final UndirectedSparseGraph<Agent, InteractionEdge> interactionGraph;

    public Interactions(Configuration config) {
        this.config = config;
        interactionGraph = NetworkUtils.getInteractionGraph(config.getNetwork());
    }

    @Override
    public boolean isStochastic() {
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public void step(SimState simState) {
        Log.fine("Interactions");

        for (InteractionEdge edge : interactionGraph.getEdges()) {
            Pair<Agent> agents = interactionGraph.getEndpoints(edge);
            Agent agent0 = agents.getFirst();
            Agent agent1 = agents.getSecond();

            Payoffs payoffs = config.getGame().play(agent0.getStrategy(), agent1.getStrategy());
            agent0.increaseScore(payoffs.getPayoffForPlayer(0));
            agent1.increaseScore(payoffs.getPayoffForPlayer(1));

            if (config.getGame().isSequential()) {
                Payoffs reversePayoffs = config.getGame().play(agent1.getStrategy(), agent0.getStrategy());
                agent1.increaseScore(reversePayoffs.getPayoffForPlayer(0));
                agent0.increaseScore(reversePayoffs.getPayoffForPlayer(1));
            }
        }
    }
}
