package seom;

import seom.processes.*;
import sim.engine.Schedule;
import sim.engine.SimState;

public class Simulation extends SimState {
    private static final long seed = 560646223;

    private final Configuration config;
    private final Interactions interactions;
    private final Learning learning;
    private final Mutation mutation;
    private final Stability stability;

    public Simulation(Configuration config) throws Exception {
        super(config.getRandom());

        if (!config.validate()) {
            throw new Exception("Configuration is invalid");
        }

        this.config = config;
        interactions = new Interactions(config);
        learning = new Learning(config);
        mutation = new Mutation(config);
        stability = new Stability(this);
    }

    @Override
    public void start() {
        super.start();

        schedule.scheduleOnce(Schedule.EPOCH, new Initialization(config));

        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, 0, interactions);
        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, 0, learning);
        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, 0, mutation);

        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, 1, stability);

        System.out.println("Start");
    }

    @Override
    public void finish() {
        super.finish();

        System.out.println("Finish (" + schedule.getSteps() + ")");
        System.out.println();
    }

    public static long getSeed() {
        return seed;
    }

    public Configuration getConfig() {
        return config;
    }
}
