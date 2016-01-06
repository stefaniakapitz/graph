package de.unistuttgart.ims.fictionnet.datastructure.layers.content;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rafael Harth
 * @version 10-10-2015
 * 
 *          Associated with TokenLayer.java
 * 
 *          This class represents a single token (word) in the source text
 * 
 *          See Layer.java for additional Information.
 *          
 *			WARNING: DON'T CHANGE THIS CLASS! WILL CAUSE ERROR IN IMSANALYSIS!!!
 */

public class Token extends Annotation {
	private String lemma;
	private String pos; // part of speach tag

	public Token() {
		// all persisted classes must define a no-arg constructor
		// with at least package visibility
	}

	public Token(int start, int end) {
		super(start, end);
	}

	/*
	 * Basic Getters and Setters
	 */
	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getPos() {
		return pos;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}
}