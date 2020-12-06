package seom.learning;

import ec.util.MersenneTwisterFast;
import seom.Agent;
import seom.games.Game;
import seom.games.Strategy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class ImitateBest implements LearningRule {
    @Override
    public Strategy getUpdatedStrategy(Agent agent, Collection<Agent> neighbors, Game game) {
        var bestNeighbors = new ArrayList<Agent>(neighbors.size());
        double bestNeighborScore = 0.0;
        for (Agent neighbor : neighbors) {
            if (neighbor.getScore() == bestNeighborScore) {
                bestNeighbors.add(neighbor);
            } else if (neighbor.getScore() > bestNeighborScore) {
                bestNeighbors.clear();
                bestNeighbors.add(neighbor);
                bestNeighborScore = neighbor.getScore();
            }
        }

        if (agent.getScore() >= bestNeighborScore) {
            return agent.getStrategy();
        }

        bestNeighbors.sort(Comparator.comparingInt(Agent::getId));
        return bestNeighbors.get(0).getStrategy();
    }

    @Override
    public void setRandom(MersenneTwisterFast random) {
    }
}
