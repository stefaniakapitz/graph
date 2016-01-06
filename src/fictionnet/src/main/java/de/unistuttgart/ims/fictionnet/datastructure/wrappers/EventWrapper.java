package de.unistuttgart.ims.fictionnet.datastructure.wrappers;

import java.io.Serializable;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Event;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.EventType;

/**
 * @author Lukas Rieger
 * @version 05-11-2015
 * 
 *          Wrapper class. This avoids database related annotations from clases
 *          used by the IMSAnalyzer.
 */

public class EventWrapper extends AbstractWrapper implements Serializable {

	private EventType eventType;
	//@ForeignCollectionField(eager = true)
	//private ForeignCollection<Annotation> occurences;

	
	/**
	 * Required by ORMLite
	 */
	public EventWrapper() {

	}

	public EventWrapper(Event event) {
		this.start = event.getStart();
		this.end = event.getEnd();
		this.confidence = event.getConfidence();
		this.changeLog = (LinkedList<String>) (event.getChangeLog());
		this.eventType = event.getEventType();
		//this.occurences = (ForeignCollection<Annotation>) event.getOccurences();
	}

	public Event getEvent() {
		Event event = new Event();
		event.setEnd(this.start);
		event.setStart(this.start);
		event.setConfidence(this.confidence);
		//event.setOccurences((List<Annotation>) this.occurences);
		event.setEventType(this.eventType);
		event.setChangeLog(this.changeLog);

		return event;
	}

	/**
	 * @return the eventType
	 */
	public EventType getEventType() {
		return eventType;
	}

	/**
	 * @param eventType the eventType to set
	 */
	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

}
