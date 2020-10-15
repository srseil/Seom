package seom.networks;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import seom.Agent;
import seom.Relationship;

public class Lattice2D extends UndirectedSparseGraph<Agent, Relationship> {
    public Lattice2D(int width, int height, boolean wrapAround) {
        Agent[] agents = new Agent[width * height];
        for (int i = 0; i < agents.length; i++) {
            agents[i] = new Agent();
        }

        for (int i = 0; i < agents.length; i++) {
            addVertex(agents[i]);

            Agent agent = agents[i];
            Agent agentLeft = null;
            Agent agentRight = null;
            Agent agentUp = null;
            Agent agentDown = null;

            int agentX = i % width;
            int agentY = i / width;

            // Determine edges

            if (agentX > 0) {
                agentLeft = agents[i - 1];
            } else if (wrapAround) {
                agentLeft = agents[i + width - 1];
            }

            if (agentX < width - 1) {
                agentRight = agents[i + 1];
            } else if (wrapAround) {
                agentRight = agents[i - width + 1];
            }

            if (agentY > 0) {
                agentUp = agents[i - width];
            } else if (wrapAround) {
                agentUp = agents[i + width * (height - 1)];
            }

            if (agentY < height - 1) {
                agentDown = agents[i + width];
            } else if (wrapAround) {
                agentDown = agents[i - width * (height - 1)];
            }

            // Create edges

            if (agentLeft != null) {
                addEdge(new Relationship(), agent, agentLeft);
            }

            if (agentRight != null) {
                addEdge(new Relationship(), agent, agentRight);
            }

            if (agentUp != null) {
                addEdge(new Relationship(), agent, agentUp);
            }

            if (agentDown != null) {
                addEdge(new Relationship(), agent, agentDown);
            }
        }
    }
}
