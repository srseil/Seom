package de.lmu.ifi.pms.seom.analysis.parameters.smallworld2d;

import de.lmu.ifi.pms.seom.analysis.parameters.Parameter;
import de.lmu.ifi.pms.seom.networks.SmallWorld2DBuilder;
import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.analysis.GameType;
import de.lmu.ifi.pms.seom.analysis.NetworkType;

public class WrapAround implements Parameter<Boolean> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.SmallWorld2D;
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
        assert config.getNetworkBuilder() instanceof SmallWorld2DBuilder : "Incorrect network builder";
        var builder = (SmallWorld2DBuilder) config.getNetworkBuilder();
        builder.setWrapAround(value);
    }

    @Override
    public String getValueName(Boolean value) {
        return value.toString();
    }

    @Override
    public String getName() {
        return "wrap around";
    }
}
