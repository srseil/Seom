package seom.games;

public interface Game {
    Strategy[] getStrategies();
    Payoffs play(Strategy... strategies);
}
