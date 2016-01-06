package de.unistuttgart.ims.fictionnet.datastructure.wrappers;

import java.io.Serializable;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Token;

/**
 * @author Lukas Rieger
 * @version 05-11-2015
 * 
 *          Wrapper class. This avoids database related annotations in clases
 *          used by the IMSAnalyzer.
 */

public class TokenWrapper extends AbstractWrapper implements Serializable {
	private String lemma;
	private String pos;

	/**
	 * Required by ORMLite
	 */
	public TokenWrapper() {

	}

	public TokenWrapper(Token token) {
		this.start = token.getStart();
		this.end = token.getEnd();
		this.confidence = token.getEnd();
		this.lemma = token.getLemma();
		this.pos = token.getPos();
		this.changeLog = (LinkedList<String>) (token.getChangeLog());
	}

	public Token getToken() {
		Token token = new Token();
		token.setStart(this.start);
		token.setEnd(this.end);
		token.setConfidence(this.confidence);
		token.setLemma(this.lemma);
		token.setPos(this.pos);
		token.setChangeLog(this.changeLog);

		return token;
	}
}
