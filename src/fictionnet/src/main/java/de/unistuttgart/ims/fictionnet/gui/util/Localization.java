package de.unistuttgart.ims.fictionnet.gui.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;

/**
 * Manages the language resources.
 *
 */
public class Localization {
	private final transient String basepath;
	private transient Properties langFile;
	private String language;

	/**
	 * Initialize class with the language path.
	 * 
	 * @param path
	 *            The absolute path of the language directory
	 */
	public Localization(String path) {
		basepath = path;
	}

	/**
	 * Get all available languages.
	 * 
	 * @return a map with the Language name as key and the language code as value
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public Map<String, String> getAllLanguages() {
		final Map<String, String> langs = new ConcurrentHashMap<>();

		final File[] files = new File(basepath).listFiles();
		if (files == null) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Could not find language files");
		} else {
			final Localization loc = new Localization(basepath);
			for (final File file : files) {
				if (file.isFile() && file.getName().endsWith(".properties")) {
					final String name = file.getName().substring(0, 2);
					loc.setLanguage(name);
					langs.put(loc.getValue("LANG_NAME"), name);
				}
			}
		}

		return langs;
	}

	/**
	 * Gets the translation for the current language.
	 * 
	 * @param key
	 *            - Key of the word/sentence, that should be translated.
	 * @return Translation or "" when no language is loaded. Returns key, if key is not found.
	 */
	@SuppressWarnings("PMD.OnlyOneReturn")
	public String getValue(final String key) {
		if (langFile == null) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Language File is null!");
			return "";
		} else {
			final String value = langFile.getProperty(key);
			if (value == null) {
				Logger.getLogger(getClass().getName()).log(Level.WARNING, String.format("Language value for %s is missing.", key));
				return key;
			} else {
				return value;
			}
		}
	}

	/**
	 * Set the language.
	 * 
	 * @param lang
	 *            The ISO-639-1 language name
	 */
	public void setLanguage(String lang) {
		language = lang; 
		try (FileInputStream inputStream = new FileInputStream(basepath + lang + ".properties")) {
			langFile = new Properties();
			langFile.load(inputStream);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Localization.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Localization.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public String getLanguageString() {
		return language;
	}

}
