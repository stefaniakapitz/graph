package de.unistuttgart.ims.fictionnet.datastructure.wrappers;

import java.io.Serializable;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Sentence;

/**
 * @author Lukas Rieger
 * @version 05-11-2015
 * 
 * Wrapper class. 
 * This avoids database related annotations in clases
 * used by the IMSAnalyzer.
 */

public class SentenceWrapper extends AbstractWrapper implements Serializable {
	
	/**
	 * Required by ORMLite
	 */
	public SentenceWrapper() {
		
	}
	
	public SentenceWrapper(Sentence sentence) {
		this.start = sentence.getStart();
		this.end = sentence.getEnd();
		this.confidence = sentence.getConfidence();
		this.changeLog=(LinkedList<String>) (sentence.getChangeLog());
	}
	
	public Sentence getSentence() {
		Sentence sentence = new Sentence();
		sentence.setStart(this.start);
		sentence.setEnd(this.end);
		sentence.setConfidence(this.confidence);
		sentence.setChangeLog(this.changeLog);
		
		return sentence;
	}
}
