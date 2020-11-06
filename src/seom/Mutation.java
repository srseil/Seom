package seom;

import seom.games.Strategy;
import seom.utils.JavaRandomFacade;
import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Mutation implements Steppable {
    private final Configuration config;
    private final JavaRandomFacade javaRandom;

    public Mutation(Configuration config) {
        this.config = config;
        javaRandom = new JavaRandomFacade(config.getRandom());
    }

    @Override
    public void step(SimState simState) {
        for (Agent agent : config.getNetwork().getVertices()) {
            double rand = config.getRandom().nextDouble();
            if (rand >= config.getMutationProbability()) continue;

            List<Strategy> shuffledStrategies = Arrays.asList(config.getGame().getStrategies());
            Collections.shuffle(shuffledStrategies, javaRandom);
            if (shuffledStrategies.get(0) != agent.getStrategy()) {
                agent.setStrategy(shuffledStrategies.get(0));
            } else {
                agent.setStrategy(shuffledStrategies.get(1));
            }
        }
    }
}
