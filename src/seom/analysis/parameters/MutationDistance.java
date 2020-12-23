package seom.analysis.parameters;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;

public class MutationDistance implements Parameter<Integer> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType != NetworkType.FullyConnected;
    }

    @Override
    public Integer getNominal() {
        return 0;
    }

    @Override
    public Integer[] getValues() {
        return new Integer[] {
            0,
            1,
            2
        };
    }

    @Override
    public void apply(Integer value, Configuration config) {
        config.setMutationDistance(value);
    }

    @Override
    public String getValueName(Integer value) {
        return value.toString();
    }

    @Override
    public String getName() {
        return "mutation distance";
    }
}
