package seom.games;

import java.awt.*;
import java.util.HashMap;

public class StagHunt implements Game {
    private final HashMap<StrategyProfile, Payoffs> results = new HashMap<>();

    public enum Strategy implements seom.games.Strategy {
        Stag,
        Hare;

        @Override
        public int getId() {
            return ordinal();
        }

        @Override
        public Color getColor() {
            switch (this) {
                case Stag: return Color.LIGHT_GRAY;
                case Hare: return Color.BLACK;
                default: return null;
            }
        }
    }

    public StagHunt() {
        results.put(new StrategyProfile(Strategy.Stag, Strategy.Stag), new Payoffs(2, 2));
        results.put(new StrategyProfile(Strategy.Stag, Strategy.Hare), new Payoffs(0, 1));
        results.put(new StrategyProfile(Strategy.Hare, Strategy.Stag), new Payoffs(1, 0));
        results.put(new StrategyProfile(Strategy.Hare, Strategy.Hare), new Payoffs(1, 1));
    }

    public StagHunt(Payoffs ss, Payoffs sh, Payoffs hs, Payoffs hh) {
        results.put(new StrategyProfile(Strategy.Stag, Strategy.Stag), ss);
        results.put(new StrategyProfile(Strategy.Stag, Strategy.Hare), sh);
        results.put(new StrategyProfile(Strategy.Hare, Strategy.Stag), hs);
        results.put(new StrategyProfile(Strategy.Hare, Strategy.Hare), hh);
    }

    @Override
    public seom.games.Strategy[] getStrategies() {
        return Strategy.values();
    }

    @Override
    public Payoffs play(seom.games.Strategy... strategies) {
        StrategyProfile profile = new StrategyProfile(strategies);
        return results.get(profile);
    }
}
