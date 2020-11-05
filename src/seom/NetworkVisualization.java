package seom;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import seom.networks.Edge;
import seom.networks.InteractionEdge;
import seom.networks.NetworkUtils;
import sim.engine.SimState;
import sim.engine.Steppable;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class NetworkVisualization extends JFrame implements Steppable {
    private final VisualizationViewer<Agent, Edge> visualizer;

    public NetworkVisualization(Configuration config) {
        int width = 600;
        int height = 600;

        Graph<Agent, Edge> graph = null;
        if (config.areInteractionsVisualized() && config.isLearningVisualized()) {
            graph = config.getNetwork();
        } else if (config.areInteractionsVisualized()) {
            graph = NetworkUtils.getGenericInteractionGraph(config.getNetwork());
        } else if (config.isLearningVisualized()) {
            graph = NetworkUtils.getGenericLearningGraph(config.getNetwork());
        } else {
            assert false : "Either interactions or learning need to be visualized";
        }

        var layout = new CircleLayout<>(graph);
        layout.setVertexOrder(Comparator.comparingInt(Agent::getId));
        layout.setSize(new Dimension(width, height));
        layout.initialize();

        /* Note for edge visualization:
         * The visualizer draws the edge from the first endpoint to the second.
         * This means that if two edges connecting the same nodes have the same
         * order of endpoints, one edge gets "overdrawn" by the other.
         * This is why the visualization doesn't show all of the learning edges
         * even if we activate it here.
         */

        visualizer = new VisualizationViewer<>(layout);
        visualizer.setPreferredSize(new Dimension(width, height));
        visualizer.getRenderContext().setVertexFillPaintTransformer(Agent::getStrategyColor);
        visualizer.getRenderContext().setEdgeDrawPaintTransformer(Edge::getColor);

        var graphMouse = new DefaultModalGraphMouse<Agent, InteractionEdge>();
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
