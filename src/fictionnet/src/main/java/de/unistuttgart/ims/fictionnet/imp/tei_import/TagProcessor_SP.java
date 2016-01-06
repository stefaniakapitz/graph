package de.unistuttgart.ims.fictionnet.imp.tei_import;

import java.util.ArrayList;
import java.util.List;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.SectionWithType;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.TextType;
import de.unistuttgart.ims.fictionnet.imp.util.Methods;

public class TagProcessor_SP extends TagProcessor {
	private static final String[] SPEARKER_TAG_BEGINNINGS = { "p xml", "l rend", "p rend", "l xml" };
	private final List<SectionWithType> sections = new ArrayList<>();
	private String currentSpeaker = "";

	private List<Act> acts = new ArrayList<>();
	private List<Scene> scenes = new ArrayList<>();
	private int actStart = -1;
	private int sceneStart = -1;
	private int actEnd = -1;
	private int sceneEnd = -1;
	private final StringBuilder content = new StringBuilder();
	private int actStatementOffset;

	public TagProcessor_SP(List<String> sourceText) {
		source = Methods.concat(sourceText);
	}

	/**
	 * @param spTagContent
	 *        the source text within the ﻿＜sp＞ tag layer
	 * @param offset
	 *        the amount of letters already written for the entire text
	 * @return the source text interpreted and formatted
	 * @throws ImportException
	 *         in case of an error
	 */
	public String transcribe() throws ImportException {
		for (index = 0; index < source.length(); index++) {
			setCurrentAndNextLetter();
			progressIndexTo('<');
			final String tag = getCurrentTag();
			String append;
			TextType textType;

			if (tag.startsWith("speaker")) {
				append = removeDot(Tools.concatLinesInTagStructure(recordTagLayer()).trim());
				textType = TextType.SPEAKER_STATEMENT;
				currentSpeaker = append;
			} else if (tagStartsWithSpeakerRole(tag)) {
				append = recordTagContent().trim();
				textType = TextType.SPOKEN_TEXT;
			} else if (tag.startsWith("stage")) {
				append = Tools.concatLinesInTagStructure(recordTagLayer());
				textType = TextType.STAGE_INSTRUCTION;
			} else if (tag.startsWith("aa")) {
				append = recordTagContent();
				textType = TextType.ACT_HEADER;
				actEnd = content.length();

				setIndexesForLatestAct();
				acts.add(new Act(acts.size()+1, append));
				actStart = actEnd;
			} else if (tag.startsWith("ss")) {
				append = recordTagContent();
				textType = TextType.SCENE_HEADER;
				sceneEnd = content.length();
				setIndexesForLatestScene();

				scenes.add(new Scene(scenes.size()+1, append, latestAct()));
				sceneStart = sceneEnd;
			} else {
				progressIndexTo('>');
				continue;
			}

			append += '\n';
			final int start = content.length();
			content.append(append);
			final int end = content.length();

			if (textType == TextType.SPOKEN_TEXT) {
				sections.add(new SectionWithType(start, end, textType, currentSpeaker));
			} else {
				sections.add(new SectionWithType(start, end, textType));
			}

			actStatementOffset = textType == TextType.ACT_HEADER ? append.length() : 0;

			progressIndexTo('>');
		}
		actEnd = content.length();
		setIndexesForLatestAct();
		sceneEnd = content.length();
		setIndexesForLatestScene();

		return content.toString();
	}

	private void setIndexesForLatestAct() {
		if (!acts.isEmpty()) {
			latestAct().setStart(actStart);
			latestAct().setEnd(actEnd);
		}
	}

	private void setIndexesForLatestScene() {
		if (!scenes.isEmpty()) {
			latestScene().setStart(sceneStart);
			latestScene().setEnd(sceneEnd - actStatementOffset);
		}
	}

	private Act latestAct() {
		return acts.get(acts.size() - 1);
	}

	private Scene latestScene() {
		return scenes.get(scenes.size() - 1);
	}

	private boolean tagStartsWithSpeakerRole(String tag) {
		for (String beginning : SPEARKER_TAG_BEGINNINGS) {
			if (tag.startsWith(beginning)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param string
	 *        a speaker statement
	 * @return said statement without a '.' dot at the end
	 */
	private static String removeDot(String string) {
		if (string != null && string.length() > 1 && string.charAt(string.length() - 1) == '.') {
			return string.substring(0, string.length() - 1);
		} else {
			return string;
		}
	}

	public List<SectionWithType> getSections() {
		return sections;
	}

	public List<Act> getActs() {
		return acts;
	}

	public List<Scene> getScenes() {
		return scenes;
	}

	public String getContent() {
		return content.toString();
	}
}