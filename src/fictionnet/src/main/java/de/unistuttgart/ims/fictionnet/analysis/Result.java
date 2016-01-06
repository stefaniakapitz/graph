package de.unistuttgart.ims.fictionnet.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Annotation;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Presence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Sentence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Speaker;

/**
 * 
 * @author Domas Mikalkinas
 *
 */
@SuppressWarnings({ "PMD.TooManyMethods"})
public class Result {
	private Set<Act> act = new HashSet<>();
	private Set<Scene> scene = new HashSet<>();
	private Set<Speaker> speaker = new HashSet<>();
	private ActionTypes action;
	private Set<Presence> presentEntities = new HashSet<>();
	private Set<Sentence> sentences = new HashSet<>();

	private List<SingleResult> results = new ArrayList<>();

	public List<SingleResult> createSingleResults(final Result result) {
		final List<SingleResult> singleResults = new ArrayList<>();
		final List<Act> actList = new ArrayList<>(result.getAct());
		final List<Scene> sceneList = new ArrayList<>(result.getScene());
		final List<Speaker> speakerList = new ArrayList<>(result.getSpeaker());
		final List<Presence> presenceList = new ArrayList<>(result.getPresentEntities());
		final List<Sentence> sentenceList = new ArrayList<>(result.getSentences());
		int index = 0;
		Collections.sort(actList, new ActComparator());
		Collections.sort(sceneList, new SceneComparator());
		Collections.sort(speakerList, new SpeakerComparator());
		Collections.sort(presenceList, new PresenceComparator());
		Collections.sort(sentenceList, new SentenceComparator());
		for (int i = 0; i < actList.size(); i++) {
			final SingleResult singleResult = new SingleResult(actList.get(i), sceneList.get(i), sentenceList.get(i));
			singleResults.add(singleResult);
		}
		for (int i = 0; i < singleResults.size(); i++) {
			index = singleResults.get(i).getAct().getStart();

			for (int j = 0; j < speakerList.size(); j++) {
				if (speakerList.get(j).getStart() == index) {
//					singleResults.get(i).addSpeaker(speakerList.get(j));
				}
			}
			for (int j = 0; j < presenceList.size(); j++) {
				if (presenceList.get(j).getStart() == index) {
					singleResults.get(i).addPresentEntity(presenceList.get(j));
				}
			}
		}
		return singleResults;

	}

	/**
	 * @return the act
	 */
	public Set<Act> getAct() {
		return act;
	}

	/**
	 * @param act
	 *            the act to set
	 */
	public void setAct(final Set<Act> act) {
		this.act = act;
	}

	/**
	 * @return the scene
	 */
	public Set<Scene> getScene() {
		return scene;
	}

	/**
	 * @param scene
	 *            the scene to set
	 */
	public void setScene(final Set<Scene> scene) {
		this.scene = scene;
	}

	/**
	 * @return the speaker
	 */
	public Set<Speaker> getSpeaker() {
		return speaker;
	}

	/**
	 * Creates a Map with Annotations of a certain Speaker in a certain Act
	 * 
	 * @param speakerName
	 * @return A map with Speaker with the Act as Key
	 */
	public TreeMap<Act, ArrayList<Speaker>> getSpeakerAsMap(final String speakerName) {
		final TreeMap<Act, ArrayList<Speaker>> map = new TreeMap<>(new ActComparator());
		final List<Act> actList = new ArrayList<Act>(this.getAct());
		Collections.sort(actList, new ActComparator());
		final List<Speaker> speakerList = new ArrayList<>(this.getSpeaker());
		Collections.sort(speakerList, new SpeakerComparator());

		final Iterator<Speaker> iterator = speakerList.iterator();
		while (iterator.hasNext()) {
			final Speaker speaker = iterator.next();
			//TODO: getNames returns a HashSet
			if (!speakerName.equals(speaker.getNames())) {
				iterator.remove();
			}

		}

		int actIndex = 0;
		int speakerIndex = 0;
		ArrayList<Speaker> speakers;

		while (actIndex < actList.size() && speakerIndex < speakerList.size()) {
			final Act act = actList.get(actIndex);
			final Speaker speaker = speakerList.get(speakerIndex);

			if (act.getEnd() >= speaker.getStart()) {
				if (map.containsKey(act)) {
					map.get(act).add(speaker);
				} else {
					speakers = new ArrayList<Speaker>();
					speakers.add(speaker);
					map.put(act, speakers);
				}
				speakerIndex++;
			} else {
				actIndex++;
			}
		}

		return map;
	}

	/**
	 * @param speaker
	 *            the speaker to set
	 */
	public void setSpeaker(final Set<Speaker> speaker) {
		this.speaker = speaker;
	}

	/**
	 * @return the action
	 */
	public ActionTypes getAction() {
		return action;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(final ActionTypes action) {
		this.action = action;
	}

	/**
	 * @return the presentEntities
	 */
	public Set<Presence> getPresentEntities() {
		return presentEntities;
	}

	/**
	 * @param presentEntities
	 *            the presentEntities to set
	 */
	public void setPresentEntities(final Set<Presence> presentEntities) {
		this.presentEntities = presentEntities;
	}

	/**
	 * @return the sentences
	 */
	public Set<Sentence> getSentences() {
		return sentences;
	}

	/**
	 * @param sentences
	 *            the sentences to set
	 */
	public void setSentences(final Set<Sentence> sentences) {
		this.sentences = sentences;
	}

	/**
	 * adds an act to the result.
	 * 
	 * @param foundAct
	 *            the found act
	 */
	public void addAct(final Act foundAct) {
		act.add(foundAct);
	}

	/**
	 * adds a scene to the result.
	 * 
	 * @param foundScene
	 *            the found scene
	 */
	public void addScene(final Scene foundScene) {
		scene.add(foundScene);
	}

	/**
	 * adds a speaker to the result.
	 * 
	 * @param foundSpeaker
	 *            the found speaker
	 */
	public void addSpeaker(final Speaker foundSpeaker) {
		speaker.add(foundSpeaker);
	}

	/**
	 * adds an entity to the result.
	 * 
	 * @param foundPresentEntities
	 *            the found entities
	 */
	@SuppressWarnings("PMD.LongVariable")
	public void addPresentEntities(final Presence foundPresentEntities) {
		presentEntities.add(foundPresentEntities);
	}

	/**
	 * adds a sentence to the result.
	 * 
	 * @param foundSentences
	 *            the found sentences
	 */
	public void addSentences(final Sentence foundSentences) {
		sentences.add(foundSentences);
	}

	/**
	 * @return the results
	 */
	public List<SingleResult> getResults() {
		return results;
	}

	/**
	 * @param results
	 *            the results to set
	 */
	public void setResults(final List<SingleResult> results) {
		this.results = results;
	}

	class ActComparator implements Comparator<Act> {
		@Override
		public int compare(final Act act1, final Act act2) {
			return act1.getStart() - act2.getStart();
		}
	}

	class SceneComparator implements Comparator<Scene> {
		@Override
		public int compare(final Scene scene1, final Scene scene2) {
			return scene1.getStart() - scene2.getStart();
		}
	}

	class SpeakerComparator implements Comparator<Speaker> {
		@Override
		public int compare(final Speaker speaker1, final Speaker speaker2) {
			return speaker1.getStart() - speaker2.getStart();
		}
	}

	class PresenceComparator implements Comparator<Presence> {
		@Override
		public int compare(final Presence presence1, final Presence presence2) {
			return presence1.getStart() - presence2.getStart();
		}
	}

	class SentenceComparator implements Comparator<Sentence> {
		@Override
		public int compare(final Sentence sentence1, final Sentence sentence2) {
			return sentence1.getStart() - sentence2.getStart();
		}
	}

	class AnnotationComparator implements Comparator<Annotation> {
		@Override
		public int compare(final Annotation annotator1, final Annotation annotator2) {
			return annotator1.getStart() - annotator2.getStart();
		}
	}
}
