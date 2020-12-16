package seom.analysis.parameters;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.games.Payoffs;
import seom.games.StagHunt;

public class RiskDominance implements Parameter<Boolean> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return gameType == GameType.StagHunt;
    }

    @Override
    public Boolean getNominal() {
        return true;
    }

    @Override
    public Boolean[] getValues() {
        return new Boolean[] {
            true,
            false
        };
    }

    @Override
    public void apply(Boolean value, Configuration config) {
        if (value) {
            config.setGame(new StagHunt(
                new Payoffs(3, 3),
                new Payoffs(0, 2),
                new Payoffs(2, 0),
                new Payoffs(1, 1)
            ));
        } else {
            config.setGame(new StagHunt(
                new Payoffs(2, 2),
                new Payoffs(0, 1),
                new Payoffs(1, 0),
                new Payoffs(1, 1)
            ));
        }
    }

    @Override
    public String getValueName(Boolean value) {
        return value.toString();
    }

    @Override
    public String getName() {
        return "risk dominance";
    }
}
