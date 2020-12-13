package seom.utils.tuples;

import java.util.Objects;

public class Vector3<T> implements Tuple3<T, T, T> {
    private final T first;
    private final T second;
    private final T third;

    public Vector3(T first, T second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public static <T> Vector3<T> of(T first, T second, T third) {
        return new Vector3<>(first, second, third);
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
    public T getThird() {
        return third;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector3<?>)) return false;
        var vector3 = (Vector3<?>) obj;
        return first.equals(vector3.first) && second.equals(vector3.second) && third.equals(vector3.third);
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
