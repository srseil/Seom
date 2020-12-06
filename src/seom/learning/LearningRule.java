package seom.learning;

import ec.util.MersenneTwisterFast;
import seom.Agent;
import seom.games.Game;
import seom.games.Strategy;

import java.util.Collection;

public interface LearningRule {
    Strategy getUpdatedStrategy(Agent agent, Collection<Agent> neighbors, Game game);
    void setRandom(MersenneTwisterFast random);
}
