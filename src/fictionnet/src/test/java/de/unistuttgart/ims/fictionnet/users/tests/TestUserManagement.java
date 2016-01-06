package de.unistuttgart.ims.fictionnet.users.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TokenLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Token;
import de.unistuttgart.ims.fictionnet.users.Role;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * Test for the class UserManagement.
 * Use this for regression testing.
 * 
 * @author Lukas Rieger
 * @version 21.09.2015
 *
 */
public class TestUserManagement {

  private DBAccessManager db;
  private UserManagement userManagement;

  @Before
  public void setUp() throws SQLException {
    db = DBAccessManager.getTheInstance();
    userManagement = UserManagement.getTheInstance();

    // Test with mariadb or sqlite
    // db.connect("jdbc:mariadb://localhost:3306/fictionnet?user=root&password=root");
    db.connect("jdbc:sqlite::memory:");
    User user = userManagement.createUser("user@fictionnet.de", "1&1&1");
        
    userManagement.deleteUser(user);
    assertTrue(userManagement.isEmailUnique("user@fictionnet.de"));
  }

  @Test
  public void testLogin() throws SQLException {
    userManagement.createUser("test1@fictionnet.de", "mysecretPassword123!§");
    User user = userManagement.login("test1@fictionnet.de", "mysecretPassword123!§");

    assertNotNull(user);
    user.printUserData();
  }

  @Test
  public void testChangeUserRole() throws SQLException {
    userManagement.createUser("admin@fictionnet.de", "admin123");
    userManagement.changeUserRole("admin@fictionnet.de", Role.ADMIN);

    User user = userManagement.login("admin@fictionnet.de", "admin123");

    assertEquals(Role.ADMIN, user.getRole());
  }

  @Test
  public void testWrongPassword() throws SQLException {
    userManagement.createUser("user12@fictionnet.de", "mypassword123");
    assertNull(userManagement.login("user12@fictionnet.de", "mypassword145"));
  }
  
  @Test
  public void testCloneFunction() throws SQLException {
	  User sender = userManagement.createUser("sender@email.de", "pw12");
	  userManagement.createUser("receiver@email.com", "superpw");
	  Project project = sender.createProject("toBeSent");
	  Corpus corpus = project.createCorpus("corpusClone");
	  Text text = corpus.createText("clone", "clone", "12.12.12", "clone clone clone !!");
	  TokenLayer tokenLayer = new TokenLayer();
	  Token token = new Token(1, 5);
	  ArrayList<Token> tokens = new ArrayList<Token>();
	  tokens.add(token);
	  tokenLayer.setTokens(tokens);
	  text.getLayerContainer().setLayer(tokenLayer);
	  db.saveAllUserData(sender);
	  String[] emails = new String[2];
	  emails[0] = "receiver@email.com";
	  emails[1] = "wrongEmail@email.com";
	  ArrayList<String> successes = userManagement.cloneProjectToEmails(emails, project, "New shared project");
	  
	  assertEquals(1, successes.size());
  }

  @After
  public void tearDown() throws SQLException {
    db.disconnect();
  }

}
