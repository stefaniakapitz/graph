package de.unistuttgart.ims.fictionnet.datastructure.layers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Annotation;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.ActWrapper;

/**
 * @author Rafael Harth
 * @version 10-09-2015
 * 
 *          This concrete Layer divides the text into acts.
 * 
 *          See Layer.java for additional Information.
 */

@DatabaseTable(tableName = "actLayer")
public class ActLayer extends Layer {
	@DatabaseField(dataType = DataType.SERIALIZABLE,columnDefinition = "LONGBLOB")
	private ArrayList<ActWrapper> acts;

	public ActLayer() {
		this.acts = new ArrayList<ActWrapper>();
	}

	public ActLayer(List<Act> acts) {
		ArrayList<ActWrapper> actwrappers = new ArrayList<>();

		for (int i = 0; i < acts.size(); i++) {
			actwrappers.add(new ActWrapper(acts.get(i)));
		}
		this.acts = actwrappers;
	}

	@Override
	public Annotation getAnnotation(int index) {
		return acts.get(index).getAct();
	}

	public void addAct(Act act) {
		acts.add(new ActWrapper(act));
	}

	/*
	 * Basic Getters and Setters
	 */

	public Act getAct(int index) {
		return acts.get(index).getAct();
	}

	/**
	 * @param acts
	 *            the acts to set
	 */
	public void setActs(List<Act> acts) {
		ArrayList<ActWrapper> actwrappers = new ArrayList<>();

		for (int i = 0; i < acts.size(); i++) {
			actwrappers.add(new ActWrapper(acts.get(i)));
		}
		this.acts = actwrappers;
	}

	@XmlTransient
	public List<Act> getActs() {
		List<Act> newActs = new ArrayList<>();
		for (int i = 0; i < acts.size(); i++) {
			newActs.add(acts.get(i).getAct());
		}
		return newActs;
	}
	
	
	/**
	 * Just for XML Serialization, use getActs()
	 * @return
	 */
	@XmlElement (name= "acts")
	public List<ActWrapper> getActWrappers(){
		return acts;
	}

	public Act getLatestAct() {
		return acts.get(acts.size() - 1).getAct();
	}

	/**
	 * @param acts the acts to set
	 */
	public void setActs(ArrayList<ActWrapper> acts) {
		this.acts = acts;
	}
}