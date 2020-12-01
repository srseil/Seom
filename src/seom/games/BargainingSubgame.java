package seom.games;

import java.awt.*;

public class BargainingSubgame implements Game {
    public enum Strategy implements seom.games.Strategy {
        Demand4,
        Demand5,
        Demand6;

        @Override
        public int getId() {
            return ordinal();
        }

        @Override
        public Color getColor() {
            return switch (this) {
                case Demand4 -> new Color(0, 173, 239);
                case Demand5 -> new Color(236, 0, 140);
                case Demand6 -> new Color(255, 242, 0);
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
            switch (demand) {
                case Demand4 -> sum += 4;
                case Demand5 -> sum += 5;
                case Demand6 -> sum += 6;
            }
        }

        double[] payoffs = new double[strategies.length];
        if (sum > 10) {
            for (int i = 0; i < strategies.length; i++) {
                payoffs[i] = 0;
            }
        } else {
            for (int i = 0; i < strategies.length; i++) {
                Strategy demand = (Strategy) strategies[i];
                switch (demand) {
                    case Demand4 -> payoffs[i] = 4;
                    case Demand5 -> payoffs[i] = 5;
                    case Demand6 -> payoffs[i] = 6;
                }
            }
        }
        return new Payoffs(payoffs);
    }
}
