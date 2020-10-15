package seom;

import edu.uci.ics.jung.graph.Graph;
import seom.games.Game;
import seom.learning.LearningRule;

public class Configuration {
    private Game game;
    private LearningRule learningRule;
    private Graph<Agent, Relationship> network;
    private double learningProbability;

    public boolean validate() {
        return game != null && learningRule != null && network != null;
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

    public Graph<Agent, Relationship> getNetwork() {
        return network;
    }

    public void setNetwork(Graph<Agent, Relationship> network) {
        this.network = network;
    }

    public double getLearningProbability() {
        return learningProbability;
    }

    public void setLearningProbability(double learningProbability) {
        this.learningProbability = learningProbability;
    }
}
