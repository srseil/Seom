package seom.utils.tuples;

import java.util.Objects;

public class Vector2<T> implements Tuple2<T, T> {
    private final T first;
    private final T second;

    public Vector2(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public static <T> Vector2<T> of(T first, T second) {
        return new Vector2<>(first, second);
    }

    @Override
    public T getFirst() {
        return first;
    }

    @Override
    public T getSecond() {
        return second;
    }

    @Override
    public Vector2<T> getInverted() {
        return new Vector2<>(second, first);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2<?>)) return false;
        var vector2 = (Vector2<?>) obj;
        return first.equals(vector2.first) && second.equals(vector2.second);
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
