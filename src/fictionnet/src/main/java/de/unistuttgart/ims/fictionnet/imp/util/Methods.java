package de.unistuttgart.ims.fictionnet.imp.util;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Rafael Harth
 * @version 10-31-2015
 *
 *          This class is a collection of non-project specific tools. Every method should be called in a static way.
 */
public final class Methods {
	/**
	 * No reason to instantiate this class.
	 */
	private Methods() {
	}

	/**
	 * @param string
	 *        any string
	 * @param start
	 *        a string that should be a substring of the original string
	 * @param ending
	 *        a string that should be a substring of the original string
	 * @return the substring that is between start and end in the original string OR null if there is none
	 * 
	 *         <pre>
	 * ("TESTING", "TE", "ING") -> "ST" | ("TESTING", "TEST", "ING") -> "" | ("TESTING", "T2", "ING") -> null
	 * </pre>
	 */
	public static String substring(String string, final String start, final String ending) {
		string = substring(string, start);

		if (string == null) {
			return null;
		}

		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) != ending.charAt(0)) {
				continue;
			} else if (string.substring(i).startsWith(ending)) {
				return string.substring(0, i);
			}
		}

		return null;
	}

	/**
	 * @param string
	 *        any string
	 * @param start
	 *        a string that should be a substring of the original string
	 * @return the substring that comes right after the first instance of start in the original string OR null if start
	 *         is not part of the original string or if the original string is null
	 * 
	 *         <pre>
	 * ("VAKA", "V") -> "AKA" | ("VAKA", "VAKA") -> "" | ("TAPESTRY", "RUBBER") -> null
	 * </pre>
	 */
	public static String substring(final String string, final String start) {
		if (start.isEmpty()) {
			return string;
		} else if (string == null) {
			return null;
		}

		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == start.charAt(0) && string.substring(i, i + start.length()).equals(start)) {
				return string.substring(i + start.length());
			}
		}
		return null;
	}

	/**
	 * @param strings
	 *        an array of strings
	 * @return all strings aligned as a single string OR null if strings == null
	 */
	public static String concat(final String... strings) {
		if (strings == null) {
			return null;
		}
		final StringBuilder concat = new StringBuilder();

		for (final String line : strings) {
			concat.append(line);
		}
		return concat.toString();
	}

	/**
	 * @param strings
	 *        a list of strings
	 * @return all strings aligned as a single string OR null if strings == null
	 */
	public static String concat(final List<String> strings) {
		if (strings == null) {
			return null;
		}

		final StringBuilder concat = new StringBuilder();

		for (final String line : strings) {
			concat.append(line);
		}
		return concat.toString();
	}

	/**
	 * @param strings
	 *        an array of strings
	 * @param separator
	 *        any char
	 * @return all strings aligned as a single string with the separator put between any two original strings OR null if
	 *         strings == null
	 */
	public static String concatPutSeparator(final String[] strings, final char separator) {
		if (strings == null) {
			return null;
		}

		for (int i = 0; i < strings.length - 1; i++) {
			strings[i] = strings[i] + separator;
		}
		return concat(strings);
	}

	/**
	 * @param strings
	 *        a list of strings
	 * @param separator
	 *        any char
	 * @return all strings aligned as a single string with the separator put between any two original strings OR null if
	 *         strings == null
	 */
	public static String concatPutSeparator(final List<String> strings, final char separator) {
		if (strings == null) {
			return null;
		}

		for (int i = 0; i < strings.size() - 1; i++) {
			strings.set(i, strings.get(i) + separator);
		}
		return concat(strings);
	}

	/**
	 * @param string
	 *        any string
	 * @return the string with all symbols removed from the start and the end. symbol is defined as any char that's not
	 *         one of {'a', ..., 'z', 'A', ..., 'Z', '1', ... '9', '0'}
	 * 
	 *         <pre>
	 * " call" -> "call" | "%$&test$" -> "test" | "t4p3stry$$$$1" -> "t4p3stry$$$$1"
	 * </pre>
	 */
	public static String trimSymbols(final String string) {
		if (string == null) {
			return null;
		}

		int left = 0;

		for (int i = 0; i < string.length(); i++) {
			final char letter = string.charAt(i);
			if (Methods.isLetterOrNumber(letter)) {
				left = i;
				break;
			}
		}

		for (int i = string.length() - 1; i >= left; i--) {
			final char letter = string.charAt(i);

			if (Methods.isLetterOrNumber(letter)) {
				return string.substring(left, i + 1);
			}
		}
		return "";
	}

	/**
	 * @param character
	 *        any char
	 * @return true IF the char is any of {'a', ..., 'z', 'A', ..., 'Z', '1', ... '9', '0'}
	 */
	public static boolean isLetterOrNumber(final char character) {
		final int number = character;

		return number >= 48 && number <= 57 || number >= 65 && number <= 90 || number >= 97 && number <= 122;
	}

	/**
	 * @param string
	 *        any string
	 * @param from
	 *        any int, should be >= 0 and < string.length()
	 * @param to
	 *        any char
	 * @return string.substring(from, X) where X is the index of the first instance of to in string after the index from
	 *         OR string.length() if to doesn't appear in the string. Returns null if the parameters are invalid (e.g.
	 *         strings == null)
	 */
	public static String substring(final String string, final int from, final char to) {
		if (string == null || from < 0 || from >= string.length()) {
			return null;
		}

		for (int i = from; i <= string.length(); i++) {
			if (string.charAt(i) == to) {
				return string.substring(from, i);
			}
		}
		return string.substring(from);
	}

	/**
	 * @param string
	 *        any string
	 * @param from
	 *        any char
	 * @param to
	 *        any char
	 * @return the substring that is between the first instance of the char from in the string and the next instance of
	 *         the char to. If from is not part of the string, returns null. If from is but to is not, return the string
	 *         itself.
	 */
	public static String substring(final String string, final char from, final char to) {
		if (string == null) {
			return null;
		}

		for (int i = 0; i <= string.length(); i++) {
			if (string.charAt(i) == from) {
				return substring(string, i+1, to);
			}
		}
		return null;
	}

	/**
	 * @param string
	 *        any string
	 * @param divider
	 *        any char
	 * @return a list of all substrings of the original string divided by instances of divider
	 * 
	 *         <pre>
	 * ("Racines Dénudées", ' ') -> ["Racines", "Dénudées"] | ("QQ", 'Q') -> ["","",""] | ("qwerty", '1') -> ["qwerty"]
	 * </pre>
	 */
	public static List<String> splitString(final String string, final char divider) {
		if (string == null) {
			return null;
		}

		final List<String> lines = new LinkedList<>();
		final StringBuilder line = new StringBuilder();

		for (int i = 0; i < string.length(); i++) {
			final char letter = string.charAt(i);

			if (letter == divider) {
				lines.add(line.toString());
				line.delete(0, line.length());
			} else {
				line.append(letter);
			}
		}

		lines.add(line.toString());
		return lines;
	}

	/**
	 * Calls trim() on any instance of a list of strings.
	 * 
	 * @param strings
	 *        a list of strings
	 * @return the same list with every instance replaced by instance.trim()
	 */
	public static List<String> mapTrim(final List<String> strings) {
		if (strings == null) {
			return null;
		}

		for (int i = 0; i < strings.size(); i++) {
			strings.set(i, strings.get(i).trim());
		}
		return strings;
	}
}