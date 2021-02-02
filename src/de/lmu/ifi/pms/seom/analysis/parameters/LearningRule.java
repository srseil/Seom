package de.lmu.ifi.pms.seom.analysis.parameters;

import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.analysis.GameType;
import de.lmu.ifi.pms.seom.analysis.NetworkType;
import de.lmu.ifi.pms.seom.learning.BestResponse;
import de.lmu.ifi.pms.seom.learning.ImitateAverage;
import de.lmu.ifi.pms.seom.learning.ImitateBest;
import de.lmu.ifi.pms.seom.learning.ImitateProbability;

public class LearningRule implements Parameter<de.lmu.ifi.pms.seom.learning.LearningRule> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return true;
    }

    @Override
    public de.lmu.ifi.pms.seom.learning.LearningRule getNominal() {
        return new ImitateBest();
    }

    @Override
    public de.lmu.ifi.pms.seom.learning.LearningRule[] getValues() {
        return new de.lmu.ifi.pms.seom.learning.LearningRule[] {
            new ImitateBest(),
            new ImitateProbability(),
            new ImitateAverage(),
            new BestResponse()
        };
    }

    @Override
    public void apply(de.lmu.ifi.pms.seom.learning.LearningRule value, Configuration config) {
        config.setLearningRule(value);
    }

    @Override
    public String getValueName(de.lmu.ifi.pms.seom.learning.LearningRule value) {
        if (value instanceof ImitateBest) {
            return "IBest";
        } else if (value instanceof ImitateProbability) {
            return "IProb";
        } else if (value instanceof ImitateAverage) {
            return "IAvg";
        } else if (value instanceof BestResponse) {
            return "BestR";
        } else {
            return "unknown";
        }
    }

    @Override
    public String getName() {
        return "learning rule";
    }
}
