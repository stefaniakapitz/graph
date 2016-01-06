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
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.TalksAboutInstance;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.ActWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.TalksAboutInstanceWrapper;

/**
 * @author Rafael Harth
 * @version 10-10-2015
 * 
 *          This concrete Layer stores occurrences in which a a group (1+) of
 *          persons mentions another group (1+) in the source text.
 * 
 *          See Layer.java for additional Information.
 */
@DatabaseTable(tableName = "talksAboutLayer")
public class TalksAboutLayer extends Layer {
	@DatabaseField(dataType = DataType.SERIALIZABLE,columnDefinition = "LONGBLOB")
	private ArrayList<TalksAboutInstanceWrapper> occurences;

	public TalksAboutLayer() {
		this.occurences = new ArrayList<TalksAboutInstanceWrapper>();
	}

	public TalksAboutLayer(List<TalksAboutInstance> occurences) {
		ArrayList<TalksAboutInstanceWrapper> taiWrappers = new ArrayList<>();

		for (int i = 0; i < occurences.size(); i++) {
			taiWrappers.add(new TalksAboutInstanceWrapper(occurences.get(i)));
		}
		this.occurences = taiWrappers;
	}

	/*
	 * Basic Getters and Setters
	 */
	@XmlTransient
	public List<TalksAboutInstance> getMentions() {
		List<TalksAboutInstance> newTai = new ArrayList<>();

		for (int i = 0; i < occurences.size(); i++) {
			newTai.add(occurences.get(i).getTalksAboutInstance());
		}
		return newTai;
	}
	
	/**
	 * Just for XML Serialization, use getMentions()
	 * @return
	 */
	@XmlElement (name= "mentions")
	public List<TalksAboutInstanceWrapper> getOccurenceWrappers(){
		return occurences;
	}

	public void setMentions(List<TalksAboutInstance> occurences) {
		ArrayList<TalksAboutInstanceWrapper> taiWrappers = new ArrayList<>();

		for (int i = 0; i < occurences.size(); i++) {
			taiWrappers.add(new TalksAboutInstanceWrapper(occurences.get(i)));
		}
		this.occurences = taiWrappers;
	}

	@Override
	public Annotation getAnnotation(int index) throws InvalidRequestException {
		throw new InvalidRequestException("Cannot Request mention by index");
	}
}
