package seom.networks;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.Agent;

public class FullyConnectedNetworkBuilder {
    private int numAgents;

    public UndirectedSparseMultigraph<Agent, Edge> create() {
        assert numAgents > 1 : "The number of agents must be at least 2";

        var fullGraph = new UndirectedSparseMultigraph<Agent, Edge>();
        for (int i = 0; i < numAgents; i++) {
            fullGraph.addVertex(new Agent());
        }

        var interactionGraph = new UndirectedSparseGraph<Agent, InteractionEdge>();
        for (Agent agent : fullGraph.getVertices()) {
            interactionGraph.addVertex(agent);
        }
        for (Agent agent : interactionGraph.getVertices()) {
            for (Agent other : interactionGraph.getVertices()) {
                if (agent == other) continue;
                interactionGraph.addEdge(new InteractionEdge(), agent, other);
            }
        }

        var learningGraph = new UndirectedSparseGraph<Agent, LearningEdge>();
        for (Agent agent : fullGraph.getVertices()) {
            learningGraph.addVertex(agent);
        }
        for (Agent agent : learningGraph.getVertices()) {
            for (Agent other : learningGraph.getVertices()) {
                if (agent == other) continue;
                learningGraph.addEdge(new LearningEdge(), agent, other);
            }
        }

        for (InteractionEdge interactionEdge : interactionGraph.getEdges()) {
            Pair<Agent> endpoints = interactionGraph.getEndpoints(interactionEdge);
            fullGraph.addEdge(interactionEdge, endpoints.getFirst(), endpoints.getSecond());
        }
        for (LearningEdge learningEdge : learningGraph.getEdges()) {
            Pair<Agent> endpoints = learningGraph.getEndpoints(learningEdge);
            fullGraph.addEdge(learningEdge, endpoints.getFirst(), endpoints.getSecond());
        }

        return fullGraph;
    }

    public FullyConnectedNetworkBuilder setNumAgents(int numAgents) {
        this.numAgents = numAgents;
        return this;
    }
}
