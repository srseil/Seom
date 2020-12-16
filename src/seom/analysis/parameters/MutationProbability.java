package seom.analysis.parameters;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;

public class MutationProbability implements Parameter<Double> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return true;
    }

    @Override
    public Double getNominal() {
        return 0.0;
    }

    @Override
    public Double[] getValues() {
        return new Double[] {
            0.0,
            0.001,
            0.01,
            0.1
        };
    }

    @Override
    public void apply(Double value, Configuration config) {
        config.setMutationProbability(value);
    }

    @Override
    public String getValueName(Double value) {
        return value.toString();
    }

    @Override
    public String getName() {
        return "mutation probability";
    }
}
