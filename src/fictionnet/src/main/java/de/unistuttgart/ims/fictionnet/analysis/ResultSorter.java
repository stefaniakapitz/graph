package de.unistuttgart.ims.fictionnet.analysis;

import java.util.Comparator;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Presence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Sentence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Speaker;

@SuppressWarnings("PMD.AtLeastOneConstructor")
public class ResultSorter {
	class ActComparator implements Comparator<Act> {
		@Override
		public int compare(Act act1, Act act2) {
			return act1.getStart() - act2.getStart();
		}
	}

	class SceneComparator implements Comparator<Scene> {
		@Override
		public int compare(Scene scene1, Scene scene2) {
			return scene1.getStart() - scene2.getStart();
		}
	}

	class SpeakerComparator implements Comparator<Speaker> {
		@Override
		public int compare(Speaker speaker1, Speaker speaker2) {
			return speaker1.getStart() - speaker2.getStart();
		}
	}

	class PresenceComparator implements Comparator<Presence> {
		@Override
		public int compare(Presence presence1, Presence presence2) {
			return presence1.getStart() - presence2.getStart();
		}
	}

	class SentenceComparator implements Comparator<Sentence> {
		@Override
		public int compare(Sentence sentence1, Sentence sentence2) {
			return sentence1.getStart() - sentence2.getStart();
		}
	}
}
