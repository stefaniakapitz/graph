package de.unistuttgart.ims.fictionnet.datastructure.layers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Annotation;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Event;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Interaction;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.ActWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.EventWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.InteractionWrapper;

/**
 * @author Rafael Harth
 * @version 10-15-2015
 * 
 *          This concrete Layer stores occurrences wherein one person from the
 *          source text interacts with another person. Each instance of this
 *          corresponds to one Interaction object.
 * 
 *          See Layer.java for additional Information.
 */

@DatabaseTable(tableName = "interactionLayer")
public class InteractionLayer extends Layer {
	@DatabaseField(dataType = DataType.SERIALIZABLE,columnDefinition = "LONGBLOB")
	private ArrayList<InteractionWrapper> interactions;

	public InteractionLayer() {
		this.interactions = new ArrayList<InteractionWrapper>();
	}

	public InteractionLayer(ArrayList<InteractionWrapper> interactions) {
		this.interactions = interactions;
	}

	/*
	 * Basic Getters and Setters
	 */
	@Override
	public Annotation getAnnotation(int index) throws InvalidRequestException {
		throw new InvalidRequestException("Can't reference Interaction by index");
	}

	@XmlTransient
	public  ArrayList<Interaction> getInteractions() {
		ArrayList<Interaction> newInteractions = new ArrayList<>();

		for (int i = 0; i < interactions.size(); i++) {
			newInteractions.add(interactions.get(i).getInteraction());
		}
		return  newInteractions;
	}
	
	/**
	 * Just for XML Serialization, use getInteractions()
	 * @return
	 */
	@XmlElement (name= "interactions")
	public List<InteractionWrapper> getInteractionWrappers(){
		return interactions;
	}

	public void setInteractions( ArrayList<Interaction> interactions) {
		ArrayList<InteractionWrapper> interactionWrappers = new ArrayList<>();

		for (int i = 0; i < interactions.size(); i++) {
			interactionWrappers.add(new InteractionWrapper(interactions.get(i)));
		}
		this.interactions = interactionWrappers;
	}
}