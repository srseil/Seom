package seom;

import java.util.HashMap;

public class PrisonersDilemma implements IGame {
    private final HashMap<StrategyProfile, Payoffs> results = new HashMap<>();

    public enum Strategy implements IStrategy {
        Cooperate,
        Defect
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
    public Payoffs play(IStrategy... strategies) {
        StrategyProfile profile = new StrategyProfile(strategies);
        return results.get(profile);
    }
}
