package de.lmu.ifi.pms.seom.networks;

import java.awt.*;

public class LearningEdge implements Edge, Comparable<LearningEdge> {
    private static int nextId = 0;
    private final int id;

    public LearningEdge() {
        id = nextId++;
    }

    @Override
    public Color getColor() {
        return Color.RED;
    }

    @Override
    public int compareTo(LearningEdge o) {
        return Integer.compare(id, o.id);
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    public static void resetIds() {
        nextId = 0;
    }
}
