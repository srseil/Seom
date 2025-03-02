package de.lmu.ifi.pms.seom.processes;

import de.lmu.ifi.pms.seom.Agent;
import de.lmu.ifi.pms.seom.Simulation;
import de.lmu.ifi.pms.seom.utils.Log;
import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.games.Strategy;
import sim.engine.SimState;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.*;

public class Stability implements SimulationProcess {
    private final Simulation simulation;
    private final Configuration config;
    private final List<Agent> sortedAgents;
    private final ByteBuffer byteBuffer;
    private final List<Strategy[]> strategyHistory;
    private final List<byte[]> strategyHashHistory;

    private boolean cycleDetectionEnabled;
    private boolean homogeneityDetectionEnabled;

    public Stability(Simulation simulation) {
        this.simulation = simulation;
        config = simulation.getConfig();
        sortedAgents = new ArrayList<>(config.getNetwork().getVertices());
        Collections.sort(sortedAgents);
        byteBuffer = ByteBuffer.allocate(Integer.BYTES * config.getNetwork().getVertexCount());
        strategyHistory = new ArrayList<>();
        strategyHashHistory = new ArrayList<>();
    }

    @Override
    public boolean isStochastic() {
        return false;
    }

    @Override
    public void reset() {
        strategyHistory.clear();
        strategyHashHistory.clear();
    }

    @Override
    public void step(SimState simState) {
        Log.fine("Stability");

        // Store strategies in history
        var strategies = new Strategy[sortedAgents.size()];
        for (int i = 0; i < sortedAgents.size(); i++) {
            strategies[i] = sortedAgents.get(i).getStrategy();
        }
        strategyHistory.add(strategies);

        // Maximum number of generations
        if (strategyHistory.size() == config.getMaxNumGenerations()) {
            simulation.getResult().computeOutputs(strategyHistory);
            simulation.kill();
        }

        // Only one strategy left
        if (homogeneityDetectionEnabled) {
            boolean homogenous = true;
            Strategy stableStrategy = null;
            for (Agent agent : config.getNetwork().getVertices()) {
                if (stableStrategy == null) {
                    stableStrategy = agent.getStrategy();
                } else if (stableStrategy != agent.getStrategy()) {
                    homogenous = false;
                    break;
                }
            }
            if (homogenous) {
                int lastStep = strategyHistory.size();
                simulation.getResult().setCycle(lastStep, lastStep);
                simulation.getResult().computeOutputs(strategyHistory);
                simulation.kill();
            }
        }

        // Cycle detection
        if (cycleDetectionEnabled) {
            byteBuffer.clear();
            for (Agent agent : sortedAgents) {
                byteBuffer.putInt(agent.getStrategy().getId());
            }

            MessageDigest sha256 = config.getSha256();
            byte[] currentHash = sha256.digest(byteBuffer.array());

            for (byte[] hash : strategyHashHistory) {
                if (!Arrays.equals(hash, currentHash)) continue;

                int start = strategyHashHistory.indexOf(hash) + 1;
                // Cycle ends one generation before this one
                int end = strategyHashHistory.size();
                simulation.getResult().setCycle(start, end);
                simulation.getResult().computeOutputs(strategyHistory);
                simulation.kill();
            }

            strategyHashHistory.add(currentHash);
        }
    }

    public void setHomogeneityDetectionEnabled(boolean homogeneityDetectionEnabled) {
        this.homogeneityDetectionEnabled = homogeneityDetectionEnabled;
    }

    public void setCycleDetectionEnabled(boolean cycleDetectionEnabled) {
        this.cycleDetectionEnabled = cycleDetectionEnabled;
    }
}
