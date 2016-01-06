package de.unistuttgart.ims.fictionnet.imp.tei_import;

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ims.fictionnet.imp.util.Methods;

/**
 * @author Rafael Harth
 * @version 10-28-2015
 * 
 *          This class bundles the functionality for processing a string with opening and closing html tags. It is used
 *          for .tei files and for subsections of .tei files.
 */
public abstract class TagProcessor {
	private static final boolean RECORDING = true;
	private static final boolean SKIPPING = false;
	private static final boolean FORWARD = true;
	private static final boolean BACKWARD = false;

	protected String source; // entire source text
	protected int index;
	protected char letter;
	protected char nextLetter;
	protected int depth;
	protected int recordingStart; // marks beginning index in case of a recording
	protected int tagLengthAfterSkipOrRecord;
	protected List<String> results = new ArrayList<>();
	protected int specifiedDepth;

	/**
	 * Progresses the index until the current letter is the specified letter. If the current letter is the specified
	 * letter, do nothing.
	 * 
	 * @param target
	 *        the letter to skip to
	 * 
	 * @return number of letters skipped
	 */
	protected int progressIndexTo(char... targets) {
		return moveIndexTo(FORWARD, targets);
	}


	protected int degressIndexTo(char... targets) {
		return moveIndexTo(BACKWARD, targets);
}
	
	protected int moveIndexTo(boolean forward, char... targets) {
		int numberOfLettersSkipped = 0;

		while (!contains(targets, letter)) {
			index = forward ? index + 1 : index - 1;
			letter = source.charAt(index);
			numberOfLettersSkipped++;
		}
		nextLetter = (index == source.length() - 1) ? '-' : source.charAt(index + 1);
		return numberOfLettersSkipped;
	}

	private boolean contains(char[] chars, char charToFind) {
		for (char character : chars) {
			if (character == charToFind) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Sets letter to letter at current index and nextLetter to letter at next index OR to '-' if no next index exists.
	 */
	protected void setCurrentAndNextLetter() {
		letter = (index > source.length() - 1) ? '-' : source.charAt(index);
		nextLetter = (index > source.length() - 2) ? '-' : source.charAt(index + 1);
	}

	/**
	 * Current letter must be '＜' when this method is called.
	 * 
	 * @return current tag (everything from current letter to next '＞')
	 */
	protected String getCurrentTag() {
		return Methods.substring(source, index + 1, '>');
	}

	/**
	 * Skips current tag layer and set index to the end of its closing tag ('＞').
	 * 
	 * @return content of the tag layer
	 * @throws ImportException
	 *         if somewhere a tag layer never closes.
	 */
	protected String recordTagLayer() throws ImportException {
		return processTagLayer(RECORDING);
	}

	/**
	 * Skips current tag layer and set index to the end of its closing tag ('＞').
	 * 
	 * @throws ImportException
	 *         if somewhere a tag layer never closes.
	 */
	protected void skipTagLayer() throws ImportException {
		processTagLayer(SKIPPING);
	}

	/**
	 * Skips or records the current tag layer. Sets the index to the end of the closing tag statement ('﻿＞')
	 * 
	 * For this purpose, we need to look for opening and closing tags, and a depth must be stored which is at 1 at
	 * start. The depth increases at opening tags and decreases at closing tags. Once the depth hits zero, the tag layer
	 * is closed. ﻿
	 * 
	 * <pre>
	 *    Source Text:
	 *    1 ﻿＜profileDesc﻿＞
	 *    2    ﻿＜creation﻿＞
	 *    3      ﻿＜date when="1903"/﻿＞
	 *    4    ﻿＜/creation﻿＞
	 *    5    ﻿＜textClass﻿＞
	 *    6      ﻿＜keywords scheme="http://textgrid.info/namespaces/metadata/core/2010#genre"﻿＞
	 *    7      ﻿＜/keywords﻿＞
	 *    8    ﻿＜/textClass﻿＞
	 *    9  ﻿＜/profileDesc﻿＞
	 *     
	 *     In the beginning, state = running
	 *     ﻿＜profileDesc﻿＞ is not recognized as a relevant tag, so it is to be skipped: state is set to skipping,
	 *     depth is set to 1, and index jumps at the point after the profileDesc tag
	 *     
	 *     At '﻿＜' of row 2, depth is set to 2
	 *     At '﻿＜' of row 3, depth is set to 3  
	 *     At '/﻿＞' of row 3, depth is set to 2 
	 *     At '﻿＜/' of row 4, depth is set to 1
	 *     At '﻿＜' of row 5, depth is set to 2
	 *     At '﻿＜' of row 6, depth is set to 3
	 *     At '﻿＜/' of row 7, depth is set to 2
	 *     At '﻿＜/' of row 8, depth is set to 1
	 *     At '﻿＜/' of row 9, depth is set to 0 -> finished
	 *     
	 *     Now progress index to '＞' and quit.
	 * </pre>
	 * 
	 * 
	 * @param recording
	 *        determines whether the tag layer is recored or just skipped.
	 * @return the tag layer content OR null if recording is false.
	 * @throws ImportException
	 *         if somewhere a tag layer never closes
	 */
	private String processTagLayer(boolean recording) throws ImportException {
		final int oldIndex = index;

		recordingStart = index;
		depth = 0;

		for (; index < source.length(); index++) {
			setCurrentAndNextLetter();

			if (letter == '<') {
				if (nextLetter == '/') { // ＜/...＞
					if (decreaseDepthAndStoreTagLength()) {
						return recording ? source.substring(recordingStart, index + 1) : null;
					}
				} else { // ＜...＞
					depth++;
				}
			} else if (letter == '/' && nextLetter == '>') { // ＜.../＞
				if (decreaseDepthAndStoreTagLength()) {
					return recording ? source.substring(recordingStart, index + 1) : null;
				}
			}
		}
		throw new ImportException("Failed processing tag layer\n\n" + source.substring(oldIndex));
	}

	/**
	 * Usable only for ＜...＞ content ﻿＜/...＞ structures when index is somewhere in or at the end of the opening tag.
	 * Sets index to the end of the tag layer.
	 * 
	 * @return the content between next closing and next opening tag
	 */
	protected String recordTagContent() {
		final String content = Methods.substring(source.substring(index), '>', '<');
		progressIndexTo('>');
		progressIndexTo('<');
		progressIndexTo('>');
		return content;

	}

	/**
	 * Decreases current depth (see main class comment) and skips to the end of the current tag. If depth is now 0, also
	 * sets state to running. If depth is now 0 and state was on recording, also stores data and quits node
	 * 
	 * @return true IF depth is now 0
	 */
	private boolean decreaseDepthAndStoreTagLength() {
		depth--;
		tagLengthAfterSkipOrRecord = progressIndexTo('>');
		return depth == 0;
	}
}
