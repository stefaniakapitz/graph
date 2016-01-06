package de.unistuttgart.ims.fictionnet.imp;

import java.util.ArrayList;
import java.util.List;import java.util.Set;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Event;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Mention;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.SectionWithType;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Sentence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Token;

/**
 * @author Rafael Harth
 * @version 10-09-2015
 * 
 *          This class is used to store the data for our interaction with the external algorithms from IMS. It will be
 *          sent to them a part of the variables filled, and the algorithms will fill the rest.
 *          
 *           WARNING: DON'T CHANGE THIS CLASS! WILL CAUSE ERROR IN IMSANALYSIS!!!
 */
public class IMSInteractionObject {
	/* Internal responsibility */
	private String text;
	private Set<String> castList;
	private List<SectionWithType> sections;
	private List<Act> acts;
	private List<Scene> scenes;

	/* External responsibility */
	private List<Sentence> sentences = new ArrayList<>();
	private List<Token> tokens = new ArrayList<>();
	private List<Event> events = new ArrayList<>();
	private List<Mention> mentions = new ArrayList<>();
	private List<MentionCluster> mentionClusters = new ArrayList<>();

	/*
	 * Basic Getters and Setters
	 */
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

	public List<Sentence> getSentences() {
		return sentences;
	}

	public void setSentences(List<Sentence> sentences) {
		this.sentences = sentences;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public void setTokens(List<Token> tokens) {
		this.tokens = tokens;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

	public List<SectionWithType> getSections() {
		return sections;
	}

	public void setSections(List<SectionWithType> sections) {
		this.sections = sections;
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

	public List<Mention> getMentions() {
		return mentions;
	}
	
	public void setMentions(List<Mention> mentions) {
		this.mentions = mentions;
	}
	
	public List<MentionCluster> getMentionClusters() {
		return mentionClusters;
	}

	public void setMentionClusters(List<MentionCluster> mentionClusters) {
		this.mentionClusters = mentionClusters;
	}
}
