package seom;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import sim.engine.SimState;
import sim.engine.Steppable;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class NetworkVisualization extends JFrame implements Steppable {
    private final BasicVisualizationServer<Agent, Relationship> visualizer;

    public NetworkVisualization(Configuration config) {
        CircleLayout<Agent, Relationship> layout = new CircleLayout<>(config.getNetwork());
        layout.setVertexOrder(Comparator.comparingInt(Agent::getId));
        layout.setSize(new Dimension(300, 300));
        layout.initialize();

        visualizer = new BasicVisualizationServer<>(layout);
        visualizer.setPreferredSize(new Dimension(350, 350));
        visualizer.getRenderContext().setVertexFillPaintTransformer(Agent::getStrategyColor);

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
