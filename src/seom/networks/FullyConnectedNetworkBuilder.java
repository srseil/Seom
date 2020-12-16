package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.Agent;

public class FullyConnectedNetworkBuilder implements NetworkBuilder {
    private int numAgents;

    @Override
    public void setRandom(MersenneTwisterFast random) {
    }

    @Override
    public UndirectedSparseMultigraph<Agent, Edge> create() {
        UndirectedSparseGraph<Agent, InteractionEdge> interactionGraph = createInteractionGraph();

        var learningGraph = new UndirectedSparseGraph<Agent, LearningEdge>();
        for (Agent agent : interactionGraph.getVertices()) {
            learningGraph.addVertex(agent);
        }
        for (Agent agent : learningGraph.getVertices()) {
            for (Agent other : learningGraph.getVertices()) {
                if (agent == other) continue;
                learningGraph.addEdge(new LearningEdge(), agent, other);
            }
        }

        var fullGraph = new UndirectedSparseMultigraph<Agent, Edge>();
        for (Agent agent : interactionGraph.getVertices()) {
            fullGraph.addVertex(agent);
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

    UndirectedSparseGraph<Agent, InteractionEdge> createInteractionGraph() {
        assert numAgents > 1 : "The number of agents must be at least 2";

        var interactionGraph = new UndirectedSparseGraph<Agent, InteractionEdge>();
        for (int i = 0; i < numAgents; i++) {
            interactionGraph.addVertex(new Agent());
        }
        for (Agent agent : interactionGraph.getVertices()) {
            for (Agent other : interactionGraph.getVertices()) {
                if (agent == other) continue;
                interactionGraph.addEdge(new InteractionEdge(), agent, other);
            }
        }

        return interactionGraph;
    }

    public FullyConnectedNetworkBuilder setNumAgents(int numAgents) {
        this.numAgents = numAgents;
        return this;
    }
}
