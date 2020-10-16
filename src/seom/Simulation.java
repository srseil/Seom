package seom;

import sim.engine.Schedule;
import sim.engine.SimState;

public class Simulation extends SimState {
    private static final long seed = 560646223;

    private final Configuration config;
    private final Interactions interactions;

    public Simulation(Configuration config) throws Exception {
        this(config, seed);
    }

    public Simulation(Configuration config, long seed) throws Exception {
        super(seed);

        if (!config.validate()) {
            throw new Exception("Configuration is invalid");
        }

        this.config = config;
        interactions = new Interactions(config);
    }

    @Override
    public void start() {
        super.start();
        schedule.scheduleOnce(Schedule.EPOCH, new Initialization(config));
        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, interactions);
    }

    public static long getSeed() {
        return seed;
    }

    public Configuration getConfig() {
        return config;
    }
}
