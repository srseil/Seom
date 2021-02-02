package de.lmu.ifi.pms.seom.analysis.parameters.lattice1d;

import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.analysis.GameType;
import de.lmu.ifi.pms.seom.analysis.NetworkType;
import de.lmu.ifi.pms.seom.analysis.parameters.Parameter;
import de.lmu.ifi.pms.seom.networks.Lattice1DBuilder;

public class LearningDistance implements Parameter<Integer> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.Lattice1D;
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
        assert config.getNetworkBuilder() instanceof Lattice1DBuilder : "Incorrect network builder";
        var builder = (Lattice1DBuilder) config.getNetworkBuilder();
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
