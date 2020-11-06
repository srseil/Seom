package seom.learning;

import ec.util.MersenneTwisterFast;
import seom.Agent;
import seom.games.Game;

import java.util.Collection;

public interface LearningRule {
    void updateStrategy(Agent agent, Collection<Agent> neighbors, Game game);
    void setRandom(MersenneTwisterFast random);
}
