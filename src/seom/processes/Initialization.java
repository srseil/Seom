package seom.processes;

import seom.Agent;
import seom.Configuration;
import seom.games.Strategy;
import seom.utils.Log;
import sim.engine.SimState;

import java.util.*;

public class Initialization implements SimulationProcess {
    private final Configuration config;
    private final double[] strategyProbabilities;

    public Initialization(Configuration config) {
        this.config = config;

        Strategy[] gameStrategies = config.getGame().getStrategies();
        strategyProbabilities = new double[config.getGame().getStrategies().length];
        List<Strategy> moralStrategies = Arrays.asList(config.getGame().getMoralStrategies());

        double probabilitySum = 0.0;
        for (int i = 0; i < gameStrategies.length; i++) {
            double mean = moralStrategies.contains(gameStrategies[i])
                ? config.getInitialMoralMean()
                : 0.5;
            double rand = config.getRandom().nextGaussian() * 0.1 + mean;
            strategyProbabilities[i] = Math.min(Math.max(0.0, rand), 1.0);
            probabilitySum += strategyProbabilities[i];
        }

        for (int i = 0; i < strategyProbabilities.length; i++) {
            if (i == strategyProbabilities.length - 1) {
                strategyProbabilities[i] = 1.0;
                break;
            }

            strategyProbabilities[i] /= probabilitySum;

            if (i > 0) {
                strategyProbabilities[i] += strategyProbabilities[i - 1];
            }
        }
    }

    @Override
    public boolean isStochastic() {
        // Even though it is randomized, the initial distribution of
        // strategies is not considered to be a stochastic process.
        return false;
    }

    @Override
    public void reset() {
    }

    @Override
    public void step(SimState simState) {
        Log.fine("Initialization");

        var sortedAgents = new ArrayList<>(config.getNetwork().getVertices());
        Collections.sort(sortedAgents);
        for (Agent agent : sortedAgents) {
            int strategyIndex;
            double rand = config.getRandom().nextDouble();
            for (strategyIndex = 0; strategyIndex < strategyProbabilities.length; strategyIndex++) {
                if (rand < strategyProbabilities[strategyIndex]) break;
            }
            agent.setStrategy(config.getGame().getStrategies()[strategyIndex]);
        }
    }
}
