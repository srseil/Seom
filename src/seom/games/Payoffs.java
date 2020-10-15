package seom.games;

public class Payoffs {
    private final int[] payoffs;

    public Payoffs(int... payoffs) {
        this.payoffs = payoffs;
    }

    public int[] getPayoffs() {
        return payoffs;
    }

    public int getPayoffForPlayer(int player) {
        return payoffs[player];
    }
}
