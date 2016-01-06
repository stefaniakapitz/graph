package de.unistuttgart.ims.fictionnet.imp;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Mention;

/**
 * @author Rafael Harth
 * @version 11-06-2015
 * 
 *          Record of Mention & a confidence value
 */
public class MentionEntity {
	public Mention mention;
	public float confidence;

	/**
	 * @param mention
	 * @param confidence
	 */
	public MentionEntity(Mention mention, float confidence) {
		this.mention = mention;
		this.confidence = confidence;
	}
}