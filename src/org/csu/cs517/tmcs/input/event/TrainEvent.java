package org.csu.cs517.tmcs.input.event;

public class TrainEvent extends InputEvent {
  
  private String id;

  public TrainEvent(String args) {
    super(0, args, InputEvent.ParseState.TRAIN);
  }

  public String getTrainId() {
    return id;
  }

  @Override
  protected void parseEnd() {
    this.currentState = InputEvent.ParseState.DONE;
    this.id = this.tempStringBuffer.toString();
    this.tempStringBuffer.setLength(0);
  }
  
  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "(" + this.id + ")";
  }
}
