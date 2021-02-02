package de.lmu.ifi.pms.seom;

import de.lmu.ifi.pms.seom.games.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GameTests {
    @Test
    public void playPrisonersDilemma() {
        var game = new PrisonersDilemma(
            new Payoffs(2.0, 2.0),
            new Payoffs(0.0, 3.0),
            new Payoffs(3.0, 0.0),
            new Payoffs(1.0, 1.0)
        );

        Assertions.assertArrayEquals(
            game.play(PrisonersDilemma.Strategy.Cooperate, PrisonersDilemma.Strategy.Cooperate).getPayoffs(),
            new double[] {2.0, 2.0}
        );
        Assertions.assertArrayEquals(
            game.play(PrisonersDilemma.Strategy.Cooperate, PrisonersDilemma.Strategy.Defect).getPayoffs(),
            new double[] {0.0, 3.0}
        );
        Assertions.assertArrayEquals(
            game.play(PrisonersDilemma.Strategy.Defect, PrisonersDilemma.Strategy.Cooperate).getPayoffs(),
            new double[] {3.0, 0.0}
        );
        Assertions.assertArrayEquals(
            game.play(PrisonersDilemma.Strategy.Defect, PrisonersDilemma.Strategy.Defect).getPayoffs(),
            new double[] {1.0, 1.0}
        );
    }

    @Test
    public void playStagHunt() {
        var game = new StagHunt(
            new Payoffs(2.0, 2.0),
            new Payoffs(0.0, 1.0),
            new Payoffs(1.0, 0.0),
            new Payoffs(1.0, 1.0)
        );

        Assertions.assertArrayEquals(
            game.play(StagHunt.Strategy.Stag, StagHunt.Strategy.Stag).getPayoffs(),
            new double[] {2.0, 2.0}
        );
        Assertions.assertArrayEquals(
            game.play(StagHunt.Strategy.Stag, StagHunt.Strategy.Hare).getPayoffs(),
            new double[] {0.0, 1.0}
        );
        Assertions.assertArrayEquals(
            game.play(StagHunt.Strategy.Hare, StagHunt.Strategy.Stag).getPayoffs(),
            new double[] {1.0, 0.0}
        );
        Assertions.assertArrayEquals(
            game.play(StagHunt.Strategy.Hare, StagHunt.Strategy.Hare).getPayoffs(),
            new double[] {1.0, 1.0}
        );
    }

    @Test
    public void playBargainingSubgame() {
        var game = new BargainingSubgame();

        Assertions.assertArrayEquals(
            game.play(BargainingSubgame.Strategy.Demand5, BargainingSubgame.Strategy.Demand5).getPayoffs(),
            new double[] {5.0, 5.0}
        );
        Assertions.assertArrayEquals(
            game.play(BargainingSubgame.Strategy.Demand4, BargainingSubgame.Strategy.Demand5).getPayoffs(),
            new double[] {4.0, 5.0}
        );
        Assertions.assertArrayEquals(
            game.play(BargainingSubgame.Strategy.Demand5, BargainingSubgame.Strategy.Demand6).getPayoffs(),
            new double[] {0.0, 0.0}
        );
        Assertions.assertArrayEquals(
            game.play(BargainingSubgame.Strategy.Demand6, BargainingSubgame.Strategy.Demand4).getPayoffs(),
            new double[] {6.0, 4.0}
        );
        Assertions.assertArrayEquals(
            game.play(BargainingSubgame.Strategy.Demand4, BargainingSubgame.Strategy.Demand4).getPayoffs(),
            new double[] {4.0, 4.0}
        );
        Assertions.assertArrayEquals(
            game.play(BargainingSubgame.Strategy.Demand6, BargainingSubgame.Strategy.Demand6).getPayoffs(),
            new double[] {0.0, 0.0}
        );
    }

    @Test
    public void playUltimatumSubgame() {
        var game = new UltimatumSubgame();

        // Fairman is proposer
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S7Fairman, UltimatumSubgame.Strategy.S1Gamesman).getPayoffs(),
            new double[] {5.0, 5.0}
        );
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S7Fairman, UltimatumSubgame.Strategy.S2).getPayoffs(),
            new double[] {0.0, 0.0}
        );
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S7Fairman, UltimatumSubgame.Strategy.S3).getPayoffs(),
            new double[] {5.0, 5.0}
        );
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S7Fairman, UltimatumSubgame.Strategy.S4MadDog).getPayoffs(),
            new double[] {0.0, 0.0}
        );
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S7Fairman, UltimatumSubgame.Strategy.S5EasyRider).getPayoffs(),
            new double[] {5.0, 5.0}
        );
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S7Fairman, UltimatumSubgame.Strategy.S6).getPayoffs(),
            new double[] {0.0, 0.0}
        );
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S7Fairman, UltimatumSubgame.Strategy.S7Fairman).getPayoffs(),
            new double[] {5.0, 5.0}
        );
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S7Fairman, UltimatumSubgame.Strategy.S8).getPayoffs(),
            new double[] {0.0, 0.0}
        );

        // Fairman is responder
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S1Gamesman, UltimatumSubgame.Strategy.S7Fairman).getPayoffs(),
            new double[] {0.0, 0.0}
        );
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S2, UltimatumSubgame.Strategy.S7Fairman).getPayoffs(),
            new double[] {0.0, 0.0}
        );
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S3, UltimatumSubgame.Strategy.S7Fairman).getPayoffs(),
            new double[] {0.0, 0.0}
        );
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S4MadDog, UltimatumSubgame.Strategy.S7Fairman).getPayoffs(),
            new double[] {0.0, 0.0}
        );
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S5EasyRider, UltimatumSubgame.Strategy.S7Fairman).getPayoffs(),
            new double[] {5.0, 5.0}
        );
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S6, UltimatumSubgame.Strategy.S7Fairman).getPayoffs(),
            new double[] {5.0, 5.0}
        );
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S7Fairman, UltimatumSubgame.Strategy.S7Fairman).getPayoffs(),
            new double[] {5.0, 5.0}
        );
        Assertions.assertArrayEquals(
            game.play(UltimatumSubgame.Strategy.S8, UltimatumSubgame.Strategy.S7Fairman).getPayoffs(),
            new double[] {5.0, 5.0}
        );
    }
}
