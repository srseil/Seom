package de.lmu.ifi.pms.seom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import de.lmu.ifi.pms.seom.games.BargainingSubgame;
import de.lmu.ifi.pms.seom.games.Strategy;

import java.util.ArrayList;

public class ResultTests {
    @Test
    public void computeOutputs() {
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
        Assertions.assertEquals(2, result.getStrategyMap().get(BargainingSubgame.Strategy.Demand4));
        Assertions.assertEquals(6, result.getStrategyMap().get(BargainingSubgame.Strategy.Demand5));
        Assertions.assertEquals(1, result.getStrategyMap().get(BargainingSubgame.Strategy.Demand6));
    }
}
