package de.lmu.ifi.pms.seom.games;

import java.awt.*;

public class BargainingSubgame implements Game {
    public enum Strategy implements de.lmu.ifi.pms.seom.games.Strategy {
        Demand4,
        Demand5,
        Demand6;

        @Override
        public int getId() {
            return ordinal();
        }

        @Override
        public Color getColor() {
            switch (this) {
                case Demand4: return new Color(0, 173, 239);
                case Demand5: return new Color(236, 0, 140);
                case Demand6: return new Color(255, 242, 0);
                default: return null;
            }
        }
    }

    @Override
    public boolean isSequential() {
        return false;
    }

    @Override
    public de.lmu.ifi.pms.seom.games.Strategy[] getStrategies() {
        return Strategy.values();
    }

    @Override
    public de.lmu.ifi.pms.seom.games.Strategy[] getMoralStrategies() {
        return new Strategy[] {Strategy.Demand5};
    }

    @Override
    public Payoffs play(de.lmu.ifi.pms.seom.games.Strategy... strategies) {
        int sum = 0;
        for (de.lmu.ifi.pms.seom.games.Strategy strategy : strategies) {
            Strategy demand = (Strategy) strategy;
            switch (demand) {
                case Demand4: sum += 4; break;
                case Demand5: sum += 5; break;
                case Demand6: sum += 6; break;
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
                    case Demand4: payoffs[i] = 4; break;
                    case Demand5: payoffs[i] = 5; break;
                    case Demand6: payoffs[i] = 6; break;
                }
            }
        }
        return new Payoffs(payoffs);
    }

    @Override
    public String toString() {
        return "BargainingSubgame";
    }
}
