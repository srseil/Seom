package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.Agent;
import seom.utils.JavaRandomFacade;
import seom.utils.Log;

import java.util.ArrayList;
import java.util.Collections;

public class SmallWorldBuilder implements NetworkBuilder {
    private UndirectedSparseGraph<Agent, InteractionEdge> baseLattice;
    private int learningDistance;
    private double beta;

    private MersenneTwisterFast random;
    private JavaRandomFacade javaRandom;
    private UndirectedSparseGraph<Agent, InteractionEdge> interactionGraph;

    public SmallWorldBuilder() {
    }

    public SmallWorldBuilder(MersenneTwisterFast random) {
        setRandom(random);
    }

    @Override
    public void setRandom(MersenneTwisterFast random) {
        this.random = random;
        javaRandom = new JavaRandomFacade(random);
    }

    @Override
    public UndirectedSparseMultigraph<Agent, Edge> create() {
        assert baseLattice.getVertexCount() > 1 : "The number of agents in base lattice must be at least 2";
        assert beta >= 0 && beta <= 1.0 : "Beta must be in [0,1]";

        //noinspection StatementWithEmptyBody
        while (!tryCreateNetwork()) ;

        return NetworkUtils.createFullGraph(learningDistance, interactionGraph);
    }

    //region Builder Setters

    public SmallWorldBuilder setBaseLattice(UndirectedSparseGraph<Agent, InteractionEdge> baseLattice) {
        this.baseLattice = baseLattice;
        return this;
    }

    public SmallWorldBuilder setLearningDistance(int learningDistance) {
        this.learningDistance = learningDistance;
        return this;
    }

    public SmallWorldBuilder setBeta(double beta) {
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

        var sortedEdges = new ArrayList<>(interactionGraph.getEdges());
        Collections.sort(sortedEdges);
        for (InteractionEdge edge : sortedEdges) {
            double rand = random.nextDouble();
            if (rand >= beta) continue;

            Pair<Agent> endpoints = interactionGraph.getEndpoints(edge);
            Agent agent1 = random.nextBoolean() ? endpoints.getFirst() : endpoints.getSecond();

            Agent agent2 = null;
            var nodes = new ArrayList<>(interactionGraph.getVertices());
            Collections.shuffle(nodes, javaRandom);
            for (Agent node : nodes) {
                if (agent1 != node && interactionGraph.findEdge(agent1, node) == null) {
                    agent2 = node;
                }
            }

            if (agent2 == null) {
                Log.finer("Could not rewire edge, attempting network construction from scratch");
                return false;
            }

            interactionGraph.removeEdge(edge);
            interactionGraph.addEdge(new InteractionEdge(), agent1, agent2);
        }

        if (!NetworkUtils.isConnected(interactionGraph)) {
            Log.finer("Constructed graph is not connected, attempting network construction from scratch");
            return false;
        }

        return true;
    }
}
