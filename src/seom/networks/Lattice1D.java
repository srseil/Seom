package seom.networks;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import seom.Agent;
import seom.Relationship;

public class Lattice1D extends UndirectedSparseGraph<Agent, Relationship> {
    public Lattice1D(int length, boolean wrapAround) {
        Agent[] agents = new Agent[length];
        for (int i = 0; i < length; i++) {
            Agent agent = new Agent(this);
            agents[i] = agent;
            addVertex(agent);

            if (i > 0) {
                addEdge(new Relationship(), agents[i - 1], agent);
            }
            if (i == length - 1 && wrapAround) {
                addEdge(new Relationship(), agent, agents[0]);
            }
        }
    }
}
