package seom;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import sim.display.Controller;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.engine.Steppable;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class SimulationWithUI extends GUIState implements Steppable {
    private final Simulation simulation;
    private CircleLayout<Agent, Relationship> layout;
    private BasicVisualizationServer<Agent, Relationship> visualizer;
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

        this.scheduleRepeatingImmediatelyAfter(this);

        // If there is ever more than the network visualization frame, refactor this into its own class
        layout = new CircleLayout<>(simulation.getGraph());
        layout.setVertexOrder(Comparator.comparingInt(Agent::getId));
        layout.setSize(new Dimension(300, 300));
        layout.initialize();
        visualizer = new BasicVisualizationServer<>(layout);
        visualizer.setPreferredSize(new Dimension(350, 350));
        visualizer.getRenderContext().setVertexFillPaintTransformer(Agent::getStrategyColor);

        frame = new JFrame();
        frame.setTitle("Network Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(visualizer);
        frame.pack();
        controller.registerFrame(frame);
        frame.setVisible(true);
    }

    @Override
    public void quit() {
        super.quit();

        if (frame != null) {
            frame.dispose();
        }
        frame = null;
    }

    @Override
    public void step(SimState simState) {
        // If the layout ever changes over the run of the simulation, we need to call this
        /*
        layout = new CircleLayout<>(simulation.getGraph());
        layout.setVertexOrder(Comparator.comparingInt(Agent::getId));
        layout.setSize(new Dimension(300, 300));
        layout.initialize();
        visualizer.setGraphLayout(layout);
        //visualizer.repaint();
        */
    }

    public Simulation getSimulation() {
        return simulation;
    }
}
