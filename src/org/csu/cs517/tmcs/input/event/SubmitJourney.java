package org.csu.cs517.tmcs.input.event;

import java.util.ArrayList;
import java.util.List;

public class SubmitJourney extends InputEvent {
  
  private String trainId;
  private String startTime;
  private List<String> routeIds;
  private List<String> stopStationIds;
  
  public SubmitJourney(String args) {
    super(1, args, ParseState.TRAIN);
  }

  public String getTrainId() {
    return trainId;
  }

  public String getStartTime() {
    return startTime;
  }

  public List<String> getRouteIds() {
    return routeIds;
  }

  public List<String> getStopStationIds() {
    return stopStationIds;
  }

  @Override
  protected void parseOpenParen() {
    if (this.currentState == InputEvent.ParseState.TIME) {
      this.currentState = InputEvent.ParseState.ROUTE;
    } else if (this.currentState == InputEvent.ParseState.ROUTE) {
      this.currentState = InputEvent.ParseState.STATION;
    }    
  }

  @Override
  protected void parseCloseParen() {
    if (this.currentState == InputEvent.ParseState.ROUTE) {
      this.routeIds.add(this.tempStringBuffer.toString());
      this.tempStringBuffer.setLength(0);
    } else if (this.currentState == InputEvent.ParseState.STATION) {
      this.currentState = InputEvent.ParseState.DONE;
      if (this.tempStringBuffer.length() > 0) {
        this.stopStationIds.add(this.tempStringBuffer.toString());
      }
      this.tempStringBuffer.setLength(0);
    }    
  }

  @Override
  protected void parseComma() {
    if (this.tempStringBuffer.length() > 0) {
      if (this.routeIds == null) {
        this.routeIds = new ArrayList<>();
      }
      if (this.stopStationIds == null) {
        this.stopStationIds = new ArrayList<>();
      }
      if (this.currentState == InputEvent.ParseState.TRAIN) {
        this.currentState = InputEvent.ParseState.TIME;
        this.trainId = this.tempStringBuffer.toString();
        this.tempStringBuffer.setLength(0);
      } else if (this.currentState == InputEvent.ParseState.TIME) {
        this.startTime = this.tempStringBuffer.toString();
        this.tempStringBuffer.setLength(0);
      } else if (this.currentState == InputEvent.ParseState.ROUTE) {
        this.routeIds.add(this.tempStringBuffer.toString());
        this.tempStringBuffer.setLength(0);
      } else if (this.currentState == InputEvent.ParseState.STATION) {
        this.stopStationIds.add(this.tempStringBuffer.toString());
        this.tempStringBuffer.setLength(0);
      }
    }
  }
  
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.getClass().getSimpleName() + 
        "(" + this.trainId + "," + this.startTime + ",(");
    boolean firstId = true;
    for (String rId : this.routeIds) {
      if (!firstId) {
        sb.append(",");
      }
      sb.append(rId);
      firstId = false;
    }
    sb.append("),(");
    firstId = true;
    for (String sId : this.stopStationIds) {
      if (!firstId) {
        sb.append(",");
      }
      sb.append(sId);
      firstId = false;
    }
    sb.append("))");
    return sb.toString();
  }
}
