package org.csu.cs517.tmcs.simulation;

/**
 * The class is only accessible to the simulation part of the software so that
 * only the simulation may be able to increment time.
 */
class SimulationTime extends CurrentTmcsTime {

  private SimulationTime() {
  }

  /**
   * Increments the current TMCS Time by adding one to the current time units.
   */
  public static void increment() {
    SimulationTime.time = new Time(SimulationTime.time.getUnits() + 1);
    SimulationTime.notifyListeners();
  }
}
