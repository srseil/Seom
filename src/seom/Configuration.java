package seom;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import seom.games.Game;
import seom.learning.LearningRule;
import seom.networks.Edge;
import seom.utils.JavaRandomFacade;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Configuration {
    private MersenneTwisterFast random;
    private JavaRandomFacade javaRandom;
    private final MessageDigest sha256;

    private Game game;
    private LearningRule learningRule;
    private UndirectedSparseMultigraph<Agent, Edge> network;
    private double mutationProbability;
    private int mutationDistance;
    private boolean interactionsVisualized;
    private boolean learningVisualized;
    private boolean cycleDetectionEnabled;

    public Configuration() {
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Missing SHA-256 implementation");
        }
    }

    public boolean validate() {
        return game != null && learningRule != null && network != null;
    }

    public MessageDigest getSha256() {
        return sha256;
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

    public int getMutationDistance() {
        return mutationDistance;
    }

    public void setMutationDistance(int mutationDistance) {
        this.mutationDistance = mutationDistance;
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

    public boolean isCycleDetectionEnabled() {
        return cycleDetectionEnabled;
    }

    public void setCycleDetectionEnabled(boolean cycleDetectionEnabled) {
        this.cycleDetectionEnabled = cycleDetectionEnabled;
    }
}
