package de.lmu.ifi.pms.seom.learning;

import de.lmu.ifi.pms.seom.Agent;
import de.lmu.ifi.pms.seom.games.Game;
import de.lmu.ifi.pms.seom.games.Strategy;
import ec.util.MersenneTwisterFast;

import java.util.*;

public class ImitateProbability implements LearningRule {
    private MersenneTwisterFast random;

    @Override
    public Strategy getUpdatedStrategy(Agent agent, Collection<Agent> neighbors, Game game) {
        var betterNeighbors = new ArrayList<Agent>(neighbors.size());
        double totalScoreDifference = 0.0;
        for (Agent neighbor : neighbors) {
            if (neighbor.getScore() > agent.getScore()) {
                betterNeighbors.add(neighbor);
                totalScoreDifference += neighbor.getScore() - agent.getScore();
            }
        }

        Collections.sort(betterNeighbors);
        if (betterNeighbors.size() == 0) {
            return agent.getStrategy();
        } else if (betterNeighbors.size() == 1) {
            return betterNeighbors.get(0).getStrategy();
        }

        var betterNeighborDistribution = new HashMap<Range, Agent>();
        double lastUpperBound = 0.0;
        for (Agent neighbor : betterNeighbors) {
            double scoreDifference = neighbor.getScore() - agent.getScore();
            double relativeDifference = scoreDifference / totalScoreDifference;
            Range range = new Range(lastUpperBound, lastUpperBound + relativeDifference);
            betterNeighborDistribution.put(range, neighbor);
            lastUpperBound = range.upperBound;
        }

        double rand = random.nextDouble();
        for (Map.Entry<Range, Agent> entry : betterNeighborDistribution.entrySet()) {
            if (entry.getKey().contains(rand)) {
                return entry.getValue().getStrategy();
            }
        }

        assert false;
        return null;
    }

    private static class Range {
        final double lowerBound;
        final double upperBound;

        Range(double lowerBound, double upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        boolean contains(double value) {
            return value >= lowerBound && value < upperBound;
        }
    }

    @Override
    public void setRandom(MersenneTwisterFast random) {
        this.random = random;
    }

    @Override
    public String toString() {
        return "ImitateProbability";
    }
}
