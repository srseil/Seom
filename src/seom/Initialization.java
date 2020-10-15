package seom;

import ec.util.MersenneTwisterFast;
import seom.games.Strategy;
import sim.engine.SimState;
import sim.engine.Steppable;

public class Initialization implements Steppable {
    private final MersenneTwisterFast random = new MersenneTwisterFast();
    private final Configuration config;

    public Initialization(Configuration config) {
        this.config = config;
    }

    @Override
    public void step(SimState simState) {
        System.out.println("initialization");

        Strategy[] strategies = config.getGame().getStrategies();
        for (Agent agent : config.getNetwork().getVertices()) {
            int rand = random.nextInt(strategies.length);
            agent.setStrategy(strategies[rand]);
        }
    }
}
