package seom;

import seom.games.Strategy;
import sim.engine.SimState;
import sim.engine.Steppable;

public class Stability implements Steppable {
    private final Simulation simulation;

    public Stability(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public void step(SimState simState) {
        System.out.println("Stability");

        boolean stable = true;
        Strategy stableStratey = null;
        for (Agent agent : simulation.getConfig().getNetwork().getVertices()) {
            if (stableStratey == null) {
                stableStratey = agent.getStrategy();
            } else if (stableStratey != agent.getStrategy()) {
                stable = false;
                break;
            }
        }

        if (stable) {
            simulation.kill();
        }
    }
}
