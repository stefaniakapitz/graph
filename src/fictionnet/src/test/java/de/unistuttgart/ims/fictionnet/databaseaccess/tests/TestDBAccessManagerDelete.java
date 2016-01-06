package de.unistuttgart.ims.fictionnet.databaseaccess.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.j256.ormlite.table.TableUtils;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.users.Role;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * Tests for class DBAccessManager.
 * Use this for regression testing.
 * 
 * @author Lukas Rieger
 * @version 22-09-2015
 *
 */
public class TestDBAccessManagerDelete {

	public DBAccessManager db = DBAccessManager.getTheInstance();
	public UserManagement userManagement = UserManagement.getTheInstance();
	public User testUser1;
	public User testUser2;
	
	@Before
	public void initialize(){
		db = DBAccessManager.getTheInstance();
		userManagement = UserManagement.getTheInstance();
		
		try {
			db.connect("jdbc:mariadb://localhost:3306/fictionnet?user=root&password=root");
			
			TableUtils.clearTable(db.getConnectionSource(), User.class);
			TableUtils.clearTable(db.getConnectionSource(), Project.class);
			TableUtils.clearTable(db.getConnectionSource(), Corpus.class);
			TableUtils.clearTable(db.getConnectionSource(), Text.class);
			
			userManagement.createUser("testUser1@fictionnet.de", "katze");
			userManagement.createUser("testUser2@fictionnet.de", "hund");
			
			testUser1 = userManagement.login("testUser1@fictionnet.de", "katze");
			testUser2 = userManagement.login("testUser2@fictionnet.de", "hund");	
			userManagement.changeUserRole("testUser1@fictionnet.de", Role.ADMIN);
			
			assertNotNull(testUser1);
			assertNotNull(testUser2);
			
		} catch (Exception e) {
			System.out.println("ERROR initialize(): " + e.getMessage());
		}
	}
	
	@Test
	public void testDeleteProject() throws Exception {
		testUser1.createProject("Project_0");
		testUser1.createProject("Project_1");
		Project p = testUser1.createProject("Project_2");
		Corpus c = p.createCorpus("corpus");
		c.createText("lulu", "h", "12.12.12", "/&&$&");
		
		testUser1.deleteProject(p);		
		
		assertNull(testUser1.getProject("Project_2"));
		assertNotNull(testUser1.getProject("Project_0"));
	}

}
