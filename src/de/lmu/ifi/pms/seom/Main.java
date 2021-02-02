package de.lmu.ifi.pms.seom;

import de.lmu.ifi.pms.seom.analysis.GameType;
import de.lmu.ifi.pms.seom.analysis.NetworkType;
import de.lmu.ifi.pms.seom.analysis.SensitivityAnalysis;
import de.lmu.ifi.pms.seom.utils.Log;

public class Main {
    public static void main(String[] args) {
        for (GameType gameType : GameType.values()) {
            for (NetworkType networkType : NetworkType.values()) {
                String logPath = "analysis/" + gameType + "/" + networkType + "/log.txt";
                Log.reset();
                Log.enableFileLogging(logPath);
                Log.enableSimpleLogging();

                // Use fewer repetitions for fully connected networks due to bad performance
                if (networkType == NetworkType.FullyConnected) {
                    SensitivityAnalysis.NUM_REPETITIONS = 10;
                } else {
                    SensitivityAnalysis.NUM_REPETITIONS = 100;
                }

                var analysis = new SensitivityAnalysis(gameType, networkType);
                analysis.simulate();
                analysis.writeResultTables("analysis");
            }
        }
    }
}
