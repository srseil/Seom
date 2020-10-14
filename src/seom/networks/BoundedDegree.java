package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import seom.Agent;
import seom.Relationship;
import seom.Simulation;

public class BoundedDegree extends UndirectedSparseGraph<Agent, Relationship> {
    /* Note:
     * This implementation creates a graph in which the different possible degrees
     * (between minDegree and maxDegree) are not uniformly distributed among the nodes.
     * While he doesn't mention it explicitly, it looks like Alexander uses such a kind
     * of bounded degree network, too. Therefore, it seems best to keep this implementation,
     * but add another one whereby the degrees are uniformly distributed.
     *
     * WARNING:
     * This can run into an infinite loop!
     */
    public BoundedDegree(int numAgents, int minDegree, int maxDegree) {
        MersenneTwisterFast random = new MersenneTwisterFast();
        random.setSeed(Simulation.getSeed());

        Agent[] agents = new Agent[numAgents];
        for (int i = 0; i < numAgents; i++) {
            agents[i] = new Agent(this);
            addVertex(agents[i]);
        }

        for (Agent agent : agents) {
            int degree = minDegree + random.nextInt(maxDegree - minDegree + 1);

            for (int j = getNeighborCount(agent); j < degree; j++) {
                Agent other;
                do {
                    int rand = random.nextInt(numAgents);
                    other = agents[rand];
                } while (other == agent || isNeighbor(other, agent) || getNeighborCount(other) == maxDegree);

                addEdge(new Relationship(), agent, other);
            }
        }
    }
}
