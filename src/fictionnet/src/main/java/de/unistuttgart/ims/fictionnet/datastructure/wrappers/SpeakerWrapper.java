package de.unistuttgart.ims.fictionnet.datastructure.wrappers;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Speaker;

public class SpeakerWrapper extends AbstractWrapper implements Serializable{
	private HashSet<String> name;
	/**
	 * Required by ORMLite
	 */
	public SpeakerWrapper() {
		
	}
	
	public SpeakerWrapper(Speaker speaker) {
		this.start = speaker.getStart();
		this.end = speaker.getEnd();
		this.confidence = speaker.getConfidence();
		this.name=speaker.getNames();
		this.changeLog=(LinkedList<String>) (speaker.getChangeLog());
	}
	
	public Speaker getSpeaker() {
		Speaker speaker = new Speaker(this.name, this.start, this.end);
		
		speaker.setConfidence(this.confidence);
		speaker.setChangeLog(this.changeLog);
		
		return speaker;
	}
}
