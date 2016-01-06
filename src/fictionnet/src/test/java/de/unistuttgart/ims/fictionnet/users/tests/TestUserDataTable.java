package de.unistuttgart.ims.fictionnet.users.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * Tests for the UserDataTable.
 * 
 * @author Lukas Rieger
 * @version 30-10-2015
 *
 */
public class TestUserDataTable {
	private DBAccessManager db = DBAccessManager.getTheInstance();
	private UserManagement um = UserManagement.getTheInstance();
	
	@Before
	public void setUp() throws Exception {
	    // Test with mariadb or sqlite
	    // db.connect("jdbc:sqlite::memory:");
		db.connect("jdbc:mariadb://localhost:3306/fictionnet?user=root&password=root");	
	}
	
	@Test
	public void test() throws Exception {
		um.createUser("testUser1@fictionnet.de", "supersecretpw1");
		assertTrue(db.checkIfEmailExists("testUser1@fictionnet.de"));
		User user = um.login("testUser1@fictionnet.de", "supersecretpw1");
		assertNotNull(user);
		db.deleteUser(user);
	}
}
