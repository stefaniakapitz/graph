package de.unistuttgart.ims.fictionnet.imp.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Rafael Harth
 * @version 10-31-2015
 * 
 *          A collection of methods offering data type conversion which java doesn't support.
 *
 */
public final class Converter {
	/**
	 * No reason to instantiate this class.
	 */
	private Converter() {
	}

	/**
	 * @param strings
	 *        a list of strings
	 * @return the same strings in a set OR null if strings == null. Multiple entities will not only appear in the set.
	 */
	public static Set<String> toStringSet(List<String> strings) {
		if (strings == null) {
			return null;
		}

		final Set<String> stringSet = new HashSet<>();
		stringSet.addAll(strings);
		return stringSet;
	}
}