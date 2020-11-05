package seom;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import seom.games.Game;
import seom.learning.LearningRule;
import seom.networks.Edge;

public class Configuration {
    private MersenneTwisterFast random;
    private Game game;
    private LearningRule learningRule;
    private UndirectedSparseMultigraph<Agent, Edge> network;
    private double learningProbability;
    private boolean interactionsVisualized;
    private boolean learningVisualized;

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

    public UndirectedSparseMultigraph<Agent, Edge> getNetwork() {
        return network;
    }

    public void setNetwork(UndirectedSparseMultigraph<Agent, Edge> network) {
        this.network = network;
    }

    public double getLearningProbability() {
        return learningProbability;
    }

    public void setLearningProbability(double learningProbability) {
        this.learningProbability = learningProbability;
    }

    public boolean areInteractionsVisualized() {
        return interactionsVisualized;
    }

    public void setInteractionsVisualized(boolean interactionsVisualized) {
        this.interactionsVisualized = interactionsVisualized;
    }

    public boolean isLearningVisualized() {
        return learningVisualized;
    }

    public void setLearningVisualized(boolean learningVisualized) {
        this.learningVisualized = learningVisualized;
    }
}
