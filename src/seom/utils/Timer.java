package seom.utils;

import java.time.Duration;

public class Timer {
    private static long start = 0;
    private static Duration duration = Duration.ZERO;

    public static void start() {
        start = System.nanoTime();
    }

    public static Duration stop() {
        long stop = System.nanoTime();
        duration = Duration.ofNanos(stop - start);
        return duration;
    }

    public static Duration duration() {
        return duration;
    }
}
