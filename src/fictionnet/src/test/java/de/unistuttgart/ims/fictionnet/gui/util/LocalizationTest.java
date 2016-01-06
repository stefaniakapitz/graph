package de.unistuttgart.ims.fictionnet.gui.util;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocalizationTest {
	String basepath;
	Localization loc;

	
	public LocalizationTest() {
		// TODO: Path guessing good enough for tests?
		//	 Maybe set up a mock directory and files instead?
		basepath = System.getProperty("user.dir")
				+ "/src/main/webapp/WEB-INF/lang/";
		
		loc = new Localization(basepath);
	}
	
	@Test
	public void testSetLanguage() {
		assertEquals("", new Localization(basepath).getValue("LANG_NAME"));
		
		loc.setLanguage("en");
		assertEquals("English", loc.getValue("LANG_NAME"));
		loc.setLanguage("de");
		assertEquals("Deutsch", loc.getValue("LANG_NAME"));
	}

	@Test
	public void testGetValue() {
		loc.setLanguage("en");
		assertEquals("Welcome to FictionNet!", loc.getValue("WELCOME"));

	}

	@Test
	public void testGetAllLanguages() {
		Map<String, String> map = new HashMap<>();
		map.put("English", "en");
		map.put("Deutsch", "de");

		assertEquals(map, loc.getAllLanguages());
	}
	
}
