package de.unistuttgart.ims.fictionnet.datastructure.layers;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Annotation;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.ActWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.SceneWrapper;

/**
 * @author Rafael Harth
 * @version 10-18-2015
 * 
 *          This concrete Layer divides the text into scenes.
 * 
 *          See Layer.java for additional Information.
 */
@DatabaseTable(tableName = "sceneLayer")
public class SceneLayer extends Layer {
	@DatabaseField(dataType = DataType.SERIALIZABLE,columnDefinition = "LONGBLOB")
	private ArrayList<SceneWrapper> scenes;

	public SceneLayer() {
		this.scenes = new ArrayList<SceneWrapper>();
	}

	public SceneLayer(List<Scene> scenes) {
		ArrayList<SceneWrapper> sceneWrappers = new ArrayList<>();

		for (int i = 0; i < scenes.size(); i++) {
			sceneWrappers.add(new SceneWrapper(scenes.get(i)));
		}
		this.scenes = sceneWrappers;
	}

	@Override
	public Annotation getAnnotation(int index) {
		return scenes.get(index).getScene();
	}

	/*
	 * Basic Getters and Setters
	 */

	public void addScene(Scene scene) {
		scenes.add(new SceneWrapper(scene));
	}

	public Scene getScene(int index) {
		return scenes.get(index).getScene();
	}

	@XmlTransient
	public List<Scene> getScenes() {
		List<Scene> newScenes = new ArrayList<>();

		for (int i = 0; i < scenes.size(); i++) {
			newScenes.add(scenes.get(i).getScene());
		}
		return newScenes;
	}

	/**
	 * Just for XML Serialization, use getActs()
	 * @return
	 */
	@XmlElement (name= "scenes")
	public List<SceneWrapper> getSceneWrappers(){
		return scenes;
	}
	
	/**
	 * @param scenes
	 *            the scenes to set
	 */
	public void setScenes(List<Scene> scenes) {
		ArrayList<SceneWrapper> sceneWrappers = new ArrayList<>();

		for (int i = 0; i < scenes.size(); i++) {
			sceneWrappers.add(new SceneWrapper(scenes.get(i)));
		}
		this.scenes = sceneWrappers;
	}

}