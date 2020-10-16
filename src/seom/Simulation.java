package seom;

import sim.engine.Schedule;
import sim.engine.SimState;

public class Simulation extends SimState {
    private static final long seed = 560646223;

    private final Configuration config;
    private final Interactions interactions;
    private final Stability stability;

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
        stability = new Stability(this);
    }

    @Override
    public void start() {
        super.start();

        schedule.scheduleOnce(Schedule.EPOCH, new Initialization(config));
        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, 0, interactions);
        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, 1, stability);
    }

    public static long getSeed() {
        return seed;
    }

    public Configuration getConfig() {
        return config;
    }
}
