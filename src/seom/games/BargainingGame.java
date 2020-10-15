package seom.games;

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
        Demand10
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

        int[] payoffs = new int[strategies.length];
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
