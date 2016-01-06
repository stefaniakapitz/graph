package de.unistuttgart.ims.fictionnet.analysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.ActLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.PresenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SceneLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SentenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Interaction;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Presence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Sentence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.TalksAboutInstance;

/**
 * 
 * @author Domas Mikalkinas
 *
 */
public class AlgorithmTalksTo extends Algorithm {

	/**
	 * 
	 */
	public AlgorithmTalksTo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * ToDo: need final datastructures. For the alpha it's just a mock method,
	 * which always returns the same result
	 * 
	 * @param text
	 *            the text object
	 * @param filter
	 *            the filter to use
	 * @return Result the result object
	 */
	public Result run(Text text, Filter filter) {

		Result result = new Result();
		List<SingleResult> singleResults = new ArrayList<>();

		List<Interaction> instanceList = text.getLayerContainer().getInteractionLayer().getInteractions();

		List<String> actionSubjects = new ArrayList<>();
		for (int i = 0; i < filter.getActionSubject().size(); i++) {
			actionSubjects.add(filter.getActionSubject().get(i));
			actionSubjects.addAll(text.getStringSynonymsFor(filter.getActionSubject().get(i)));
		}

		List<String> actionObjects = new ArrayList<>();
		for (int i = 0; i < filter.getActionObject().size(); i++) {
			actionObjects.add(filter.getActionObject().get(i));
			actionObjects.addAll(text.getStringSynonymsFor(filter.getActionObject().get(i)));

		}

		int startIndex = 0;

		SingleResult single;

		for (int i = 0; i < instanceList.size(); i++) {

			for (int j = 0; j < actionSubjects.size(); j++) {

				Iterator<String> it = instanceList.get(i).getProtagonist().iterator();
				while (it.hasNext()) {

					String temp = it.next().toLowerCase();
					if (actionSubjects.get(j).toLowerCase().contains(temp)) {

						for (int k = 0; k < actionObjects.size(); k++) {

							Iterator<String> it2 = instanceList.get(i).getInvolvedCastMembers().iterator();
							while (it2.hasNext()) {

								String temp2 = it2.next();
								if (actionObjects.get(k).toLowerCase().contains(temp2.toLowerCase())) {
									startIndex = instanceList.get(i).getStart();
									single = new SingleResult();
									single.setAct(searchAct(startIndex, text.getLayerContainer().getActLayer()));
									single.setAction(filter.getActionType());
									single.setPresentEntities(searchPresentEntities(startIndex,
											text.getLayerContainer().getPresenceLayer()));
									single.setScene(searchScene(startIndex, text.getLayerContainer().getSceneLayer()));
									single.setSentences(
											searchSentences(startIndex, text.getLayerContainer().getSentenceLayer()));

									single.setConversationObjects(instanceList.get(i).getInvolvedCastMembers());
									Set<String> speaker = new HashSet<>();
									for (String value : instanceList.get(i).getProtagonist()) {

										if (text.getCastMembersForSynonym(value).size() == 1) {
											speaker.addAll(text.getCastMembersForSynonym(value));

										} else {
											speaker.add(actionSubjects.get(j));
										}
									}

									single.setSpeaker(speaker);

									if (singleResults.size() == 0 || (singleResults.get(singleResults.size() - 1)
											.getSentences().getStart() != single.getSentences().getStart())) {
										singleResults.add(single);
									}
									if (!contains(singleResults, single)) {

									}
								}
							}

						}
					}
				}
			}
		}
		result.setResults(singleResults);
		return result;
	}

	/**
	 * Check, if this SingleResult already has been added to result list.
	 * 
	 * @param results
	 *            - List<SingleResult>
	 * @param result
	 *            - {@link SingleResult}
	 * @return True, if SingleResult for the same sentences already was added.
	 */
	private boolean contains(List<SingleResult> results, SingleResult result) {
		for (SingleResult singleResult : results) {
			if (result.getSentences().equals(singleResult.getSentences())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Helpermethod to search for acts in the actLayer.
	 * 
	 * @param index
	 *            index of the talk
	 * @param actLayer
	 *            the layer where to search
	 * @return found act or act with act number -1 if no act was found
	 */
	private Act searchAct(int index, ActLayer actLayer) {
		final List<Act> searchList = actLayer.getActs();
		Act act = new Act(0, 0, -1);
		for (int i = 0; i < searchList.size(); i++) {
			if (index >= searchList.get(i).getStart()) {
				act = searchList.get(i);

			}
		}
		return act;

	}

	/**
	 * Helpermethod to search for scenes in the sceneLayer.
	 * 
	 * @param index
	 *            index of the talk
	 * @param sceneLayer
	 *            the layer where to search
	 * @return found scene or scene with scene number -1 if no scene was found
	 */
	private Scene searchScene(int index, SceneLayer sceneLayer) {
		final List<Scene> searchList = sceneLayer.getScenes();
		Scene scene = new Scene();
		for (int i = 0; i < searchList.size(); i++) {

			if (index >= searchList.get(i).getStart()) {
				scene = searchList.get(i);

			}
		}
		return scene;

	}

	/**
	 * Helpermethod to search for present entities in the presenceLayer.
	 * 
	 * @param index
	 *            index of the talk
	 * @param presenceLayer
	 *            the layer where to search
	 * @return found entity the entities which were found
	 */
	private Set<Presence> searchPresentEntities(int index, PresenceLayer presenceLayer) {
		final List<Presence> searchList = presenceLayer.getPresences();
		final Set<Presence> foundEntities = new HashSet<>();

		for (int i = 0; i < searchList.size(); i++) {
			if (index >= searchList.get(i).getStart()) {
				foundEntities.add(searchList.get(i));

			}
		}

		return foundEntities;

	}

	// /**
	// * Helpermethod to search for speaker in the speakerLayer.
	// *
	// * @param index
	// * index of the talk
	// * @param speakerLayer
	// * the layer where to search
	// * @return found speakers the entities which were found
	// */
	// private Set<Speaker> searchSpeaker(int index, SpeakerLayer speakerLayer)
	// {
	// final List<Speaker> searchList = speakerLayer.getSpeakers();
	// final Set<Speaker> foundSpeaker = new HashSet<>();
	//
	// for (int i = 0; i < searchList.size(); i++) {
	// if (index >= searchList.get(i).getStart() && index <=
	// searchList.get(i).getEnd()) {
	// foundSpeaker.add(searchList.get(i));
	//
	// }
	// }
	//
	// return foundSpeaker;
	//
	// }

	/**
	 * Helpermethod to search for sentences in the sentenceLayer.
	 * 
	 * @param index
	 *            index of the talk
	 * @param sentenceLayer
	 *            the layer where to search
	 * @return found sentence
	 */
	private Sentence searchSentences(int index, SentenceLayer sentenceLayer) {
		final List<Sentence> searchList = sentenceLayer.getSentences();
		Sentence sentence = new Sentence();
		for (int i = 0; i < searchList.size(); i++) {
			if (index >= searchList.get(i).getStart()) {
				sentence = searchList.get(i);
			}
		}
		return sentence;

	}

}
