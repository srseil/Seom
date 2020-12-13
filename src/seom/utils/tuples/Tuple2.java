package seom.utils.tuples;

public interface Tuple2<T1, T2> {
    T1 getFirst();
    T2 getSecond();
    Tuple2<T2, T1> getInverted();
}
