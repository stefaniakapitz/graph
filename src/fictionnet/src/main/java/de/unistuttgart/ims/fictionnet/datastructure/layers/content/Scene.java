package de.unistuttgart.ims.fictionnet.datastructure.layers.content;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rafael Harth
 * @version 10-09-2015
 * 
 *          Corresponds to SceneLayer. A scene is an annotation with a scene number and represents a scene in a drama.
 * 
 *          See Layer.java for additional Information.
 * 
 *          WARNING: DON'T CHANGE THIS CLASS! WILL CAUSE ERROR IN IMSANALYSIS!!!
 */

public class Scene extends Annotation {
	private int sceneNumber = 0;
	private String content;
	private Act act;

	public Scene() {
		// all persisted classes must define a no-arg constructor
		// with at least package visibility
	}

	/**
	 * @param start
	 *        @link Annotation
	 * @param end
	 *        @link Annotation
	 * @param sceneNumber
	 *        the scene's number (1, 2, 3, ...)
	 */
	public Scene(int start, int end, int sceneNumber) {
		super(start, end);
		this.sceneNumber = sceneNumber;
	}

	public Scene(int sceneNumber, String content, Act act) {
		this.sceneNumber = sceneNumber;
		this.content = content;
		this.act = act;
	}
	
	@Override
	public String toString() {
		return content;
	}

	/*
	 * Basic Getters and Setters
	 */
	public int getSceneNumber() {
		return sceneNumber;
	}

	public void setSceneNumber(int sceneNumber) {
		this.sceneNumber = sceneNumber;
	}

	public Act getAct() {
		return act;
	}

	public void setAct(Act act) {
		this.act = act;		
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}