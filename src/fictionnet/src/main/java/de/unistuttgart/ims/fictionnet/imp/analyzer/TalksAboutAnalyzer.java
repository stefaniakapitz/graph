package de.unistuttgart.ims.fictionnet.imp.analyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.SectionWithType;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Speaker;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.TalksAboutInstance;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.TextType;

/**
 * Calculates all Talksabout- Annotations
 * @author David Schütz
 *
 */
public class TalksAboutAnalyzer extends TextAnalyser{
	
	public TalksAboutAnalyzer(Text text) {
		super(text);
	}


	//Regular Expression for not in the alphabet
	private final String regex = "[^a-zA-ZÄÖÜäöü]";
	
	private List<TalksAboutInstance> talksAbout;
	private List<Speaker> speakers;


	
	/**
	 * Analyses all Speakersection and all Sections a Speaker talks about another Person
	 */
	@Override
	public void analyze(){
		
		final List<TalksAboutInstance> talksAboutResult = new ArrayList<TalksAboutInstance>();
		final List<Speaker> speakerResult = new ArrayList<Speaker>();
		
		final List<SectionWithType>  sections = text.getLayerContainer().getTypeOfTextLayer().getSections();
		final Iterator<SectionWithType> iterator = sections.iterator();
		
		final HashSet<String> entities = getAllEntities();
		
		// Search a Speaker
		if(!iterator.hasNext()) {
			return;
		}
		SectionWithType spokenAboutSection = iterator.next();

		while (iterator.hasNext()) {
			
			//Speakerlayer
			if (spokenAboutSection.getSectionType() == TextType.SPEAKER_STATEMENT) {
				final int speakerStart = spokenAboutSection.getStart();
				final int speakerEnd = spokenAboutSection.getEnd();
				String speakerName = text.getSourceText()
						.substring(speakerStart, speakerEnd)
						.replace(".", "").replace("\n", "");
				HashSet<String> speakerNames = new HashSet<String>(text.getCastMembersForSynonym(speakerName));
				
				//TODO: Fix when fictionnet.anaylsis can handle multiple speaker
				if (speakerNames.size()<=0) {
					boolean synExist = false;
					for (final String member: text.getCastList()) {
						if (member.equalsIgnoreCase(speakerName.toLowerCase())) {
							speakerName = member;
							synExist = true;
							break;
						}
					}
					
					if (!synExist) {
						text.addNotIdentifiedEntitie(speakerName, speakerStart, speakerEnd);
					}
					speakerNames = new HashSet<String>();
					speakerNames.add(speakerName);
				}
				final Speaker speaker = new Speaker(speakerNames,speakerStart , speakerEnd);
				speakerResult.add(speaker);
			}
			
			
			if (spokenAboutSection.getSectionType() == TextType.SPOKEN_TEXT) {

				//Find the sections where the speaker speaks
				speakerResult.get(speakerResult.size()-1).setEnd(spokenAboutSection.getEnd());
				
				final HashSet<String> talksAbout = new HashSet<String>();
				
				final String spokenText = text.getSourceText()
						.substring(spokenAboutSection.getStart(), spokenAboutSection.getEnd());

				talksAbout.addAll(findEntities(spokenText, entities));

				if (!talksAbout.isEmpty()) {
					final TalksAboutInstance talksAboutInstance = new TalksAboutInstance(spokenAboutSection.getStart(),
							spokenAboutSection.getEnd());
					final HashSet<String> speakerSet = new HashSet<String>();
					String speakerName = spokenAboutSection.getSpeakerString().replace(".", "").toLowerCase();
					final List<String> speakerNames = text.getCastMembersForSynonym(speakerName);
					
					if (speakerNames.size()>0) {
						speakerSet.addAll(speakerNames);
					}
					else {
						for (final String member: text.getCastList()) {
							if (member.equalsIgnoreCase(speakerName.toLowerCase())) {
								speakerName = member;
								break;
							}
						}
					}
					
					speakerSet.add(speakerName);
					talksAboutInstance.setActingCastMembers(speakerSet);
					talksAboutInstance.setMentionedCastMembers(talksAbout);
					talksAboutResult.add(talksAboutInstance);
				}
			}

			spokenAboutSection = iterator.next();

		}
		talksAbout = talksAboutResult;
		speakers = speakerResult;
	}
	
	


	
	/**
	 * @return the talksAbout
	 */
	public List<TalksAboutInstance> getResults() {
		return talksAbout;
	}
	

	/**
	 * @return the speakers
	 */
	public List<Speaker> getSpeakers() {
		return speakers;
	}

	
}
