package de.unistuttgart.ims.fictionnet.analysis;

import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;

/**
 * 
 * @author Domas Mikalkinas
 *
 */
public enum ActionTypes {
	TALKS_WITH("TALKS_WITH", true, true), 
	TALKS_ABOUT("TALKS_ABOUT", true, true),
	IS_MENTIONED("IS_MENTIONED", true, false);

	private final String languageKey;
	private final boolean subject;
	private final boolean object;

	/**
	 * Default constructor.
	 * 
	 * @param languageKey
	 *            - Key for Localisation.getValue(String);
	 * @param subject
	 *            - True, if action needs a subject.
	 * @param object
	 *            - True, if action needs an object.
	 */
	ActionTypes(String languageKey, boolean subject, boolean object) {
		this.languageKey = languageKey;
		this.subject = subject;
		this.object = object;

	}

	/**
	 * Returns an already translated String, not the language key.
	 */
	@Override
	public String toString() {
		return FictionManager.getLocalization().getValue(languageKey);
	}

	/**
	 * True, if this action needs a subject.
	 * 
	 * @return boolean
	 */
	public boolean hasSubject() {
		return subject;
	}

	/**
	 * True, if this action need an object.
	 * 
	 * @return boolean
	 */
	public boolean hasObject() {
		return object;
	}
}
