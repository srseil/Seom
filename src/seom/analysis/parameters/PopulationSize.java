package seom.analysis.parameters;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.networks.*;

public class PopulationSize implements Parameter<Integer> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return true;
    }

    @Override
    public Integer getNominal() {
        return 1000;
    }

    @Override
    public Integer[] getValues() {
        return new Integer[] {
            100,
            1000,
            10000
        };
    }

    @Override
    public void apply(Integer value, Configuration config) {
        assert config.getNetworkBuilder() != null : "Network builder is null";

        if (config.getNetworkBuilder() instanceof FullyConnectedNetworkBuilder) {
            var builder = (FullyConnectedNetworkBuilder) config.getNetworkBuilder();
            builder.setNumAgents(value);
        } else if (config.getNetworkBuilder() instanceof Lattice1DBuilder) {
            var builder = (Lattice1DBuilder) config.getNetworkBuilder();
            builder.setLength(value);
        } else if (config.getNetworkBuilder() instanceof Lattice2DBuilder) {
            var builder = (Lattice2DBuilder) config.getNetworkBuilder();
            int sqrt = (int) Math.round(Math.sqrt(value));
            builder.setWidth(sqrt);
            builder.setHeight(sqrt);
        } else if (config.getNetworkBuilder() instanceof SmallWorld1DBuilder) {
            var builder = (SmallWorld1DBuilder) config.getNetworkBuilder();
            builder.setLength(value);
        } else if (config.getNetworkBuilder() instanceof SmallWorld2DBuilder) {
            var builder = (SmallWorld2DBuilder) config.getNetworkBuilder();
            int sqrt = (int) Math.round(Math.sqrt(value));
            builder.setWidth(sqrt);
            builder.setHeight(sqrt);
        } else if (config.getNetworkBuilder() instanceof BoundedDegreeNetworkBuilder) {
            var builder = (BoundedDegreeNetworkBuilder) config.getNetworkBuilder();
            builder.setNumAgents(value);
        } else if (config.getNetworkBuilder() instanceof RandomNetworkBuilder) {
            var builder = (RandomNetworkBuilder) config.getNetworkBuilder();
            builder.setNumAgents(value);
        } else {
            assert false : "Unknown network builder";
        }
    }

    @Override
    public String getValueName(Integer value) {
        return value.toString();
    }

    @Override
    public String getName() {
        return "population size";
    }
}
