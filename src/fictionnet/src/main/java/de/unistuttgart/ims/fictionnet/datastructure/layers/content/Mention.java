package de.unistuttgart.ims.fictionnet.datastructure.layers.content;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rafael Harth
 * @version 10-24-2015
 * 
 *          -- REPLACES OLD MENTION --
 * 
 *          Does NOT correspond to a layer.
 * 
 *          A mention is simply an occurrence wherein a person is mentioned, either by a stage rend or by another
 *          person. Has no additional data beyond the basic annotation, though the confidence value will be used
 *          regularly.
 *          
 *          WARNING: DON'T CHANGE THIS CLASS! WILL CAUSE ERROR IN IMSANALYSIS!!!
 */

public class Mention extends Annotation {

	public Mention(int start, int end, float confidence) {
		super(start, end);

		setConfidence(confidence);
	}
}
