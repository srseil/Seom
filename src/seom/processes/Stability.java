package seom.processes;

import seom.Agent;
import seom.Simulation;
import seom.games.Strategy;
import sim.engine.SimState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stability implements SimulationProcess {
    private final Simulation simulation;
    private final Map<Agent, Strategy> strategyMap;
    private final List<Integer> strategyProgression;

    private boolean cycleDetectionEnabled;

    public Stability(Simulation simulation) {
        this.simulation = simulation;
        strategyMap = new HashMap<>();
        strategyProgression = new ArrayList<>();
    }

    @Override
    public boolean isStochastic() {
        return false;
    }

    @Override
    public void reset() {
        strategyProgression.clear();
    }

    @Override
    public void step(SimState simState) {
        System.out.println("Stability");

        // Cycle detection
        if (cycleDetectionEnabled) {
            strategyMap.clear();
            for (Agent agent : simulation.getConfig().getNetwork().getVertices()) {
                strategyMap.put(agent, agent.getStrategy());
            }

            int currentHashCode = strategyMap.hashCode();
            for (int hashCode : strategyProgression) {
                if (hashCode == currentHashCode) {
                    int start = strategyProgression.indexOf(hashCode) + 1;
                    int end = strategyProgression.size() + 1;
                    simulation.getResult().setCycle(start, end);
                    simulation.kill();
                }
            }

            strategyProgression.add(currentHashCode);
        }

        // Only one strategy left
        boolean stable = true;
        Strategy stableStrategy = null;
        for (Agent agent : simulation.getConfig().getNetwork().getVertices()) {
            if (stableStrategy == null) {
                stableStrategy = agent.getStrategy();
            } else if (stableStrategy != agent.getStrategy()) {
                stable = false;
                break;
            }
        }

        if (stable) {
            simulation.kill();
        }
    }

    public void setCycleDetectionEnabled(boolean cycleDetectionEnabled) {
        this.cycleDetectionEnabled = cycleDetectionEnabled;
    }
}
