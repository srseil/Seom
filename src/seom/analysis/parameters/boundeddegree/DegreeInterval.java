package seom.analysis.parameters.boundeddegree;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.analysis.parameters.Parameter;
import seom.networks.BoundedDegreeBuilder;
import seom.utils.tuples.Pair;

public class DegreeInterval implements Parameter<Pair<Integer, Integer>> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.BoundedDegree;
    }

    @Override
    public Pair<Integer, Integer> getNominal() {
        return Pair.of(4, 7);
    }

    @Override
    public Pair<Integer, Integer>[] getValues() {
        //noinspection unchecked
        return new Pair[] {
            Pair.of(2, 3),
            Pair.of(5, 6),
            Pair.of(8, 9),
            Pair.of(4, 7),
            Pair.of(3, 8),
        };
    }

    @Override
    public void apply(Pair<Integer, Integer> value, Configuration config) {
        assert config.getNetworkBuilder() instanceof BoundedDegreeBuilder : "Incorrect network builder";
        var builder = (BoundedDegreeBuilder) config.getNetworkBuilder();
        builder.setMinDegree(value.getFirst());
        builder.setMaxDegree(value.getSecond());
    }

    @Override
    public String getValueName(Pair<Integer, Integer> value) {
        return value.getFirst() + "-" + value.getSecond();
    }

    @Override
    public String getName() {
        return "degree interval";
    }
}
