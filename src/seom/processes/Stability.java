package seom.processes;

import seom.Agent;
import seom.Configuration;
import seom.Simulation;
import seom.games.Strategy;
import sim.engine.SimState;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.*;

public class Stability implements SimulationProcess {
    private final Simulation simulation;
    private final Configuration config;
    private final List<Agent> sortedAgents;
    private final ByteBuffer byteBuffer;
    private final List<byte[]> strategyProgression;

    private boolean cycleDetectionEnabled;

    public Stability(Simulation simulation) {
        this.simulation = simulation;
        config = simulation.getConfig();
        sortedAgents = new ArrayList<>(config.getNetwork().getVertices());
        sortedAgents.sort(Comparator.comparingInt(Agent::getId));
        byteBuffer = ByteBuffer.allocate(Integer.BYTES * config.getNetwork().getVertexCount());
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

        // Maximum number of generations
        if (simState.schedule.getSteps() == config.getMaxNumGenerations()) {
            simulation.kill();
        }

        // Cycle detection
        if (cycleDetectionEnabled) {
            byteBuffer.clear();
            for (Agent agent : sortedAgents) {
                byteBuffer.putInt(agent.getStrategy().getId());
            }

            MessageDigest sha256 = config.getSha256();
            byte[] currentHash = sha256.digest(byteBuffer.array());

            for (byte[] hash : strategyProgression) {
                if (!Arrays.equals(hash, currentHash)) continue;

                int start = strategyProgression.indexOf(hash) + 1;
                // Cycle ends one generation before this one
                int end = strategyProgression.size();
                simulation.getResult().setCycle(start, end);
                simulation.kill();
            }

            strategyProgression.add(currentHash);
        }

        // Only one strategy left
        boolean stable = true;
        Strategy stableStrategy = null;
        for (Agent agent : config.getNetwork().getVertices()) {
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
