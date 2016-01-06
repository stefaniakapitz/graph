package de.unistuttgart.ims.fictionnet.imp.analyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Presence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.SectionWithType;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.TextType;

/**
 * @author Lukas Rieger
 * @version 15-11-2015
 *
 *          Contains the algorithm to find the presences using the data of the IMSInteractionObject.
 */
public class PresencesAnalyzer extends TextAnalyser {

	public PresencesAnalyzer(Text text) {
		super(text);
		// TODO Auto-generated constructor stub
	}

	private final List<Presence> presences = new ArrayList<Presence>();

	/**
	 * This method gets all Presences in a Text and returns a list of presence objects.
	 * 
	 * @param text
	 * @param iO
	 * @return list of presences
	 */
	@Override
	public void analyze() {
		boolean sceneStart = false;
		final List<SectionWithType>  sections = text.getLayerContainer().getTypeOfTextLayer().getSections();
		final List<Scene> scenes = text.getLayerContainer().getSceneLayer().getScenes();
		final Iterator<Scene> i = scenes.iterator();
		Scene actualScene = null;
		
		LinkedHashSet<Presence> presentInScene = new LinkedHashSet<Presence>();

		if (i.hasNext()) {
			actualScene = i.next();
			sceneStart = true;
		}

		for (final SectionWithType section : sections) {
			int start = 0;
			int end = 0;

			if (i.hasNext() && section.getStart() > actualScene.getEnd()) {
				actualScene = i.next();
				presences.addAll(presentInScene);
				presentInScene = new LinkedHashSet<Presence>();
				sceneStart = true;
			}

			if (section.getSectionType() != null && section.getSectionType() != TextType.PARAGRAPH
					&& section.getSectionType() != TextType.STAGE_INSTRUCTION
					&& section.getSectionType() != TextType.ACT_HEADER) {
				sceneStart = false;
			}

			if (section.getSectionType() == TextType.STAGE_INSTRUCTION) {
				start = section.getStart();
				end = section.getEnd();
				final String stageInstructionText = text.getSourceText().substring(start, end);
				final HashSet<String> presenceStrings = super.findEntities(stageInstructionText, super.getAllEntities());
				for (final String presenceString : presenceStrings) {
					final Presence presence = new Presence(actualScene.getStart(), actualScene.getEnd());
					presence.setPerson(presenceString);
					presentInScene.add(presence);
				}

			}
		}
	}

	public List<Presence> getResults() {
		return this.presences;
	}

}
