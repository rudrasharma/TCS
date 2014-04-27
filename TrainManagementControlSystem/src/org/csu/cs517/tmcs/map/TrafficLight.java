package org.csu.cs517.tmcs.map;

import org.csu.cs517.tmcs.system.event.ChangeLight;
import org.csu.cs517.tmcs.system.event.IChangeLightListener;
import org.csu.cs517.tmcs.system.event.SystemEventGenerator;

public class TrafficLight implements IChangeLightListener {
  private TrafficLightColor trafficLightColor = TrafficLightColor.GREEN;
  private final Segment atSegment;
  
  public TrafficLight(Segment atSegment) {
    this.atSegment = atSegment;
    SystemEventGenerator.getInstance().addChangeLightListener(this);
  }

  @Override
  public void lightChange(ChangeLight changeLight) {
    if ((changeLight.getRoute() == this.atSegment.getRoute() ||
        changeLight.getStation() == this.atSegment.getStation()) &&
        changeLight.getSegmentId() == this.atSegment) {
      this.trafficLightColor = changeLight.getColor();
    }
  }

  public TrafficLightColor getColor() {
    return this.trafficLightColor;
  }
}
