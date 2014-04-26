package org.csu.cs517.tmcs.map;

public enum TrafficLightColor {
	RED("Red"),
	GREEN("Green");
	
	private final String name;
	TrafficLightColor(String name){
		this.name = name;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
	  return name;
	}
}
