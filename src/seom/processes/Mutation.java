package seom.processes;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import seom.Agent;
import seom.Configuration;
import seom.games.Strategy;
import seom.networks.InteractionEdge;
import seom.networks.NetworkUtils;
import sim.engine.SimState;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        System.out.println("Mutation");

        if (config.getMutationProbability() == 0.0) return;

        for (Agent agent : config.getNetwork().getVertices()) {
            double rand = config.getRandom().nextDouble();
            if (rand >= config.getMutationProbability()) continue;

            List<Strategy> shuffledStrategies = Arrays.asList(config.getGame().getStrategies());
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
