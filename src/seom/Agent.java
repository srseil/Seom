package seom;

import seom.games.Strategy;

import java.awt.*;

public class Agent {
    private static int nextId = 0;

    private final int id;
    private Strategy strategy;
    private int score;

    public Agent() {
        id = nextId++;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
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

    public int getScore() {
        return score;
    }

    public void increaseScore(int value) {
        score += value;
    }

    public void resetScore() {
        score = 0;
    }
}
