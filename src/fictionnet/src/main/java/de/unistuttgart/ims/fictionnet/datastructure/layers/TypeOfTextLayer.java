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
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.SectionWithType;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.ActWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.SectionWrapper;

/**
 * @author Rafael Harth
 * @version 10-09-2015
 *
 *          Associated with SectionWithType.java and TextType.java
 *
 *          This concrete Layer determines a type for each section of the text,
 *          and - if necessary - a corresponding set of cast members.
 * 
 *          See Layer.java for additional Information.
 */
@DatabaseTable(tableName = "typeOfTextLayer")
public class TypeOfTextLayer extends Layer {
	@DatabaseField(dataType = DataType.SERIALIZABLE,columnDefinition = "LONGBLOB")
	private ArrayList<SectionWrapper> sections = new ArrayList<>();

	public TypeOfTextLayer(List<SectionWithType> sections) {
		ArrayList<SectionWrapper> sectionWrappers = new ArrayList<>();

		for (int i = 0; i < sections.size(); i++) {
			sectionWrappers.add(new SectionWrapper(sections.get(i)));
		}
		this.sections = sectionWrappers;
	}

	public TypeOfTextLayer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Annotation getAnnotation(int index) {
		return sections.get(index).getSectionWithType();
	}

	/*
	 * Basic Getters and Setters
	 */
	@XmlTransient
	public List<SectionWithType> getSections() {
		List<SectionWithType> newSections = new ArrayList<>();

		for (int i = 0; i < sections.size(); i++) {
			newSections.add(sections.get(i).getSectionWithType());
		}
		return newSections;
	}
	
	/**
	 * Just for XML Serialization, use getSections()
	 * @return
	 */
	@XmlElement (name= "sections")
	public List<SectionWrapper> getSectionWrappers(){
		return sections;
	}

	public void setSections(List<SectionWithType> sections) {
		ArrayList<SectionWrapper> sectionWrappers = new ArrayList<>();

		for (int i = 0; i < sections.size(); i++) {
			sectionWrappers.add(new SectionWrapper(sections.get(i)));
		}
		this.sections = sectionWrappers;
	}
}