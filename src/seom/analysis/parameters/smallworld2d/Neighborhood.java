package seom.analysis.parameters.smallworld2d;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.analysis.parameters.Parameter;
import seom.networks.Lattice2DBuilder;
import seom.networks.SmallWorld2DBuilder;
import seom.utils.tuples.Pair;

public class Neighborhood implements Parameter<Pair<Lattice2DBuilder.Neighborhood, Integer>> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return networkType == NetworkType.SmallWorld2D;
    }

    @Override
    public Pair<Lattice2DBuilder.Neighborhood, Integer> getNominal() {
        return Pair.of(Lattice2DBuilder.Neighborhood.Moore, 1);
    }

    @Override
    public Pair<Lattice2DBuilder.Neighborhood, Integer>[] getValues() {
        //noinspection unchecked
        return new Pair[] {
            Pair.of(Lattice2DBuilder.Neighborhood.VonNeumann, 1),
            Pair.of(Lattice2DBuilder.Neighborhood.VonNeumann, 2),
            Pair.of(Lattice2DBuilder.Neighborhood.VonNeumann, 3),
            Pair.of(Lattice2DBuilder.Neighborhood.Moore, 1),
            Pair.of(Lattice2DBuilder.Neighborhood.Moore, 2),
            Pair.of(Lattice2DBuilder.Neighborhood.Moore, 3)
        };
    }

    @Override
    public void apply(Pair<Lattice2DBuilder.Neighborhood, Integer> value, Configuration config) {
        assert config.getNetworkBuilder() instanceof SmallWorld2DBuilder : "Incorrect network builder";
        var builder = (SmallWorld2DBuilder) config.getNetworkBuilder();
        builder.setNeighborhood(value.getFirst());
        builder.setInteractionDistance(value.getSecond());
    }

    @Override
    public String getValueName(Pair<Lattice2DBuilder.Neighborhood, Integer> value) {
        if (value.getFirst() == Lattice2DBuilder.Neighborhood.VonNeumann) {
            return "N" + ((2 + 2 * value.getSecond()) * value.getSecond());
        } else if (value.getFirst() == Lattice2DBuilder.Neighborhood.Moore) {
            return "M" + ((value.getSecond() * 2 + 1) * (value.getSecond() * 2 + 1) - 1);
        } else {
            return "unknown";
        }
    }

    @Override
    public String getName() {
        return "neighborhood";
    }
}
