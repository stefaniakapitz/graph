package de.unistuttgart.ims.fictionnet.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Presence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Sentence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Speaker;

/**
 * 
 * @author Domas Mikalkinas
 *
 */
public class SingleResult {
	private Act act;
	private Scene scene;
	private Set<String> speaker = new HashSet<>();
	private ActionTypes action;
	private Set<Presence> presentEntities = new HashSet<>();
	private Sentence sentences;
	private Set<String> conversationObjects= new HashSet<>();

	/**
	 * @return the act
	 */
	public Act getAct() {
		return act;
	}

	SingleResult() {

	}

	SingleResult(Act act, Scene scene, Sentence sentences) {
		this.act = act;
		this.scene = scene;
		this.sentences = sentences;
	}

	/**
	 * @param act
	 *            the act to set
	 */
	public void setAct(Act act) {
		this.act = act;
	}

	/**
	 * @return the scene
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * @param scene
	 *            the scene to set
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
	}

	/**
	 * @return the speaker
	 */
	public Set<String> getSpeaker() {
		return speaker;
	}

	/**
	 * @param speaker
	 *            the speaker to set
	 */
	public void setSpeaker(Set<String> speaker) {
		this.speaker = speaker;
	}

	public void addSpeaker(String speaker) {
		this.speaker.add(speaker);
	}

	/**
	 * @return the action
	 */
	public ActionTypes getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(ActionTypes action) {
		this.action = action;
	}

	/**
	 * @return the presentEntities
	 */
	public Set<Presence> getPresentEntities() {
		return presentEntities;
	}

	/**
	 * @param presentEntities
	 *            the presentEntities to set
	 */
	public void setPresentEntities(Set<Presence> presentEntities) {
		this.presentEntities = presentEntities;
	}

	/**
	 * 
	 * @param presentEntity
	 *            the entity to add
	 */
	public void addPresentEntity(Presence presentEntity) {
		this.presentEntities.add(presentEntity);
	}

	/**
	 * @return the sentences
	 */
	public Sentence getSentences() {
		return sentences;
	}

	/**
	 * @param sentences
	 *            the sentences to set
	 */
	public void setSentences(Sentence sentences) {
		this.sentences = sentences;
	}

	/**
	 * @return the conversationObjects
	 */
	public Set<String> getConversationObjects() {
		return conversationObjects;
	}

	/**
	 * @param conversationObjects the conversationObjects to set
	 */
	public void setConversationObjects(Set<String> conversationObjects) {
		this.conversationObjects = conversationObjects;
	}
}
