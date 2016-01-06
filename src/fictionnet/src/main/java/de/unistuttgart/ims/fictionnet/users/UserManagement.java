package de.unistuttgart.ims.fictionnet.users;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Project;

/**
 * @author Lukas Rieger, Erol Aktay
 * @version 26-10-15
 * 
 *          This class takes care of the users.
 *          It can create and delete users and offers the login-function.
 *          The Singleton pattern is implemented.
 *          The salt attribute is added to the passwords in order to avoid
 *          recognizable hash values for well know passwords.
 */
public class UserManagement {
  private static UserManagement theInstance;
  private static final String salt = "4#d3";
  private DBAccessManager db = DBAccessManager.getTheInstance();
  private ArrayList<UserChangeListener> userChangeListeners = new ArrayList<>();

  // private for singleton pattern
  private UserManagement() {
  }

  /**
   * This method returns the only instance of this class.
   * 
   * @return theInstance
   */
  public synchronized static UserManagement getTheInstance() {
    if (theInstance == null) {
      theInstance = new UserManagement();
    }
    return theInstance;
  }
  
  /**
   * Hash password
   * 
   * @param pwd clear-text password
   * @return password hash
   */
  public static long hash(String pwd) {
	  // FIXME: This is not how salting works!
	  pwd = pwd + salt;
	  // FIXME: hashCode() should never be used for cryptographic hashes!
	  return String.valueOf((pwd + salt).hashCode()).hashCode();
  }
  
  /**
   * This method creates a new user with the given email and password.
   * It also creates the new user in the database,
   * adds an id to the object and the returns it.
   * It ensures that the emailadress is unique and not empty.
   * 
   * @param email
   * @param password
   * @return User new user
   * @throws SQLException
   */
  public User createUser(final String email, final String password) throws SQLException {
    final User newUser = new User(email);
    newUser.setPassword(password);
    db.saveUser(newUser);
    return newUser;
  }

  /**
   * This method sets the user who was given by the email adress
   * to the role that was given and updates it in the
   * database. If the update was successful it returns true, else false.
   * 
   * @param email
   * @param role
   *          role
   * @return boolean
   */
  public boolean changeUserRole(String email, Role role) {
    User user;
	Role oldRole;
    try {
      user = db.getUser(email);
	  oldRole = user.getRole();
      user.setRole(role);
      db.saveUser(user);
    } catch (SQLException e) {
      return false;
    }
	for (UserChangeListener listener : userChangeListeners) {
		listener.onRoleChange(user, oldRole, role);
	}
	return true;

  }

  /**
   * This method adds a clone of the given project to each user given
   * in the emails array. It returns a new email array
   * containing the mails of those emails that received the project, that is they were valid.
   * IMPORTANT: SAVE THE PROJECT TO CLONE FIRST THEN CLONE IT TO AVOID LOSS OF UNSAVED DATA IN 
   * CLONED PROJECT.
   * 
   * @param email of users to receive a clone of the project
   * @param project to clone
   * @return list of successful receivers of the project
   * @throws SQLException
   */
  public ArrayList<String> cloneProjectToEmails(String[] emails, Project project, String newCloneString) throws SQLException {
    final ArrayList<String> successfullyClonedToEmails = new ArrayList<String>();
    // TODO: create deep copy of the project object here (by serializing it?)
    User receiver = null;
    for(String email : emails) {
    	receiver = db.getUser(email);
    	if(receiver != null) {
    		project.setOwner(receiver);
    		db.cloneProjectTo(project, receiver, newCloneString);
    		successfullyClonedToEmails.add(email);
    	}
    	receiver = null;
    }
    return successfullyClonedToEmails;
  }

  /**
   * This method deletes the given user from the database.
   * It returns true if it was succssful, else false.
   * 
   * @param user
   * @return boolean
   * @throws SQLException 
   */
  public boolean deleteUser(final User user) throws SQLException {
	 for (UserChangeListener listener : userChangeListeners) {
		listener.onDeletion(user.getEmail());
	}
    db.deleteUser(user);
    return true;
  }

  /**
   * This method checks if the given email belongs to the given password.
   * If this is correct it loads the user and
   * returns it with all the data. Returns null if the password is not correct.
   * 
   * @param email of user to login
   * @param password of user to login
   * @return user
   * @throws Exception 
   */
  public User login(String email, final String password) throws SQLException {
	email = email.toLowerCase();
    final User user = db.loadUserWithData(email);
    if (user != null) {
      // password hashes are compared -> long values
      if (user.getPassword() == hash(password)) {
        db.setLastLogin(user, new Date());
        return user;
      }
    }
    return null;
  }

  /**
   * This method checks if the email adress is unique
   * 
   * @param email
   * @return boolean
 * @throws SQLException 
   */
  public boolean isEmailUnique(final String email) throws SQLException {
    return !db.checkIfEmailExists(email);
  }
  
  /**
   * Add a new listener to changes on a user
   * 
   * @param listener 
   */
  public void addUserChangeListener(UserChangeListener listener) {
	  userChangeListeners.add(listener);
  }
   
  /**
   * Remove a previously added user
   * 
   * @param listener 
   */
  public void removeUserChangeListener(UserChangeListener listener) {
	  userChangeListeners.remove(listener);
  }
}
