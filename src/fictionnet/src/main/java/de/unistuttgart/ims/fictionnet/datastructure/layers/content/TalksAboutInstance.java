package de.unistuttgart.ims.fictionnet.datastructure.layers.content;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.unistuttgart.ims.fictionnet.datastructure.layers.TalksAboutLayer;

/**
 * @author Rafael Harth
 * @version 10-10-2015
 * 
 *          Associated with MentionLayer.java
 * 
 *          This class represents a single instance wherein one person mentions
 *          another, or several others, in the source text.
 * 
 *          See Layer.java for additional Information.
 */

public class TalksAboutInstance extends Annotation {
	private boolean stageRend;
	private HashSet<String> actingCastMembers = new HashSet<>();
	private HashSet<String> mentionedCastMembers = new HashSet<>();

	TalksAboutInstance() {
		// all persisted classes must define a no-arg constructor
		// with at least package visibility
	}

	public TalksAboutInstance(int start, int end) {
		super(start, end);
	}

	public Set<String> getMentionedCastMembers() {
		return mentionedCastMembers;
	}

	public void setMentionedCastMembers(HashSet<String> mentionedCastMembers) {
		this.mentionedCastMembers = mentionedCastMembers;
	}

	public boolean isStageRend() {
		return stageRend;
	}

	public void setStageRend(boolean stageRend) {
		this.stageRend = stageRend;
	}

	public Set<String> getActingCastMembers() {
		return actingCastMembers;
	}

	public void setActingCastMembers(HashSet<String> actingCastMembers) {
		this.actingCastMembers = actingCastMembers;
	}
}