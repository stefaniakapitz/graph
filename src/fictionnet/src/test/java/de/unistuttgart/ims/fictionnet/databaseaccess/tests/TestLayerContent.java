package de.unistuttgart.ims.fictionnet.databaseaccess.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.imp.TextImportProcess;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * 
 * @author Lukas Rieger
 * @version 14-11-2015
 * 
 * Checks wheather all layers exist after file import.
 *
 */
public class TestLayerContent {
	private DBAccessManager db;
	private UserManagement userManagement;
	private final String TEST_FILE_PATH = "C:/Users/User/git/FictionNet/doc/Anleitung Alpha/Beispiel TEI Texte/DieBojaren.xml";

	@Before
	public void setUp() throws SQLException {
		db = DBAccessManager.getTheInstance();
		userManagement = UserManagement.getTheInstance();
		db.connect("jdbc:mariadb://localhost:3306/fictionnet?user=root&password=root");
	}

	@Test
	public void testFileImport() throws SQLException {
		User user = userManagement.createUser("user@fictionnet.de", "1&1&1");
		Corpus corpus = user.createProject("TargetProject").createCorpus("TargetCorpus");
		File f = new File(TEST_FILE_PATH);
		TextImportProcess importProcess = new TextImportProcess(f, corpus);
		importProcess.run();
		
		db.saveAllUserData(user);
		
		assertNotNull(corpus.getTexts());
		Text text = corpus.getTexts().get(0);
		
		assertNotNull(text.getLayerContainer());
		assertNotNull(text.getLayerContainer().getTalksAboutLayer());
		assertNotNull(text.getLayerContainer().getInteractionLayer());
		assertNotNull(text.getLayerContainer().getSceneLayer());
		assertNotNull(text.getLayerContainer().getSentenceLayer());
		assertNotNull(text.getLayerContainer().getEventLayer());
		assertNotNull(text.getLayerContainer().getActLayer());
		assertNotNull(text.getLayerContainer().getTypeOfTextLayer());
	}

}
