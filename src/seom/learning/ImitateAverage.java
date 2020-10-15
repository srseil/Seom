package seom.learning;

import ec.util.MersenneTwisterFast;
import seom.Agent;
import seom.Simulation;
import seom.games.Game;
import seom.games.Strategy;

import java.util.*;

public class ImitateAverage implements LearningRule {
    private final MersenneTwisterFast random = new MersenneTwisterFast(Simulation.getSeed());

    @Override
    public void updateStrategy(Agent agent, Collection<Agent> neighbors, Game game) {
        var strategyPayoffs = new HashMap<Strategy, ArrayList<Integer>>();
        for (Agent neighbor : neighbors) {
            if (!strategyPayoffs.containsKey(neighbor.getStrategy())) {
                strategyPayoffs.put(neighbor.getStrategy(), new ArrayList<>());
            }

            strategyPayoffs.get(neighbor.getStrategy()).add(neighbor.getScore());
        }

        var avgStrategyPayoffs = new HashMap<Strategy, Double>(strategyPayoffs.size());
        for (Map.Entry<Strategy, ArrayList<Integer>> entry : strategyPayoffs.entrySet()) {
            double sum = 0;
            for (Integer score : entry.getValue()) {
                sum += score;
            }

            double avg = sum / entry.getValue().size();
            avgStrategyPayoffs.put(entry.getKey(), avg);
        }

        var bestStrategies = new ArrayList<Strategy>(avgStrategyPayoffs.size());
        double maxAvgPayoff = 0.0;
        for (Map.Entry<Strategy, Double> entry : avgStrategyPayoffs.entrySet()) {
            if (entry.getValue() == maxAvgPayoff) {
                bestStrategies.add(entry.getKey());
            } else if (entry.getValue() > maxAvgPayoff) {
                bestStrategies.clear();
                bestStrategies.add(entry.getKey());
                maxAvgPayoff = entry.getValue();
            }
        }

        if (agent.getScore() >= maxAvgPayoff) {
            return;
        }

        if (bestStrategies.size() == 1) {
            agent.setStrategy(bestStrategies.get(0));
        } else {
            int rand = random.nextInt(bestStrategies.size());
            agent.setStrategy(bestStrategies.get(rand));
        }
    }
}
