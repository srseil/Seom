package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import seom.Agent;

public class Lattice1DBuilder implements NetworkBuilder {
    private int length;
    private int interactionDistance;
    private int learningDistance;
    private boolean wrapAround;

    @Override
    public void setRandom(MersenneTwisterFast random) {
    }

    @Override
    public UndirectedSparseMultigraph<Agent, Edge> create() {
        assert length > 1 : "Length must be at least 2";
        assert interactionDistance <= length / 2 : "Maximum interaction distance is length / 2";

        var interactionGraph = new UndirectedSparseGraph<Agent, InteractionEdge>();

        Agent[] agents = new Agent[length];
        for (int i = 0; i < length; i++) {
            Agent agent = new Agent();
            agents[i] = agent;
            interactionGraph.addVertex(agent);
        }

        for (int i = 0; i < length; i++) {
            int neighborIndex = i;
            for (int j = 0; j < interactionDistance; j++) {
                if (neighborIndex < length - 1) {
                    neighborIndex += 1;
                } else if (neighborIndex == length - 1 && wrapAround) {
                    neighborIndex = 0;
                } else {
                    break;
                }
                interactionGraph.addEdge(new InteractionEdge(), agents[i], agents[neighborIndex]);
            }
        }

        return NetworkUtils.createFullGraph(learningDistance, interactionGraph);
    }

    public Lattice1DBuilder setLength(int length) {
        this.length = length;
        return this;
    }

    public Lattice1DBuilder setInteractionDistance(int interactionDistance) {
        this.interactionDistance = interactionDistance;
        return this;
    }

    public Lattice1DBuilder setLearningDistance(int learningDistance) {
        this.learningDistance = learningDistance;
        return this;
    }

    public Lattice1DBuilder setWrapAround(boolean wrapAround) {
        this.wrapAround = wrapAround;
        return this;
    }
}
