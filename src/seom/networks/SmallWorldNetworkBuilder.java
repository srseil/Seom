package seom.networks;

import ec.util.MersenneTwisterFast;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.Pair;
import seom.Agent;
import seom.utils.JavaRandomFacade;

import java.util.ArrayList;
import java.util.Collections;

public class SmallWorldNetworkBuilder {
    private int numAgents;
    private int learningDistance;
    private int interactionDistance;
    private double beta;

    private final MersenneTwisterFast random;
    private final JavaRandomFacade javaRandom;

    public SmallWorldNetworkBuilder(MersenneTwisterFast random) {
        this.random = random;
        javaRandom = new JavaRandomFacade(random);
    }

    public UndirectedSparseMultigraph<Agent, Edge> create() {
        assert numAgents > 1 : "The number of agents must be at least 2";
        assert interactionDistance <= numAgents / 2 : "Maximum radius is numAgents / 2";
        assert beta >= 0 && beta <= 1.0 : "Beta must be in [0,1]";

        var interactionGraph = new UndirectedSparseGraph<Agent, InteractionEdge>();

        Agent[] agents = new Agent[numAgents];
        for (int i = 0; i < numAgents; i++) {
            Agent agent = new Agent();
            agents[i] = agent;
            interactionGraph.addVertex(agent);
        }

        for (int i = 0; i < numAgents; i++) {
            int neighborIndex = i;
            for (int j = 0; j < interactionDistance; j++) {
                if (neighborIndex < numAgents - 1) {
                    neighborIndex += 1;
                } else if (neighborIndex == numAgents - 1) {
                    neighborIndex = 0;
                }
                interactionGraph.addEdge(new InteractionEdge(), agents[i], agents[neighborIndex]);
            }
        }

        for (InteractionEdge edge : interactionGraph.getEdges().toArray(InteractionEdge[]::new)) {
            double rand = random.nextDouble();
            if (rand < beta) {
                Agent agent1;
                Pair<Agent> endpoints = interactionGraph.getEndpoints(edge);
                boolean pickFirst = random.nextBoolean();
                if (pickFirst) {
                    agent1 = endpoints.getFirst();
                } else {
                    agent1 = endpoints.getSecond();
                }

                Agent agent2 = null;
                var nodes = new ArrayList<>(interactionGraph.getVertices());
                Collections.shuffle(nodes, javaRandom);
                for (Agent node : nodes) {
                    if (agent1 != node && interactionGraph.findEdge(agent1, node) == null) {
                        agent2 = node;
                    }
                }

                interactionGraph.addEdge(new InteractionEdge(), agent1, agent2);
            }
        }

        return NetworkUtils.createFullGraph(learningDistance, interactionGraph);
    }

    public SmallWorldNetworkBuilder setNumAgents(int numAgents) {
        this.numAgents = numAgents;
        return this;
    }

    public SmallWorldNetworkBuilder setInteractionDistance(int interactionDistance) {
        this.interactionDistance = interactionDistance;
        return this;
    }

    public SmallWorldNetworkBuilder setLearningDistance(int learningDistance) {
        this.learningDistance = learningDistance;
        return this;
    }

    public SmallWorldNetworkBuilder setBeta(double beta) {
        this.beta = beta;
        return this;
    }
}
