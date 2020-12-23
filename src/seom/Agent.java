package seom;

import seom.games.Strategy;

import java.awt.*;

public class Agent implements Comparable<Agent> {
    private static int nextId = 0;

    private final int id;
    private Strategy strategy;
    private double score;

    public Agent() {
        id = nextId++;
    }

    public Paint getStrategyColor() {
        if (strategy != null) {
            return strategy.getColor();
        } else {
            return Color.WHITE;
        }
    }

    public int getId() {
        return id;
    }

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    public double getScore() {
        return score;
    }

    public void increaseScore(double value) {
        score += value;
    }

    public void resetScore() {
        score = 0.0;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    @Override
    public int compareTo(Agent o) {
        return Integer.compare(id, o.id);
    }

    public static void resetIds() {
        nextId = 0;
    }
}
