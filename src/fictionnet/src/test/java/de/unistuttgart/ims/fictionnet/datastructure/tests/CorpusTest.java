package de.unistuttgart.ims.fictionnet.datastructure.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import objectsForTests.TestCorpusConstructor;

public class CorpusTest {

	private Corpus corpus = null;
	
	@Before
	public void setUp(){
		corpus = new TestCorpusConstructor();
	}
	
	@Test
	public void testCreateText() {
		String name = "TestKorpus";
		String author = "Test";
		String publishingDate = "11.11.2011";
		String sourceText = "Test test test"
				+ "test testet set set set se tsetse"
				+ " tesetsetsetset< set stsetst wweräwerpwoerp";
		
		try {
			corpus.createText(name, author, publishingDate, sourceText);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Text text = corpus.getTexts().get(0);
		
		assertEquals("TestKorpus",text.getTextName());
		assertEquals("Test",text.getAuthorName());
		assertEquals("11.11.2011",text.getPublishingDate());
	}
	
	public void testCreateTextException() {
		
		
	}

	@Test
	@Ignore
	public void testDeleteTextText() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDeleteTextInt() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDeleteTextString() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testAddTexts() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testAddClonesOfTexts() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSetTexts() {
		fail("Not yet implemented");
	}

}
