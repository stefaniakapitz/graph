package de.unistuttgart.ims.fictionnet.imp.tei_import;

import java.util.List;
import de.unistuttgart.ims.fictionnet.imp.util.Methods;

/**
 * @author Rafael Harth
 * @version 10-25-2015
 * 
 *          This class is a collection of static project-specific Tools
 */
public final class Tools {
	/**
	 * No reason to instantiate this class.
	 */
	private Tools() {
	}


	/**
	 * Attempts to extract all substrings on the lowest tag layer of the xml structure, then trims & concats the
	 * substrings.
	 * 
	 * Example: "＜openingLayer1＞ dd ＜contentTag/＞ dd ＜openingLayer2> content1＜/closingLayer2＞
	 * qwe＜openingLayer2＞content2＜/closingLayer2＞＜/closingLayer1＞" will return ("content1content2")
	 * 
	 * @param string
	 *        the string to extract from
	 * @return all extracted substrings trimed as a single string
	 * @throws ImportException
	 *         if something with the tag structure is wrong
	 */
	public static String concatLinesInTagStructure(final String string) throws ImportException {
		final List<String> lines = new TagProcessor_General(string).extractLines();
		final StringBuilder concat = new StringBuilder();

		for (final String line : lines) {
			concat.append(line.trim());
		}
		return concat.toString();
	}



	/**
	 * Attempts to solve a number of issues with the native extractino of hte cast list
	 * 
	 * - There is often a dot or period at the end<br>
	 * - The final entry is often a description of the szene such as "Die Szene ist in Wien"
	 * 
	 * @param castList
	 *        the original cast list
	 * @return a corrected cast list
	 */
	public static List<String> purifyCastList(List<String> castList) {
		for (int i = 0; i < castList.size(); i++) {
			castList.set(i, Methods.trimSymbols(castList.get(i)));
		}

		String finalElement = castList.get(castList.size() - 1);

		if (finalElement.contains("ist") || finalElement.contains("Szene") || finalElement.contains("Handlung")) {
			castList.remove(castList.size() - 1);
		}
		return castList;
	}
}