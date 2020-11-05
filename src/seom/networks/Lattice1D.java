package seom.networks;

import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import seom.Agent;

public class Lattice1D extends UndirectedSparseMultigraph<Agent, Edge> {
    public Lattice1D(int length, int learningDistance, int radius, boolean wrapAround) {
        assert length > 1 : "Length must be at least 2";
        assert radius <= length / 2 : "Maximum radius is length / 2";

        Agent[] agents = new Agent[length];
        for (int i = 0; i < length; i++) {
            Agent agent = new Agent();
            agents[i] = agent;
            addVertex(agent);
        }

        for (int i = 0; i < length; i++) {
            int neighborIndex = i;
            for (int j = 0; j < radius; j++) {
                if (neighborIndex < length - 1) {
                    neighborIndex += 1;
                } else if (neighborIndex == length - 1 && wrapAround) {
                    neighborIndex = 0;
                } else {
                    break;
                }
                addEdge(new InteractionEdge(), agents[i], agents[neighborIndex]);
            }
        }

        NetworkUtils.addLearningEdges(learningDistance, this);
    }
}
