package de.unistuttgart.ims.fictionnet.datastructure.wrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Interaction;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.InteractionType;

/**
 * @author Lukas Rieger
 * @version 05-11-2015
 * 
 * Wrapper class. 
 * This avoids database related annotations from clases
 * used by the IMSAnalyzer.
 */

public class InteractionWrapper extends AbstractWrapper implements Serializable{
	private HashSet<String> protagonists = new HashSet<String>();
	private HashSet<String> involvedCastMembers = new HashSet<String>();
	private InteractionType interactionType;
	
	/**
	 * Required by ORMLite
	 */
	public InteractionWrapper() {
		
	}
	
	public InteractionWrapper(Interaction interaction) {
		this.start = interaction.getStart();
		this.end = interaction.getEnd();
		this.confidence = interaction.getConfidence();
		this.changeLog=(LinkedList<String>) (interaction.getChangeLog());
		this.involvedCastMembers = (HashSet<String>) interaction.getInvolvedCastMembers();
		this.interactionType = interaction.getType();
		this.protagonists = interaction.getProtagonist();
	}
	
	public Interaction getInteraction() {
		Interaction interaction = new Interaction(this.start, this.end);
		interaction.setConfidence(this.confidence);
		interaction.setInvolvedCastMembers(this.involvedCastMembers);
		interaction.setType(this.interactionType);
		interaction.setChangeLog(this.changeLog);
		interaction.setProtagonist(this.protagonists);
		return interaction;
	}

	/**
	 * @return the protagonists
	 */
	public HashSet<String> getProtagonists() {
		return protagonists;
	}

	/**
	 * @param protagonists the protagonists to set
	 */
	public void setProtagonists(HashSet<String> protagonists) {
		this.protagonists = protagonists;
	}

	/**
	 * @return the involvedCastMembers
	 */
	public HashSet<String> getInvolvedCastMembers() {
		return involvedCastMembers;
	}

	/**
	 * @param involvedCastMembers the involvedCastMembers to set
	 */
	public void setInvolvedCastMembers(HashSet<String> involvedCastMembers) {
		this.involvedCastMembers = involvedCastMembers;
	}

	/**
	 * @return the interactionType
	 */
	public InteractionType getInteractionType() {
		return interactionType;
	}

	/**
	 * @param interactionType the interactionType to set
	 */
	public void setInteractionType(InteractionType interactionType) {
		this.interactionType = interactionType;
	}
	
	

}
