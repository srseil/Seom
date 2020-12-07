package seom.games;

import java.awt.*;

public class UltimatumSubgame implements Game {
    public enum Strategy implements seom.games.Strategy {
        S1Gamesman(9, 5, 9),
        S2(9),
        S3(9, 5),
        S4MadDog(9, 9),
        S5EasyRider(5, 5, 9),
        S6(5),
        S7Fairman(5, 5),
        S8(5, 9);

        private final int demand;
        private final int[] accept;

        Strategy(int demand, int... accept) {
            this.demand = demand;
            this.accept = accept;
        }

        private boolean accepts(int demand) {
            for (int i : accept) {
                if (i == demand) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public int getId() {
            return ordinal();
        }

        @Override
        public Color getColor() {
            switch (this) {
                case S1Gamesman: return new Color(0, 0, 0);
                case S2: return new Color(45, 48, 146);
                case S3: return new Color(0, 166, 79);
                case S4MadDog: return new Color(0, 173, 239);
                case S5EasyRider: return new Color(237, 27, 35);
                case S6: return new Color(236, 0, 140);
                case S7Fairman: return new Color(255, 242, 0);
                case S8: return new Color(255, 255, 255);
                default: return null;
            }
        }
    }

    @Override
    public boolean isSequential() {
        return true;
    }

    @Override
    public seom.games.Strategy[] getStrategies() {
        return Strategy.values();
    }

    @Override
    public seom.games.Strategy[] getMoralStrategies() {
        return new Strategy[] {Strategy.S7Fairman};
    }

    @Override
    public Payoffs play(seom.games.Strategy... strategies) {
        Strategy p1Strategy = (Strategy) strategies[0];
        Strategy p2Strategy = (Strategy) strategies[1];

        if (p2Strategy.accepts(p1Strategy.demand)) {
            return new Payoffs(p1Strategy.demand, 10 - p1Strategy.demand);
        } else {
            return new Payoffs(0, 0);
        }
    }
}
