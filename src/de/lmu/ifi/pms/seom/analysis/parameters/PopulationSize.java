package de.lmu.ifi.pms.seom.analysis.parameters;

import de.lmu.ifi.pms.seom.networks.*;
import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.analysis.GameType;
import de.lmu.ifi.pms.seom.analysis.NetworkType;

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

        if (config.getNetworkBuilder() instanceof FullyConnectedBuilder) {
            var builder = (FullyConnectedBuilder) config.getNetworkBuilder();
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
        } else if (config.getNetworkBuilder() instanceof BoundedDegreeBuilder) {
            var builder = (BoundedDegreeBuilder) config.getNetworkBuilder();
            builder.setNumAgents(value);
        } else if (config.getNetworkBuilder() instanceof FullyRandomBuilder) {
            var builder = (FullyRandomBuilder) config.getNetworkBuilder();
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
