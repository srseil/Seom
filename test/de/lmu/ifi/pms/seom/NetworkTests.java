package de.lmu.ifi.pms.seom;

import de.lmu.ifi.pms.seom.networks.*;
import ec.util.MersenneTwisterFast;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;

public class NetworkTests {
    @Test
    public void createInteractionGraph() {
        var graph = new Lattice1DBuilder()
            .setLength(10)
            .setInteractionDistance(2)
            .setLearningDistance(3)
            .setWrapAround(true)
            .create();
        var interactionGraph = NetworkUtils.getInteractionGraph(graph);

        Assertions.assertEquals(20, interactionGraph.getEdgeCount());
    }

    @Test
    public void createLearningGraph() {
        var graph = new Lattice1DBuilder()
            .setLength(10)
            .setInteractionDistance(2)
            .setLearningDistance(2)
            .setWrapAround(true)
            .create();
        var learningGraph = NetworkUtils.getLearningGraph(graph);

        Assertions.assertEquals(40, learningGraph.getEdgeCount());
    }

    @Test
    public void createFullyConnectedNetwork() {
        var graph = new FullyConnectedBuilder()
            .setNumAgents(10)
            .create();
        var interactionGraph = NetworkUtils.getInteractionGraph(graph);

        Assertions.assertEquals(45, interactionGraph.getEdgeCount());
        for (Agent agent : interactionGraph.getVertices()) {
            for (Agent neighbor : interactionGraph.getVertices()) {
                Collection<InteractionEdge> edges = interactionGraph.findEdgeSet(agent, neighbor);
                if (agent == neighbor) {
                    Assertions.assertEquals(0, edges.size());
                } else {
                    Assertions.assertEquals(1, edges.size());
                }
            }
        }
    }

    @Test
    public void createLattice1D() {
        var graph = new Lattice1DBuilder()
            .setLength(10)
            .setInteractionDistance(2)
            .setLearningDistance(1)
            .setWrapAround(true)
            .create();
        var interactionGraph = NetworkUtils.getInteractionGraph(graph);

        Assertions.assertEquals(20, interactionGraph.getEdgeCount());
        for (Agent agent : interactionGraph.getVertices()) {
            Collection<InteractionEdge> edges = interactionGraph.getOutEdges(agent);
            Assertions.assertEquals(4, edges.size());
        }
    }

    @Test
    public void createLattice2D() {
        var graph = new Lattice2DBuilder()
            .setWidth(3)
            .setHeight(3)
            .setInteractionDistance(2)
            .setLearningDistance(1)
            .setNeighborhood(Lattice2DBuilder.Neighborhood.VonNeumann)
            .setWrapAround(false)
            .create();
        var interactionGraph = NetworkUtils.getInteractionGraph(graph);

        Assertions.assertEquals(18, interactionGraph.getEdgeCount());
        for (Agent agent : interactionGraph.getVertices()) {
            Collection<InteractionEdge> edges = interactionGraph.getOutEdges(agent);
            Assertions.assertEquals(4, edges.size());
        }
    }

    @Test
    public void createSmallWorld1D() {
        // This seed makes the preceding calls to nextDouble() produce 4 values below 0.2
        var random = new MersenneTwisterFast(12345678);
        var lattice = new Lattice1DBuilder()
            .setLength(10)
            .setInteractionDistance(2)
            .setLearningDistance(1)
            .setWrapAround(true)
            .create();
        var interactionLattice = NetworkUtils.getInteractionGraph(lattice);
        var graph = new SmallWorldBuilder(random)
            .setBaseLattice(interactionLattice)
            .setLearningDistance(1)
            .setBeta(0.2)
            .create();
        var interactionGraph = NetworkUtils.getInteractionGraph(graph);

        Assertions.assertEquals(20, interactionGraph.getEdgeCount());
        var latticeEdgeSet = new HashSet<>(interactionLattice.getEdges());
        int numRewiredEdges = 0;
        for (InteractionEdge edge : interactionGraph.getEdges()) {
            if (!latticeEdgeSet.contains(edge)) {
                numRewiredEdges++;
            }
        }
        Assertions.assertEquals(4, numRewiredEdges);
    }

    @Test
    public void createSmallWorld2D() {
        // This seed makes the preceding calls to nextDouble() produce 4 values below 0.2
        var random = new MersenneTwisterFast(12345678);
        var lattice = new Lattice2DBuilder()
            .setWidth(3)
            .setHeight(3)
            .setInteractionDistance(1)
            .setLearningDistance(1)
            .setNeighborhood(Lattice2DBuilder.Neighborhood.VonNeumann)
            .setWrapAround(true)
            .create();
        var interactionLattice = NetworkUtils.getInteractionGraph(lattice);
        var graph = new SmallWorldBuilder(random)
            .setBaseLattice(interactionLattice)
            .setLearningDistance(1)
            .setBeta(0.2)
            .create();
        var interactionGraph = NetworkUtils.getInteractionGraph(graph);

        Assertions.assertEquals(18, interactionGraph.getEdgeCount());
        var latticeEdgeSet = new HashSet<>(interactionLattice.getEdges());
        int numRewiredEdges = 0;
        for (InteractionEdge edge : interactionGraph.getEdges()) {
            if (!latticeEdgeSet.contains(edge)) {
                numRewiredEdges++;
            }
        }
        Assertions.assertEquals(4, numRewiredEdges);
    }

    @Test
    public void createBoundedDegreeNetwork() {
        var random = new MersenneTwisterFast(12345678);
        var graph = new BoundedDegreeBuilder(random)
            .setNumAgents(10)
            .setLearningDistance(1)
            .setMinDegree(1)
            .setMaxDegree(3)
            .create();
        var interactionGraph = NetworkUtils.getInteractionGraph(graph);

        for (Agent agent : interactionGraph.getVertices()) {
            int neighbors = interactionGraph.getNeighborCount(agent);
            Assertions.assertTrue(neighbors >= 1 && neighbors <= 3);
        }
    }

    @Test
    public void createRandomNetwork() {
        var random = new MersenneTwisterFast(12345678);
        var graph = new FullyRandomBuilder(random)
            .setNumAgents(5)
            .setLearningDistance(1)
            .setEdgeProbability(0.2)
            .create();
        var interactionGraph = NetworkUtils.getInteractionGraph(graph);

        Assertions.assertTrue(interactionGraph.getEdgeCount() >= 5);
    }
}
