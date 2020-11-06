package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.Agent;
import seom.utils.JavaRandomFacade;

import java.util.ArrayList;
import java.util.Collections;

public class SmallWorldNetworkBuilder {
    private UndirectedSparseGraph<Agent, InteractionEdge> baseLattice;
    private int learningDistance;
    private double beta;

    private final MersenneTwisterFast random;
    private final JavaRandomFacade javaRandom;
    private UndirectedSparseGraph<Agent, InteractionEdge> interactionGraph;

    public SmallWorldNetworkBuilder(MersenneTwisterFast random) {
        this.random = random;
        javaRandom = new JavaRandomFacade(random);
    }

    public UndirectedSparseMultigraph<Agent, Edge> create() {
        assert baseLattice.getVertexCount() > 1 : "The number of agents in base lattice must be at least 2";
        assert beta >= 0 && beta <= 1.0 : "Beta must be in [0,1]";
        Agent[] vertices = baseLattice.getVertices().toArray(new Agent[0]);
        for (int i = 1; i < baseLattice.getVertexCount(); i++) {
            assert baseLattice.getNeighborCount(vertices[i]) == baseLattice.getNeighborCount(vertices[i - 1])
                : "Base lattice is not a uniform ring";
        }

        //noinspection StatementWithEmptyBody
        while (!tryCreateNetwork()) ;

        return NetworkUtils.createFullGraph(learningDistance, interactionGraph);
    }

    //region Builder Setters

    public SmallWorldNetworkBuilder setBaseLattice(UndirectedSparseGraph<Agent, InteractionEdge> baseLattice) {
        this.baseLattice = baseLattice;
        return this;
    }

    public SmallWorldNetworkBuilder setLearningDistance(int learningDistance) {
        this.learningDistance = learningDistance;
        return this;
    }

    public SmallWorldNetworkBuilder setBeta(double beta) {
        this.beta = beta;
        return this;
    }

    //endregion

    private boolean tryCreateNetwork() {
        interactionGraph = new UndirectedSparseGraph<>();
        for (Agent agent : baseLattice.getVertices()) {
            interactionGraph.addVertex(agent);
        }
        for (InteractionEdge edge : baseLattice.getEdges()) {
            Pair<Agent> endpoints = baseLattice.getEndpoints(edge);
            interactionGraph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
        }

        for (InteractionEdge edge : interactionGraph.getEdges().toArray(InteractionEdge[]::new)) {
            double rand = random.nextDouble();
            if (rand >= beta) continue;

            Pair<Agent> endpoints = interactionGraph.getEndpoints(edge);
            Agent agent1 = endpoints.getFirst();

            Agent agent2 = null;
            var nodes = new ArrayList<>(interactionGraph.getVertices());
            Collections.shuffle(nodes, javaRandom);
            for (Agent node : nodes) {
                if (agent1 != node && interactionGraph.findEdge(agent1, node) == null) {
                    agent2 = node;
                }
            }

            if (agent2 == null) {
                System.out.println("Could not rewire edge, attempting network construction from scratch");
                return false;
            }

            interactionGraph.removeEdge(edge);
            interactionGraph.addEdge(new InteractionEdge(), agent1, agent2);
        }

        return true;
    }
}
