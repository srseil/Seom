package de.lmu.ifi.pms.seom.analysis.parameters;

import de.lmu.ifi.pms.seom.Configuration;
import de.lmu.ifi.pms.seom.analysis.GameType;
import de.lmu.ifi.pms.seom.analysis.NetworkType;

public interface Parameter<T> {
    boolean isApplicable(GameType gameType, NetworkType networkType);
    T getNominal();
    T[] getValues();
    void apply(T value, Configuration config);
    String getValueName(T value);
    String getName();
}
