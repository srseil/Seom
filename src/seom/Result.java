package seom;

public class Result {
    private boolean cyclic;
    private int cycleStart;
    private int cycleEnd;

    public Result() {
        cyclic = false;
    }

    public void setCycle(int start, int end) {
        cyclic = true;
        cycleStart = start;
        cycleEnd = end;
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
}
