package de.lmu.ifi.pms.seom.learning;

import de.lmu.ifi.pms.seom.Agent;
import de.lmu.ifi.pms.seom.games.Game;
import de.lmu.ifi.pms.seom.games.Payoffs;
import de.lmu.ifi.pms.seom.games.Strategy;
import ec.util.MersenneTwisterFast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BestResponse implements LearningRule {
    @Override
    public Strategy getUpdatedStrategy(Agent agent, Collection<Agent> neighbors, Game game) {
        var strategyScores = new HashMap<Strategy, Double>();
        for (Strategy strategy : game.getStrategies()) {
            if (agent.getStrategy() == strategy) {
                continue;
            }

            double score = 0.0;
            for (Agent neighbor : neighbors) {
                Payoffs payoffs = game.play(strategy, neighbor.getStrategy());
                score += payoffs.getPayoffForPlayer(0);

                if (game.isSequential()) {
                    Payoffs reversePayoffs = game.play(neighbor.getStrategy(), strategy);
                    score += reversePayoffs.getPayoffForPlayer(1);
                }
            }

            strategyScores.put(strategy, score);
        }

        var bestStrategies = new ArrayList<Strategy>(strategyScores.size());
        double bestStrategyScore = 0.0;
        for (Map.Entry<Strategy, Double> entry : strategyScores.entrySet()) {
            if (entry.getValue() == bestStrategyScore) {
                bestStrategies.add(entry.getKey());
            } else if (entry.getValue() > bestStrategyScore) {
                bestStrategies.clear();
                bestStrategies.add(entry.getKey());
                bestStrategyScore = entry.getValue();
            }
        }

        if (agent.getScore() >= bestStrategyScore) {
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
        return "BestResponse";
    }
}
