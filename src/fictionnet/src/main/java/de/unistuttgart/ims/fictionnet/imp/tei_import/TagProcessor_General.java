package de.unistuttgart.ims.fictionnet.imp.tei_import;

import java.util.LinkedList;
import java.util.List;

public class TagProcessor_General extends TagProcessor {
	private static final String MISSING_CLOSING_TAG = "Detected '/' without following '>' (tag layer doesn't close)";

	public TagProcessor_General(String text) {
		source = text;
	}

	/**
	 * Attempts to extract all substrings on the lowest tag layer of the xml structure.
	 * 
	 * Example: "＜openingLayer1＞ dd ＜contentTag/＞ dd ＜openingLayer2> content1＜/closingLayer2＞
	 * qwe＜openingLayer2＞content2＜/closingLayer2＞＜/closingLayer1＞" will return (" content1", "content2")
	 * 
	 * @param source
	 *        the string to extract from
	 * @param minDepth
	 *        the max depth of the html structure. including this parameter is optional but leads to increased safety.
	 * 
	 * @return the extracted substrings in a list
	 * @throws ImportException
	 *         if something with the tag structure is wrong
	 */
	public List<String> extractLines(int minDepth) throws ImportException {
		specifiedDepth = minDepth;
		return progress(Task.EXTRACT_LINES_IN_TAG_STRUCTURE);
	}

	/**
	 * Example: "＜openingLayer1＞ dd ＜contentTag/＞ dd ＜openingLayer2>content1＜/closingLayer2＞
	 * qwe＜openingLayer2＞content2＜/closingLayer2＞＜/closingLayer1＞" will return 2.
	 * 
	 * @throws ImportException
	 */

	public List<String> extractLines() throws ImportException {
		progress(Task.DETERMINE_MAX_DEPTH);
		depth = 0;
		index = 0;
		return 	progress(Task.EXTRACT_LINES_IN_TAG_STRUCTURE);

	}

	/**
	 * Goes through the source string to do the specified task.
	 * 
	 * @param task
	 *        the task to accomplish. So corresponding methods.
	 * @return 
	 * @throws ImportException
	 *         if something with the tag structure is wrong
	 */
	private List<String> progress(Task task) throws ImportException {
		for (index = 0; index < source.length(); index++) {
			setCurrentAndNextLetter();

			if (letter == '\"') {
				letter = '-';
				progressIndexTo('\"');
				continue;
			}

			progressIndexTo('<');

			if (nextLetter == '/') {
				depth--;
			} else {
				progressIndexTo('/', '>');

				if (letter == '/') {
					if (nextLetter != '>') {
						throw new ImportException(MISSING_CLOSING_TAG);
					}
				} else if (letter == '>') {
					depth++;

					if (task == Task.DETERMINE_MAX_DEPTH) {
						specifiedDepth = Math.max(depth, specifiedDepth);
					} else if (task == Task.EXTRACT_LINES_IN_TAG_STRUCTURE && depth == specifiedDepth) {
						degressIndexTo('<');
						results.addAll(new TagProcessor_General(recordTagLayer()).extractStringsBetweenTags());
					}
				}
			}
			progressIndexTo('>');
		}
		return results;
	}

	
	public List<String> extractStringsBetweenTags() {
		List<String> strings = new LinkedList<>();

		for (; index < source.length(); index++) {
			setCurrentAndNextLetter();

			int record = index;

			progressIndexTo('<');

			if (record + 1 < index) {
				strings.add(source.substring(record, index));
			}
			progressIndexTo('>');
		}
		return strings;
	}

}

enum Task {
	DETERMINE_MAX_DEPTH, EXTRACT_LINES_IN_TAG_STRUCTURE
}