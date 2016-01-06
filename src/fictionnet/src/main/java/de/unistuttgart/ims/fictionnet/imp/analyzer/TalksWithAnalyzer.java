package de.unistuttgart.ims.fictionnet.imp.analyzer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Interaction;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.InteractionType;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Presence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Speaker;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.InteractionWrapper;

/**
 * Analyses if a person speaks with other persons.
 * Needs the calculated speaker and presencelayer
 * @author David
 *
 */
public class TalksWithAnalyzer extends TextAnalyser{

	private final ArrayList<InteractionWrapper> talksWithResults = new ArrayList<InteractionWrapper>();
	
	public TalksWithAnalyzer(Text text) {
		super(text);
	}
	
	
	/**
	 * 
	 */
	@Override
	public void analyze(){
		final List<Speaker> speakers = text.getLayerContainer().getSpeakerLayer().getSpeakers();
		HashSet<String> presentEntities = new HashSet<String>();
		for (final Speaker speaker: speakers) {
			presentEntities = getPresentEntities(speaker.getEnd());
			final Interaction interaction = new Interaction(speaker.getStart(), speaker.getEnd());
			interaction.setType(InteractionType.TALKSTO);
			interaction.setInvolvedCastMembers(presentEntities);
			interaction.setProtagonist(speaker.getNames());
			
			final InteractionWrapper wrapper = new InteractionWrapper(interaction);
			talksWithResults.add(wrapper);
			
		}
	}
	
	private HashSet<String> getPresentEntities(int end) {
		final List<Presence> presences = text.getLayerContainer().getPresenceLayer().getPresences();
		final HashSet<String> presentEntities = new HashSet<String>();
		for (final Presence presence: presences) {
			if (presence.getEnd() > end && presence.getStart()< end) {
				presentEntities.add(presence.getPerson());
			}
			if (presence.getStart()> end) {
				break;
			}
		}
		return presentEntities;
	}
	
	public ArrayList<InteractionWrapper> getResults() {
		return talksWithResults;
	}
}
