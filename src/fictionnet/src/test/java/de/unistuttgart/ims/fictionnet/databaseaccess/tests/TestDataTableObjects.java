
package de.unistuttgart.ims.fictionnet.databaseaccess.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Date;

import objectsForTests.TestUserConstructor;

import org.junit.Before;
import org.junit.Test;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.databaseaccess.DataTableLayercontainer;
import de.unistuttgart.ims.fictionnet.databaseaccess.DataTableProject;
import de.unistuttgart.ims.fictionnet.databaseaccess.DataTableUsers;
import de.unistuttgart.ims.fictionnet.datastructure.LayerContainer;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.layers.InteractionLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TokenLayer;
import de.unistuttgart.ims.fictionnet.users.Role;
import de.unistuttgart.ims.fictionnet.users.User;
/**
 * Tests for the DataTableClasses.
 * Use this for regression testing.
 * 
 * @author Lukas Rieger
 * @version 30-10-2015
 *
 */
public class TestDataTableObjects extends Project{
	
	public TestDataTableObjects() {
		super();
	}
	
	DBAccessManager db = DBAccessManager.getTheInstance();
	@Before
	public void setUp() {
		try {
			db.connect("jdbc:mariadb://localhost:3306/fictionnet?user=root&password=root");
		} catch (SQLException e) {
			System.out.println("Exception while connecting.");
		}
		
	}
	@Test
	public void testDataTableUsers() throws Exception {
		DataTableUsers dtUsers = db.getDataTableUsers();
		User user = new TestUserConstructor();
		user.setUserEmail("testUser@fictionnet");
		user.setEncryptedPassword(12312313);
		user.setRole(Role.USER);
		user.setRegistrationDate(new Date());
		user.setLastLoginDate(new Date());
		assertFalse(dtUsers.checkIfEmailExists("someNotExistingEmail@googlemail.com"));
		assertFalse(dtUsers.deleteById("someNotExistingEmail@googlemail.com"));
		assertTrue(dtUsers.createOrUpdate(user));
		User loadedUser = dtUsers.selectFromThisAllWhere().eq("userEmail", "testUser@fictionnet").queryForFirst();
		assertTrue(dtUsers.delete(user));
		assertNotNull(loadedUser);
		dtUsers.createOrUpdate(loadedUser);
		loadedUser.setRole(Role.ADMIN);
		dtUsers.update(loadedUser);
		user = dtUsers.selectFromThisAllWhere().eq("userEmail", "testUser@fictionnet").queryForFirst();
		assertEquals(Role.ADMIN, user.getRole());
	}
	
	@Test
	public void testDataTableProjects() throws Exception {
		DataTableProject dtProjects = db.getDataTableProject();
		Project project = new TestDataTableObjects();
		project.setCreationDate(new Date());
		project.setLastChangeDate(new Date());
		project.setName("a test project");
		User user = new TestUserConstructor();
		user.setUserEmail("testUser@fictionnet");
		user.setEncryptedPassword(12312313);
		user.setRole(Role.USER);
		user.setRegistrationDate(new Date());
		user.setLastLoginDate(new Date());
		project.setOwner(user);
		project.setOwnerEmail(user.getEmail());
		assertTrue(dtProjects.createOrUpdate(project));
		assertTrue(dtProjects.delete(project));
	}
	
	@Test
	public void testDataTableLayercontainer() throws Exception {
		DataTableLayercontainer dtLayercontainer = db.getDataTableLayercontainer();
		LayerContainer container = new LayerContainer();
		container.setId(5);
		TokenLayer tokenLayer = new TokenLayer();
		InteractionLayer interactionLayer = new InteractionLayer();
		//interactionLayer.getInteractions().add(new Interaction());
		container.setLayer(tokenLayer);
		container.setLayer(interactionLayer);
		dtLayercontainer.createOrUpdate(container);
		tokenLayer = new TokenLayer();
		container.setLayer(tokenLayer);
		dtLayercontainer.createOrUpdate(container);
		tokenLayer = new TokenLayer();
		container.setLayer(tokenLayer);
		dtLayercontainer.createOrUpdate(container);
	}

}
