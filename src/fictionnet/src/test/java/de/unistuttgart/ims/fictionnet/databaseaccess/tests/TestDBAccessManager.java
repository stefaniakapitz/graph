package de.unistuttgart.ims.fictionnet.databaseaccess.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.j256.ormlite.table.TableUtils;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.ActLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.EventLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.InteractionLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.PresenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SceneLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SentenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TalksAboutLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TokenLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TypeOfTextLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.users.Role;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * Tests for class DBAccessManager.
 * 
 * @author Lukas Rieger
 * @version 27-10-2015
 * 
 *          Tests for the DBAccessManager class. Use this for regression tests.
 *
 */
public class TestDBAccessManager {
	public DBAccessManager db = DBAccessManager.getTheInstance();
	public UserManagement userManagement = UserManagement.getTheInstance();
	public User testUser1;
	public User testUser2;

	@Before
	public void initialize() {
		db = DBAccessManager.getTheInstance();
		userManagement = UserManagement.getTheInstance();

		try {
			db.connect("jdbc:mariadb://localhost:3306/fictionnet?user=root&password=");

			TableUtils.clearTable(db.getConnectionSource(), User.class);
			TableUtils.clearTable(db.getConnectionSource(), Project.class);
			TableUtils.clearTable(db.getConnectionSource(), Corpus.class);

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
	public void testUpgradeUserToAdmin() throws Exception {
		User user = db.getUser("testUser1@fictionnet.de");

		assertEquals(Role.ADMIN, user.getRole());
	}

	@Test
	public void testCheckIfEmailExists() throws Exception {
		userManagement.createUser("testUser3@fictionnet.de", "baumkokoko");
		boolean result = db.checkIfEmailExists("testUser3@fictionnet.de");
		assertTrue(result);
	}

	@Test
	public void testSaveLoadProject() throws Exception {
		testUser1.createProject("firstUsersProcjet");
		testUser1.createProject("firstUsersSecondProject");
		testUser2.createProject("secondUsersFirstProject");
		testUser2.createProject("secondUsersSecondProject");
		testUser1.createProject("firstUsersThirdProject");

		User user1 = db.loadUserWithData("testUser1@fictionnet.de");
		User user2 = db.loadUserWithData("testUser2@fictionnet.de");

		assertNotNull(user1.getProject("firstUsersThirdProject"));
		assertNotNull(user2.getProject("secondUsersFirstProject"));
	}

	@Test
	public void testSaveLoadCorpus() throws Exception {
		testUser1.createProject("ProjectWithCorpus");
		Project p = testUser1.getProject("ProjectWithCorpus");

		p.createCorpus("Kafka");
		p.createCorpus("Goethe");
		p.createCorpus("Urs");
		User user = db.loadUserWithData("testUser1@fictionnet.de");

		Corpus c = user.getProject("ProjectWithCorpus").getCorpus("Kafka");
		assertNotNull(c);
	}

	@Test
	public void testSaveLoadUserWithAllData() throws Exception {
		User user = userManagement.createUser("me@me.de", "12asd");
		User user2 = userManagement.createUser("user2", "pw2");
		User user3 = userManagement.createUser("user3", "pw3");
		Project project2 = user2.createProject("user2project");
		project2.createCorpus("user2projectCorpus");
		Project project3 = user3.createProject("user3project");
		project3.createCorpus("user3projectCorpus");
		Project project = user.createProject("testProject");
		Corpus corpus = project.createCorpus("myCorpus");
		Text text = corpus.createText("lustiger Text", "Dieter", "11.11.11", "Es war ein mal...");
		corpus.createText("popo", "pipi", "lala", "lolo");
		ActLayer actLayer = new ActLayer();
		Act act = new Act(6,11,4);
		act.setActNumber(4);
		act.setContent("Ding");
		act.setStart(6);
		actLayer.addAct(act);
		SceneLayer sceneLayer = new SceneLayer();
		Scene scene = new Scene(5, "Stuff", actLayer.getAct(0));
		scene.setEnd(5);
		scene.setStart(111);
		sceneLayer.addScene(scene);
		System.out.println("Scene number "+ sceneLayer.getScenes().get(0).getStart());
		text.getLayerContainer().setLayer(new TalksAboutLayer());
		text.getLayerContainer().setLayer(new InteractionLayer());
		text.getLayerContainer().setLayer(new EventLayer());
		text.getLayerContainer().setLayer(new TypeOfTextLayer());
		text.getLayerContainer().setLayer(new TokenLayer());
		text.getLayerContainer().setLayer(new SentenceLayer());
		text.getLayerContainer().setLayer(sceneLayer);
		text.getLayerContainer().setLayer(new PresenceLayer());
		text.getLayerContainer().setLayer(actLayer);

		db.saveAllUserData(user);
		db.saveAllUserData(user3);
		db.saveAllUserData(user2);
		Text text2 = db.loadTextsOfCorpus(3).get(0);
		text2 = db.loadTextContent(text2);

		assertEquals(1, text2.getLayerContainer().getActLayer().getActs().size());
		assertEquals(4, text2.getLayerContainer().getActLayer().getActs().get(0).getActNumber());
		assertEquals("Ding", text2.getLayerContainer().getActLayer().getActs().get(0).getContent());
		assertEquals(5, text2.getLayerContainer().getSceneLayer().getScenes().get(0).getSceneNumber());
		assertEquals("Stuff", text2.getLayerContainer().getSceneLayer().getScenes().get(0).getContent());
		assertEquals(4, text2.getLayerContainer().getSceneLayer().getScenes().get(0).getAct().getActNumber());
		assertEquals(6, text2.getLayerContainer().getSceneLayer().getScenes().get(0).getAct().getStart());


ArrayList<User> users = db.getAllUsersWithData();
		for (User u : users) {

			u.printUserData();
		}
	}

	@After
	public void cleanUp() throws SQLException {
		db.disconnect();
	}

}
