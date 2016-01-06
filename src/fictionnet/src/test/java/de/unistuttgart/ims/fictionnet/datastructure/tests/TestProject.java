package de.unistuttgart.ims.fictionnet.datastructure.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;
/**
 * Tests for the DBAccessManager class focusing on the text functions.
 * Use this for regression tests.
 * 
 * @author Lukas Rieger
 * @version 30-10-2015
 *
 */
public class TestProject {
	DBAccessManager db;
	UserManagement um;
	
	@Before
	public void setUp() throws SQLException {
		db = DBAccessManager.getTheInstance();
		um = UserManagement.getTheInstance();
	    // Test with mariadb or sqlite
	    // db.connect("jdbc:mariadb://localhost:3306/fictionnet?user=root&password=root");
		db.connect("jdbc:sqlite::memory:");
	}
	@Test
	public void testTextNaming() throws SQLException {
		User user = um.createUser("testUser1@email.de", "pw1");
		Project p = user.createProject("myProject");
		Corpus c = p.createCorpus("myCorpus");
		c.createText("name", "author", "12.12.12", "sourceText lalala!2§%$)()=´`");
		c.createText("name", "author", "12.12.12", "sourceText lalala");
		c.createText("name", "author", "12.12.12", "sourceText lalala");
		c.createText("name", "author", "12.12.12", "sourceText lalala");
		c.createText("name", "author", "12.12.12", "sourceText lalala");
		
		ArrayList<Text> texts = c.getTexts();
		for (final Text t : texts) {
			assertTrue(t.getTextName().equals("name") || 
					t.getTextName().equals("name (1)") ||
					t.getTextName().equals("name (2)") ||
					t.getTextName().equals("name (3)") ||
					t.getTextName().equals("name (4)"));
		}
		assertEquals(5, db.loadTextsOfCorpus(c.getId()).size());
		db.deleteCorpus(c);
		assertEquals(0, db.loadTextsOfCorpus(c.getId()).size());
		assertEquals(0, db.loadCorporaOfProject(p.getId()).size());
	}
	
	@Test
	public void testSetGet() throws SQLException {
		User user = um.createUser("testUser2@email.de", "pw1");
		Project p = user.createProject("myProject2");
		
		p.setCorpora(new ArrayList<Corpus>());
		p.setLastChangeDate(new Date());
		p.setName("newProjectName");
		p.setOwner(user);
		assertNotNull(p.getCorpora());
		assertNotNull(p.getOwner());
		
	}

}
