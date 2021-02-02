package de.lmu.ifi.pms.seom.networks;

import java.awt.*;

public class InteractionEdge implements Edge, Comparable<InteractionEdge> {
    private static int nextId = 0;
    private final int id;

    public InteractionEdge() {
        id = nextId++;
    }

    @Override
    public Color getColor() {
        return Color.BLACK;
    }

    @Override
    public int compareTo(InteractionEdge o) {
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
