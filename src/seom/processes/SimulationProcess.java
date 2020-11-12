package seom.processes;

import sim.engine.Steppable;

public interface SimulationProcess extends Steppable {
    boolean isStochastic();
    void reset();
}
