package seom;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.games.Payoffs;
import seom.networks.InteractionEdge;
import seom.networks.LearningEdge;
import seom.networks.NetworkUtils;
import sim.engine.SimState;
import sim.engine.Steppable;

public class Interactions implements Steppable {
    private final Configuration config;
    private final MersenneTwisterFast random;
    private final UndirectedSparseGraph<Agent, InteractionEdge> interactionGraph;
    private final UndirectedSparseGraph<Agent, LearningEdge> learningGraph;


    public Interactions(Configuration config) {
        this.config = config;
        random = new MersenneTwisterFast();
        interactionGraph = NetworkUtils.getInteractionGraph(config.getNetwork());
        learningGraph = NetworkUtils.getLearningGraph(config.getNetwork());
    }

    @Override
    public void step(SimState simState) {
        System.out.println("Interactions");

        for (InteractionEdge edge : interactionGraph.getEdges()) {
            Pair<Agent> agents = interactionGraph.getEndpoints(edge);
            Agent agent0 = agents.getFirst();
            Agent agent1 = agents.getSecond();

            Payoffs payoffs = config.getGame().play(agent0.getStrategy(), agent1.getStrategy());
            agent0.increaseScore(payoffs.getPayoffForPlayer(0));
            agent1.increaseScore(payoffs.getPayoffForPlayer(1));
        }

        if (sampleUpdateProbability()) {
            for (Agent agent : learningGraph.getVertices()) {
                config.getLearningRule().updateStrategy(agent, learningGraph.getNeighbors(agent), config.getGame());
            }

            for (Agent agent : learningGraph.getVertices()) {
                agent.resetScore();
            }
        }
    }

    private boolean sampleUpdateProbability() {
        if (config.getLearningProbability() == 1.0) {
            return true;
        } else {
            double rand = random.nextDouble();
            return rand < config.getLearningProbability();
        }
    }
}
