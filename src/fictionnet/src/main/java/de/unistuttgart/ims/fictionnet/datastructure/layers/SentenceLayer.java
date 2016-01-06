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
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Sentence;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.ActWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.SentenceWrapper;

/**
 * @author Rafael Harth
 * @version 10-28-2015
 * 
 *          This concrete Layer divides the text into sentences.
 * 
 *          See Layer.java for additional Information.
 */
@DatabaseTable(tableName = "sentenceLayer")
public class SentenceLayer extends Layer {
	@DatabaseField(dataType = DataType.SERIALIZABLE,columnDefinition = "LONGBLOB")
	private ArrayList<SentenceWrapper> sentences = new ArrayList<>();

	public SentenceLayer() {
		this.sentences = new ArrayList<SentenceWrapper>();
	}

	public SentenceLayer(List<Sentence> sentences) {
		ArrayList<SentenceWrapper> sentenceWrappers = new ArrayList<>();

		for (int i = 0; i < sentences.size(); i++) {
			sentenceWrappers.add(new SentenceWrapper(sentences.get(i)));
		}
		this.sentences = sentenceWrappers;
	}

	@Override
	public Annotation getAnnotation(int index) {
		return sentences.get(index).getSentence();
	}

	/*
	 * Basic Getters and Setters
	 */

	@XmlTransient
	public List<Sentence> getSentences() {
		List<Sentence> newSentences = new ArrayList<>();

		for (int i = 0; i < sentences.size(); i++) {
			newSentences.add((sentences.get(i).getSentence()));
		}
		return newSentences;
	}
	
	/**
	 * Just for XML Serialization, use getSentences()
	 * @return
	 */
	@XmlElement (name= "sentences")
	public List<SentenceWrapper> getSentenceWrappers(){
		return sentences;
	}

	public void setSentences(List<Sentence> sentences) {
		ArrayList<SentenceWrapper> sentenceWrappers = new ArrayList<>();

		for (int i = 0; i < sentences.size(); i++) {
			sentenceWrappers.add(new SentenceWrapper(sentences.get(i)));
		}
		this.sentences = sentenceWrappers;
	}
}
