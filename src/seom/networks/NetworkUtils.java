package seom.networks;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.Agent;

import java.util.ArrayList;
import java.util.List;

public class NetworkUtils {
    public static Graph<Agent, InteractionEdge> getInteractionGraph(Graph<Agent, Edge> graph) {
        var interactionGraph = new UndirectedSparseGraph<Agent, InteractionEdge>();
        for (Agent agent : graph.getVertices()) {
            interactionGraph.addVertex(agent);
        }
        for (InteractionEdge edge : getInteractionEdges(graph)) {
            Pair<Agent> endpoints = graph.getEndpoints(edge);
            interactionGraph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
        }
        return interactionGraph;
    }

    public static Graph<Agent, LearningEdge> getLearningGraph(Graph<Agent, Edge> graph) {
        var learningGraph = new UndirectedSparseGraph<Agent, LearningEdge>();
        for (Agent agent : graph.getVertices()) {
            learningGraph.addVertex(agent);
        }
        for (LearningEdge edge : getLearningEdges(graph)) {
            Pair<Agent> endpoints = graph.getEndpoints(edge);
            learningGraph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
        }
        return learningGraph;
    }

    public static List<InteractionEdge> getInteractionEdges(Graph<Agent, Edge> graph) {
        var interactionEdges = new ArrayList<InteractionEdge>();
        for (Edge edge : graph.getEdges()) {
            if (edge instanceof InteractionEdge) {
                interactionEdges.add((InteractionEdge) edge);
            }
        }
        return interactionEdges;
    }

    public static List<LearningEdge> getLearningEdges(Graph<Agent, Edge> graph) {
        var learningEdges = new ArrayList<LearningEdge>();
        for (Edge edge : graph.getEdges()) {
            if (edge instanceof LearningEdge) {
                learningEdges.add((LearningEdge) edge);
            }
        }
        return learningEdges;
    }

    public static void addLearningEdges(int steps, Graph<Agent, Edge> graph) {
        for (Agent agent : graph.getVertices()) {
            addLearningEdges(steps, agent, agent, graph);
        }
    }

    private static void addLearningEdges(int steps, Agent agent, Agent hub, Graph<Agent, Edge> graph) {
        var learningEdges = new ArrayList<Pair<Agent>>();
        for (Edge interactionEdge : graph.getOutEdges(hub)) {
            if (!(interactionEdge instanceof InteractionEdge)) continue;

            Pair<Agent> endpoints = graph.getEndpoints(interactionEdge);
            Agent hubNeighbor = endpoints.getFirst() == hub ? endpoints.getSecond() : endpoints.getFirst();
            learningEdges.add(new Pair<>(agent, hubNeighbor));
        }

        if (steps > 1) {
            for (Agent neighbor : graph.getNeighbors(hub)) {
                addLearningEdges(steps - 1, agent, neighbor, graph);
            }
        }

        for (Pair<Agent> pair : learningEdges) {
            graph.addEdge(new LearningEdge(), pair.getFirst(), pair.getSecond());
        }
    }
}
