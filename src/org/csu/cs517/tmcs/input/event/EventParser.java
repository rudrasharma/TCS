package org.csu.cs517.tmcs.input.event;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


public class EventParser {
  
  public static final String DEFAULT_FILE = "inputEventSimulationFile.txt";
  
  private List<InputEvent> events = new ArrayList<>();
  
  private StringBuffer tempStringBuffer = new StringBuffer();
  private String tempEventName;
  private int openParenCount;
  
	public EventParser(String file) throws ClassNotFoundException,
			NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, IOException {
	  this.parseFile(file);
  }

	public EventParser() throws ClassNotFoundException, NoSuchMethodException,
			SecurityException, InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, IOException {
	  this(DEFAULT_FILE);
  }
  
  public void parseFile(String fileName) 
      throws IOException, ClassNotFoundException, NoSuchMethodException, 
      SecurityException, InstantiationException, IllegalAccessException, 
      IllegalArgumentException, InvocationTargetException {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
	    BufferedReader in = new BufferedReader(
	    		new InputStreamReader(inputStream));
    String line = in.readLine();
    while (line != null) {
      this.processLine(line);
      line = in.readLine();
    }
    in.close();
  }
  
  public EventQueue getEventQueue() {
    EventQueue eventQueue = new EventQueue(this.events);
    return eventQueue;
  }

  private void processLine(String line) 
      throws ClassNotFoundException, NoSuchMethodException, SecurityException, 
      InstantiationException, IllegalAccessException, IllegalArgumentException, 
      InvocationTargetException {
    char[] charArrayArgs = line.toCharArray();
    boolean isCommentLine = false;
    for (char c : charArrayArgs) {
      if (isCommentLine) {
        break;
      }
      switch (c) {
        case '#':
        case '/':
          isCommentLine = true;
          break;
        case ' ':
        case '\t':
          // Ignore whitespace
          break;
        case '(':
          if (this.openParenCount == 0) {
            this.tempEventName = this.tempStringBuffer.toString();
            this.tempStringBuffer.setLength(0);
          } else {
            this.tempStringBuffer.append(c);
          }
          this.openParenCount++;
          break;
        case ')':
          this.openParenCount--;
          if (this.openParenCount != 0) {
            this.tempStringBuffer.append(c);
          } else {
            this.createAndStoreEvent();
          }
          break;
        default:
          this.tempStringBuffer.append(c);
      }
    }
    if (this.tempStringBuffer.toString().equals("end")) {
      this.tempEventName = this.tempStringBuffer.toString();
      this.tempStringBuffer.setLength(0);
      this.createAndStoreEvent();
    }
  }

  private void createAndStoreEvent() throws ClassNotFoundException,
      NoSuchMethodException, InstantiationException, IllegalAccessException,
      InvocationTargetException {
    Class<?>[] eventArgTypes = new Class[] {String.class};
    String eventParametersString = this.tempStringBuffer.toString();
    Object[] eventArgs = new Object[] {eventParametersString};
    if (this.tempEventName.equals("end")) {
      this.tempEventName = "End";
    }
    Class<?> eventDefinition = Class.forName(
        this.getClass().getPackage().getName() + "." + this.tempEventName);
    Constructor<?> eventConstructor = 
        eventDefinition.getConstructor(eventArgTypes);
    InputEvent event = (InputEvent) eventConstructor.newInstance(eventArgs);
    this.events.add(event);
    this.tempStringBuffer.setLength(0);
  }  
}
