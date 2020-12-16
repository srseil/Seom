package seom.networks;

import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import seom.Agent;

public interface NetworkBuilder {
    UndirectedSparseMultigraph<Agent, Edge> create();
}
