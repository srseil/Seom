package seom;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.games.Payoffs;
import sim.engine.SimState;
import sim.engine.Steppable;

public class Interactions implements Steppable {
    private final Configuration config;
    private final MersenneTwisterFast random;

    public Interactions(Configuration config) {
        this.config = config;
        random = new MersenneTwisterFast();
    }

    @Override
    public void step(SimState simState) {
        System.out.println("Interactions");

        Graph<Agent, Relationship> network = config.getNetwork();

        for (Relationship edge : network.getEdges()) {
            Pair<Agent> agents = network.getEndpoints(edge);
            Agent agent0 = agents.getFirst();
            Agent agent1 = agents.getSecond();

            Payoffs payoffs = config.getGame().play(agent0.getStrategy(), agent1.getStrategy());
            agent0.increaseScore(payoffs.getPayoffForPlayer(0));
            agent1.increaseScore(payoffs.getPayoffForPlayer(1));
        }

        if (sampleUpdateProbability()) {
            for (Agent agent : network.getVertices()) {
                config.getLearningRule().updateStrategy(agent, network.getNeighbors(agent), config.getGame());
            }

            for (Agent agent : network.getVertices()) {
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
