package seom.analysis.parameters.randomnetwork;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.analysis.parameters.Parameter;
import seom.networks.RandomNetworkBuilder;

public class LearningDistance implements Parameter<Integer> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.RandomNetwork;
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
        assert config.getNetworkBuilder() instanceof RandomNetworkBuilder : "Incorrect network builder";
        var builder = (RandomNetworkBuilder) config.getNetworkBuilder();
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
