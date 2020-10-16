package seom;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import sim.engine.SimState;
import sim.engine.Steppable;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class NetworkVisualization extends JFrame implements Steppable {
    private final VisualizationViewer<Agent, Relationship> visualizer;

    public NetworkVisualization(Configuration config) {
        int width = 600;
        int height = 600;

        CircleLayout<Agent, Relationship> layout = new CircleLayout<>(config.getNetwork());
        layout.setVertexOrder(Comparator.comparingInt(Agent::getId));
        layout.setSize(new Dimension(width, height));
        layout.initialize();

        visualizer = new VisualizationViewer<>(layout);
        visualizer.setPreferredSize(new Dimension(width, height));
        visualizer.getRenderContext().setVertexFillPaintTransformer(Agent::getStrategyColor);

        var graphMouse = new DefaultModalGraphMouse<Agent, Relationship>();
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        visualizer.setGraphMouse(graphMouse);

        setTitle("Network Visualization");
        getContentPane().add(visualizer);
        pack();
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
        */

        visualizer.repaint();
    }
}
