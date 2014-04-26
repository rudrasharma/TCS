package org.csu.cs517.tmcs.input.event;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class EventQueue {
  Queue<InputEvent> queue = new LinkedList<>();

  public EventQueue(List<InputEvent> events) {
    this.setEventsIntoQueueByPriorityWithinSimulationLoops(events);
  }
  
  /**
   * @return The next event to be processed and consumed.
   */
  public InputEvent getNextEvent() {
    return queue.poll();
  }

  /**
   * Populates the {@link #queue} where events are prioritized within each
   * end loop.
   * @param events
   */
  private void setEventsIntoQueueByPriorityWithinSimulationLoops(
      List<InputEvent> events) {
    PriorityQueue<InputEvent> priorityQueue = new PriorityQueue<>();
    for (InputEvent event : events) {
      priorityQueue.add(event);
      if (event instanceof End) {
        this.queue.addAll(priorityQueue);
        priorityQueue.clear();
      }
    }
  }  
}
