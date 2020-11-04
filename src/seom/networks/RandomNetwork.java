package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import seom.Agent;

public class RandomNetwork extends UndirectedSparseGraph<Agent, Edge> {
    public RandomNetwork(int numAgents, double edgeProbability, MersenneTwisterFast random) {
        assert numAgents > 1 : "The number of agents must be at least 2";
        assert edgeProbability >= 0 && edgeProbability <= 1.0 : "Edge probability must be in [0,1]";

        for (int i = 0; i < numAgents; i++) {
            addVertex(new Agent());
        }

        for (Agent agent : getVertices()) {
            for (Agent other : getVertices()) {
                if (agent == other) continue;

                double rand = random.nextDouble();
                if (rand < edgeProbability) {
                    addEdge(new InteractionEdge(), agent, other);
                }
            }
        }
    }
}
