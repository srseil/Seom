package seom.analysis.parameters.smallworld1d;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.analysis.parameters.Parameter;
import seom.networks.SmallWorld1DBuilder;

public class WrapAround implements Parameter<Boolean> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.SmallWorld1D;
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
        assert config.getNetworkBuilder() instanceof SmallWorld1DBuilder : "Incorrect network builder";
        var builder = (SmallWorld1DBuilder) config.getNetworkBuilder();
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
