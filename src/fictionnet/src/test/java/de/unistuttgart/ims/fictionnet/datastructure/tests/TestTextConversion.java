package de.unistuttgart.ims.fictionnet.datastructure.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import de.unistuttgart.ims.fictionnet.datastructure.TextConversion;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.SectionWithType;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.TextType;

public class TestTextConversion {

	@Test
	public void testGenerateHtmlText(){
		String textSection = "huhu";
		ArrayList<SectionWithType> sectionType = new ArrayList<SectionWithType>();
		SectionWithType speakerStatement = new SectionWithType(0,4,TextType.SPEAKER_STATEMENT);
		sectionType.add(speakerStatement);
		
		SectionWithType spokenText = new SectionWithType(0,4,TextType.SPOKEN_TEXT);
		sectionType.add(spokenText);
		
		SectionWithType stageInstruction = new SectionWithType(0,4,TextType.STAGE_INSTRUCTION);
		sectionType.add(stageInstruction);
		
		SectionWithType paragraph = new SectionWithType(0,4,TextType.PARAGRAPH);
		sectionType.add(paragraph);
		
		SectionWithType actHeader = new SectionWithType(0,4,TextType.ACT_HEADER);
		sectionType.add(actHeader);
		
		SectionWithType sceneHeader = new SectionWithType(0,4,TextType.SCENE_HEADER);
		sectionType.add(sceneHeader);
		
		
		boolean highlight = true;
		String result = TextConversion.generateHtmlText(textSection, sectionType, highlight, "test");
		
	// hier kommt noch wAS
	    
	}
	
	
	
	

}
