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
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Presence;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.ActWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.PresenceWrapper;

/**
 * @author Rafael Harth
 * @version 10-28-2015
 * 
 *          This concrete Layer determines during which sections of a text each
 *          person is present.
 * 
 *          See Layer.java for additional Information.
 */

@DatabaseTable(tableName = "presenceLayer")
public class PresenceLayer extends Layer {
	@DatabaseField(dataType = DataType.SERIALIZABLE,columnDefinition = "LONGBLOB")
	private ArrayList<PresenceWrapper> presences;

	public PresenceLayer() {
		this.presences = new ArrayList<PresenceWrapper>();
	}

	public PresenceLayer(List<Presence> presences) {
		ArrayList<PresenceWrapper> presenceWrappers = new ArrayList<>();

		for (int i = 0; i < presences.size(); i++) {
			presenceWrappers.add(new PresenceWrapper(presences.get(i)));
		}
		this.presences = presenceWrappers;
	}

	@Override
	public Annotation getAnnotation(int index) throws InvalidRequestException {
		throw new InvalidRequestException("Can't request presence by index only.");
	}

	/*
	 * Basic Getters and Setters
	 */

	/**
	 * @return the presences
	 */
	@XmlTransient
	public List<Presence> getPresences() {
		List<Presence> newPresences = new ArrayList<>();

		for (int i = 0; i < presences.size(); i++) {
			newPresences.add(presences.get(i).getPresence());
		}
		return newPresences;
	}
	
	/**
	 * Just for XML Serialization, use getPresences()
	 * @return
	 */
	@XmlElement (name= "presences")
	public List<PresenceWrapper> getPresenceWrappers(){
		return presences;
	}

	/**
	 * @param presences
	 *            the presences to set
	 */
	public void setPresences(List<Presence> presences) {
		ArrayList<PresenceWrapper> presenceWrappers = new ArrayList<>();

		for (int i = 0; i < presences.size(); i++) {
			presenceWrappers.add(new PresenceWrapper(presences.get(i)));
		}
		this.presences = presenceWrappers;
	}

}