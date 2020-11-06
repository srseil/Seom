package seom;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import seom.games.Game;
import seom.learning.LearningRule;
import seom.networks.Edge;
import seom.utils.JavaRandomFacade;

public class Configuration {
    private MersenneTwisterFast random;
    private JavaRandomFacade javaRandom;
    private Game game;
    private LearningRule learningRule;
    private UndirectedSparseMultigraph<Agent, Edge> network;
    private double mutationProbability;
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
        javaRandom = new JavaRandomFacade(random);
        if (learningRule != null) {
            learningRule.setRandom(random);
        }
    }

    public JavaRandomFacade getJavaRandom() {
        return javaRandom;
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
        if (random != null) {
            learningRule.setRandom(random);
        }
    }

    public UndirectedSparseMultigraph<Agent, Edge> getNetwork() {
        return network;
    }

    public void setNetwork(UndirectedSparseMultigraph<Agent, Edge> network) {
        this.network = network;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
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
