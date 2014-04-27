package org.csu.cs517.tmcs.input.event;



public abstract class InputEvent implements Comparable<InputEvent> {

  //Lower number is higher priority within one simulation time unit.
  private int priority;
  
  //A secondary ordering to the priority when prioties are equal.
  //This will preserve the ordering of events received
  private int orderEventReceived;
  
  private static int lastOrder = 0;
  
  protected String args;
  protected StringBuffer tempStringBuffer = new StringBuffer();
  
  protected enum ParseState {
    TRAIN,
    TIME,
    ROUTE,
    STATION,
    DONE
  } 
  
  protected ParseState currentState = ParseState.TRAIN;

  public InputEvent(int priority, String args, ParseState initialState) {
    this.priority = priority;
    this.orderEventReceived = lastOrder++;
    if (this instanceof End) {
      InputEvent.lastOrder = 0;
    }
    this.args = args;
    this.currentState = initialState;
    this.parseArgumentsString();
  }
  
  public int getPriority() {
    return this.priority;
  }
  
  @Override
  public int compareTo(InputEvent o) {
    if (this.priority < o.priority) {
      return -1;
    } else if (this.priority > o.priority) {
      return 1;
    } else if (this.priority == o.priority) {
      if (this.orderEventReceived < o.orderEventReceived) {
        return -1;
      } else if (this.orderEventReceived > o.orderEventReceived) {
        return 1;
      } else {
        return 0;
      }
    }
    return 0;
  }
  
  protected void parseOpenParen() {}
  
  protected void parseCloseParen() {}
  
  protected void parseComma() {}

  protected void parseChar(char c) {
    if (this.currentState != InputEvent.ParseState.DONE) {
      this.tempStringBuffer.append(c);
    }
  }

  protected void parseEnd() {
    this.currentState = InputEvent.ParseState.DONE;
    this.tempStringBuffer.setLength(0);
  }
  
  private void parseArgumentsString() {
    char[] charArrayArgs = this.args.toCharArray();
    for (char c : charArrayArgs) {
      switch (c) {
        case ' ':
          break;
        case '(':
          this.parseOpenParen();
          break;
        case ')':
          this.parseCloseParen();
          break;
        case ',':
          this.parseComma();
          break;
        default:
          this.parseChar(c);
      }
    }
    this.parseEnd();
  }
  
  @Override
  public String toString() {
    return this.getClass().getSimpleName();
  }
}
