package seom.networks;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import seom.Agent;
import seom.Relationship;

public class Lattice1D extends UndirectedSparseGraph<Agent, Relationship> {
    public Lattice1D(int length, int radius, boolean wrapAround) {
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
                addEdge(new Relationship(), agents[i], agents[neighborIndex]);
            }
        }
    }
}
