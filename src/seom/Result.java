package seom;

import seom.games.Strategy;

import java.util.List;

public class Result {
    private final Configuration config;

    private long generationCount;
    private boolean cyclic;
    private int cycleStart;
    private int cycleEnd;
    private double stability;
    private double morality;

    public Result(Configuration config) {
        this.config = config;
        cyclic = false;
    }

    public void computeOutputs(List<Strategy[]> strategyHistory) {
        int start = 0;
        int end = strategyHistory.size() - 1;
        stability = 1.0 / config.getMaxNumGenerations();

        if (isCyclic()) {
            start = cycleStart;
            end = cycleEnd;
            stability = 1.0 / getCycleLength();
        }

        double relativeMorality = 0.0;
        for (int i = start - 1; i < end; i++) {
            Strategy[] strategies = strategyHistory.get(i);
            int moralAgents = 0;
            for (Strategy strategy : strategies) {
                for (Strategy moralStrategy : config.getGame().getMoralStrategies()) {
                    if (strategy == moralStrategy) {
                        moralAgents++;
                        break;
                    }
                }
            }
            relativeMorality += (double) moralAgents / strategies.length;
        }

        morality = relativeMorality / getCycleLength();
    }

    public long getGenerationCount() {
        return generationCount;
    }

    public void setGenerationCount(long generationCount) {
        this.generationCount = generationCount;
    }

    public void setCycle(int start, int end) {
        cycleStart = start;
        cycleEnd = end;
        cyclic = true;
    }

    public boolean isCyclic() {
        return cyclic;
    }

    public int getCycleStart() {
        return cycleStart;
    }

    public int getCycleEnd() {
        return cycleEnd;
    }

    public int getCycleLength() {
        return cycleEnd - cycleStart + 1;
    }

    public double getStability() {
        return stability;
    }

    public double getMorality() {
        return morality;
    }
}
