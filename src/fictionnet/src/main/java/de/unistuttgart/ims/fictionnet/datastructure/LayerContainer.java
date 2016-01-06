package de.unistuttgart.ims.fictionnet.datastructure;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.layers.ActLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.EventLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.InteractionLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.Layer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TalksAboutLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.PresenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SceneLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SentenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SpeakerLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TokenLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TypeOfTextLayer;

/**
 * @author Lukas Rieger, Domas Mikalkinas
 * @version 13-11-15
 * 
 *          The LayerContainer class stores the data about a specific text in
 *          different layers and is contained in every object of the Text class.
 *
 */

@DatabaseTable(tableName = "layerContainers")
public class LayerContainer {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private int textId;
	private SentenceLayer sentenceLayer;
	private ActLayer actLayer;
	private TypeOfTextLayer typeOfTextLayer;
	private PresenceLayer presenceLayer;
	private InteractionLayer interactionLayer;
	private EventLayer eventLayer;
	private TokenLayer tokenLayer;
	private SceneLayer sceneLayer;
	private TalksAboutLayer talksAboutLayer;
	private SpeakerLayer speakerLayer;
	private DBAccessManager db = DBAccessManager.getTheInstance();

	// empty constructor for serialization
	public LayerContainer() {

	}

	/**
	 * Returns an array list of all the instantiated layers. Like this it's
	 * possible to iterate over all the layers.
	 * 
	 * @return layers as arrayList
	 */
	public ArrayList<Layer> getLayers() {
		ArrayList<Layer> layers = new ArrayList<Layer>();
		if (this.sentenceLayer != null) {
			layers.add(sentenceLayer);
		}
		if (this.actLayer != null) {
			layers.add(actLayer);
		}
		if (this.typeOfTextLayer != null) {
			layers.add(typeOfTextLayer);
		}
		if (this.presenceLayer != null) {
			layers.add(presenceLayer);
		}
		if (this.interactionLayer != null) {
			layers.add(interactionLayer);
		}
		if (this.eventLayer != null) {
			layers.add(eventLayer);
		}
		if (this.tokenLayer != null) {
			layers.add(tokenLayer);
		}
		if (this.sceneLayer != null) {
			layers.add(sceneLayer);
		}
		if (this.talksAboutLayer != null) {
			layers.add(talksAboutLayer);
		}
		if (this.speakerLayer != null) {
			layers.add(speakerLayer);
		}
		return layers;
	}

	/**
	 * Sets the layer attribute of the type of the given layer. It deletes the
	 * old layer attribute form the db.
	 * 
	 * @param layer
	 * @throws Exception
	 */
	public void setLayer(Layer layer) throws SQLException {
		if (layer != null) {
			if (layer instanceof ActLayer) {
				if (this.actLayer != null) {
					db.deleteLayer(this.actLayer);
				}
				this.actLayer = (ActLayer) layer;
			} else if (layer instanceof TokenLayer) {
				if (this.tokenLayer != null) {
					db.deleteLayer(this.tokenLayer);
				}
				this.tokenLayer = (TokenLayer) layer;
			} else if (layer instanceof SentenceLayer) {
				if (this.sentenceLayer != null) {
					db.deleteLayer(this.sentenceLayer);
				}
				this.sentenceLayer = (SentenceLayer) layer;
			} else if (layer instanceof TypeOfTextLayer) {
				if (this.typeOfTextLayer != null) {
					db.deleteLayer(this.typeOfTextLayer);
				}
				this.typeOfTextLayer = (TypeOfTextLayer) layer;
			} else if (layer instanceof PresenceLayer) {
				if (this.presenceLayer != null) {
					db.deleteLayer(this.presenceLayer);
				}
				this.presenceLayer = (PresenceLayer) layer;
			} else if (layer instanceof InteractionLayer) {
				if (this.interactionLayer != null) {
					db.deleteLayer(this.interactionLayer);
				}
				this.interactionLayer = (InteractionLayer) layer;
			} else if (layer instanceof EventLayer) {
				if (this.eventLayer != null) {
					db.deleteLayer(this.eventLayer);
				}
				this.eventLayer = (EventLayer) layer;
			} else if (layer instanceof SceneLayer) {
				if (this.sceneLayer != null) {
					db.deleteLayer(this.sceneLayer);
				}
				this.sceneLayer = (SceneLayer) layer;
			} else if (layer instanceof TalksAboutLayer) {
				if (this.talksAboutLayer != null) {
					db.deleteLayer(this.talksAboutLayer);
				}
				this.talksAboutLayer = (TalksAboutLayer) layer;
			} else if (layer instanceof SpeakerLayer) {
				if (this.speakerLayer != null) {
					db.deleteLayer(this.speakerLayer);
				}
				this.speakerLayer = (SpeakerLayer) layer;
			} else {
				throw new InvalidParameterException();
			}
			layer.setContainerId(this.id);
		}
	}

	public int getTextId() {
		return this.textId;
	}

	public void setTextId(int id) {
		this.textId = id;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement
	public TokenLayer getTokenLayer() {
		return tokenLayer;
	}

	@XmlElement
	public SentenceLayer getSentenceLayer() {
		return sentenceLayer;
	}

	@XmlElement (name="Actlayer", namespace = "de.unistuttgart.ims.fictionnet.datastructure.layers")
	public ActLayer getActLayer() {
		return actLayer;
	}

	@XmlElement
	public TypeOfTextLayer getTypeOfTextLayer() {
		return typeOfTextLayer;
	}

	@XmlElement
	public PresenceLayer getPresenceLayer() {
		return presenceLayer;
	}

	@XmlElement
	public InteractionLayer getInteractionLayer() {
		return interactionLayer;
	}

	@XmlElement
	public EventLayer getEventLayer() {
		return eventLayer;
	}

	@XmlElement
	public SceneLayer getSceneLayer() {
		return sceneLayer;
	}

	@XmlElement
	public TalksAboutLayer getTalksAboutLayer() {
		return talksAboutLayer;
	}
	
	@XmlElement
	public SpeakerLayer getSpeakerLayer() {
		return speakerLayer;
	}

	/**
	 * @param sentenceLayer the sentenceLayer to set
	 */
	public void setSentenceLayer(SentenceLayer sentenceLayer) {
		this.sentenceLayer = sentenceLayer;
	}

	/**
	 * @param actLayer the actLayer to set
	 */
	public void setActLayer(ActLayer actLayer) {
		this.actLayer = actLayer;
	}

	/**
	 * @param typeOfTextLayer the typeOfTextLayer to set
	 */
	public void setTypeOfTextLayer(TypeOfTextLayer typeOfTextLayer) {
		this.typeOfTextLayer = typeOfTextLayer;
	}

	/**
	 * @param presenceLayer the presenceLayer to set
	 */
	public void setPresenceLayer(PresenceLayer presenceLayer) {
		this.presenceLayer = presenceLayer;
	}

	/**
	 * @param interactionLayer the interactionLayer to set
	 */
	public void setInteractionLayer(InteractionLayer interactionLayer) {
		this.interactionLayer = interactionLayer;
	}

	/**
	 * @param eventLayer the eventLayer to set
	 */
	public void setEventLayer(EventLayer eventLayer) {
		this.eventLayer = eventLayer;
	}

	/**
	 * @param tokenLayer the tokenLayer to set
	 */
	public void setTokenLayer(TokenLayer tokenLayer) {
		this.tokenLayer = tokenLayer;
	}

	/**
	 * @param sceneLayer the sceneLayer to set
	 */
	public void setSceneLayer(SceneLayer sceneLayer) {
		this.sceneLayer = sceneLayer;
	}

	/**
	 * @param talksAboutLayer the talksAboutLayer to set
	 */
	public void setTalksAboutLayer(TalksAboutLayer talksAboutLayer) {
		this.talksAboutLayer = talksAboutLayer;
	}

	/**
	 * @param speakerLayer the speakerLayer to set
	 */
	public void setSpeakerLayer(SpeakerLayer speakerLayer) {
		this.speakerLayer = speakerLayer;
	}

}
