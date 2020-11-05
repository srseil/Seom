package seom.networks;

import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import seom.Agent;

public class Lattice2D extends UndirectedSparseMultigraph<Agent, Edge> {
    private final int width;
    private final int height;
    private final boolean wrapAround;
    private final Agent[] agents;

    public enum Neighborhood {
        VonNeumann,
        Moore
    }

    public Lattice2D(int width, int height, int learningDistance, int radius, boolean wrapAround, Neighborhood neighborhood) {
        assert width > 1 || height > 1 : "Either width or height must be larger than 1";
        assert radius < width || radius < height : "Radius must be smaller than largest dimension";

        this.width = width;
        this.height = height;
        this.wrapAround = wrapAround;

        agents = new Agent[width * height];
        for (int i = 0; i < agents.length; i++) {
            agents[i] = new Agent();
        }

        for (int i = 0; i < agents.length; i++) {
            Agent agent = agents[i];
            addVertex(agent);

            if (neighborhood == Neighborhood.VonNeumann || neighborhood == Neighborhood.Moore) {
                int topIndex = i;
                int rightIndex = i;
                int bottomIndex = i;
                int leftIndex = i;
                for (int j = 0; j < radius; j++) {
                    topIndex = topOf(topIndex);
                    rightIndex = rightOf(rightIndex);
                    bottomIndex = bottomOf(bottomIndex);
                    leftIndex = leftOf(leftIndex);
                    addEdgeIfPossible(agent, topIndex);
                    addEdgeIfPossible(agent, rightIndex);
                    addEdgeIfPossible(agent, bottomIndex);
                    addEdgeIfPossible(agent, leftIndex);
                }
            }

            if (neighborhood == Neighborhood.Moore) {
                int topRightIndex = i;
                int bottomRightIndex = i;
                int bottomLeftIndex = i;
                int topLeftIndex = i;
                for (int j = 0; j < radius; j++) {
                    topRightIndex = topOf(rightOf(topRightIndex));
                    bottomRightIndex = bottomOf(rightOf(bottomRightIndex));
                    bottomLeftIndex = bottomOf(leftOf(bottomLeftIndex));
                    topLeftIndex = topOf(leftOf(topLeftIndex));
                    addEdgeIfPossible(agent, topRightIndex);
                    addEdgeIfPossible(agent, bottomRightIndex);
                    addEdgeIfPossible(agent, bottomLeftIndex);
                    addEdgeIfPossible(agent, topLeftIndex);
                }
            }
        }

        NetworkUtils.addLearningEdges(learningDistance, this);
    }

    private void addEdgeIfPossible(Agent agent, int targetIndex) {
        if (targetIndex != -1) {
            addEdge(new InteractionEdge(), agent, agents[targetIndex]);
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
