package seom.learning;

import ec.util.MersenneTwisterFast;
import seom.Agent;
import seom.games.Game;
import seom.games.Payoffs;
import seom.games.Strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BestResponse implements LearningRule {
    @Override
    public void updateStrategy(Agent agent, Collection<Agent> neighbors, Game game) {
        var strategyScores = new HashMap<Strategy, Double>();
        for (Strategy strategy : game.getStrategies()) {
            if (agent.getStrategy() == strategy) {
                continue;
            }

            double score = 0;
            for (Agent neighbor : neighbors) {
                Payoffs payoffs = game.play(strategy, neighbor.getStrategy());
                score += payoffs.getPayoffForPlayer(0);
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
            return;
        }

        if (bestStrategies.size() == 1) {
            agent.setStrategy(bestStrategies.get(0));
        } else {
            for (Strategy strategy : game.getStrategies()) {
                if (bestStrategies.contains(strategy)) {
                    agent.setStrategy(strategy);
                }
            }
        }
    }

    @Override
    public void setRandom(MersenneTwisterFast random) {
    }
}
