package de.unistuttgart.ims.fictionnet.datastructure.wrappers;

import java.io.Serializable;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Presence;

/**
 * @author Lukas Rieger
 * @version 05-11-2015
 * 
 *          Wrapper class. This avoids database related annotations from clases
 *          used by the IMSAnalyzer.
 */

public class PresenceWrapper extends AbstractWrapper implements Serializable {
	private String person;

	/**
	 * Required by ORMLite
	 */
	public PresenceWrapper() {

	}

	public PresenceWrapper(Presence presence) {
		this.start = presence.getStart();
		this.end = presence.getEnd();
		this.confidence = presence.getConfidence();
		this.person = presence.getPerson();
		this.changeLog = (LinkedList<String>) (presence.getChangeLog());
	}

	public Presence getPresence() {
		Presence presence = new Presence(this.start, this.end);
		presence.setChangeLog(this.changeLog);
		presence.setConfidence(this.confidence);
		presence.setPerson(this.person);

		return presence;
	}

	/**
	 * @return the person
	 */
	public String getPerson() {
		return person;
	}

	/**
	 * @param person the person to set
	 */
	public void setPerson(String person) {
		this.person = person;
	}
}
