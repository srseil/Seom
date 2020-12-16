package seom.analysis.parameters.smallworld1d;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.analysis.parameters.Parameter;
import seom.networks.SmallWorld1DBuilder;

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
