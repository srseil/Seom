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

public class FullyRandomBuilder implements NetworkBuilder {
    private int numAgents;
    private int learningDistance;
    private double edgeProbability;

    private MersenneTwisterFast random;
    private JavaRandomFacade javaRandom;
    private UndirectedSparseGraph<Agent, InteractionEdge> interactionGraph;

    public FullyRandomBuilder() {
    }

    public FullyRandomBuilder(MersenneTwisterFast random) {
        setRandom(random);
    }

    @Override
    public void setRandom(MersenneTwisterFast random) {
        this.random = random;
        javaRandom = new JavaRandomFacade(random);
    }

    @Override
    public UndirectedSparseMultigraph<Agent, Edge> create() {
        assert numAgents > 1 : "The number of agents must be at least 2";
        assert edgeProbability >= 0 && edgeProbability <= 1.0 : "Edge probability must be in [0,1]";

        //noinspection StatementWithEmptyBody
        while (!tryCreateNetwork()) ;

        return NetworkUtils.createFullGraph(learningDistance, interactionGraph);
    }

    //region Builder Setters

    public FullyRandomBuilder setNumAgents(int numAgents) {
        this.numAgents = numAgents;
        return this;
    }

    public FullyRandomBuilder setLearningDistance(int learningDistance) {
        this.learningDistance = learningDistance;
        return this;
    }

    public FullyRandomBuilder setEdgeProbability(double edgeProbability) {
        this.edgeProbability = edgeProbability;
        return this;
    }

    //endregion

    private boolean tryCreateNetwork() {
        interactionGraph = new FullyConnectedBuilder()
            .setNumAgents(numAgents)
            .createInteractionGraph();

        var toRemove = new ArrayList<InteractionEdge>();
        var sortedEdges = new ArrayList<>(interactionGraph.getEdges());
        Collections.sort(sortedEdges);
        for (InteractionEdge edge : sortedEdges) {
            double rand = random.nextDouble();
            if (rand >= edgeProbability) {
                toRemove.add(edge);
            }
        }

        int leftover = 0;
        for (InteractionEdge edge : toRemove) {
            Pair<Agent> endpoints = interactionGraph.getEndpoints(edge);
            if (interactionGraph.getNeighborCount(endpoints.getFirst()) > 1
                && interactionGraph.getNeighborCount(endpoints.getSecond()) > 1) {
                interactionGraph.removeEdge(edge);
            } else {
                leftover++;
            }
        }

        ArrayList<InteractionEdge> shuffledEdges = new ArrayList<>(interactionGraph.getEdges());
        Collections.sort(shuffledEdges);
        Collections.shuffle(shuffledEdges, javaRandom);
        for (InteractionEdge edge : shuffledEdges) {
            if (leftover == 0) break;

            Pair<Agent> endpoints = interactionGraph.getEndpoints(edge);
            if (interactionGraph.getNeighborCount(endpoints.getFirst()) > 1
                && interactionGraph.getNeighborCount(endpoints.getSecond()) > 1) {
                interactionGraph.removeEdge(edge);
                leftover--;
            }
        }

        if (leftover != 0) {
            Log.finer("Could not remove enough edges, attempting network construction from scratch");
            return false;
        } else if (!NetworkUtils.isConnected(interactionGraph)) {
            Log.finer("Generated network is not connected, attempting network construction from scratch");
            return false;
        }

        return true;
    }
}
