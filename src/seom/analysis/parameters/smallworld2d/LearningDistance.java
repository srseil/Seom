package seom.analysis.parameters.smallworld2d;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.analysis.parameters.Parameter;
import seom.networks.SmallWorld2DBuilder;

public class LearningDistance implements Parameter<Integer> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.SmallWorld2D;
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
        assert config.getNetworkBuilder() instanceof SmallWorld2DBuilder : "Incorrect network builder";
        var builder = (SmallWorld2DBuilder) config.getNetworkBuilder();
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
