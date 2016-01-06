package de.unistuttgart.ims.fictionnet.imp.analysis;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.SQLException;
import java.util.Iterator;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.layers.PresenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TalksAboutLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Presence;
import de.unistuttgart.ims.fictionnet.imp.TextImportProcess;
import de.unistuttgart.ims.fictionnet.users.User;
import objectsForTests.TestUserConstructor;

public class PresencesAnalyzerTest {

	private static File f = new File("C:/Users/David/git/FictionNet/doc/Anleitung Alpha/Beispiel TEI Texte/WilhelmTell.xml");
	private static User user = new TestUserConstructor("test@test.com");
	private static Project project = null;
	private static Corpus corpus = null;
	private static PresenceLayer presence= null;
	
	@BeforeClass
	public static void initDB() {
		try {
			DBAccessManager db = DBAccessManager.getTheInstance();
			db.connect("jdbc:sqlite::memory:");
			project = user.createProject("project");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Before
	public void setUp(){
				
		try {
			corpus = project.createCorpus("myCorpus");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TextImportProcess t = new TextImportProcess(f, corpus);
		t.run();
		presence = corpus.getTexts().get(0).getLayerContainer().getPresenceLayer();
	}
	
	
	
	@Test
	public void testAnalysis() {
		Iterator<Presence> i = presence.getPresences().iterator();
	}

}
