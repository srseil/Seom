package seom.learning;

import seom.Agent;
import seom.games.Game;

import java.util.Collection;

public interface LearningRule {
    void updateStrategy(Agent agent, Collection<Agent> neighbors, Game game);
}
