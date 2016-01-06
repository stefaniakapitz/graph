package de.unistuttgart.ims.fictionnet.datastructure.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;

import objectsForTests.TestTextConstructor;

import org.junit.Before;
import org.junit.Test;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.LayerContainer;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.Synonym;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.ActLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.EventLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.InteractionLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SceneLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TalksAboutLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.PresenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SentenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TokenLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TypeOfTextLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Presence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Sentence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Token;
import de.unistuttgart.ims.fictionnet.imp.TextImportProcess;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * Tests for the class Text. 
 * Use this for regression testing.
 * 
 * @author Lukas Rieger
 * @version 22.09.2015
 *
 */
public class TestText{
	private DBAccessManager db;
	private UserManagement um;
	private String TEI_TESTFILE_PATH = "C:/Users/User/git/FictionNet/doc/Anleitung Alpha/Beispiel TEI Texte/MacBeth.xml";
	
	@Before
	public void setUp() throws SQLException {
		db = DBAccessManager.getTheInstance();
		um = UserManagement.getTheInstance();
		// can only be tested with mariadb. SQLite has a restriction for parameters (999) in sqlstatements.
		db.connect("jdbc:mariadb://localhost:3306/fictionnet?user=root&password=root");
	}
	@Test
	public void testWithRealImport() throws SQLException {
		User user = um.createUser("test@test", "123");
		Project p = user.createProject("project123");
		Corpus c = p.createCorpus("corpus123");
		File file = new File(TEI_TESTFILE_PATH);
		TextImportProcess tip = new TextImportProcess(file, c);
		tip.run();
		Text t = c.getTexts().get(0);
		for(String s : t.getStringSynonymsFor("Macbeth")) {
			System.out.println(s);
		}
		Synonym s1 = new Synonym("Egon", 0.89f, 0, 0);
		t.addSynonym("Macbeth", s1);
		
		t.rebuild();
		for(String s : t.getStringSynonymsFor("Macbeth")) {
			System.out.println(s);
		}	
	}
//
//	@Test
//	public void testText() throws Exception {
//		User user = um.createUser("llalala", "asdf");
//		Project p = user.createProject("project");
//		Corpus c = p.createCorpus("corpus");		
//		Text testText = c.createText("", "", "", "");
//		HashSet<String> castList = new HashSet<String>();		
//		castList.add("Dieter");
//		castList.add("Walter");
//		castList.add("Gudrun");
//		castList.add("Gundula");
//		testText.setCastList(castList);		
//		testText.setAuthorName("Viktor Asimov");
//		testText.setTextName("Der Mond");
//		testText.setPublishingDate("12.07.1979");
//		testText.setSourceText("lsaofkkjerroiwtu0893 u40tu0304u oqhf 03u#+##asdf+he  ahsdf##!!");
//		
//		assertNotNull(testText.getCastList());
//		assertEquals(testText.getSourceText().length(), testText.getNumberOfChars());
//		assertEquals("Viktor Asimov", testText.getAuthorName());
//		assertEquals("Der Mond", testText.getTextName());
//		
//		Synonym s1 = new Synonym("der König", 0.5f);
//		Synonym s2 = new Synonym("die zwei Frauen", 0.7f);
//		Synonym s3 = new Synonym("Didi", 0.8f);
//		Synonym s4 = new Synonym("alle", 0.8f);
//		
//		testText.addSynonym("Gudrun", s2);
//		testText.addSynonym("Gundula", s2);
//		testText.addSynonym("Walter", s1);
//		testText.addSynonym("Dieter", s3);
//		testText.addSynonym("Dieter", s4);
//		testText.addSynonym("Walter", s4);
//		testText.addSynonym("Gudrun", s4);
//		testText.addSynonym("Gundula", s4);
//		
//		db.saveText(testText);
//		
//		assertEquals(2, testText.getStringSynonymsFor("Dieter").size());
//		assertEquals(4, testText.getCastMembersForSynonym("alle").size());
//		
//		testText.deleteSynonym("Dieter", s3);
//		testText.addSynonym("Walter", s3);
//		
//		assertEquals(1, testText.getStringSynonymsFor("Dieter").size());
//		assertEquals(3, testText.getStringSynonymsFor("Walter").size());
//		
//		ArrayList<Token> tokens = new ArrayList<Token>();
//		for(int i=0; i<100; i++) {
//			tokens.add(new Token(i, i+10));
//		}
//		
//		ArrayList<Sentence> sentences = new ArrayList<Sentence>();
//		for(int i=0; i<100; i++) {
//			sentences.add(new Sentence(1, i));
//		}
//		
//		ArrayList<Presence> presences = new ArrayList<Presence>();
//		for(int i=0; i<5000; i++) {
//			presences.add(new Presence(i, i+2));
//		}
//		testText.getLayerContainer().setLayer(new TokenLayer(tokens));
//		testText.getLayerContainer().setLayer(new TypeOfTextLayer());
//		testText.getLayerContainer().setLayer(new SentenceLayer(sentences));
//		testText.getLayerContainer().setLayer(new PresenceLayer(presences));
//		testText.getLayerContainer().setLayer(new TalksAboutLayer());
//		testText.getLayerContainer().setLayer(new InteractionLayer());
//		testText.getLayerContainer().setLayer(new EventLayer());
//		testText.getLayerContainer().setLayer(new ActLayer());
//		testText.getLayerContainer().setLayer(new SceneLayer());
//		
//		db.saveText(testText);
//		
//		testText.getLayerContainer().setLayer(new PresenceLayer(presences));
//		
//		testText.rebuild();
//		
//		assertEquals(100, testText.getLayerContainer().getSentenceLayer().getSentences().size());
//		assertNotNull(testText.getLayerContainer().getPresenceLayer());
//		assertNotNull(testText.getLayerContainer().getTalksAboutLayer());
//		
//
//	}
//	
//	@Test
//	public void testTextSynonyms() {
//		Text text = new TestTextConstructor();
//		text.setTextName("TestText");
//		text.setAuthorName("derAutor");
//		text.setPublishingDate("12.04.89");
//		text.setSourceText("no text available");
//		
//		
//	}
//	
//	@Test
//	public void testLayerContainer() throws SQLException {
//		Text text = new TestTextConstructor();
//		text.setTextName("textTitle");
//		text.setAuthorName("authorName");
//		text.setPublishingDate("12.12.12");
//		text.setSourceText("sourceText");
//		db.saveText(text);
//		LayerContainer layerContainer = new LayerContainer();
//		layerContainer.setTextId(text.getId());
//		db.saveLayerContainer(layerContainer);
//		TokenLayer tokenLayer = new TokenLayer();
//		tokenLayer.setContainerId(layerContainer.getId());
//		layerContainer.setLayer(tokenLayer);
//		text.setLayerContainer(layerContainer);
//		db.saveText(text);
//		text = db.loadTextContent(text);
//		
//		assertNotNull(text.getLayerContainer());
//		assertNotNull(text.getLayerContainer().getTokenLayer());
//	}
//	
//	@Test
//	public void testSetGet() {
//		Text text = new TestTextConstructor();
//		text.setTextName("TestText");
//		text.setAuthorName("Autor");
//		text.setPublishingDate("12.12.12");
//		text.setSourceText("98(/&(/)((/aksjdhf!");
//		text.setAuthorName("Dieter Delelel");
//		text.setSourceText("Es war zweimal lalal123/(§!");
//		text.setPublishingDate("09.09.2017");
//		assertEquals("Dieter Delelel", text.getAuthorName());
//		assertEquals("Es war zweimal lalal123/(§!".length(), text.getNumberOfChars());
//		assertEquals("09.09.2017", text.getPublishingDate());
//		assertEquals("Es war zweimal lalal123/(§!", text.getSourceText());
//		assertEquals("TestText", text.getTextName());		
//	}
}
