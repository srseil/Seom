package de.lmu.ifi.pms.seom.analysis.parameters;

import de.lmu.ifi.pms.seom.games.Payoffs;
import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.analysis.GameType;
import de.lmu.ifi.pms.seom.analysis.NetworkType;
import de.lmu.ifi.pms.seom.games.PrisonersDilemma;

public class DefectionIncentive implements Parameter<Double> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return gameType == GameType.PrisonersDilemma;
    }

    @Override
    public Double getNominal() {
        return 1.0;
    }

    @Override
    public Double[] getValues() {
        return new Double[] {
            0.2,
            1.0,
            5.0
        };
    }

    @Override
    public void apply(Double value, Configuration config) {
        // Problem: cooperation incentive nominal value is hard-coded here
        config.setGame(new PrisonersDilemma(
            new Payoffs(2.0, 2.0),
            new Payoffs(0.0, 2.0 + value),
            new Payoffs(2.0 + value , 0.0),
            new Payoffs(1.0, 1.0)
        ));
    }

    @Override
    public String getValueName(Double value) {
        return value.toString();
    }

    @Override
    public String getName() {
        return "defection incentive";
    }
}
