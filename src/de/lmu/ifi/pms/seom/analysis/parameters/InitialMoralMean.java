package de.lmu.ifi.pms.seom.analysis.parameters;

import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.analysis.GameType;
import de.lmu.ifi.pms.seom.analysis.NetworkType;

public class InitialMoralMean implements Parameter<Double> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return true;
    }

    @Override
    public Double getNominal() {
        return 0.5;
    }

    @Override
    public Double[] getValues() {
        return new Double[] {
            0.25,
            0.5,
            0.75
        };
    }

    @Override
    public void apply(Double value, Configuration config) {
        config.setInitialMoralMean(value);
    }

    @Override
    public String getValueName(Double value) {
        return value.toString();
    }

    @Override
    public String getName() {
        return "initial moral mean";
    }
}
