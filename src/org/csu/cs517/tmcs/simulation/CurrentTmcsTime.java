package org.csu.cs517.tmcs.simulation;

public abstract class CurrentTmcsTime {
  
  protected static Time time = new Time(0);

  protected CurrentTmcsTime() {
  }

  public static Time get() {
    return CurrentTmcsTime.time;
  }
}
