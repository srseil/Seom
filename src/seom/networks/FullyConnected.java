package seom.networks;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import seom.Agent;
import seom.Relationship;

public class FullyConnected extends UndirectedSparseGraph<Agent, Relationship> {
    public FullyConnected(int numAgents) {
        assert numAgents > 1 : "The number of agents must be at least 2";

        for (int i = 0; i < numAgents; i++) {
            addVertex(new Agent());
        }

        for (Agent agent : getVertices()) {
            for (Agent other : getVertices()) {
                if (agent == other) continue;
                addEdge(new Relationship(), agent, other);
            }
        }
    }
}
