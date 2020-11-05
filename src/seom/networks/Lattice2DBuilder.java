package seom.networks;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import seom.Agent;

public class Lattice2DBuilder {
    private int width;
    private int height;
    private int interactionDistance;
    private int learningDistance;
    private boolean wrapAround;
    private Neighborhood neighborhood;

    private Agent[] agents;
    private UndirectedSparseGraph<Agent, InteractionEdge> interactionGraph;

    public enum Neighborhood {
        VonNeumann,
        Moore
    }

    public UndirectedSparseMultigraph<Agent, Edge> create() {
        assert width > 1 || height > 1 : "Either width or height must be larger than 1";
        assert interactionDistance < width || interactionDistance < height : "Interaction distance must be smaller than largest dimension";
        assert learningDistance < width || learningDistance < height : "Interaction distance must be smaller than largest dimension";

        interactionGraph = new UndirectedSparseGraph<>();
        agents = new Agent[width * height];
        for (int i = 0; i < agents.length; i++) {
            agents[i] = new Agent();
        }

        for (int i = 0; i < agents.length; i++) {
            Agent agent = agents[i];
            interactionGraph.addVertex(agent);

            if (neighborhood == Neighborhood.VonNeumann || neighborhood == Neighborhood.Moore) {
                int topIndex = i;
                int rightIndex = i;
                int bottomIndex = i;
                int leftIndex = i;
                for (int j = 0; j < interactionDistance; j++) {
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
                for (int j = 0; j < interactionDistance; j++) {
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

        return NetworkUtils.createFullGraph(learningDistance, interactionGraph);
    }

    //region Builder Setters

    public Lattice2DBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public Lattice2DBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public Lattice2DBuilder setInteractionDistance(int interactionDistance) {
        this.interactionDistance = interactionDistance;
        return this;
    }

    public Lattice2DBuilder setLearningDistance(int learningDistance) {
        this.learningDistance = learningDistance;
        return this;
    }

    public Lattice2DBuilder setWrapAround(boolean wrapAround) {
        this.wrapAround = wrapAround;
        return this;
    }

    public Lattice2DBuilder setNeighborhood(Neighborhood neighborhood) {
        this.neighborhood = neighborhood;
        return this;
    }

    //endregion

    private void addEdgeIfPossible(Agent agent, int targetIndex) {
        if (targetIndex != -1) {
            interactionGraph.addEdge(new InteractionEdge(), agent, agents[targetIndex]);
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
