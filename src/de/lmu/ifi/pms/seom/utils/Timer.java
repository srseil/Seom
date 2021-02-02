package de.lmu.ifi.pms.seom.utils;

import java.time.Duration;
import java.util.Stack;

public class Timer {
    private static final Stack<Long> startStack = new Stack<>();
    private static Duration duration = Duration.ZERO;

    public static void start() {
        startStack.push(System.nanoTime());
    }

    public static Duration stop() {
        long start = startStack.pop();
        long stop = System.nanoTime();
        duration = Duration.ofNanos(stop - start);
        return duration;
    }

    public static Duration duration() {
        return duration;
    }
}
