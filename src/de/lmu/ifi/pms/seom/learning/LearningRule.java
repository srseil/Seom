package de.lmu.ifi.pms.seom.learning;

import de.lmu.ifi.pms.seom.Agent;
import de.lmu.ifi.pms.seom.games.Game;
import de.lmu.ifi.pms.seom.games.Strategy;
import ec.util.MersenneTwisterFast;

import java.util.Collection;

public interface LearningRule {
    Strategy getUpdatedStrategy(Agent agent, Collection<Agent> neighbors, Game game);
    void setRandom(MersenneTwisterFast random);
}
