package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.Agent;
import seom.utils.JavaRandomFacade;
import seom.utils.Log;

import java.util.*;

public class BoundedDegreeNetworkBuilder implements NetworkBuilder {
    private int numAgents;
    private int minDegree;
    private int maxDegree;
    private int learningDistance;

    private MersenneTwisterFast random;
    private JavaRandomFacade javaRandom;
    private Agent[] stubs;
    private UndirectedSparseGraph<Agent, InteractionEdge> interactionGraph;

    public BoundedDegreeNetworkBuilder() {
    }

    public BoundedDegreeNetworkBuilder(MersenneTwisterFast random) {
        setRandom(random);
    }

    @Override
    public void setRandom(MersenneTwisterFast random) {
        this.random = random;
        javaRandom = new JavaRandomFacade(random);
    }

    @Override
    public UndirectedSparseMultigraph<Agent, Edge> create() {
        assert numAgents > 0 : "Number of agents must be larger than one";
        assert minDegree <= maxDegree : "Minimum degree must not be larger than maximum degree";
        assert numAgents > minDegree : "Number of agents must be larger than minimum degree";
        assert minDegree != maxDegree || (numAgents * minDegree) % 2 == 0
            : "If the minimum and maximum degrees are equal, (degree * numAgents) must be even";


        stubs = new Agent[maxDegree];

        //noinspection StatementWithEmptyBody
        while (!tryCreateNetwork()) ;

        for (Agent agent : interactionGraph.getVertices()) {
            int neighborCount = interactionGraph.getNeighborCount(agent);
            assert neighborCount >= minDegree : "Graph contains node with degree smaller than minimum bound";
            assert neighborCount <= maxDegree : "Graph contains node with degree larger than maximum bound";
        }

        for (InteractionEdge edge : interactionGraph.getEdges()) {
            Pair<Agent> endpoints = interactionGraph.getEndpoints(edge);
            assert endpoints.getFirst() != endpoints.getSecond() : "Graph contains loop edge";
        }

        return NetworkUtils.createFullGraph(learningDistance, interactionGraph);
    }

    //region Builder Setters

    public BoundedDegreeNetworkBuilder setNumAgents(int numAgents) {
        this.numAgents = numAgents;
        return this;
    }

    public BoundedDegreeNetworkBuilder setMinDegree(int minDegree) {
        this.minDegree = minDegree;
        return this;
    }

    public BoundedDegreeNetworkBuilder setMaxDegree(int maxDegree) {
        this.maxDegree = maxDegree;
        return this;
    }

    public BoundedDegreeNetworkBuilder setLearningDistance(int learningDistance) {
        this.learningDistance = learningDistance;
        return this;
    }

    //endregion

    private boolean tryCreateNetwork() {
        var configGraph = new UndirectedSparseMultigraph<Agent, InteractionEdge>();

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
                    InteractionEdge edge = (InteractionEdge) configGraph.getOutEdges(agent).toArray()[0];
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
        InteractionEdge[] shuffledEdges = getShuffledEdges(configGraph);
        for (int i = 0; i < shuffledEdges.length; i += 2) {
            Pair<Agent> endpoints1 = configGraph.getEndpoints(shuffledEdges[i]);
            Pair<Agent> endpoints2 = configGraph.getEndpoints(shuffledEdges[i + 1]);
            configGraph.removeEdge(shuffledEdges[i]);
            configGraph.removeEdge(shuffledEdges[i + 1]);
            configGraph.addEdge(new InteractionEdge(), endpoints1.getFirst(), endpoints2.getFirst());
        }

        // Collect loops and duplicate edges
        List<InteractionEdge> unwantedEdges = new ArrayList<>();
        Set<Pair<Agent>> edgeSet = new HashSet<>();
        for (InteractionEdge edge : configGraph.getEdges()) {
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
        Collections.sort(unwantedEdges);
        for (InteractionEdge unwantedEdge : unwantedEdges) {
            if (!configGraph.containsEdge(unwantedEdge)) continue;

            boolean removed = false;
            for (InteractionEdge edge : getShuffledEdges(configGraph)) {
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
                Log.finer("Could not remove unwanted edge, attempting network construction from scratch");
                return false;
            }
        }

        // Remove stubs
        for (Agent stub : stubs) {
            configGraph.removeVertex(stub);
        }

        if (!NetworkUtils.isConnected(configGraph)) {
            Log.finer("Generated network is not connected, attempting network construction from scratch");
            return false;
        }

        // Create network from config graph
        interactionGraph = new UndirectedSparseGraph<>();
        for (Agent agent : configGraph.getVertices()) {
            interactionGraph.addVertex(agent);
        }
        for (InteractionEdge edge : configGraph.getEdges()) {
            Pair<Agent> endpoints = configGraph.getEndpoints(edge);
            interactionGraph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
        }

        return true;
    }

    private boolean tryReplaceLoopTriple(InteractionEdge edge1, InteractionEdge edge2,
                                         UndirectedSparseMultigraph<Agent, InteractionEdge> configGraph) {
        Pair<Agent> endpoints1 = configGraph.getEndpoints(edge1);
        Pair<Agent> endpoints2 = configGraph.getEndpoints(edge2);

        // Two different loops need to be given
        if (endpoints1.getFirst() != endpoints1.getSecond()
            || endpoints2.getFirst() != endpoints2.getSecond()
            || endpoints1.getFirst() == endpoints2.getFirst()) {
            return false;
        }

        // Attempt to find suitable third loop
        for (InteractionEdge edge3 : getShuffledEdges(configGraph)) {
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

    private boolean tryReplaceDuplicateEdge(InteractionEdge edge1, InteractionEdge edge2,
                                            UndirectedSparseMultigraph<Agent, InteractionEdge> configGraph) {
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

    private Agent[] getShuffledNodesWithoutStubs(UndirectedSparseMultigraph<Agent, InteractionEdge> configGraph) {
        ArrayList<Agent> agents = new ArrayList<>(configGraph.getVertices());
        for (Agent stub : stubs) {
            agents.remove(stub);
        }
        Collections.shuffle(agents, javaRandom);
        return agents.toArray(Agent[]::new);
    }

    private InteractionEdge[] getShuffledEdges(UndirectedSparseMultigraph<Agent, InteractionEdge> configGraph) {
        ArrayList<InteractionEdge> edges = new ArrayList<>(configGraph.getEdges());
        Collections.shuffle(edges, javaRandom);
        return edges.toArray(InteractionEdge[]::new);
    }
}
