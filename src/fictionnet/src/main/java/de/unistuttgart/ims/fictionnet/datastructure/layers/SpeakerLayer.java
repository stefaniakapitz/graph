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
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Speaker;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.ActWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.SpeakerWrapper;

@DatabaseTable(tableName = "speakerLayer")
public class SpeakerLayer extends Layer {
	@DatabaseField(dataType = DataType.SERIALIZABLE,columnDefinition = "LONGBLOB")
	private ArrayList<SpeakerWrapper> speakers;

	public SpeakerLayer() {
		this.speakers = new ArrayList<SpeakerWrapper>();
	}

	/**
	 * 
	 * @param speakers
	 */
	public SpeakerLayer(List<Speaker> speakers) {
		ArrayList<SpeakerWrapper> speakerWrappers = new ArrayList<>();

		for (int i = 0; i < speakers.size(); i++) {
			speakerWrappers.add(new SpeakerWrapper(speakers.get(i)));
		}
		this.speakers = speakerWrappers;
	}

	@Override
	public Annotation getAnnotation(int index) throws InvalidRequestException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the speakers
	 */
	@XmlTransient
	public List<Speaker> getSpeakers() {
		List<Speaker> newSpeakers = new ArrayList<>();

		for (int i = 0; i < speakers.size(); i++) {
			newSpeakers.add((speakers.get(i).getSpeaker()));
		}
		return newSpeakers;
	}
	
	/**
	 * Just for XML Serialization, use getSpeakers()
	 * @return
	 */
	@XmlElement (name= "speakers")
	public List<SpeakerWrapper> getSpeakerWrapper(){
		return speakers;
	}

	/**
	 * @param speakers
	 *            the speakers to set
	 */
	public void setSpeakers(List<Speaker> speakers) {
		ArrayList<SpeakerWrapper> speakerWrappers = new ArrayList<>();

		for (int i = 0; i < speakers.size(); i++) {
			speakerWrappers.add(new SpeakerWrapper(speakers.get(i)));
		}
		this.speakers = speakerWrappers;
	}

}
