package de.unistuttgart.ims.fictionnet.datastructure.layers;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import com.j256.ormlite.field.DatabaseField;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Annotation;

/**
 * @author Rafael Harth
 * @version 10-09-2015
 * 
 *          This class is the interface for our system of saving meta information for static piece of text. Each kind of
 *          information corresponds to its own concrete layer class. Each type of layer stores a varying number of
 *          Annotation instances that divide the entire text into sections by way of indexing. The division may or may
 *          not be exhaustive (= cover every index of the text), but is always without overlap. The source text is not
 *          influenced by these layers.
 * 
 *          Example: actLayer may have the sections (0, 1242), (1266, 5002), (5032, 8822), (8845, 11301) and would thus
 *          divide the entire text into 4 sections (with a few letters in between) that correspond to four respective
 *          acts.
 * 
 *          This way, the amount of data stored can easily be expanded by including additional layers.
 * 
 *          Each concrete layer class will extends this. Also see Annotation.java
 */

public abstract class Layer {

	/**
	 * Former name: getAnnotationForIndex()
	 * 
	 * @return the annotation that covers the given index
	 * @throws InvalidRequestException if the current layer can't return annotations by index
	 */
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int containerId;
	
	public abstract Annotation getAnnotation(int index) throws InvalidRequestException;

	public int getLayerId() {
		return this.id;
	}

	public void setContainerId(int id){
		this.containerId = id;
	}

	public int getContainerId() {
		return this.containerId;
	}
}