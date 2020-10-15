package seom.games;

import java.util.Arrays;

public class StrategyProfile {
    private final Strategy[] strategies;

    public StrategyProfile(Strategy... strategies) {
        this.strategies = strategies;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StrategyProfile)) return false;
        StrategyProfile other = (StrategyProfile) obj;
        return Arrays.equals(this.strategies, other.strategies);
    }

    @Override
    public int hashCode() {
        int code = strategies[0].hashCode();
        for (int i = 1; i < strategies.length; i++) {
            code = code ^ strategies[1].hashCode();
        }
        return code;
    }
}
