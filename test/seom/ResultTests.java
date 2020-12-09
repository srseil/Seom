package seom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seom.games.BargainingSubgame;
import seom.games.Strategy;

import java.util.ArrayList;

public class ResultTests {
    @Test
    public void computeMorality() {
        var config = new Configuration();
        config.setGame(new BargainingSubgame());
        config.setMaxNumGenerations(10);
        var result = new Result(config);

        var history = new ArrayList<Strategy[]>() {{
            add(new Strategy[]{
                BargainingSubgame.Strategy.Demand4,
                BargainingSubgame.Strategy.Demand5,
                BargainingSubgame.Strategy.Demand6
            });
            add(new Strategy[]{
                BargainingSubgame.Strategy.Demand5,
                BargainingSubgame.Strategy.Demand4,
                BargainingSubgame.Strategy.Demand5
            });
            add(new Strategy[]{
                BargainingSubgame.Strategy.Demand5,
                BargainingSubgame.Strategy.Demand5,
                BargainingSubgame.Strategy.Demand5
            });
        }};
        result.setCycle(1, 3);
        result.computeOutputs(history);
        Assertions.assertEquals(2.0/3.0, result.getMorality());
    }
}
