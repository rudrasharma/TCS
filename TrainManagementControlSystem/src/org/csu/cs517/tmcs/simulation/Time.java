package org.csu.cs517.tmcs.simulation;

public class Time {
  protected int units;

  public Time(int units) {
    if (units < 0) {
      // Time cannot be negative
      throw new IllegalArgumentException("Argument units is less than zero.");
    }
    this.units = units;
  }

  public Time(Time time) {
    this(time.units);
  }

  public int getUnits() {
    return this.units;
  }

  public boolean less(Time other) {
    return this.units < other.units;
  }

  public boolean equal(Time other) {
    return this.units == other.units;
  }

  public boolean greater(Time other) {
    return this.units > other.units;
  }
  
  @Override
  public String toString() {
  	return Integer.toString(units);
  }

  public Time sub(Time time) {
    return new Time(this.units - time.units);
  }
}
