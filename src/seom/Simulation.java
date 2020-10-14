package seom;

import edu.uci.ics.jung.graph.Graph;
import sim.engine.SimState;

public class Simulation extends SimState {
    public static long seed = 560646223;

    private Graph<Agent, Relationship> graph;

    public Simulation() {
        super(seed);
    }

    public Simulation(long seed) {
        super(seed);
    }

    @Override
    public void start() {
        super.start();
    }

    public Graph<Agent, Relationship> getGraph() {
        return graph;
    }
}
