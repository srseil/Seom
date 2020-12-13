package seom.games;

import java.awt.*;
import java.util.HashMap;

public class PrisonersDilemma implements Game {
    private final HashMap<StrategyProfile, Payoffs> results = new HashMap<>();

    public enum Strategy implements seom.games.Strategy {
        Cooperate,
        Defect;

        @Override
        public int getId() {
            return ordinal();
        }

        @Override
        public Color getColor() {
            switch (this) {
                case Cooperate: return Color.WHITE;
                case Defect: return Color.BLACK;
                default: return null;
            }
        }
    }

    public PrisonersDilemma() {
        results.put(new StrategyProfile(Strategy.Cooperate, Strategy.Cooperate), new Payoffs(2, 2));
        results.put(new StrategyProfile(Strategy.Cooperate, Strategy.Defect),    new Payoffs(0, 3));
        results.put(new StrategyProfile(Strategy.Defect,    Strategy.Cooperate), new Payoffs(3, 0));
        results.put(new StrategyProfile(Strategy.Defect,    Strategy.Defect),    new Payoffs(1, 1));
    }

    public PrisonersDilemma(Payoffs cc, Payoffs cd, Payoffs dc, Payoffs dd) {
        results.put(new StrategyProfile(Strategy.Cooperate, Strategy.Cooperate), cc);
        results.put(new StrategyProfile(Strategy.Cooperate, Strategy.Defect),    cd);
        results.put(new StrategyProfile(Strategy.Defect,    Strategy.Cooperate), dc);
        results.put(new StrategyProfile(Strategy.Defect,    Strategy.Defect),    dd);
    }

    @Override
    public boolean isSequential() {
        return false;
    }

    @Override
    public seom.games.Strategy[] getStrategies() {
        return Strategy.values();
    }

    @Override
    public seom.games.Strategy[] getMoralStrategies() {
        return new Strategy[] {Strategy.Cooperate};
    }

    @Override
    public Payoffs play(seom.games.Strategy... strategies) {
        StrategyProfile profile = new StrategyProfile(strategies);
        return results.get(profile);
    }

    @Override
    public String toString() {
        return "PrisonersDilemma";
    }
}
