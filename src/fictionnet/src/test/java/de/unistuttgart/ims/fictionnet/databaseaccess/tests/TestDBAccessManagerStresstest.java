package de.unistuttgart.ims.fictionnet.databaseaccess.tests;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.users.Role;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * Tests for class DBAccessManager. 
 * This testcase can be used to check the performance.
 * It creates 100 users, 100 projects, 100 corpora and 1000 texts in the database.
 * 
 * @author Lukas Rieger
 * @version 22.09.2015
 *
 */
public class TestDBAccessManagerStresstest {
	private DBAccessManager db = DBAccessManager.getTheInstance();
	private UserManagement um = UserManagement.getTheInstance();
	private User user;
	
	@Before
	public void setUp() throws Exception {
		db.connect("jdbc:mariadb://localhost:3306/fictionnet?user=root&password=root");
		user = um.createUser("me@fictionnet.de", "hi");		
	}
	
	@Test
	public void testStresstest() throws Exception {
		int i, j;		
		Project p;
		Corpus c;
		
		for (i = 0; i < 100; i++) {
			um.createUser("testuser" + i + "@fictionnet.de", "user" + i);
		}
		
		for (i = 0; i < 100; i++) {
			um.changeUserRole("testuser" + i + "@fictionnet.de", Role.ADMIN);
			um.login("testuser" + i + "@fictionnet.de", "user" + i);
		}
		
		for (i = 0; i < 100; i++) {
			p = user.createProject("project" + i);
			c = p.createCorpus("corpus" + i);
			for (j = 0; j < 10; j++) {
				c.createText("test", "test", "01.01.10", "testtest");
			}
		}
		
		ArrayList<User> loadedUsers  = new ArrayList<User>();
		
		for (i = 0; i < 100; i++) {
			loadedUsers.add(db.loadUserWithData("testuser" + i + "@fictionnet.de"));
		}

		for (User u : loadedUsers) {
				u.printUserData();
		}
	}
}
