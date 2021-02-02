package de.lmu.ifi.pms.seom.utils;

import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.Result;
import de.lmu.ifi.pms.seom.Simulation;
import de.lmu.ifi.pms.seom.SimulationWithUI;
import sim.display.Console;

public class SimulationRunner {
    public static Result run(Configuration config) {
        var simulation = new Simulation(config);
        simulation.start();
        //noinspection StatementWithEmptyBody
        while (simulation.schedule.step(simulation)) ;
        simulation.finish();
        return simulation.getResult();
    }

    public static void runWithUI(Configuration config) {
        var simUI = new SimulationWithUI(config);
        var console = new Console(simUI);
        console.setVisible(true);
        console.setPlaySleep(125);
        console.setIncrementSeedOnStop(false);
    }
}
