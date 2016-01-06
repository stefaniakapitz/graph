package de.unistuttgart.ims.fictionnet.datastructure.layers.content;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Rafael Harth
 * @version 10-10-2015
 * 
 *          Associated with typeOfTextLayer.java and SectionWithType.java
 * 
 *          Determines the type of text for one instance of SectionWithType.
 * 
 *          See Layer.java for additional information.
 */

public enum TextType {
  /*
   * Example for Speaker: 'THE THREE KINGS:' Example for Spoken Text: 'You are banished!'
   */
  STAGE_INSTRUCTION, SPEAKER_STATEMENT, SPOKEN_TEXT, OTHER, PARAGRAPH, ACT_HEADER, SCENE_HEADER
}