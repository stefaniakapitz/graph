package de.unistuttgart.ims.fictionnet.datastructure.wrappers;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

/**
 * @author Lukas Rieger
 * @version 05-11-2015
 * 
 * The super class of all wrapper classes. Wrapperclasses are used
 * to separate database related things form the rest. This is 
 * necessary for the IMSAnalyzer.
 */

public abstract class AbstractWrapper implements Serializable {
	@DatabaseField(generatedId = true)
	protected int id;
	
	protected int parentId;
	
	protected int start;
	protected int end;

	protected float confidence;
	protected LinkedList<String> changeLog;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	/**
	 * @return the changeLog
	 */
	public LinkedList<String> getChangeLog() {
		return changeLog;
	}

	/**
	 * @param changeLog the changeLog to set
	 */
	public void setChangeLog(LinkedList<String> changeLog) {
		this.changeLog = changeLog;
	}

	/**
	 * @return the start
	 */
	public int getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(int start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public int getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(int end) {
		this.end = end;
	}

	/**
	 * @return the confidence
	 */
	public float getConfidence() {
		return confidence;
	}

	/**
	 * @param confidence the confidence to set
	 */
	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}
}
