package de.unistuttgart.ims.fictionnet.datastructure.layers.content;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rafael Harth
 * @version 10-10-2015
 *
 *          Associated with EventLayer.java and EventType.java
 * 
 *          This class represents a single event. It stores... - a to-be defined
 *          type of event - a list of 1+ sections in the text wherein the event
 *          takes place
 * 
 *          See Layer.java for additional information.
 *          
  *         WARNING: DON'T CHANGE THIS CLASS! WILL CAUSE ERROR IN IMSANALYSIS!!!
 */

public class Event extends Annotation {
	private EventType eventType;

	private List<Annotation> occurences = new ArrayList<Annotation>();

	public Event() {
		// all persisted classes must define a no-arg constructor
		// with at least package visibility
	}

	public Event(int start, int end) {
		super(start, end);
	}

	/*
	 * Basic Getters and Setters
	 */
	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public List<Annotation> getOccurences() {
		return occurences;
	}

	public void setOccurences(List<Annotation> occurences) {
		this.occurences = occurences;
	}
}