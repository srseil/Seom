package seom.analysis.parameters.lattice2d;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.analysis.parameters.Parameter;
import seom.networks.Lattice2DBuilder;

public class WrapAround implements Parameter<Boolean> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.Lattice2D;
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
        assert config.getNetworkBuilder() instanceof Lattice2DBuilder : "Incorrect network builder";
        var builder = (Lattice2DBuilder) config.getNetworkBuilder();
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
