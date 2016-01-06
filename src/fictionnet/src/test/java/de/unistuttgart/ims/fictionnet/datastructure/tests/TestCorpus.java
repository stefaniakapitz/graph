package de.unistuttgart.ims.fictionnet.datastructure.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * Tests for the corpus class.
 * Use this for regression testing.
 * 
 * @author Lukas Rieger
 * @version 30-10-2015
 *
 */
public class TestCorpus {
	DBAccessManager db;
	UserManagement um;
	
	@Before
	public void setUp() throws SQLException {
		db = DBAccessManager.getTheInstance();
		um = UserManagement.getTheInstance();
		db.connect("jdbc:sqlite::memory:");
	}
	
	@Test
	public void testCorpus() throws SQLException {
		User u = um.createUser("test@test.de", "super");
		Project p = u.createProject("testProject");
		Corpus c = p.createCorpus("testCorpus");
		c.createText("name", "author", "12.12.12", "texttext35345!(/&(");
		c.createText("name", "author", "12.12.12", "texttext35345!(/&(");
		assertEquals(2, c.getTexts().size());
		c.deleteText("name (1)");
		assertEquals(1, c.getTexts().size());
		assertEquals("testCorpus", c.getCorpusName());
		assertNotNull(c.getProjectId());
		
	}

}
