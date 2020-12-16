package seom.analysis.parameters;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;
import seom.learning.BestResponse;
import seom.learning.ImitateAverage;
import seom.learning.ImitateBest;
import seom.learning.ImitateProbability;

public class LearningRule implements Parameter<seom.learning.LearningRule> {
    @Override
    public boolean isApplicable(GameType gameType, NetworkType networkType) {
        return true;
    }

    @Override
    public seom.learning.LearningRule getNominal() {
        return new ImitateBest();
    }

    @Override
    public seom.learning.LearningRule[] getValues() {
        return new seom.learning.LearningRule[] {
            new ImitateBest(),
            new ImitateProbability(),
            new ImitateAverage(),
            new BestResponse()
        };
    }

    @Override
    public void apply(seom.learning.LearningRule value, Configuration config) {
        config.setLearningRule(value);
    }

    @Override
    public String getValueName(seom.learning.LearningRule value) {
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
