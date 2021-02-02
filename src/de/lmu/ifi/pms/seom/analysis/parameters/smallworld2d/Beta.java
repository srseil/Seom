package de.lmu.ifi.pms.seom.analysis.parameters.smallworld2d;

import de.lmu.ifi.pms.seom.analysis.parameters.Parameter;
import de.lmu.ifi.pms.seom.networks.SmallWorld2DBuilder;
import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.analysis.GameType;
import de.lmu.ifi.pms.seom.analysis.NetworkType;

public class Beta implements Parameter<Double> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.SmallWorld2D;
    }

    @Override
    public Double getNominal() {
        return 0.09;
    }

    @Override
    public Double[] getValues() {
        return new Double[] {
            0.008,
            0.04,
            0.09,
            0.17,
            0.275
        };
    }

    @Override
    public void apply(Double value, Configuration config) {
        assert config.getNetworkBuilder() instanceof SmallWorld2DBuilder : "Incorrect network builder";
        var builder = (SmallWorld2DBuilder) config.getNetworkBuilder();
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
