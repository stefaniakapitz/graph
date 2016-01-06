package de.unistuttgart.ims.fictionnet.datastructure;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "synonym")

public class Synonym implements Serializable {
	@DatabaseField
	private float confidence;
	@DatabaseField
	private String name;
	@DatabaseField
	private int start;
	@DatabaseField
	private int end;

	public Synonym(String name, float confidence, int start, int end) {
		this.confidence = confidence;
		this.name = name;
		this.start = start;
		this.end = end;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getConfidence() {
		return confidence;
	}

	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}
	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void setEnd(int end) {
		this.end = end;
	}
}
