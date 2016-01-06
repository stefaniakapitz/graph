package de.unistuttgart.ims.fictionnet.datastructure.layers.content;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * @author Rafael Harth
 * @version 10-09-2015
 * 
 *          This class bundles the commonalities of all concrete layer classes
 *          (see layer.java). Each layer divides the entire text into sections
 *          (annotations) to store any kind of meta information.
 * 
 *          Each Annotation has... <code>
 *          - a start index (inclusive)
 *          - an end index (exclusive)
 *          - a confidence value ϵ [0,1]
 *          - a change log </code>
 * 
 *          See Layer.java for additional information.
 */
@XmlTransient
public abstract class Annotation {
	private int start;
	private int end;
	private float confidence;
	private LinkedList<String> changeLog = new LinkedList<String>();

	public Annotation() {

	}

	public Annotation(int start, int end) {
		this.start = start;
		this.end = end;
	}

	/*
	 * Basic Getters and Setters
	 */
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public float getConfidence() {
		return confidence;
	}

	public void setConfidence(float cofnidence) {
		this.confidence = cofnidence;
	}

	public LinkedList<String> getChangeLog() {
		return changeLog;
	}

	public void setChangeLog(LinkedList<String> changeLog) {
		this.changeLog = changeLog;
	}
}