package seom.learning;

import ec.util.MersenneTwisterFast;
import seom.Agent;
import seom.games.Game;
import seom.games.Strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ImitateAverage implements LearningRule {
    @Override
    public Strategy getUpdatedStrategy(Agent agent, Collection<Agent> neighbors, Game game) {
        var strategyPayoffs = new HashMap<Strategy, ArrayList<Double>>();
        for (Agent neighbor : neighbors) {
            if (!strategyPayoffs.containsKey(neighbor.getStrategy())) {
                strategyPayoffs.put(neighbor.getStrategy(), new ArrayList<>());
            }

            strategyPayoffs.get(neighbor.getStrategy()).add(neighbor.getScore());
        }

        var avgStrategyPayoffs = new HashMap<Strategy, Double>(strategyPayoffs.size());
        for (Map.Entry<Strategy, ArrayList<Double>> entry : strategyPayoffs.entrySet()) {
            double sum = 0.0;
            for (Double score : entry.getValue()) {
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
            return agent.getStrategy();
        }

        if (bestStrategies.size() == 1) {
            return bestStrategies.get(0);
        } else {
            for (Strategy strategy : game.getStrategies()) {
                if (bestStrategies.contains(strategy)) {
                    return strategy;
                }
            }
        }

        assert false;
        return null;
    }

    @Override
    public void setRandom(MersenneTwisterFast random) {
    }

    @Override
    public String toString() {
        return "ImitateAverage";
    }
}
