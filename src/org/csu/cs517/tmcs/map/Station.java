package org.csu.cs517.tmcs.map;

/**
 * Station where trains stops at and where the train leaves to reach other
 * stations. Routes branch from stations
 */
public class Station {
  private final String id;
  private Segment platform;
  private Availability availability = Availability.OPEN;

  public Station(String id) {
    this.id = id;
    this.platform = new Segment(0, this);
  }

  public Segment getPlatform() {
    return platform;
  }

  public Availability getAvailability() {
    return availability;
  }
  
  public void setAvailability(Availability availability) {
    this.availability = availability;
  }
  
  @Override
  public String toString() {
    return this.id;
  }
}
