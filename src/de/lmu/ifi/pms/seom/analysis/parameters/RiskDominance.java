package de.lmu.ifi.pms.seom.analysis.parameters;

import de.lmu.ifi.pms.seom.games.Payoffs;
import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.analysis.GameType;
import de.lmu.ifi.pms.seom.analysis.NetworkType;
import de.lmu.ifi.pms.seom.games.StagHunt;

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
