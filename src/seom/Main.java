package seom;

public class Main {

    public static void main(String[] args) {
	    var pd = new PrisonersDilemma();
	    var payoffs = pd.play(PrisonersDilemma.Strategy.Cooperate, PrisonersDilemma.Strategy.Defect);

	    var bg = new BargainingGame();
	    payoffs = bg.play(BargainingGame.Strategy.Demand10, BargainingGame.Strategy.Demand0);

		var ug = new UltimatumGame();
		payoffs = ug.play(UltimatumGame.Strategy.S7Fairman, UltimatumGame.Strategy.S4MadDog);

	    return;
    }
}
