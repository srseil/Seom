package de.lmu.ifi.pms.seom.learning;

import de.lmu.ifi.pms.seom.Agent;
import de.lmu.ifi.pms.seom.games.Game;
import de.lmu.ifi.pms.seom.games.Strategy;
import ec.util.MersenneTwisterFast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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

        Collections.sort(bestNeighbors);
        return bestNeighbors.get(0).getStrategy();
    }

    @Override
    public void setRandom(MersenneTwisterFast random) {
    }

    @Override
    public String toString() {
        return "ImitateBest";
    }
}
