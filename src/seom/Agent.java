package seom;

import edu.uci.ics.jung.graph.Graph;
import sim.engine.SimState;
import sim.engine.Steppable;

import java.awt.*;

public class Agent implements Steppable {
    private static int nextId = 0;

    private final Graph<Agent, Relationship> network;
    private final int id;

    public Agent(Graph<Agent, Relationship> network) {
        this.network = network;
        id = nextId++;
    }

    @Override
    public void step(SimState simState) {
        for (Agent neighbor : network.getNeighbors(this)) {
            // Do something
        }
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public Paint getStrategyColor() {
        return Color.BLUE;
    }

    public int getId() {
        return id;
    }
}
