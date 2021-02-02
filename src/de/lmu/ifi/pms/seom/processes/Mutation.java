package de.lmu.ifi.pms.seom.processes;

import de.lmu.ifi.pms.seom.Agent;
import de.lmu.ifi.pms.seom.networks.InteractionEdge;
import de.lmu.ifi.pms.seom.utils.Log;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.games.Strategy;
import de.lmu.ifi.pms.seom.networks.NetworkUtils;
import sim.engine.SimState;

import java.util.*;

public class Mutation implements SimulationProcess {
    private final Configuration config;
    private final UndirectedSparseGraph<Agent, InteractionEdge> interactionGraph;

    public Mutation(Configuration config) {
        this.config = config;
        interactionGraph = NetworkUtils.getInteractionGraph(config.getNetwork());
    }

    @Override
    public boolean isStochastic() {
        return config.getMutationProbability() > 0.0;
    }

    @Override
    public void reset() {
    }

    @Override
    public void step(SimState simState) {
        Log.fine("Mutation");

        if (config.getMutationProbability() == 0.0) return;

        var sortedAgents = new ArrayList<>(config.getNetwork().getVertices());
        Collections.sort(sortedAgents);
        for (Agent agent : sortedAgents) {
            double rand = config.getRandom().nextDouble();
            if (rand >= config.getMutationProbability()) continue;

            var shuffledStrategies = new ArrayList<>(Arrays.asList(config.getGame().getStrategies()));
            Collections.shuffle(shuffledStrategies, config.getJavaRandom());
            Strategy newStrategy = shuffledStrategies.get(0) != agent.getStrategy()
                ? shuffledStrategies.get(0)
                : shuffledStrategies.get(1);

            agent.setStrategy(newStrategy);
            Agent[] neighborhood = NetworkUtils.getDistanceNeighborhood(agent, config.getMutationDistance(), interactionGraph);
            for (Agent neighbor : neighborhood) {
                neighbor.setStrategy(newStrategy);
            }
        }
    }
}
