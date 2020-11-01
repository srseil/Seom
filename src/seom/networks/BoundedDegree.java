package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.Agent;
import seom.Relationship;
import seom.utils.JavaRandomFacade;

import java.util.*;

public class BoundedDegree extends UndirectedSparseGraph<Agent, Relationship> {
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
        while (!tryCreateNetwork(numAgents, minDegree, maxDegree));

        for (Agent agent : getVertices()) {
            int neighborCount = getNeighborCount(agent);
            assert neighborCount >= minDegree : "Graph contains node with degree smaller than minimum bound";
            assert neighborCount <= maxDegree : "Graph contains node with degree larger than maximum bound";
        }

        for (Relationship edge : getEdges()) {
            Pair<Agent> endpoints = getEndpoints(edge);
            assert endpoints.getFirst() != endpoints.getSecond() : "Graph contains loop edge";
        }
    }

    private boolean tryCreateNetwork(int numAgents, int minDegree, int maxDegree) {
        var configGraph = new UndirectedSparseMultigraph<Agent, Relationship>();

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
                configGraph.addEdge(new Relationship(), agents[i], stubs[j]);
            }
        }

        // Ensure that the sum of degrees is even
        if (configGraph.getEdgeCount() % 2 == 1) {
            // Try to remove random stub
            boolean removed = false;
            for (Agent agent : getShuffledNodesWithoutStubs(configGraph)) {
                if (configGraph.getNeighborCount(agent) > minDegree) {
                    Relationship edge = (Relationship) configGraph.getOutEdges(agent).toArray()[0];
                    configGraph.removeEdge(edge);
                    removed = true;
                    break;
                }
            }
            if (!removed) {
                // Add random stub
                for (Agent agent : getShuffledNodesWithoutStubs(configGraph)) {
                    if (configGraph.getNeighborCount(agent) < maxDegree) {
                        configGraph.addEdge(new Relationship(), agent, stubs[maxDegree - 1]);
                    }
                }
            }
        }

        // Randomly connect stubs
        Relationship[] shuffledEdges = getShuffledEdges(configGraph);
        for (int i = 0; i < shuffledEdges.length; i += 2) {
            Pair<Agent> endpoints1 = configGraph.getEndpoints(shuffledEdges[i]);
            Pair<Agent> endpoints2 = configGraph.getEndpoints(shuffledEdges[i + 1]);
            configGraph.removeEdge(shuffledEdges[i]);
            configGraph.removeEdge(shuffledEdges[i + 1]);
            configGraph.addEdge(new Relationship(), endpoints1.getFirst(), endpoints2.getFirst());
        }

        // Collect loops and duplicate edges
        List<Relationship> unwantedEdges = new ArrayList<>();
        Set<Pair<Agent>> edgeSet = new HashSet<>();
        for (Relationship edge : configGraph.getEdges()) {
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
        for (Relationship unwantedEdge : unwantedEdges) {
            if (!configGraph.containsEdge(unwantedEdge)) continue;

            boolean removed = false;
            for (Relationship edge : getShuffledEdges(configGraph)) {
                if (edge == unwantedEdge) continue;

                Pair<Agent> endpoints1 = configGraph.getEndpoints(edge);
                Pair<Agent> endpoints2 = configGraph.getEndpoints(unwantedEdge);

                // Both edges are loops
                if (endpoints1.getFirst() == endpoints1.getSecond()
                    && endpoints2.getFirst() == endpoints2.getSecond()) {
                    continue;
                }

                // Edges share at least one node
                if (endpoints1.getFirst() == endpoints2.getFirst()
                    || endpoints1.getFirst() == endpoints2.getSecond()
                    || endpoints1.getSecond() == endpoints2.getFirst()
                    || endpoints1.getSecond() == endpoints2.getSecond()) {
                    continue;
                }

                // Edges are connected through exactly one other edge
                if (configGraph.isNeighbor(endpoints1.getFirst(), endpoints2.getFirst())
                    || configGraph.isNeighbor(endpoints1.getFirst(), endpoints2.getSecond())
                    || configGraph.isNeighbor(endpoints1.getSecond(), endpoints2.getFirst())
                    || configGraph.isNeighbor(endpoints1.getSecond(), endpoints2.getSecond())) {
                    continue;
                }

                configGraph.removeEdge(edge);
                configGraph.removeEdge(unwantedEdge);
                configGraph.addEdge(new Relationship(), endpoints1.getFirst(), endpoints2.getFirst());
                configGraph.addEdge(new Relationship(), endpoints1.getSecond(), endpoints2.getSecond());
                removed = true;
                break;
            }

            if (!removed) {
                System.out.println("Could not remove unwanted edge, trying network construction from scratch");
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
        for (Relationship edge : configGraph.getEdges()) {
            Pair<Agent> endpoints = configGraph.getEndpoints(edge);
            addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
        }

        return true;
    }

    private Agent[] getShuffledNodesWithoutStubs(Graph<Agent, Relationship> graph) {
        ArrayList<Agent> agents = new ArrayList<>(graph.getVertices());
        for (Agent stub : stubs) {
            agents.remove(stub);
        }
        Collections.shuffle(agents, javaRandom);
        return agents.toArray(Agent[]::new);
    }

    private Relationship[] getShuffledEdges(Graph<Agent, Relationship> graph) {
        ArrayList<Relationship> edges = new ArrayList<>(graph.getEdges());
        Collections.shuffle(edges, javaRandom);
        return edges.toArray(Relationship[]::new);
    }
}
