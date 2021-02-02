package de.lmu.ifi.pms.seom.analysis.parameters.fullyrandom;

import de.lmu.ifi.pms.seom.networks.FullyRandomBuilder;
import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.analysis.GameType;
import de.lmu.ifi.pms.seom.analysis.NetworkType;
import de.lmu.ifi.pms.seom.analysis.parameters.Parameter;

public class EdgeProbability implements Parameter<Double> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.FullyRandom;
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
        assert config.getNetworkBuilder() instanceof FullyRandomBuilder : "Incorrect network builder";
        var builder = (FullyRandomBuilder) config.getNetworkBuilder();
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
