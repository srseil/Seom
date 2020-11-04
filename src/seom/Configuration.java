package seom;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.Graph;
import seom.games.Game;
import seom.learning.LearningRule;
import seom.networks.Edge;

public class Configuration {
    private MersenneTwisterFast random;
    private Game game;
    private LearningRule learningRule;
    private Graph<Agent, Edge> network;
    private double learningProbability;

    public boolean validate() {
        return game != null && learningRule != null && network != null;
    }

    public MersenneTwisterFast getRandom() {
        return random;
    }

    public void setRandom(MersenneTwisterFast random) {
        this.random = random;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public LearningRule getLearningRule() {
        return learningRule;
    }

    public void setLearningRule(LearningRule learningRule) {
        this.learningRule = learningRule;
    }

    public Graph<Agent, Edge> getNetwork() {
        return network;
    }

    public void setNetwork(Graph<Agent, Edge> network) {
        this.network = network;
    }

    public double getLearningProbability() {
        return learningProbability;
    }

    public void setLearningProbability(double learningProbability) {
        this.learningProbability = learningProbability;
    }
}
