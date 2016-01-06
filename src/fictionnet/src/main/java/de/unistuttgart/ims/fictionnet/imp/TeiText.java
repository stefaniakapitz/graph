package de.unistuttgart.ims.fictionnet.imp;

import java.util.List;
import java.util.Set;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.SectionWithType;

public class TeiText {
	private String author;
	private String publicationDate;
	private String title;
	private String text;
	private String notes;
	private Set<String> castList;
	private List<Act> acts;
	private List<Scene> scenes;
	private List<SectionWithType> sections;

	@Override
	public String toString() {
		StringBuilder content = new StringBuilder();

		content.append("Author: " + author + '\n');
		content.append("Title: " + title + '\n');
		content.append("CastList:\n" + concatPutSeparator(castList, '\n') + "\n\n");
		content.append("\n\n\n--------------------Text--------------------\n");
//		content.append(text);

		for (Scene section : scenes) {
			try {
				content.append(section + "\n--------\n" + text.substring(section.getStart(), section.getEnd())).append("\n\n\n");
			} catch (Exception e) {
				
			}

		}
//		content.append("\n\n" + TextConversion.generateHtmlText(text, sections, false));
		// content.append(TextConversion.generateHtmlText(text, sections, 0));
		return content.toString();
	}

	public static String concatPutSeparator(Set<String> lines, char separator) {
		StringBuilder concat = new StringBuilder();

		for (String line : lines) {
			concat.append(line + separator);
		}

		return concat.toString();
	}

	public static IMSInteractionObject createInteractionObject(TeiText teiText) {
		IMSInteractionObject iO = new IMSInteractionObject();

		iO.setText(teiText.text);
		iO.setCastList(teiText.castList);
		iO.setSections(teiText.sections);
		iO.setActs(teiText.getActs());
		iO.setScenes(teiText.getScenes());

		return iO;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Set<String> getCastList() {
		return castList;
	}

	public void setCastList(Set<String> castList) {
		this.castList = castList;
	}

	public List<Act> getActs() {
		return acts;
	}

	public void setActs(List<Act> acts) {
		this.acts = acts;
	}

	public List<Scene> getScenes() {
		return scenes;
	}

	public void setScenes(List<Scene> scenes) {
		this.scenes = scenes;
	}

	public List<SectionWithType> getSections() {
		return sections;
	}

	public void setSections(List<SectionWithType> sections) {
		this.sections = sections;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
}
