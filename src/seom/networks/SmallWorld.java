package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.Agent;
import seom.utils.JavaRandomFacade;

import java.util.ArrayList;
import java.util.Collections;

public class SmallWorld extends UndirectedSparseGraph<Agent, Edge> {
    public SmallWorld(int numAgents, int radius, double beta, MersenneTwisterFast random) {
        assert numAgents > 1 : "The number of agents must be at least 2";
        assert radius <= numAgents / 2 : "Maximum radius is numAgents / 2";
        assert beta >= 0 && beta <= 1.0 : "Beta must be in [0,1]";

        var javaRandom = new JavaRandomFacade(random);

        Agent[] agents = new Agent[numAgents];
        for (int i = 0; i < numAgents; i++) {
            Agent agent = new Agent();
            agents[i] = agent;
            addVertex(agent);
        }

        for (int i = 0; i < numAgents; i++) {
            int neighborIndex = i;
            for (int j = 0; j < radius; j++) {
                if (neighborIndex < numAgents - 1) {
                    neighborIndex += 1;
                } else if (neighborIndex == numAgents - 1) {
                    neighborIndex = 0;
                }
                addEdge(new InteractionEdge(), agents[i], agents[neighborIndex]);
            }
        }

        for (Edge edge : getEdges().toArray(Edge[]::new)) {
            double rand = random.nextDouble();
            if (rand < beta) {
                Agent agent1;
                Pair<Agent> endpoints = getEndpoints(edge);
                boolean pickFirst = random.nextBoolean();
                if (pickFirst) {
                    agent1 = endpoints.getFirst();
                } else {
                    agent1 = endpoints.getSecond();
                }

                Agent agent2 = null;
                var nodes = new ArrayList<>(getVertices());
                Collections.shuffle(nodes, javaRandom);
                for (Agent node : nodes) {
                    if (agent1 != node && findEdge(agent1, node) == null) {
                        agent2 = node;
                    }
                }

                addEdge(new InteractionEdge(), agent1, agent2);
            }
        }
    }
}
