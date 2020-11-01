package seom;

import sim.display.Controller;
import sim.display.GUIState;

public class SimulationWithUI extends GUIState {
    private final Simulation simulation;
    private NetworkVisualization networkFrame;

    public SimulationWithUI(Configuration config) throws Exception {
        super(new Simulation(config));
        simulation = (Simulation)state;
    }

    @SuppressWarnings("unused")
    public static String getName() {
        return "The Structural Evolution of Morality";
    }

    @Override
    public void init(Controller controller) {
        super.init(controller);

        networkFrame = new NetworkVisualization(simulation.getConfig());
        controller.registerFrame(networkFrame);
        networkFrame.setVisible(true);
    }

    @Override
    public void start() {
        super.start();

        scheduleRepeatingImmediatelyAfter(networkFrame);
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
