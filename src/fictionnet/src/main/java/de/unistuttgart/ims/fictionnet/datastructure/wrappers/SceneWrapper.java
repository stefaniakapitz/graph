package de.unistuttgart.ims.fictionnet.datastructure.wrappers;

import java.io.Serializable;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;

/**
 * @author Lukas Rieger
 * @version 05-11-2015
 * 
 *          Wrapper class. This avoids database related annotations from clases
 *          used by the IMSAnalyzer.
 */

public class SceneWrapper extends AbstractWrapper implements Serializable {
	private int sceneNumber = 0;
	private ActWrapper actWrapper = new ActWrapper();
	private String content;

	/**
	 * Required by ORMLite
	 */
	public SceneWrapper() {

	}

	public SceneWrapper(Scene scene) {
		this.start = scene.getStart();
		this.end = scene.getEnd();
		this.confidence = scene.getConfidence();
		this.changeLog = (LinkedList<String>) (scene.getChangeLog());
		this.sceneNumber = scene.getSceneNumber();
		this.actWrapper= new ActWrapper(scene.getAct());
		this.content=scene.getContent();
	}

	public Scene getScene() {
		Scene scene = new Scene();
		scene.setStart(this.start);
		scene.setEnd(this.end);
		scene.setConfidence(this.confidence);
		scene.setChangeLog(this.changeLog);
		scene.setSceneNumber(this.sceneNumber);
		scene.setAct(this.actWrapper.getAct());
		scene.setContent(this.content);
		return scene;
	}

	/**
	 * @return the sceneNumber
	 */
	public int getSceneNumber() {
		return sceneNumber;
	}

	/**
	 * @param sceneNumber the sceneNumber to set
	 */
	public void setSceneNumber(int sceneNumber) {
		this.sceneNumber = sceneNumber;
	}

	/**
	 * @return the actWrapper
	 */
	public ActWrapper getActWrapper() {
		return actWrapper;
	}

	/**
	 * @param actWrapper the actWrapper to set
	 */
	public void setActWrapper(ActWrapper actWrapper) {
		this.actWrapper = actWrapper;
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
