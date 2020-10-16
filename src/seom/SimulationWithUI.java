package seom;

import sim.display.Controller;
import sim.display.GUIState;

public class SimulationWithUI extends GUIState {
    private final Simulation simulation;
    private NetworkVisualization networkFrame;

    public SimulationWithUI(Configuration config) throws Exception {
        this(config, Simulation.getSeed());
    }

    public SimulationWithUI(Configuration config, long seed) throws Exception {
        super(new Simulation(config, seed));
        simulation = (Simulation)state;
    }

    @SuppressWarnings("unused")
    public static String getName() {
        return "The Structural Evolution of Morality";
    }

    @Override
    public void init(Controller controller) {
        super.init(controller);

        var networkFrame = new NetworkVisualization(simulation.getConfig());
        this.scheduleRepeatingImmediatelyAfter(networkFrame);
        controller.registerFrame(networkFrame);
        networkFrame.setVisible(true);
    }

    @Override
    public void quit() {
        super.quit();

        if (networkFrame != null) {
            networkFrame.dispose();
        }
        networkFrame = null;
    }
}
