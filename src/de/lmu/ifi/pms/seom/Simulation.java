package de.lmu.ifi.pms.seom;

import de.lmu.ifi.pms.seom.processes.*;
import de.lmu.ifi.pms.seom.utils.Log;
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

        stability.setHomogeneityDetectionEnabled(config.isHomogeneityDetectionEnabled());
        stability.setCycleDetectionEnabled(config.isCycleDetectionEnabled());
        for (SimulationProcess process : processes) {
            process.reset();
            if (process.isStochastic()) {
                stability.setHomogeneityDetectionEnabled(false);
                stability.setCycleDetectionEnabled(false);
            }
        }

        schedule.scheduleOnce(Schedule.EPOCH, initialization);

        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, 0, interactions);
        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, 1, learning);
        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, 2, mutation);

        schedule.scheduleRepeating(Schedule.EPOCH + 1.0, 3, stability);

        Log.fine("Start");
    }

    @Override
    public void finish() {
        super.finish();

        // When the simulation is killed by Stability, finish() is called one generation after
        result.setGenerationCount(schedule.getSteps() - 1);

        if (result.isCyclic()) {
            Log.fine("Cycle (length: " + result.getCycleLength()
                + ", start: " + result.getCycleStart() + ", end: " + result.getCycleEnd() + ")");
        }
        Log.fine("Finish (" + result.getGenerationCount() + ")");
        Log.fine("Result (stability: " + result.getStability() + ", morality: " + result.getMorality() + ")");
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
