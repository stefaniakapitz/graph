package de.unistuttgart.ims.fictionnet.datastructure.layers.content;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;


public class Speaker extends Annotation {
	private HashSet<String> names;

	/**
	 * Empty Constructor for XML Serialization
	 */
	public Speaker() {
		
	}
	/**
	 * Constructor
	 * 
	 * @param speaker
	 * @param start
	 * @param end
	 */
	public Speaker(HashSet<String> speaker, int start, int end) {
		super(start, end);
		this.names = speaker;

	}

	/**
	 * @return the name
	 */
	public HashSet<String> getNames() {
		return names;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setNames(HashSet<String> names) {
		this.names = names;
	}

}
