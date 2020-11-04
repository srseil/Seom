package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.Agent;
import seom.utils.JavaRandomFacade;

import java.util.*;

public class BoundedDegree extends UndirectedSparseGraph<Agent, Edge> {
    private final MersenneTwisterFast random;
    private final JavaRandomFacade javaRandom;
    private final Agent[] stubs;

    public BoundedDegree(int numAgents, int minDegree, int maxDegree, MersenneTwisterFast random) {
        assert numAgents > 0 : "Number of agents must be larger than one";
        assert minDegree <= maxDegree : "Minimum degree must not be larger than maximum degree";
        assert numAgents > minDegree : "Number of agents must be larger than minimum degree";
        assert minDegree != maxDegree || (numAgents * minDegree) % 2 == 0
            : "If the minimum and maximum degrees are equal, (degree * numAgents) must be even";

        this.random = random;
        javaRandom = new JavaRandomFacade(random);
        stubs = new Agent[maxDegree];

        //noinspection StatementWithEmptyBody
        while (!tryCreateNetwork(numAgents, minDegree, maxDegree)) ;

        for (Agent agent : getVertices()) {
            int neighborCount = getNeighborCount(agent);
            assert neighborCount >= minDegree : "Graph contains node with degree smaller than minimum bound";
            assert neighborCount <= maxDegree : "Graph contains node with degree larger than maximum bound";
        }

        for (Edge edge : getEdges()) {
            Pair<Agent> endpoints = getEndpoints(edge);
            assert endpoints.getFirst() != endpoints.getSecond() : "Graph contains loop edge";
        }
    }

    private boolean tryCreateNetwork(int numAgents, int minDegree, int maxDegree) {
        var configGraph = new UndirectedSparseMultigraph<Agent, Edge>();

        // Stubs are implemented as edges to pre-defined stub agents
        for (int i = 0; i < maxDegree; i++) {
            stubs[i] = new Agent();
            configGraph.addVertex(stubs[i]);
        }

        // Create vertices with stub edges according to randomly chosen degree
        Agent[] agents = new Agent[numAgents];
        for (int i = 0; i < numAgents; i++) {
            agents[i] = new Agent();
            configGraph.addVertex(agents[i]);

            int degree = minDegree + random.nextInt(maxDegree - minDegree + 1);
            for (int j = 0; j < degree; j++) {
                configGraph.addEdge(new InteractionEdge(), agents[i], stubs[j]);
            }
        }

        // Ensure that the sum of degrees is even
        if (configGraph.getEdgeCount() % 2 == 1) {
            // Try to remove random stub
            boolean removed = false;
            for (Agent agent : getShuffledNodesWithoutStubs(configGraph)) {
                if (configGraph.getNeighborCount(agent) > minDegree) {
                    Edge edge = (Edge) configGraph.getOutEdges(agent).toArray()[0];
                    configGraph.removeEdge(edge);
                    removed = true;
                    break;
                }
            }
            if (!removed) {
                // Add random stub
                for (Agent agent : getShuffledNodesWithoutStubs(configGraph)) {
                    if (configGraph.getNeighborCount(agent) < maxDegree) {
                        configGraph.addEdge(new InteractionEdge(), agent, stubs[maxDegree - 1]);
                    }
                }
            }
        }

        // Randomly connect stubs
        Edge[] shuffledEdges = getShuffledEdges(configGraph);
        for (int i = 0; i < shuffledEdges.length; i += 2) {
            Pair<Agent> endpoints1 = configGraph.getEndpoints(shuffledEdges[i]);
            Pair<Agent> endpoints2 = configGraph.getEndpoints(shuffledEdges[i + 1]);
            configGraph.removeEdge(shuffledEdges[i]);
            configGraph.removeEdge(shuffledEdges[i + 1]);
            configGraph.addEdge(new InteractionEdge(), endpoints1.getFirst(), endpoints2.getFirst());
        }

        // Collect loops and duplicate edges
        List<Edge> unwantedEdges = new ArrayList<>();
        Set<Pair<Agent>> edgeSet = new HashSet<>();
        for (Edge edge : configGraph.getEdges()) {
            Pair<Agent> endpoints = configGraph.getEndpoints(edge);
            if (endpoints.getFirst() == endpoints.getSecond()) {
                unwantedEdges.add(edge);
            }

            // Fix order of pair elements for equals() to work
            if (endpoints.getFirst().getId() > endpoints.getSecond().getId()) {
                endpoints = new Pair<>(endpoints.getSecond(), endpoints.getFirst());
            }

            if (!edgeSet.add(endpoints)) {
                unwantedEdges.add(edge);
            }
        }

        // Attempt to remove all loops and duplicate edges
        for (Edge unwantedEdge : unwantedEdges) {
            if (!configGraph.containsEdge(unwantedEdge)) continue;

            boolean removed = false;
            for (Edge edge : getShuffledEdges(configGraph)) {
                if (edge == unwantedEdge) continue;

//                if (tryReplaceLoopTriple(edge, unwantedEdge, configGraph)) {
//                    removed = true;
//                    break;
//                }

                if (tryReplaceDuplicateEdge(edge, unwantedEdge, configGraph)) {
                    removed = true;
                    break;
                }
            }

            if (!removed) {
                System.out.println("Could not remove unwanted edge, attempting network construction from scratch");
                return false;
            }
        }

        // Remove stubs
        for (Agent stub : stubs) {
            configGraph.removeVertex(stub);
        }

        // Create network from config graph
        for (Agent agent : configGraph.getVertices()) {
            addVertex(agent);
        }
        for (Edge edge : configGraph.getEdges()) {
            Pair<Agent> endpoints = configGraph.getEndpoints(edge);
            addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
        }

        return true;
    }

    private boolean tryReplaceLoopTriple(Edge edge1, Edge edge2, Graph<Agent, Edge> configGraph) {
        Pair<Agent> endpoints1 = configGraph.getEndpoints(edge1);
        Pair<Agent> endpoints2 = configGraph.getEndpoints(edge2);

        // Two loops need to be given
        if (endpoints1.getFirst() != endpoints1.getSecond()
            || endpoints2.getFirst() != endpoints2.getSecond()) {
            return false;
        }

        // Attempt to find suitable third loop
        for (Edge edge3 : getShuffledEdges(configGraph)) {
            if (!configGraph.containsEdge(edge3)) continue;
            if (edge3 == edge1 || edge3 == edge2) continue;

            Pair<Agent> endpoints3 = configGraph.getEndpoints(edge3);
            if (endpoints3.getFirst() != endpoints3.getSecond()) continue;

            // Loops share at least one node
            if (endpoints1.getFirst() == endpoints2.getFirst()
                || endpoints2.getFirst() == endpoints3.getFirst()
                || endpoints3.getFirst() == endpoints1.getFirst()) {
                continue;
            }

            // At least two nodes of the loops are already connected
            if (configGraph.isNeighbor(endpoints1.getFirst(), endpoints2.getFirst())
                || configGraph.isNeighbor(endpoints2.getFirst(), endpoints3.getFirst())
                || configGraph.isNeighbor(endpoints3.getFirst(), endpoints1.getFirst())) {
                continue;
            }

            configGraph.removeEdge(edge1);
            configGraph.removeEdge(edge2);
            configGraph.removeEdge(edge3);
            configGraph.addEdge(new InteractionEdge(), endpoints1.getSecond(), endpoints2.getFirst());
            configGraph.addEdge(new InteractionEdge(), endpoints2.getSecond(), endpoints3.getFirst());
            configGraph.addEdge(new InteractionEdge(), endpoints3.getSecond(), endpoints1.getFirst());
            return true;
        }

        return false;
    }

    private boolean tryReplaceDuplicateEdge(Edge edge1, Edge edge2, Graph<Agent, Edge> configGraph) {
        Pair<Agent> endpoints1 = configGraph.getEndpoints(edge1);
        Pair<Agent> endpoints2 = configGraph.getEndpoints(edge2);

        // Both edges are loops
        if (endpoints1.getFirst() == endpoints1.getSecond()
            && endpoints2.getFirst() == endpoints2.getSecond()) {
            return false;
        }

        // Edges share at least one node
        if (endpoints1.getFirst() == endpoints2.getFirst()
            || endpoints1.getFirst() == endpoints2.getSecond()
            || endpoints1.getSecond() == endpoints2.getFirst()
            || endpoints1.getSecond() == endpoints2.getSecond()) {
            return false;
        }

        // Edges are connected through exactly one other edge
        if (configGraph.isNeighbor(endpoints1.getFirst(), endpoints2.getFirst())
            || configGraph.isNeighbor(endpoints1.getFirst(), endpoints2.getSecond())
            || configGraph.isNeighbor(endpoints1.getSecond(), endpoints2.getFirst())
            || configGraph.isNeighbor(endpoints1.getSecond(), endpoints2.getSecond())) {
            return false;
        }

        configGraph.removeEdge(edge1);
        configGraph.removeEdge(edge2);
        configGraph.addEdge(new InteractionEdge(), endpoints1.getFirst(), endpoints2.getFirst());
        configGraph.addEdge(new InteractionEdge(), endpoints1.getSecond(), endpoints2.getSecond());
        return true;
    }

    private Agent[] getShuffledNodesWithoutStubs(Graph<Agent, Edge> graph) {
        ArrayList<Agent> agents = new ArrayList<>(graph.getVertices());
        for (Agent stub : stubs) {
            agents.remove(stub);
        }
        Collections.shuffle(agents, javaRandom);
        return agents.toArray(Agent[]::new);
    }

    private Edge[] getShuffledEdges(Graph<Agent, Edge> graph) {
        ArrayList<Edge> edges = new ArrayList<>(graph.getEdges());
        Collections.shuffle(edges, javaRandom);
        return edges.toArray(Edge[]::new);
    }
}
