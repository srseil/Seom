package seom.processes;

import seom.Agent;
import seom.Configuration;
import seom.games.Strategy;
import sim.engine.SimState;
import sim.engine.Steppable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Mutation implements Steppable {
    private final Configuration config;

    public Mutation(Configuration config) {
        this.config = config;
    }

    @Override
    public void step(SimState simState) {
        System.out.println("Mutation");

        for (Agent agent : config.getNetwork().getVertices()) {
            double rand = config.getRandom().nextDouble();
            if (rand >= config.getMutationProbability()) continue;

            List<Strategy> shuffledStrategies = Arrays.asList(config.getGame().getStrategies());
            Collections.shuffle(shuffledStrategies, config.getJavaRandom());
            if (shuffledStrategies.get(0) != agent.getStrategy()) {
                agent.setStrategy(shuffledStrategies.get(0));
            } else {
                agent.setStrategy(shuffledStrategies.get(1));
            }
        }
    }
}
