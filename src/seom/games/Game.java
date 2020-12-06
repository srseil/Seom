package seom.games;

public interface Game {
    Strategy[] getStrategies();
    Strategy[] getMoralStrategies();
    Payoffs play(Strategy... strategies);
}
