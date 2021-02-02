package de.lmu.ifi.pms.seom.analysis.parameters.smallworld1d;

import de.lmu.ifi.pms.seom.analysis.parameters.Parameter;
import de.lmu.ifi.pms.seom.networks.SmallWorld1DBuilder;
import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.analysis.GameType;
import de.lmu.ifi.pms.seom.analysis.NetworkType;

public class Beta implements Parameter<Double> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.SmallWorld1D;
    }

    @Override
    public Double getNominal() {
        return 0.125;
    }

    @Override
    public Double[] getValues() {
        return new Double[] {
            0.03,
            0.07,
            0.125,
            0.18,
            0.28
        };
    }

    @Override
    public void apply(Double value, Configuration config) {
        assert config.getNetworkBuilder() instanceof SmallWorld1DBuilder : "Incorrect network builder";
        var builder = (SmallWorld1DBuilder) config.getNetworkBuilder();
        builder.setBeta(value);
    }

    @Override
    public String getValueName(Double value) {
        return value.toString();
    }

    @Override
    public String getName() {
        return "beta";
    }
}
