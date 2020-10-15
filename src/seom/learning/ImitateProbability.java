package seom.learning;

import ec.util.MersenneTwisterFast;
import seom.Agent;
import seom.Simulation;
import seom.games.Game;

import java.util.ArrayList;
import java.util.Collection;

public class ImitateProbability implements LearningRule {
    private final MersenneTwisterFast random = new MersenneTwisterFast(Simulation.getSeed());

    @Override
    public void updateStrategy(Agent agent, Collection<Agent> neighbors, Game game) {
        var betterNeighbors = new ArrayList<Agent>(neighbors.size());
        var totalScoreDifference = 0;
        for (Agent neighbor : neighbors) {
            if (neighbor.getScore() > agent.getScore()) {
                betterNeighbors.add(neighbor);
                totalScoreDifference += neighbor.getScore() - agent.getScore();
            }
        }

        if (betterNeighbors.size() == 1) {
            agent.setStrategy(betterNeighbors.get(0).getStrategy());
            return;
        }

        var betterNeighborsDistribution = new ArrayList<Agent>(totalScoreDifference);
        for (Agent neighbor : betterNeighbors) {
            int scoreDifference = neighbor.getScore() - agent.getScore();
            for (int i = 0; i < scoreDifference; i++) {
                betterNeighborsDistribution.add(neighbor);
            }
        }

        int rand = random.nextInt(betterNeighborsDistribution.size());
        agent.setStrategy(betterNeighborsDistribution.get(rand).getStrategy());
    }
}
