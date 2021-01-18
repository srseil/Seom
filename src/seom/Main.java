package seom;

import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.analysis.SensitivityAnalysis;
import seom.utils.Log;

public class Main {
    public static void main(String[] args) {
        var gameTypes = new GameType[] {
            GameType.PrisonersDilemma,
            GameType.StagHunt,
            GameType.BargainingSubgame,
            GameType.UltimatumGame
        };

        for (GameType gameType : gameTypes) {
            for (NetworkType networkType : NetworkType.values()) {
                String logPath = "analysis/" + gameType + "/" + networkType + "/log.txt";
                Log.reset();
                Log.enableFileLogging(logPath);
                Log.enableSimpleLogging();

                var analysis = new SensitivityAnalysis(gameType, networkType);
                analysis.simulate();
                analysis.writeResultTables("analysis");
            }
        }
    }
}
