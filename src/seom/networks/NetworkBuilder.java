package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import seom.Agent;

public interface NetworkBuilder {
    void setRandom(MersenneTwisterFast random);
    UndirectedSparseMultigraph<Agent, Edge> create();
}
