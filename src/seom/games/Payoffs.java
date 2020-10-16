package seom.games;

public class Payoffs {
    private final double[] payoffs;

    public Payoffs(double... payoffs) {
        this.payoffs = payoffs;
    }

    public double[] getPayoffs() {
        return payoffs;
    }

    public double getPayoffForPlayer(int player) {
        return payoffs[player];
    }
}
