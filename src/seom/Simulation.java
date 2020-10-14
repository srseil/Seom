package seom;

import edu.uci.ics.jung.graph.Graph;
import sim.engine.SimState;

public class Simulation extends SimState {
    private static long seed = 560646223;

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

        for (Agent agent : graph.getVertices()) {
            schedule.scheduleRepeating(agent);
        }
    }

    public static long getSeed() {
        return seed;
    }

    public Graph<Agent, Relationship> getGraph() {
        return graph;
    }

    public void setGraph(Graph<Agent, Relationship> graph) {
        this.graph = graph;
    }
}
