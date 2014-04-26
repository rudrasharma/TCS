package org.csu.cs517.tmcs.system.event;

import java.util.ArrayList;
import java.util.List;


public class SystemEventGenerator {

  private static SystemEventGenerator instance = new SystemEventGenerator();
  
  private List<IChangeLightListener> changeLightListeners = new ArrayList<>();
  
  private SystemEventGenerator() {}
  
  public static SystemEventGenerator getInstance() {
    return instance;
  }
  
  public void generateChangeLightEvent(ChangeLight event) {
    for (IChangeLightListener changeLightListener : changeLightListeners) {
      changeLightListener.lightChange(event);
    }
  }
  
  public void addChangeLightListener(IChangeLightListener listener) {
    this.changeLightListeners.add(listener);
  }
}
