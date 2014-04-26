package org.csu.cs517.tmcs.simulation;

import java.util.ArrayList;
import java.util.List;

public abstract class CurrentTmcsTime {
  
  protected static Time time = new Time(0);
  
  private static List<ITimeIncremented> listeners = new ArrayList<>();

  protected CurrentTmcsTime() {
  }

  public static Time get() {
    return CurrentTmcsTime.time;
  }
  
  public static void addIncrementedListener(ITimeIncremented listener) {
    listeners.add(listener);
  }
  
  protected static void notifyListeners() {
    for (ITimeIncremented listener : listeners) {
      listener.incremented(time);
    }
  }
}
