package seom.games;

import java.util.Arrays;

public interface IGame {
    interface IStrategy { }

    Payoffs play(IStrategy... strategies);

    class StrategyProfile {
        private final IStrategy[] strategies;

        public StrategyProfile(IStrategy... strategies) {
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

    class Payoffs {
        private final int[] payoffs;

        public Payoffs(int... payoffs) {
            this.payoffs = payoffs;
        }

        public int[] getPayoffs() {
            return payoffs;
        }

        public int getPayoffForPlayer(int player) {
            return payoffs[player];
        }
    }
}
