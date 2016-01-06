package de.unistuttgart.ims.fictionnet.gui.components;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComboBox;

import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;
import de.unistuttgart.ims.fictionnet.gui.util.Localization;

/**
 * A component for language selection.
 */
public class LanguageSelection extends AbstractLocalizedCustomComponent {
	private static final String LANG_NAME = "LANG_NAME";
	private final transient ComboBox comboBox;

	/**
	 * Creates a new LanguageSelection. .
	 */
	@SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
	public LanguageSelection() {
		super();

		comboBox = new ComboBox();
		comboBox.setNullSelectionAllowed(false);

		setSizeUndefined();
		setCompositionRoot(comboBox);
		setLocalization();

		// Add listener, to change language and write cookie with selection.
		addValueChangeListener(new ValueChangeListener() {
			@Override
			public void valueChange(final Property.ValueChangeEvent event) {

				String lang = getLocalization().getAllLanguages().get((String) event.getProperty().getValue());
				
				if (FictionManager.getUser() != null) {
					FictionManager.getUser().setLang(lang);
				} else {
					FictionManager.writeLanguageCookie(lang);
				}
				
				FictionManager.getLocalization().setLanguage(lang);
				FictionUI.getCurrent().reloadUI();
			}
		});
	}

	/**
	 * Adds a listener, that is fired, when the {@link ComboBox} changes its selected value.
	 * 
	 * @param listener
	 *            - {@link ValueChangeListener}
	 */
	private void addValueChangeListener(ValueChangeListener listener) {
		this.comboBox.addValueChangeListener(listener);
	}

	/**
	 * Sets the language options in the combobox.
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	private void setLocalization() {

		for (final String languageOption : getLocalization().getAllLanguages().keySet()) {
			comboBox.addItem(languageOption);
		}
		comboBox.setValue(getLocalization().getValue(LANG_NAME));

	}

}
