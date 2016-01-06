package de.unistuttgart.ims.fictionnet.users;

import java.io.File;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.Layer;

/**
 * @author Lukas Rieger, Erik-Felix Tinsel
 * @version 21-09-15
 *
 *          The User class represents a person that uses the application,
 *          independent of the role. It takes care of the
 *          projects and the attributes that belong to this user.
 *          The userEmail attribute has to be unique since it is
 *          used as key in database.
 */
@DatabaseTable(tableName = "users")
public class User {
  @DatabaseField(id = true, unique = true)
  private String userEmail;
  @DatabaseField
  private Role role;
  @DatabaseField
  private long encryptedPassword;
  @DatabaseField
  private Date registrationDate;
  @DatabaseField
  private Date lastLoginDate;
  @DatabaseField
  private String lang;

  /*Added to passwords in order to prevent well known hash values*/
  private ArrayList<Project> ownedProjects = new ArrayList<Project>();
  private ArrayList<Project> receivedSharedProjects = new ArrayList<Project>();
  private DBAccessManager db = DBAccessManager.getTheInstance();

  /**
   * empty for ORMLite and serialization
   */
  protected User() {
  }

  /**
   * Constructor. Should only be called by the UserManagement class via the createUser(String email) method.
   * 
   * @param email
   */
  protected User(final String email) {
    // email must be unique and not empty. Is asserted by UserManagement
    this.userEmail = email;
    this.registrationDate = new Date();
    this.lastLoginDate = new Date();
    this.role = Role.USER;
  }

  /**
   * 
   * @return ROLE
   */
  public Role getRole() {
    return this.role;
  }

  /**
   * This method transelates user roles into int values.
   * If new roles are added this method has to be changed. The
   * method is used to determine the rights of a user in a gui session.
   * 
   * @return int value of user role
   */
  public int getRoleValue() {
	int value;
    switch (this.role) {
    case USER:
      value = 1;
      break;
    case ADMIN:
      value = 3;
      break;
    default:
      value = 0;
      break;
    }
    return value;
  }
  
  	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
  /**
   * @author Lukas Rieger
   * Ensures information hiding. Project must be 
   * instantiate here but doesn't fit in this package.
   * However it's not an optino to give the project class a public
   * consturctor. 
   *
   */
  class ProjectConstructor extends Project{
  	public ProjectConstructor(String projectName) {
  		super(projectName);
  	}
  	
  	public ProjectConstructor(String projectName, User owner) {
  		super(projectName, owner);
  	}
  }

  /**
   * 
   * @param r
   */
  public void setRole(final Role newRole) {
    this.role = newRole;
  }

  /**
   * 
   * @return registrationDate
   */
  public Date getRegistrationDate() {
    return this.registrationDate;
  }

  /**
   * 
   * @return registrationDate as String
   */
  public String getRegistrationDateString() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(registrationDate);
  }

  /**
   * 
   * @param date
   */
  public void setLastLoginDate(final Date date) {
    this.lastLoginDate = date;
  }

  /**
   * Returns the users email as String
   */
  @Override
  public String toString() {
    return getEmail();
  }

  /**
   * 
   * @return lastLoginDate
   */
  public Date getLastLoginDate() {
    return this.lastLoginDate;
  }

  /**
   * 
   * @return lastLoginDate as String
   */
  public String getLastLoginDateString() {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lastLoginDate);
  }
  
  /**
   * 
   * @return encryptedPassword as long value
   */
  public long getPassword() {
    return this.encryptedPassword;
  }

  /**
   * First encrypts then sets the password.
   * 
   * @param pw
   * @return success
   */
  public boolean setPassword(String password) {
	boolean result = true;
    if (password.equals("")) {
      result = false;
    }
    this.encryptedPassword = UserManagement.hash(password);
    return result;
  }

  /**
   * 
   * @return ownedProjects
   */
  public ArrayList<Project> getOwnedProjects() {
    return this.ownedProjects;
  }

  /**
   * 
   * @return receivedSharedProjects
   */
  public ArrayList<Project> getReceivedShares() {
    return this.receivedSharedProjects;
  }

  /**
   * 
   * @return userEmail;
   */
  public String getEmail() {
    return this.userEmail;
  }

  /**
   * Returns the project with the given name from the user's projects. Returns NULL if not found!!!
   * 
   * @param id
   * @return project
   */
  public Project getProject(final int id) {
    for (final Project proj : ownedProjects) {
      if (proj.getId() == id) {
        return proj;
      }
    }
    for (final Project proj : receivedSharedProjects) {
      if (proj.getId() == id) {
        return proj;
      }
    }
    return null;
  }

  /**
   * Returns the project with the given name from the user's projects.
   * Returns NULL if not found!!!
   * 
   * @param projName
   * @return project
   */
  public Project getProject(final String projName) {
    if (!projName.equals("")) {
      for (final Project proj : ownedProjects) {
        if (proj.getProjectName().equals(projName)) {
          return proj;
        }
      }

      for (final Project proj : receivedSharedProjects) {
        if (proj.getProjectName().equals(projName)) {
          return proj;
        }
      }
    }
    return null;
  }
  
  /**
   * Returns the corpus with the given name from the user's projects corpuses. Returns NULL if not found!!!
   * 
   * @param projectID the projects id where the corpus is in
   * @param id the corpus int id
   * @return corpus the corpus found
   */
  public Corpus getCorpus(final int projectID, final int id) {
    for (final Project proj : ownedProjects) {
      if (proj.getId() == projectID) {
        return proj.getCorpus(id);
      }
    }
    for (final Project proj : receivedSharedProjects) {
      if (proj.getId() == projectID) {
        return proj.getCorpus(id);
      }
    }
    return null;
  }

  /**
   * Returns the corpus with the given name from the user's projects corpuses. Returns NULL if not found!!!
   * 
   * @param projectID the projects id where the corpus is in
   * @param corpusName the corpus string name
   * @return corpus the corpus found
   */
  public Corpus getCorpus(final int projectID, final String corpusName) {
	    for (final Project proj : ownedProjects) {
	        if (proj.getId() == projectID) {
	          return proj.getCorpus(corpusName);
	        }
	      }
	      for (final Project proj : receivedSharedProjects) {
	        if (proj.getId() == projectID) {
	          return proj.getCorpus(corpusName);
	        }
	      }
	      return null;
  }

  /**
   * @param projName
   * @return boolean
   * 
   *         This function can be used by the GUI when the user wants
   *         to change a project's name in order to check
   *         whether there is already a project with the new name.
   */
  public boolean projectNameExists(final String projName) {
    final Project project = getProject(projName);
    return project != null;
  }

  /**
   * @param corpusName
   * @return boolean
   * 
   *         This function can be used by the GUI when the user wants
   *         to change a corpus's name in order to check
   *         whether there is already a corpus with the new name.
   */
  public boolean corpusNameExists(final int projectID, final String corpusName) {
    final Corpus corpus = getProject(projectID).getCorpus(corpusName);
    return corpus != null;
  }
  
  /**
   * Adds a project to the list of receivedSharedProjects. It doesn't create any copy or database entries.
   * 
   * @param proj
   * @return success
   */
  public boolean addSharedProject(Project proj) {
    // TODO: there must be some database entry...
    if (proj == null) {
      return false;
    }
    this.receivedSharedProjects.add(proj);
    return true;
  }

  /**
   * Adds a clone of the given project. This means a new object and a new database entry are created.
   * 
   * @param proj
   * @return success
   * @throws Exception
   */
  public boolean addClonedProject(final Project proj) throws Exception {
    if (proj == null) {
      return false;
    }
    final Project newProject = new ProjectConstructor(proj.getProjectName());
    newProject.addClonesOfCorpora(proj.getCorpora());
    db.saveProject(newProject);
    this.ownedProjects.add(newProject);
    return true;
  }

  /**
   * Creates a project for the user and creates a new database entry.
   * The new project is an ownedProject. If the project
   * name exists already or is empty this method return NULL!!!
   * 
   * @param projectName
   * @return new Project
   * @throws Exception
   */
  public Project createProject(String projectName) throws SQLException {
    if (projectNameExists(projectName) || projectName.equals("")) {
      return null;
    }
    final Project newProject = new ProjectConstructor(projectName, this);
    db.saveProject(newProject);
    this.ownedProjects.add(newProject);
    return newProject;
  }
  
  /**
   * Renames a corpus for the user and updates its database entry.
   * If the corpus name already exists or is empty this method returns Null
   * @param projectID the project id where the corpus lies in
   * @param corpusName the corpus name to change
   * @param corpusID the id of the corpus given
   * @return true if save was complete, false if the corpus does not exist or name emtpy
   * @throws SQLException throws a sqlexception if theres a db error
   */
  public Boolean renameCorpus(int projectID, String corpusName, int corpusID) throws SQLException {
	  if (corpusNameExists(projectID, corpusName) || corpusName.equals("")) {
	      return false;
	    }
	 Corpus corpus = getCorpus(projectID, corpusID);
	 corpus.setCorpusName(corpusName);
	 db.saveCorpus(corpus);
	 return true;
  }
  
  /**
   * Renames a project for the user and updates its database entry.
   * If the project name already exists or is empty this method returns Null
   * @param projectName the projects new name
   * @param projectID the id of the given project
   * @throws SQLException throws a sqlexception if theres a db error
   * @return true if save was complete, false if the project does not exist or name emtpy
   */
  public Boolean renameProject(String projectName, int projectID) throws SQLException {
	  if (projectNameExists(projectName) || projectName.equals("")) {
	      return false;
	    }
	 Project project = getProject(projectID);
	 project.setName(projectName);
	 db.saveProject(project);
	 return true;
  }

  /**
   * Deletes the given project from the user and from the database.
   * 
   * @param proj
   * @throws SQLException
   */
  public void deleteProject(final Project proj) throws SQLException {
    db.deleteProject(proj);
    if (this.receivedSharedProjects.contains(proj)) {
      // remove from db -> so far we don't store them in the db.
      // doesn't cause problems with other users' projects?
      this.receivedSharedProjects.remove(proj);
    }
    if (this.ownedProjects.contains(proj)) {
      db.deleteProject(proj);
      this.ownedProjects.remove(proj);
    }
  }

  /**
   * 
   * @param file
   * @return
   */
  public boolean importProject(File file) {
	  // ##2
    return true;
  }

  /**
   * 
   * @param proj
   * @return
   */
  public File exportProject(final Project proj) {
    if (projectBelongsToUser(proj)) {
      // TODO: export into file
    	// ##2
      return new File("testOutputFile");
    }
    throw new InvalidParameterException();
  }

  /**
   * Checks if a given project belongs to this user.
   * That can be a shared project or an owned project.
   * 
   * @param proj
   * @return boolean
   */
  private boolean projectBelongsToUser(final Project proj) {
    return this.receivedSharedProjects.contains(proj) || this.ownedProjects.contains(proj);
  }

  /**
   * This method adds an ArrayList of projects to the ownedProjects. It doesn't create database entries.
   * 
   * @param ownedProjects
   */
  public void setOwnedProjects(final ArrayList<Project> ownedProjects) {
    this.ownedProjects = ownedProjects;
  }

  /**
   * Prints the user's data to the console.
   */
  public void printUserData() {
    System.out.println("UserEmail: " + this.userEmail);
    System.out.println("Projects: ");
    System.out.println("############# owned projects ############");
    for (Project p : ownedProjects) {
      System.out.println("Project: " + p.getProjectName());
      System.out.println("---------- Corpora -------------");
      for (Corpus c : p.getCorpora()) {
        System.out.println("->  " + c.getCorpusName());
        System.out.println("Texts: ");
        for (Text t : c.getTexts()) {
          System.out.println("->>> " + t.getTextName());
          for (Layer l : t.getLayerContainer().getLayers()) {
            System.out.println(l.getClass().toString());
          }
        }
      }
      System.out.println("-------------------------------");
    }
    System.out.println("############# received shared projects ############");
    for (Project p : receivedSharedProjects) {
      System.out.println("Project: " + p.getProjectName());
      System.out.println("---------- Corpora -------------");
      for (Corpus c : p.getCorpora()) {
        System.out.println("->  " + c.getCorpusName());
        System.out.println("Texts: ");
        for (Text t : c.getTexts()) {
          System.out.println("->>> " + t.getTextName());
          for (Layer l : t.getLayerContainer().getLayers()) {
            System.out.println(l.getClass().toString());
          }
        }
      }
      System.out.println("-------------------------------");
    }
  }

  /**
   * returns a user connected to the email or null if no user exist with this email.
   * @param email the users email string
   * @return the user or null if no such user exists
   */
  public User getUser(String email) {
	  return db.getUser(email);
  }
  
  /**
   * @return the userEmail
   */
  public String getUserEmail() {
    return userEmail;
  }

  /**
   * @param userEmail
   *          the userEmail to set
   */
  public void setUserEmail(final String userEmail) {
    this.userEmail = userEmail;
  }

  /**
   * @return the encryptedPassword
   */
  public long getEncryptedPassword() {
    return encryptedPassword;
  }

  /**
   * @param encryptedPassword
   *          the encryptedPassword to set
   */
  public void setEncryptedPassword(final long encryptedPassword) {
    this.encryptedPassword = encryptedPassword;
  }

  /**
   * @return the receivedSharedProjects
   */
  public ArrayList<Project> getReceivedSharedProjects() {
    return receivedSharedProjects;
  }

  /**
   * @param receivedSharedProjects
   *          the receivedSharedProjects to set
   */
  public void setReceivedSharedProjects(final ArrayList<Project> receivedSharedProjects) {
    this.receivedSharedProjects = receivedSharedProjects;
  }

  /**
   * @param registrationDate
   *          the registrationDate to set
   */
  public void setRegistrationDate(final Date registrationDate) {
    this.registrationDate = registrationDate;
  }

/***
 * Clones projects and sets the id of the cloned projects to all users in the list.
 * @param project the project to clone
 * @param obj the users 
 * @param newCloneString the standard name for a new shared project ("shared project digit")
 * @throws SQLException 
 */
public void cloneProject(Project project, List<User> obj, String newCloneString) throws SQLException {
	for (int i = 0; i < obj.size(); i++) {
	db.cloneProjectTo(project, obj.get(i), newCloneString);
	}
	
}
}