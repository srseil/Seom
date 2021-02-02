package de.lmu.ifi.pms.seom.networks;

import de.lmu.ifi.pms.seom.Agent;
import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;

public class SmallWorld2DBuilder implements NetworkBuilder {
    private int width;
    private int height;
    private int interactionDistance;
    private boolean wrapAround;
    private Lattice2DBuilder.Neighborhood neighborhood;
    private int learningDistance;
    private double beta;

    private MersenneTwisterFast random;

    public SmallWorld2DBuilder() {
    }

    public SmallWorld2DBuilder(MersenneTwisterFast random) {
        setRandom(random);
    }

    @Override
    public void setRandom(MersenneTwisterFast random) {
        this.random = random;
    }

    @Override
    public UndirectedSparseMultigraph<Agent, Edge> create() {
        var lattice = new Lattice2DBuilder()
            .setWidth(width)
            .setHeight(height)
            .setInteractionDistance(interactionDistance)
            .setLearningDistance(1)
            .setWrapAround(wrapAround)
            .setNeighborhood(neighborhood)
            .create();
        var builder = new SmallWorldBuilder()
            .setBaseLattice(NetworkUtils.getInteractionGraph(lattice))
            .setLearningDistance(learningDistance)
            .setBeta(beta);
        builder.setRandom(random);
        return builder.create();
    }

    //region Builder Setters

    public SmallWorld2DBuilder setWidth(int width) {
        this.width = width;
        return this;
    }

    public SmallWorld2DBuilder setHeight(int height) {
        this.height = height;
        return this;
    }

    public SmallWorld2DBuilder setInteractionDistance(int interactionDistance) {
        this.interactionDistance = interactionDistance;
        return this;
    }

    public SmallWorld2DBuilder setWrapAround(boolean wrapAround) {
        this.wrapAround = wrapAround;
        return this;
    }

    public SmallWorld2DBuilder setNeighborhood(Lattice2DBuilder.Neighborhood neighborhood) {
        this.neighborhood = neighborhood;
        return this;
    }

    public SmallWorld2DBuilder setLearningDistance(int learningDistance) {
        this.learningDistance = learningDistance;
        return this;
    }

    public SmallWorld2DBuilder setBeta(double beta) {
        this.beta = beta;
        return this;
    }

    //endregion
}
