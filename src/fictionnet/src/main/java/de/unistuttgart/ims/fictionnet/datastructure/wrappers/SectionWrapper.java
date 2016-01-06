package de.unistuttgart.ims.fictionnet.datastructure.wrappers;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.SectionWithType;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.TextType;

/**
 * @author Lukas Rieger
 * @version 05-11-2015
 * 
 * Wrapper class. 
 * This avoids database related annotations from clases
 * used by the IMSAnalyzer.
 */

public class SectionWrapper extends AbstractWrapper implements Serializable{
	private TextType textType;
	private HashSet<String> castMembers = new HashSet<>();
	private String speakerString;
	
	/**
	 * Required by ORMLite
	 */
	public SectionWrapper() {
		
	}
	
	public SectionWrapper(SectionWithType section) {
		this.start = section.getStart();
		this.end = section.getEnd();
		this.confidence = section.getConfidence();
		this.changeLog=(LinkedList<String>) (section.getChangeLog());
		this.textType = section.getSectionType();
		this.castMembers = section.getCastMembers();
		this.speakerString = section.getSpeakerString();
	}
	
	public SectionWithType getSectionWithType() {
		SectionWithType section = new SectionWithType(this.start, this.end, this.textType, this.speakerString);
		section.setConfidence(this.confidence);
		section.setCastMembers(this.castMembers);
		section.setChangeLog(this.changeLog);
		
		return section;
	}

	/**
	 * @return the textType
	 */
	public TextType getTextType() {
		return textType;
	}

	/**
	 * @param textType the textType to set
	 */
	public void setTextType(TextType textType) {
		this.textType = textType;
	}

	/**
	 * @return the castMembers
	 */
	public HashSet<String> getCastMembers() {
		return castMembers;
	}

	/**
	 * @param castMembers the castMembers to set
	 */
	public void setCastMembers(HashSet<String> castMembers) {
		this.castMembers = castMembers;
	}

	/**
	 * @return the speakerString
	 */
	public String getSpeakerString() {
		return speakerString;
	}

	/**
	 * @param speakerString the speakerString to set
	 */
	public void setSpeakerString(String speakerString) {
		this.speakerString = speakerString;
	}

}
