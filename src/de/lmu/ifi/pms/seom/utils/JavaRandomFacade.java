package de.lmu.ifi.pms.seom.utils;

import ec.util.MersenneTwisterFast;

import java.util.Random;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class  JavaRandomFacade extends Random {
    private final MersenneTwisterFast mersenneTwisterFast;

    public JavaRandomFacade(MersenneTwisterFast mersenneTwisterFast) {
        this.mersenneTwisterFast = mersenneTwisterFast;
    }

    @Override
    public synchronized void setSeed(long seed) {
        // Do nothing
    }

    @Override
    protected int next(int bits) {
        assert false : "Not implemented";
        return 0;
    }

    @Override
    public void nextBytes(byte[] bytes) {
        mersenneTwisterFast.nextBytes(bytes);
    }

    @Override
    public int nextInt() {
        return mersenneTwisterFast.nextInt();
    }

    @Override
    public int nextInt(int bound) {
        return mersenneTwisterFast.nextInt(bound);
    }

    @Override
    public long nextLong() {
        return mersenneTwisterFast.nextLong();
    }

    @Override
    public boolean nextBoolean() {
        return mersenneTwisterFast.nextBoolean();
    }

    @Override
    public float nextFloat() {
        return mersenneTwisterFast.nextFloat();
    }

    @Override
    public double nextDouble() {
        return mersenneTwisterFast.nextDouble();
    }

    @Override
    public synchronized double nextGaussian() {
        return mersenneTwisterFast.nextGaussian();
    }

    @Override
    public IntStream ints(long streamSize) {
        assert false : "Not implemented";
        return null;
    }

    @Override
    public IntStream ints() {
        assert false : "Not implemented";
        return null;
    }

    @Override
    public IntStream ints(long streamSize, int randomNumberOrigin, int randomNumberBound) {
        assert false : "Not implemented";
        return null;
    }

    @Override
    public IntStream ints(int randomNumberOrigin, int randomNumberBound) {
        assert false : "Not implemented";
        return null;
    }

    @Override
    public LongStream longs(long streamSize) {
        assert false : "Not implemented";
        return null;
    }

    @Override
    public LongStream longs() {
        assert false : "Not implemented";
        return null;
    }

    @Override
    public LongStream longs(long streamSize, long randomNumberOrigin, long randomNumberBound) {
        assert false : "Not implemented";
        return null;
    }

    @Override
    public LongStream longs(long randomNumberOrigin, long randomNumberBound) {
        assert false : "Not implemented";
        return null;
    }

    @Override
    public DoubleStream doubles(long streamSize) {
        assert false : "Not implemented";
        return null;
    }

    @Override
    public DoubleStream doubles() {
        assert false : "Not implemented";
        return null;
    }

    @Override
    public DoubleStream doubles(long streamSize, double randomNumberOrigin, double randomNumberBound) {
        assert false : "Not implemented";
        return null;
    }

    @Override
    public DoubleStream doubles(double randomNumberOrigin, double randomNumberBound) {
        assert false : "Not implemented";
        return null;
    }
}
