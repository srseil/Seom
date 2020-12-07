package seom.games;

public interface Game {
    boolean isSequential();
    Strategy[] getStrategies();
    Strategy[] getMoralStrategies();
    Payoffs play(Strategy... strategies);
}
