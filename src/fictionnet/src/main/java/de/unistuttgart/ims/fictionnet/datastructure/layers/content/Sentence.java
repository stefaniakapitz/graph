package de.unistuttgart.ims.fictionnet.datastructure.layers.content;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rafael Harth
 * @version 10-10-2015
 * 
 *          Associated with SentenceLayer.java
 * 
 *          This class is an Annotation without additional data that represents
 *          a single sentence in the source text.
 * 
 *          See Layer.java for additional Information.
 *          
  *          WARNING: DON'T CHANGE THIS CLASS! WILL CAUSE ERROR IN IMSANALYSIS!!!
 */

public class Sentence extends Annotation {
	public Sentence() {
		// all persisted classes must define a no-arg constructor
		// with at least package visibility
	}

	public Sentence(int start, int end) {
		super(start, end);
	}
}