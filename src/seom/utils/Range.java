package seom.utils;

import java.math.BigDecimal;

public class Range {
    private final BigDecimal lower;
    private final BigDecimal upper;
    private final boolean lowerExclusive;
    private final boolean upperExclusive;

    private Range(double lower, double upper, boolean lowerExclusive, boolean upperExclusive) {
        this.lower = BigDecimal.valueOf(lower);
        this.upper = BigDecimal.valueOf(upper);
        this.lowerExclusive = lowerExclusive;
        this.upperExclusive = upperExclusive;
    }

    public static Range between(double lower, double upper) {
        return new Range(lower, upper, false, false);
    }

    public static Builder from(double lower) {
        return new Builder(lower, false);
    }

    public static Builder fromExclusive(double lower) {
        return new Builder(lower, true);
    }

    public boolean contains(double value) {
        var decimal = BigDecimal.valueOf(value);
        boolean result = lowerExclusive
            ? lower.compareTo(decimal) < 0
            : lower.compareTo(decimal) <= 0;
        result = result && (upperExclusive
            ? upper.compareTo(decimal) > 0
            : upper.compareTo(decimal) >= 0);
        return result;
    }

    @Override
    public String toString() {
        String string = lowerExclusive ? "(" : "[";
        string += lower + ", " + upper;
        string += upperExclusive ? ")" : "]";
        return string;
    }

    public static class Builder {
        private final double lower;
        private final boolean exclusive;

        private Builder(double lower, boolean exclusive) {
            this.lower = lower;
            this.exclusive = exclusive;
        }

        public Range to(double upper) {
            return new Range(lower, upper, exclusive, false);
        }

        public Range toExclusive(double upper) {
            return new Range(lower, upper, exclusive, true);
        }
    }
}
