package seom;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import sim.display.Controller;
import sim.display.GUIState;

import javax.swing.*;
import java.awt.*;

public class SimulationWithUI extends GUIState {
    private final Simulation simulation;
    private JFrame frame;

    public SimulationWithUI() {
        super(new Simulation());
        simulation = (Simulation)state;
    }

    public SimulationWithUI(long seed) {
        super(new Simulation(seed));
        simulation = (Simulation)state;
    }

    @SuppressWarnings("unused")
    public static String getName() {
        return "The Structural Evolution of Morality";
    }

    @Override
    public void init(Controller controller) {
        super.init(controller);

        // If there is ever more than the network visualization frame, refactor this into its own class
        var layout = new KKLayout<>(simulation.getGraph());
        layout.setSize(new Dimension(300, 300));
        var visualizer = new BasicVisualizationServer<>(layout);
        visualizer.setPreferredSize(new Dimension(350, 350));

        frame = new JFrame();
        frame.setTitle("Network Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(visualizer);
        frame.pack();
        controller.registerFrame(frame);
        frame.setVisible(true);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void quit() {
        super.quit();

        if (frame != null) {
            frame.dispose();
        }
        frame = null;
    }
}
