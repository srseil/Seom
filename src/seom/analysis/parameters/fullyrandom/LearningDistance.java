package seom.analysis.parameters.fullyrandom;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.analysis.parameters.Parameter;
import seom.networks.FullyRandomBuilder;

public class LearningDistance implements Parameter<Integer> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.FullyRandom;
    }

    @Override
    public Integer getNominal() {
        return 1;
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
        assert config.getNetworkBuilder() instanceof FullyRandomBuilder : "Incorrect network builder";
        var builder = (FullyRandomBuilder) config.getNetworkBuilder();
        builder.setLearningDistance(value);
    }

    @Override
    public String getValueName(Integer value) {
        return value.toString();
    }

    @Override
    public String getName() {
        return "learning distance";
    }
}
