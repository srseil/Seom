package de.lmu.ifi.pms.seom.utils.tuples;

import java.util.Objects;

public class Pair<T1, T2> implements Tuple2<T1, T2> {
    private final T1 first;
    private final T2 second;

    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public static <T1, T2> Pair<T1, T2> of(T1 first, T2 second) {
        return new Pair<>(first, second);
    }

    @Override
    public T1 getFirst() {
        return first;
    }

    @Override
    public T2 getSecond() {
        return second;
    }

    @Override
    public Pair<T2, T1> getInverted() {
        return new Pair<>(second, first);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair<?, ?>)) return false;
        var pair = (Pair<?, ?>) obj;
        return first.equals(pair.first) && second.equals(pair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
