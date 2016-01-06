package de.unistuttgart.ims.fictionnet.datastructure.layers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Annotation;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Event;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.ActWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.EventWrapper;

/**
 * @author Rafael Harth
 * @version 10-10-2015
 * 
 *          This concrete Layer stores any number of currently undefined Events
 *          which happen in the source text.
 * 
 *          See Layer.java for additional Information.
 */

@DatabaseTable(tableName = "eventLayer")
public class EventLayer extends Layer {
	@DatabaseField(dataType = DataType.SERIALIZABLE,columnDefinition = "LONGBLOB")
	private ArrayList<EventWrapper> events = new ArrayList<>();

	public EventLayer() {
		this.events = new ArrayList<EventWrapper>();
	}

	public EventLayer(List<Event> events) {
		ArrayList<EventWrapper> eventwrappers = new ArrayList<>();

		for (int i = 0; i < events.size(); i++) {
			eventwrappers.add(new EventWrapper(events.get(i)));
		}
		this.events = eventwrappers;
	}

	@Override
	public Annotation getAnnotation(int index) {
		return events.get(index).getEvent();
	}

	/*
	 * Basic Getters and Setters
	 */
	@XmlTransient
	public List<Event> getEvents() {
		List<Event> newEvents = new ArrayList<>();

		for (int i = 0; i < events.size(); i++) {
			newEvents.add(events.get(i).getEvent());
		}
		return newEvents;
	}
	
	/**
	 * Just for XML Serialization, use getEvents()
	 * @return
	 */
	@XmlElement (name= "events")
	public List<EventWrapper> getEventWrappers(){
		return events;
	}

	public void setEvents(List<Event> events) {
		ArrayList<EventWrapper> eventwrappers = new ArrayList<>();

		for (int i = 0; i < events.size(); i++) {
			eventwrappers.add(new EventWrapper(events.get(i)));
		}
		this.events = eventwrappers;
	}

}