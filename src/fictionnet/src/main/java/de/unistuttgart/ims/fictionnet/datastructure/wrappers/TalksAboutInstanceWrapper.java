package de.unistuttgart.ims.fictionnet.datastructure.wrappers;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.TalksAboutInstance;

/**
 * @author Lukas Rieger
 * @version 05-11-2015
 * 
 * Wrapper class. 
 * This avoids database related annotations in clases
 * used by the IMSAnalyzer.
 */

public class TalksAboutInstanceWrapper extends AbstractWrapper implements Serializable{
	private boolean stageRend;
	private HashSet<String> actingCastMembers = new HashSet<>();
	private HashSet<String> mentionedCastMembers = new HashSet<>();
	
	/**
	 * Required by ORMLite
	 */
	public TalksAboutInstanceWrapper() {
		
	}
	
	public TalksAboutInstanceWrapper(TalksAboutInstance talksInstance) {
		this.start = talksInstance.getStart();
		this.end = talksInstance.getEnd();
		this.confidence = talksInstance.getConfidence();
		this.changeLog=(LinkedList<String>) (talksInstance.getChangeLog());
		this.stageRend = talksInstance.isStageRend();
		this.actingCastMembers = (HashSet<String>) talksInstance.getActingCastMembers();
		this.mentionedCastMembers = (HashSet<String>) talksInstance.getMentionedCastMembers();
	}
	
	public TalksAboutInstance getTalksAboutInstance() {
		TalksAboutInstance instance = new TalksAboutInstance(this.start, this.end);
		instance.setConfidence(this.confidence);
		instance.setChangeLog(this.changeLog);
		instance.setStageRend(this.stageRend);
		instance.setActingCastMembers(this.actingCastMembers);
		instance.setMentionedCastMembers(this.mentionedCastMembers);
		
		return instance;
	}

}
