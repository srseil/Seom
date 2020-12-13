package seom.utils.tuples;

import java.util.Objects;

public class Triple<T1, T2, T3> implements Tuple3<T1, T2, T3> {
    private final T1 first;
    private final T2 second;
    private final T3 third;

    public Triple(T1 first, T2 second, T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <T1, T2, T3> Triple<T1, T2, T3> of(T1 first, T2 second, T3 third) {
        return new Triple<>(first, second, third);
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
    public T3 getThird() {
        return third;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Triple<?, ?, ?>)) return false;
        var triple = (Triple<?, ?, ?>) obj;
        return first.equals(triple.first) && second.equals(triple.second) && third.equals(triple.third);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second, third);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ", " + third + ")";
    }
}
