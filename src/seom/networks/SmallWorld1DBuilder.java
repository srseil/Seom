package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import seom.Agent;

public class SmallWorld1DBuilder implements NetworkBuilder {
    private int length;
    private int interactionDistance;
    private boolean wrapAround;
    private int learningDistance;
    private double beta;

    private MersenneTwisterFast random;

    public SmallWorld1DBuilder() {
    }

    public SmallWorld1DBuilder(MersenneTwisterFast random) {
        setRandom(random);
    }

    @Override
    public void setRandom(MersenneTwisterFast random) {
        this.random = random;
    }

    @Override
    public UndirectedSparseMultigraph<Agent, Edge> create() {
        var lattice = new Lattice1DBuilder()
            .setLength(length)
            .setInteractionDistance(interactionDistance)
            .setLearningDistance(1)
            .setWrapAround(wrapAround)
            .create();
        var builder = new SmallWorldBuilder()
            .setBaseLattice(NetworkUtils.getInteractionGraph(lattice))
            .setLearningDistance(learningDistance)
            .setBeta(beta);
        builder.setRandom(random);
        return builder.create();
    }

    //region Builder Setters

    public SmallWorld1DBuilder setLength(int length) {
        this.length = length;
        return this;
    }

    public SmallWorld1DBuilder setInteractionDistance(int interactionDistance) {
        this.interactionDistance = interactionDistance;
        return this;
    }

    public SmallWorld1DBuilder setWrapAround(boolean wrapAround) {
        this.wrapAround = wrapAround;
        return this;
    }

    public SmallWorld1DBuilder setLearningDistance(int learningDistance) {
        this.learningDistance = learningDistance;
        return this;
    }

    public SmallWorld1DBuilder setBeta(double beta) {
        this.beta = beta;
        return this;
    }

    //endregion
}
