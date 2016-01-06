package de.unistuttgart.ims.fictionnet.datastructure.layers.content;

import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;
/**
 * @author Rafael Harth
 * @version 10-10-2015
 * 
 *          Associated with TypeOfTextLayer.java and TextType.java
 * 
 *          This class represents a single section that's assigned to an element of the TypeOfText enum. If the section
 *          correlates to a spoken section or a speaker, it also stores the relevant subset of cast members. If not,
 *          this variable is without effect.
 * 
 *          See Layer.java for additional information.
 */

public class SectionWithType extends Annotation {
	private TextType textType;
	private HashSet<String> castMembers = new HashSet<>();
	private String speakerString;

	public SectionWithType(int start, int end) {
		super(start, end);
	}

	public SectionWithType(int start, int end, TextType textType) {
		super(start, end);
		this.textType = textType;
	}

	public SectionWithType(int start, int end, TextType textType, String speakerString) {
		super(start, end);
		this.textType = textType;
		this.speakerString = speakerString;
	}

	public SectionWithType() {
		// all persisted classes must define a no-arg constructor
		// with at least package visibility
	}

	/*
	 * Basic Getters and Setters
	 */
	public TextType getSectionType() {
		return textType;
	}

	public void setSectionType(TextType sectionType) {
		this.textType = sectionType;
	}

	public HashSet<String> getCastMembers() {
		return castMembers;
	}

	public void setCastMembers(HashSet<String> castMembers) {
		this.castMembers = castMembers;
	}

	public String getSpeakerString() {
		return speakerString;
	}

	public void setSpeakerString(String speaker) {
		this.speakerString = speaker;
	}
}