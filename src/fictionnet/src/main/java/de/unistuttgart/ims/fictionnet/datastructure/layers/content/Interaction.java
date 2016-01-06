package de.unistuttgart.ims.fictionnet.datastructure.layers.content;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author Rafael Harth
 * @version 10-15-2015
 *
 *          Associated with InteractionLayer.java and InteractionType.java
 * 
 *          This class represents a single Interaction of one person with one or
 *          more others in the source text. It belongs to one element of the
 *          cast list and stores a subset of cast list with size >= 1 as well as
 *          an enum to determine the kind of interaction
 * 
 *          See Layer.java for additional information.
 */

public class Interaction extends Annotation {
	private HashSet<String> protagonist = new HashSet<String>();
	private HashSet<String> involvedCastMembers = new HashSet<String>();
	private InteractionType interactionType;

	public Interaction() {
		// all persisted classes must define a no-arg constructor
		// with at least package visibility
	}

	public Interaction(int start, int end) {
		super(start, end);
	}

	public Set<String> getInvolvedCastMembers() {
		return involvedCastMembers;
	}

	public void setInvolvedCastMembers(HashSet<String> involvedCastMembers) {
		this.involvedCastMembers = involvedCastMembers;
	}

	public InteractionType getType() {
		return interactionType;
	}

	public void setType(InteractionType type) {
		this.interactionType = type;
	}

	/**
	 * @return the protagonist
	 */
	public HashSet<String> getProtagonist() {
		return protagonist;
	}

	/**
	 * @param protagonist the protagonist to set
	 */
	public void setProtagonist(HashSet<String> protagonist) {
		this.protagonist = protagonist;
	}
	
}