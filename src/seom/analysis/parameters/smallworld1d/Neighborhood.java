package seom.analysis.parameters.smallworld1d;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.analysis.parameters.Parameter;
import seom.networks.SmallWorld1DBuilder;

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
            1,
            2,
            3
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
