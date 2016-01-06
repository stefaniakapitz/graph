package de.unistuttgart.ims.fictionnet.analysis;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Domas Mikalkinas
 *
 */
@SuppressWarnings("PMD.AtLeastOneConstructor")
public class Filter {
	private ActionTypes actionType;
	private List<String> actionObject = new ArrayList<String>();
	private List<String> actionSubject = new ArrayList<String>();
	private List<String> speaker = new ArrayList<String>();
	private Conjunction objectConjunction;
	@SuppressWarnings("PMD.LongVariable")
	private Conjunction subjectConjunction;

	/**
	 * @return the objectConjunction
	 */
	public Conjunction getObjectConjunction() {
		return objectConjunction;
	}

	/**
	 * @param objectConjunction
	 *            the objectConjunction to set
	 */
	public void setObjectConjunction(Conjunction objectConjunction) {
		this.objectConjunction = objectConjunction;
	}

	/**
	 * @return the subjectConjunction
	 */
	public Conjunction getSubjectConjunction() {
		return subjectConjunction;
	}

	/**
	 * @param subjectConjunction
	 *            the subjectConjunction to set
	 */
	@SuppressWarnings("PMD.LongVariable")
	public void setSubjectConjunction(Conjunction subjectConjunction) {
		this.subjectConjunction = subjectConjunction;
	}

	/**
	 * @return the actionType
	 */
	public ActionTypes getActionType() {
		return actionType;
	}

	/**
	 * @param actionType
	 *            the actionType to set
	 */
	public void setActionType(ActionTypes actionType) {
		this.actionType = actionType;
	}

	/**
	 * @return the actionObject
	 */
	public List<String> getActionObject() {
		return actionObject;
	}

	/**
	 * @param actionObject
	 *            the actionObject to set
	 */
	public void setActionObject(List<String> actionObject) {
		this.actionObject = actionObject;
	}

	/**
	 * @return the actionSubject
	 */
	public List<String> getActionSubject() {
		return actionSubject;
	}

	/**
	 * @param actionSubject
	 *            the actionSubject to set
	 */
	public void setActionSubject(List<String> actionSubject) {
		this.actionSubject = actionSubject;
	}

	/**
	 * @return the speaker
	 */
	public List<String> getSpeaker() {
		return speaker;
	}

	/**
	 * @param speaker
	 *            the speaker to set
	 */
	public void setSpeaker(List<String> speaker) {
		this.speaker = speaker;
	}

}
