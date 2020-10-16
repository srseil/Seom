package seom.learning;

import ec.util.MersenneTwisterFast;
import seom.Agent;
import seom.Simulation;
import seom.games.Game;

import java.util.ArrayList;
import java.util.Collection;

public class ImitateBest implements LearningRule {
    private final MersenneTwisterFast random = new MersenneTwisterFast(Simulation.getSeed());

    @Override
    public void updateStrategy(Agent agent, Collection<Agent> neighbors, Game game) {
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
            return;
        }

        if (bestNeighbors.size() == 1) {
            agent.setStrategy(bestNeighbors.get(0).getStrategy());
        } else {
            int rand = random.nextInt(bestNeighbors.size());
            agent.setStrategy(bestNeighbors.get(rand).getStrategy());
        }
    }
}
