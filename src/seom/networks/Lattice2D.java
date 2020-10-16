package seom.networks;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import seom.Agent;
import seom.Relationship;

public class Lattice2D extends UndirectedSparseGraph<Agent, Relationship> {
    private final int width;
    private final int height;
    private final boolean wrapAround;

    public enum Neighborhood {
        VonNeumann,
        Moore
    }

    public Lattice2D(int width, int height, boolean wrapAround, Neighborhood neighborhood) {
        this.width = width;
        this.height = height;
        this.wrapAround = wrapAround;

        Agent[] agents = new Agent[width * height];
        for (int i = 0; i < agents.length; i++) {
            agents[i] = new Agent();
        }

        for (int i = 0; i < agents.length; i++) {
            Agent agent = agents[i];
            addVertex(agent);

            if (neighborhood == Neighborhood.VonNeumann || neighborhood == Neighborhood.Moore) {
                addEdgeIfPossible(agent, topOf(i), agents);
                addEdgeIfPossible(agent, rightOf(i), agents);
                addEdgeIfPossible(agent, bottomOf(i), agents);
                addEdgeIfPossible(agent, leftOf(i), agents);
            }

            if (neighborhood == Neighborhood.Moore) {
                addEdgeIfPossible(agent, topOf(rightOf(i)), agents);
                addEdgeIfPossible(agent, bottomOf(rightOf(i)), agents);
                addEdgeIfPossible(agent, bottomOf(leftOf(i)), agents);
                addEdgeIfPossible(agent, topOf(leftOf(i)), agents);
            }
        }
    }

    private void addEdgeIfPossible(Agent agent, int targetIndex, Agent[] agents) {
        if (targetIndex != -1) {
            addEdge(new Relationship(), agent, agents[targetIndex]);
        }
    }

    private int topOf(int index) {
        if (index == -1) {
            return -1;
        } else if (index / width > 0) {
            return index - width;
        } else if (wrapAround) {
            return index + width * (height - 1);
        } else {
            return -1;
        }
    }

    private int bottomOf(int index) {
        if (index == -1) {
            return -1;
        } else if (index / width < height - 1) {
            return index + width;
        } else if (wrapAround) {
            return index - width * (height - 1);
        } else {
            return -1;
        }
    }

    private int leftOf(int index) {
        if (index == -1) {
            return -1;
        } else if (index % width > 0) {
            return index - 1;
        } else if (wrapAround) {
            return index + width - 1;
        } else {
            return -1;
        }
    }

    private int rightOf(int index) {
        if (index == -1) {
            return -1;
        } else if (index % width < width - 1) {
            return index + 1;
        } else if (wrapAround) {
            return index - width + 1;
        } else {
            return -1;
        }
    }
}
