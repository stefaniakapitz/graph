package de.unistuttgart.ims.fictionnet.datastructure.layers.content;

import javax.xml.bind.annotation.XmlEnum;

/**
 * @author Rafael Harth
 * @version 10-15-2015
 * 
 *          This class determines a number of possible types for interactions (see Interaction.java)
 */
@XmlEnum
public enum InteractionType {
	MENTIONS, TALKSTO;
}
