package de.unistuttgart.ims.fictionnet.datastructure.wrappers;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Mention;

/**
 * @author Lukas Rieger
 * @version 05-11-2015
 * 
 * Wrapper class. 
 * This avoids database related annotations from clases
 * used by the IMSAnalyzer.
 */

public class MentionWrapper extends AbstractWrapper{
	
	/**
	 * Required by ORMLite
	 */
	public MentionWrapper() {
		
	}
	
	public MentionWrapper(Mention mention) {
		this.start = mention.getStart();
		this.end = mention.getEnd();
		this.confidence = mention.getConfidence();
		this.changeLog=(LinkedList<String>) (mention.getChangeLog());
	}
	
	public Mention getSentence() {
		Mention mention = new Mention(this.start, this.end, this.confidence);
		mention.setChangeLog(this.changeLog);
		
		return mention;
	}
}
