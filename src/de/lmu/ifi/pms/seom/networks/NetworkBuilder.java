package de.lmu.ifi.pms.seom.networks;

import de.lmu.ifi.pms.seom.Agent;
import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;

public interface NetworkBuilder {
    void setRandom(MersenneTwisterFast random);
    UndirectedSparseMultigraph<Agent, Edge> create();
}
