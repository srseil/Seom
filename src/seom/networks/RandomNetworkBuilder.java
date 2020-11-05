package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.Agent;
import seom.utils.JavaRandomFacade;

import java.util.ArrayList;
import java.util.Collections;

public class RandomNetworkBuilder {
    private int numAgents;
    private int learningDistance;
    private double edgeProbability;

    private final MersenneTwisterFast random;
    private final JavaRandomFacade javaRandom;
    private UndirectedSparseGraph<Agent, InteractionEdge> interactionGraph;

    public RandomNetworkBuilder(MersenneTwisterFast random) {
        this.random = random;
        javaRandom = new JavaRandomFacade(random);
    }

    public UndirectedSparseMultigraph<Agent, Edge> create() {
        assert numAgents > 1 : "The number of agents must be at least 2";
        assert edgeProbability >= 0 && edgeProbability <= 1.0 : "Edge probability must be in [0,1]";
        assert edgeProbability >= (numAgents - 1) / (double) getMaxNumEdges(numAgents)
            : "Edge probability must describe at least the minimum number of edges for a connected network";

        //noinspection StatementWithEmptyBody
        while (!tryCreateNetwork()) ;

        return NetworkUtils.createFullGraph(learningDistance, interactionGraph);
    }

    //region Builder Setters

    public RandomNetworkBuilder setNumAgents(int numAgents) {
        this.numAgents = numAgents;
        return this;
    }

    public RandomNetworkBuilder setLearningDistance(int learningDistance) {
        this.learningDistance = learningDistance;
        return this;
    }

    public RandomNetworkBuilder setEdgeProbability(double edgeProbability) {
        this.edgeProbability = edgeProbability;
        return this;
    }

    //endregion

    private boolean tryCreateNetwork() {
        interactionGraph = new FullyConnectedNetworkBuilder()
            .setNumAgents(numAgents)
            .createInteractionGraph();

        var toRemove = new ArrayList<InteractionEdge>();
        for (InteractionEdge edge : interactionGraph.getEdges()) {
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
            System.out.println("Could not remove enough edges, attempting network construction from scratch");
            return false;
        }

        return true;
    }

    private static int getMaxNumEdges(int numAgents) {
        return factorial(numAgents) / (2 * factorial(numAgents - 2));
    }

    private static int factorial(int x) {
        int result = 1;
        for (int i = 2; i <= x; i++) {
            result *= i;
        }
        return result;
    }
}
