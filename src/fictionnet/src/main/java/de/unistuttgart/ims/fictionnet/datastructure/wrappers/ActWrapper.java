package de.unistuttgart.ims.fictionnet.datastructure.wrappers;

import java.io.Serializable;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;

/**
 * @author Lukas Rieger
 * @version 05-11-2015
 * 
 *          Wrapper class. This avoids database related annotations from clases
 *          used by the IMSAnalyzer.
 */

public class ActWrapper extends AbstractWrapper implements Serializable {
	private int actNumber;
	private String content;

	/**
	 * Required by ORMLite
	 */
	public ActWrapper() {

	}

	public ActWrapper(Act act) {
		this.actNumber = act.getActNumber();
		this.start = act.getStart();
		this.end = act.getEnd();
		this.confidence = act.getConfidence();
		this.changeLog = (LinkedList<String>) (act.getChangeLog());
		this.content = act.getContent();
	}

	public Act getAct() {
		Act act = new Act();
		act.setActNumber(this.actNumber);
		act.setStart(this.start);
		act.setEnd(this.end);
		act.setChangeLog(this.changeLog);
		act.setConfidence(this.confidence);
		act.setContent(this.content);

		return act;
	}

	/**
	 * @return the actNumber
	 */
	public int getActNumber() {
		return actNumber;
	}

	/**
	 * @param actNumber the actNumber to set
	 */
	public void setActNumber(int actNumber) {
		this.actNumber = actNumber;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

}
