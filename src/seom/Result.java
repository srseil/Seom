package seom;

import seom.games.Strategy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result {
    private final Configuration config;

    private List<Strategy[]> strategyHistory;
    private Map<Strategy, Integer> strategyMap;

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
        this.strategyHistory = strategyHistory;
        computeStrategyDistribution();
        computeMorality();
    }

    private void computeStrategyDistribution() {
        int start = 0;
        int end = strategyHistory.size() - 1;
        stability = 1.0 / config.getMaxNumGenerations();

        if (isCyclic()) {
            start = cycleStart - 1;
            end = cycleEnd - 1;
            stability = 1.0 / getCycleLength();
        }

        strategyMap = new HashMap<>();
        for (int i = start; i <= end; i++) {
            for (Strategy strategy : strategyHistory.get(i)) {
                int numAgents = strategyMap.getOrDefault(strategy, 0);
                strategyMap.put(strategy, numAgents + 1);
            }
        }
    }

    private void computeMorality() {
        int moralAgents = 0;
        for (var entry : strategyMap.entrySet()) {
            for (Strategy moralStrategy : config.getGame().getMoralStrategies()) {
                if (entry.getKey() == moralStrategy) {
                    moralAgents += entry.getValue();
                    break;
                }
            }
        }

        int numAgents = strategyHistory.get(0).length;
        int numGenerations = cyclic ? getCycleLength() : strategyHistory.size();
        morality = (double) moralAgents / (numAgents * numGenerations);
    }

    @Override
    public String toString() {
        if (!cyclic) {
            return "(no cycle, " + "stability: " + stability + ", morality: " + morality + ")";
        } else {
            return "(" + "cycle length: " + getCycleLength() + ", "
                + "stability: " + stability + ", morality: " + morality + ")";
        }
    }

    public Map<Strategy, Integer> getStrategyMap() {
        return strategyMap;
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
