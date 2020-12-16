package seom.analysis.parameters;

import seom.Configuration;
import seom.analysis.GameType;
import seom.analysis.NetworkType;

public interface Parameter<T> {
    boolean isApplicable(GameType gameType, NetworkType networkType);
    T getNominal();
    T[] getValues();
    void apply(T value, Configuration config);
    String getValueName(T value);
    String getName();
}
