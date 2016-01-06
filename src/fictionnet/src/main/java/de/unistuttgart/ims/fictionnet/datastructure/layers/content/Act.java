package de.unistuttgart.ims.fictionnet.datastructure.layers.content;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rafael Harth
 * @version 10-09-2015
 * 
 *          Corresponds to ActLayer. An act is a section with an act number and
 *          represents an act in a drama.
 * 
 *          See Layer.java for additional Information.
 *          
 *          WARNING: DON'T CHANGE THIS CLASS! WILL CAUSE ERROR IN IMSANALYSIS!!!
 */

public class Act extends Annotation {
	private int actNumber;
	private String content;

	public Act() {
	}

	public Act(int start, int end, int actNumber) {
		super(start, end);
		this.actNumber = actNumber;
	}
	
	public Act(int actNumber, String content) {
		this.actNumber = actNumber;
		this.content = content;
	}

	@Override
	public String toString() {
		return content;
	}
	/*
	 * Basic Getters and Setters
	 */
	public int getActNumber() {
		return actNumber;
	}

	public void setActNumber(int actNumber) {
		this.actNumber = actNumber;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}