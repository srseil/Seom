package seom.analysis.parameters.randomnetwork;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.analysis.parameters.Parameter;
import seom.networks.RandomNetworkBuilder;

public class EdgeProbability implements Parameter<Double> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.RandomNetwork;
    }

    @Override
    public Double getNominal() {
        return 0.008;
    }

    @Override
    public Double[] getValues() {
        return new Double[] {
            0.004,
            0.008,
            0.012
        };
    }

    @Override
    public void apply(Double value, Configuration config) {
        assert config.getNetworkBuilder() instanceof RandomNetworkBuilder : "Incorrect network builder";
        var builder = (RandomNetworkBuilder) config.getNetworkBuilder();
        builder.setEdgeProbability(value);
    }

    @Override
    public String getValueName(Double value) {
        return value.toString();
    }

    @Override
    public String getName() {
        return "edge probability";
    }
}
