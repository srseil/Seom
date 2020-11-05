package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import seom.Agent;

public class RandomNetworkBuilder {
    private int numAgents;
    private int learningDistance;
    private double edgeProbability;

    private final MersenneTwisterFast random;

    public RandomNetworkBuilder(MersenneTwisterFast random) {
        this.random = random;
    }

    public UndirectedSparseMultigraph<Agent, Edge> create() {
        assert numAgents > 1 : "The number of agents must be at least 2";
        assert edgeProbability >= 0 && edgeProbability <= 1.0 : "Edge probability must be in [0,1]";

        var interactionGraph = new UndirectedSparseGraph<Agent, InteractionEdge>();
        for (int i = 0; i < numAgents; i++) {
            interactionGraph.addVertex(new Agent());
        }
        for (Agent agent : interactionGraph.getVertices()) {
            for (Agent other : interactionGraph.getVertices()) {
                if (agent == other) continue;

                double rand = random.nextDouble();
                if (rand < edgeProbability) {
                    interactionGraph.addEdge(new InteractionEdge(), agent, other);
                }
            }
        }

        return NetworkUtils.createFullGraph(learningDistance, interactionGraph);
    }

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
}
