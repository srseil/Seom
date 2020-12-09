package seom;

import seom.processes.*;
import sim.engine.Schedule;
import sim.engine.SimState;

import java.util.ArrayList;
import java.util.List;

public class Simulation extends SimState {
    private final Configuration config;
    private final List<SimulationProcess> processes;
    private Result result;

    private final Initialization initialization;
    private final Interactions interactions;
    private final Learning learning;
    private final Mutation mutation;
    private final Stability stability;

    public Simulation(Configuration config) {
        super(config.getRandom());
        assert config.validate() : "Configuration is invalid";

        this.config = config;
        processes = new ArrayList<>();

        initialization = new Initialization(config);
        processes.add(initialization);

        interactions = new Interactions(config);
        processes.add(interactions);

        learning = new Learning(config);
        processes.add(learning);

        mutation = new Mutation(config);
        processes.add(mutation);

        stability = new Stability(this);
        processes.add(stability);
    }

    @Override
    public void start() {
        super.start();

        result = new Result(config);

        stability.setCycleDetectionEnabled(config.isCycleDetectionEnabled());
        if (config.isCycleDetectionEnabled()) {
            for (SimulationProcess process : processes) {
                process.reset();
                if (process.isStochastic()) {
                    stability.setCycleDetectionEnabled(false);
                }
            }
        }

        schedule.scheduleOnce(Schedule.EPOCH, initialization);

        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, 0, interactions);
        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, 1, learning);
        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, 2, mutation);

        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, 3, stability);

        System.out.println("Start");
    }

    @Override
    public void finish() {
        super.finish();

        // When the simulation is killed by Stability, finish() is called one generation after
        result.setGenerationCount(schedule.getSteps() - 1);

        if (result.isCyclic()) {
            System.out.println("Cycle (length: " + result.getCycleLength()
                + ", start: " + result.getCycleStart() + ", end: " + result.getCycleEnd() + ")");
        }
        System.out.println("Finish (" + result.getGenerationCount() + ")");
        System.out.println("Result (stability: " + result.getStability() + ", morality: " + result.getMorality() + ")");
        System.out.println();
    }

    public Configuration getConfig() {
        return config;
    }

    public Result getResult() {
        return result;
    }

    public List<SimulationProcess> getProcesses() {
        return processes;
    }
}
