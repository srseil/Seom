package seom.networks;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.Agent;

import java.util.*;

public class NetworkUtils {
    public static UndirectedSparseGraph<Agent, InteractionEdge>
    getInteractionGraph(UndirectedSparseMultigraph<Agent, Edge> graph) {
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

    public static UndirectedSparseGraph<Agent, LearningEdge>
    getLearningGraph(UndirectedSparseMultigraph<Agent, Edge> graph) {
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

    public static UndirectedSparseGraph<Agent, Edge>
    getGenericInteractionGraph(UndirectedSparseMultigraph<Agent, Edge> graph) {
        var interactionGraph = new UndirectedSparseGraph<Agent, Edge>();
        for (Agent agent : graph.getVertices()) {
            interactionGraph.addVertex(agent);
        }
        for (InteractionEdge edge : getInteractionEdges(graph)) {
            Pair<Agent> endpoints = graph.getEndpoints(edge);
            interactionGraph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
        }
        return interactionGraph;
    }

    public static UndirectedSparseGraph<Agent, Edge>
    getGenericLearningGraph(UndirectedSparseMultigraph<Agent, Edge> graph) {
        var learningGraph = new UndirectedSparseGraph<Agent, Edge>();
        for (Agent agent : graph.getVertices()) {
            learningGraph.addVertex(agent);
        }
        for (LearningEdge edge : getLearningEdges(graph)) {
            Pair<Agent> endpoints = graph.getEndpoints(edge);
            learningGraph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
        }
        return learningGraph;
    }

    public static List<InteractionEdge> getInteractionEdges(UndirectedSparseMultigraph<Agent, Edge> graph) {
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

    public static boolean isConnected(Graph<Agent, ? extends Edge> graph) {
        Agent root = graph.getVertices().toArray(Agent[]::new)[0];
        var agentSet = new HashSet<Agent>();
        var queue = new ArrayDeque<Agent>();
        queue.add(root);
        while (!queue.isEmpty()) {
            Agent agent = queue.pop();
            if (agentSet.add(agent)) {
                queue.addAll(graph.getNeighbors(agent));
            }
        }
        return agentSet.size() == graph.getVertexCount();
    }

    public static Agent[]
    getDistanceNeighborhood(Agent agent, int distance, UndirectedSparseGraph<Agent, ? extends Edge> graph) {
        var neighbors = new HashSet<Agent>();
        neighbors.add(agent);
        collectDistanceNeighborhood(agent, distance, graph, neighbors);
        neighbors.remove(agent);
        return neighbors.toArray(Agent[]::new);
    }

    public static UndirectedSparseMultigraph<Agent, Edge>
    createFullGraph(int learningDistance, UndirectedSparseGraph<Agent, InteractionEdge> interactionGraph) {
        var learningGraph = new UndirectedSparseGraph<Agent, LearningEdge>();
        for (Agent agent : interactionGraph.getVertices()) {
            addLearningEdges(learningDistance, agent, agent, interactionGraph, learningGraph);
        }

        var fullGraph = new UndirectedSparseMultigraph<Agent, Edge>();
        for (Agent agent : interactionGraph.getVertices()) {
            fullGraph.addVertex(agent);
        }
        for (InteractionEdge edge : interactionGraph.getEdges()) {
            Pair<Agent> endpoints = interactionGraph.getEndpoints(edge);
            fullGraph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
        }

        for (Agent agent : learningGraph.getVertices()) {
            fullGraph.addVertex(agent);
        }
        for (LearningEdge edge : learningGraph.getEdges()) {
            Pair<Agent> endpoints = learningGraph.getEndpoints(edge);
            fullGraph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
        }

        return fullGraph;
    }

    private static void collectDistanceNeighborhood(Agent agent, int distance,
                                                    UndirectedSparseGraph<Agent, ? extends Edge> graph,
                                                    Set<Agent> neighbors) {
        if (distance == 0) return;

        var nextNeighbors = new ArrayList<Agent>();
        for (Agent neighbor : graph.getNeighbors(agent)) {
            if (neighbors.add(neighbor)) {
                nextNeighbors.add(neighbor);
            }
        }

        for (Agent next : nextNeighbors) {
            collectDistanceNeighborhood(next, distance - 1, graph, neighbors);
        }
    }

    private static void
    addLearningEdges(int steps, Agent agent, Agent hub,
                     UndirectedSparseGraph<Agent, InteractionEdge> interactionGraph,
                     UndirectedSparseGraph<Agent, LearningEdge> learningGraph) {
        for (InteractionEdge interactionEdge : interactionGraph.getOutEdges(hub)) {
            Pair<Agent> endpoints = interactionGraph.getEndpoints(interactionEdge);
            Agent hubNeighbor = endpoints.getFirst() == hub ? endpoints.getSecond() : endpoints.getFirst();
            if (agent != hubNeighbor) {
                learningGraph.addEdge(new LearningEdge(), agent, hubNeighbor);
            }
        }

        if (steps > 1) {
            for (Agent neighbor : interactionGraph.getNeighbors(hub)) {
                addLearningEdges(steps - 1, agent, neighbor, interactionGraph, learningGraph);
            }
        }
    }
}
