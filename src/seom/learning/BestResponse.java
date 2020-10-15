package seom.learning;

import ec.util.MersenneTwisterFast;
import seom.Agent;
import seom.Simulation;
import seom.games.Game;
import seom.games.Payoffs;
import seom.games.Strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class BestResponse implements LearningRule {
    private final MersenneTwisterFast random = new MersenneTwisterFast(Simulation.getSeed());

    @Override
    public void updateStrategy(Agent agent, Collection<Agent> neighbors, Game game) {
        var strategyScores = new HashMap<Strategy, Integer>();
        for (Strategy strategy : game.getStrategies()) {
            if (agent.getStrategy() == strategy) {
                continue;
            }

            int score = 0;
            for (Agent neighbor : neighbors) {
                Payoffs payoffs = game.play(strategy, neighbor.getStrategy());
                score += payoffs.getPayoffForPlayer(0);
            }

            strategyScores.put(strategy, score);
        }

        var bestStrategies = new ArrayList<Strategy>(strategyScores.size());
        int bestStrategyScore = 0;
        for (Map.Entry<Strategy, Integer> entry : strategyScores.entrySet()) {
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
            int rand = random.nextInt(bestStrategies.size());
            agent.setStrategy(bestStrategies.get(rand));
        }
    }
}
