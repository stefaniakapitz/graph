package de.unistuttgart.ims.fictionnet.export.tests;


import static org.junit.Assert.assertEquals;

import java.beans.XMLDecoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import objectsForTests.TestUserConstructor;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.PresenceLayer;
import de.unistuttgart.ims.fictionnet.export.ProjectExportProcess;
import de.unistuttgart.ims.fictionnet.export.ProjectImportProcess;
import de.unistuttgart.ims.fictionnet.imp.TextImportProcess;
import de.unistuttgart.ims.fictionnet.users.User;

/**
 * Test for Class ProjectExportProcess.
 * @author David Schuetz
 * @version 09-10-2015
 */
public class TestProjectExportProcess {
	
	private static File f = new File("C:/Users/David/git/FictionNet/doc/Anleitung Alpha/Beispiel TEI Texte/MacBeth.xml");
	private static User user = new TestUserConstructor();
	private static Project project = null;
	private static Corpus corpus = null;
	
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
	}
	
	
	/**
	 * Test the ExportProcess write Method.
	 * @throws IOException 
	 * @throws Exception
	 */
	@Test
	public void testWrite() {
		ProjectExportProcess exportProcess = new ProjectExportProcess();
		ProjectImportProcess importProcess = new ProjectImportProcess();
		try {
			InputStream input = exportProcess.write(project);
			Project importedProject = importProcess.importProject(input, user);
			Text text = importedProject.getCorpora().get(0).getTexts().get(0);
			text.getId();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
