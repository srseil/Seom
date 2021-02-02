package de.lmu.ifi.pms.seom.analysis.parameters.smallworld1d;

import de.lmu.ifi.pms.seom.analysis.GameType;
import de.lmu.ifi.pms.seom.analysis.NetworkType;
import de.lmu.ifi.pms.seom.analysis.parameters.Parameter;
import de.lmu.ifi.pms.seom.networks.SmallWorld1DBuilder;
import de.lmu.ifi.pms.seom.Configuration;

public class Neighborhood implements Parameter<Integer> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.SmallWorld1D;
    }

    @Override
    public Integer getNominal() {
        return 2;
    }

    @Override
    public Integer[] getValues() {
        return new Integer[] {
            2,
            3,
            4
        };
    }

    @Override
    public void apply(Integer value, Configuration config) {
        assert config.getNetworkBuilder() instanceof SmallWorld1DBuilder : "Incorrect network builder";
        var builder = (SmallWorld1DBuilder) config.getNetworkBuilder();
        builder.setInteractionDistance(value);
    }

    @Override
    public String getValueName(Integer value) {
        return value.toString();
    }

    @Override
    public String getName() {
        return "neighborhood";
    }
}
