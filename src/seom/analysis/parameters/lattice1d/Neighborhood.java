package seom.analysis.parameters.lattice1d;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.analysis.parameters.Parameter;
import seom.networks.Lattice1DBuilder;

public class Neighborhood implements Parameter<Integer> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.Lattice1D;
    }

    @Override
    public Integer getNominal() {
        return 2;
    }

    @Override
    public Integer[] getValues() {
        return new Integer[] {
            2,
            3,
            4
        };
    }

    @Override
    public void apply(Integer value, Configuration config) {
        assert config.getNetworkBuilder() instanceof Lattice1DBuilder : "Incorrect network builder";
        var builder = (Lattice1DBuilder) config.getNetworkBuilder();
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
