package seom.games;

import java.awt.*;

public class BargainingGame implements Game {
    public enum Strategy implements seom.games.Strategy {
        Demand0,
        Demand1,
        Demand2,
        Demand3,
        Demand4,
        Demand5,
        Demand6,
        Demand7,
        Demand8,
        Demand9,
        Demand10;

        @Override
        public Color getColor() {
            return switch (this) {
                case Demand0 -> new Color(0, 0, 0);
                case Demand1 -> new Color(75, 65, 105);
                case Demand2 -> new Color(71, 115, 80);
                case Demand3 -> new Color(138, 72, 58);
                case Demand4 -> new Color(0, 173, 239);
                case Demand5 -> new Color(236, 0, 140);
                case Demand6 -> new Color(255, 242, 0);
                case Demand7 -> new Color(255, 255, 255);
                case Demand8 -> new Color(127, 127, 127);
                case Demand9 -> new Color(0, 166, 79);
                case Demand10 -> new Color(237, 27, 35);
            };
        }
    }

    @Override
    public seom.games.Strategy[] getStrategies() {
        return Strategy.values();
    }

    @Override
    public Payoffs play(seom.games.Strategy... strategies) {
        int sum = 0;
        for (seom.games.Strategy strategy : strategies) {
            Strategy demand = (Strategy) strategy;
            sum += demand.ordinal();
        }

        double[] payoffs = new double[strategies.length];
        if (sum > 10) {
            for (int i = 0; i < strategies.length; i++) {
                payoffs[i] = 0;
            }
        } else {
            for (int i = 0; i < strategies.length; i++) {
                Strategy demand = (Strategy) strategies[i];
                payoffs[i] = demand.ordinal();
            }
        }
        return new Payoffs(payoffs);
    }
}
