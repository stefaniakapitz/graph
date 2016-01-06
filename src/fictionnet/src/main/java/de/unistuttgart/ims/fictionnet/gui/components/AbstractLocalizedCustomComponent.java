package de.unistuttgart.ims.fictionnet.gui.components;

import com.vaadin.ui.CustomComponent;

import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;
import de.unistuttgart.ims.fictionnet.gui.util.Localization;

/**
 * Abstract class for custom components that provides Localization.
 * 
 * @author Erol Aktay, Roman Stercken
 */
public abstract class AbstractLocalizedCustomComponent extends CustomComponent {
	private final Localization localization;

	/**
	 * Default constructor.
	 */
	public AbstractLocalizedCustomComponent() {
		super();
		localization = FictionManager.getLocalization();
	}

	/**
	 * Returns the translated language string to the given key.
	 * 
	 * @param key
	 *            - Key value, which has a language string in the language files.
	 * @return Translated string.
	 */
	public String getLocal(final String key) {
		return localization.getValue(key);
	}

	/**
	 * Gets the {@link Localization} object of the current user.
	 * 
	 * @return {@link Localization}
	 */
	public Localization getLocalization() {
		return localization;
	}
}
