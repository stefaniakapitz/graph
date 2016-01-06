package de.unistuttgart.ims.fictionnet.datastructure.layers.content;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rafael Harth
 * @version 10-23-2015
 * 
 *          Corresponds to PresenceLayer. An Presence instance is an Annotation
 *          without additional data that signals the presence of a character
 *          during its section.
 * 
 *          See Layer.java for additional Information.
 */

public class Presence extends Annotation {
	private String person;

	public Presence() {
		// all persisted classes must define a no-arg constructor
		// with at least package visibility
	}

	public Presence(int start, int end) {
		super(start, end);
	}

	/**
	 * @return the person
	 */
	public String getPerson() {
		return person;
	}

	/**
	 * @param person
	 *            the person to set
	 */
	public void setPerson(String person) {
		this.person = person;
	}
}