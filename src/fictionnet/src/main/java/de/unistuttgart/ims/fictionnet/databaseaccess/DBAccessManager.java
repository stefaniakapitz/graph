package de.unistuttgart.ims.fictionnet.databaseaccess;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.j256.ormlite.jdbc.JdbcPooledConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.LayerContainer;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.ActLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.EventLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.InteractionLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.Layer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.PresenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SceneLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SentenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SpeakerLayer;
//import de.unistuttgart.ims.fictionnet.datastructure.layers.Mention;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TalksAboutLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TokenLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TypeOfTextLayer;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.ActWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.EventWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.InteractionWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.PresenceWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.SceneWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.SectionWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.SentenceWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.SpeakerWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.TalksAboutInstanceWrapper;
import de.unistuttgart.ims.fictionnet.datastructure.wrappers.TokenWrapper;
import de.unistuttgart.ims.fictionnet.users.User;

/**
 * @author Lukas Rieger, Domas Mikalkinas, Erik-Felix Tinsel
 * @version 27-10-15
 * 
 *          This class is the interface to the database. It offers all the
 *          methods to interact with the database such as delete, load and save
 *          methods for users, projects, corpora, texts, etc... It is
 *          implemented as singleton. It uses a connection pool for all the
 *          connections to the database wich permits reuse and avoids pending
 *          open connections. An example of a connection string is the
 *          following:
 *          "jdbc:mariadb://localhost:3306/fictionnet?user=root&Password=root";
 */
public final class DBAccessManager {
	private static DBAccessManager theinstance = null;
	private ConnectionSource connectionSource;
	private DataTableProject dataTableProject;
	private DataTableCorpus dataTableCorpus;
	private DataTableText dataTableText;
	private DataTableUsers dataTableUser;
	private DataTableLayercontainer dataTableLayerContainer;
	private HashMap<Text, Integer[]> progress;

	// Private for Singleton-Pattern.
	private DBAccessManager() {
		dataTableProject = new DataTableProject();
		dataTableCorpus = new DataTableCorpus();
		dataTableText = new DataTableText();
		dataTableUser = new DataTableUsers();
		dataTableLayerContainer = new DataTableLayercontainer();
		progress = new HashMap<Text, Integer[]>();
	}

	/**
	 * Through this function we make sure that there is always only one instance
	 * of the class. It realizes the Singleton-Pattern.
	 * 
	 * @return DBAccessManager instance
	 */
	public synchronized static DBAccessManager getTheInstance() {
		if (theinstance == null) {
			theinstance = new DBAccessManager();
		}
		return theinstance;
	}

	/**
	 * Creates a new connection pool to the database indicated in the
	 * connectionString. The connectionString contains furthermore the DB user
	 * and the password. Example:
	 * "jdbc:mariadb://localhost:3306/fictionnet?user=root&password=root"
	 * 
	 * @param connectionString
	 * @throws SQLException
	 */
	public void connect(final String connectionString) throws SQLException {
		if (new MariaDBType().isDatabaseUrlThisType(connectionString)) {
			connectionSource = new JdbcPooledConnectionSource(connectionString,
					new MariaDBType());
		} else {
			connectionSource = new JdbcPooledConnectionSource(connectionString);
		}
		dataTableProject.setConnectionSource(connectionSource);
		dataTableCorpus.setConnectionSource(connectionSource);
		dataTableText.setConnectionSource(connectionSource);
		dataTableLayerContainer.setConnectionSource(connectionSource);
		dataTableUser.setConnectionSource(connectionSource);
		createTablesIfNotExists();
	}

	/**
	 * If a connection is established this function closes it.
	 * 
	 * @throws SQLException
	 */
	public void disconnect() throws SQLException {
		if (this.connectionSource.isOpen()) {
			this.connectionSource.close();
		}
	}

	/**
	 * Checks if a connection source exists and if it is open.
	 * 
	 * @return boolean
	 */
	public boolean isConnected() {
		return (this.connectionSource != null)
				&& (this.connectionSource.isOpen());
	}

	/**
	 * Get the connection source
	 * 
	 * @return connectionSource
	 */
	public ConnectionSource getConnectionSource() {
		return this.connectionSource;
	}

	/**
	 * Returns a list with all the users currently existing in the system with
	 * all their data. Careful: could be slow because of all the data!
	 * 
	 * @return ArrayList of all user with data
	 * @throws Exception
	 */
	public ArrayList<User> getAllUsersWithData() throws SQLException {
		final ArrayList<User> allUsers = (ArrayList<User>) dataTableUser.dao
				.queryForAll();
		for (final User user : allUsers) {
			loadProjectsOfUser(user);
		}
		return allUsers;
	}

	/**
	 * Returns an iterator for the user table. The users are returned without
	 * their project data.
	 * 
	 * @return useriterator
	 * @throws SQLException
	 */
	public Iterator<User> iterator() throws SQLException {
		final ArrayList<User> allUsers = (ArrayList<User>) dataTableUser.dao
				.queryForAll();
		return allUsers.iterator();
	}

	/**
	 * This function sets the lastLoginDate attribute of the user in the
	 * database to the indicated date.
	 * 
	 * @param user
	 *            that logged in
	 * @param date
	 *            of last login
	 * @throws SQLException
	 */
	public void setLastLogin(final User user, final Date date)
			throws SQLException {
		user.setLastLoginDate(date);
		dataTableUser.createOrUpdate(user);
	}

	/**
	 * Returns the user with the given email. The user object has no project
	 * data.
	 * 
	 * @param email
	 * @return user
	 */
	public User getUser(final String email) {
		try {
			return dataTableUser.queryForId(email);
		} catch (SQLException e) {
			System.out.println("Invalid email address in getUser() method.");
		}
		return null;
	}

	/**
	 * This method saves a clone of the given project to the database to which
	 * the instance of the DBAccessManager class is connected and returns the id
	 * that the database gave to the database entry of the project in order to
	 * save the id in the object.
	 * 
	 * @param project
	 *            to save to database
	 * @return database id of the project
	 * @throws Exception
	 */
	public int cloneProjectTo(final Project project, final User owner,
			String newCloneString) throws SQLException {
		setNewProjectName(project, owner, newCloneString);
		project.setOwner(owner);
		dataTableProject.create(project);
		if (project.getCorpora() != null) {
			for (final Corpus corpus : project.getCorpora()) {
				corpus.setProjectId(project.getId());
				cloneCorpus(corpus);
			}
		}
		return project.getId();
	}

	private void cloneCorpus(final Corpus corpus) throws SQLException {
		dataTableCorpus.create(corpus);
		if (corpus.getTexts() != null) {
			for (final Text text : corpus.getTexts()) {
				text.setCorpusId(corpus.getId());
				cloneText(text);
			}

		}
	}

	private void cloneText(final Text text) throws SQLException {
		dataTableText.create(text);
		if (text.getLayerContainer() != null) {
			text.getLayerContainer().setTextId(text.getId());
			dataTableLayerContainer.create(text.getLayerContainer(), text);
		}

		// TODO: save visualizations
	}

	/**
	 * This method saves a given project to the database to which the instance
	 * of the DBAccessManager class is connected and returns the id that the
	 * database gave to the database entry of the project in order to save the
	 * id in the object.
	 * 
	 * @param project
	 *            to save to database
	 * @return database id of the project
	 * @throws Exception
	 */
	public int saveProject(final Project project) throws SQLException {
		dataTableProject.createOrUpdate(project);
		if (project.getCorpora() != null) {
			for (final Corpus corpus : project.getCorpora()) {
				saveCorpus(corpus);
			}
		}
		return project.getId();
	}

	/**
	 * This method saves a given corpus to the database to which the instance of
	 * the DBAccessManager class is connected and returns the id that the
	 * database gave to the database entry of the corpus in order to save the id
	 * in the object.
	 * 
	 * @param corpus
	 *            to save to database
	 * @return dabase id of the corpus
	 * @throws Exception
	 */
	public int saveCorpus(final Corpus corpus) throws SQLException {
		dataTableCorpus.createOrUpdate(corpus);
		if (corpus.getTexts() != null) {
			for (final Text text : corpus.getTexts()) {
				saveText(text);
			}

		}
		return corpus.getId();
	}

	/**
	 * This method saves a given text to the database to which the instance of
	 * the DBAccessManager class is connected and returns the id that the
	 * database gave to the database entry of the text in order to save the id
	 * in the object.
	 * 
	 * @param text
	 *            to save to database
	 * @return database id of the text
	 * @throws Exception
	 */
	public int saveText(final Text text) throws SQLException {
		Logger.getLogger(getClass().getName()).log(Level.INFO,
				"Save text in database:");

		dataTableText.createOrUpdate(text);
		increaseProgress(text);

		Logger.getLogger(getClass().getName()).log(Level.INFO, "Done.");
		Logger.getLogger(getClass().getName()).log(Level.INFO,
				"Save layers of " + text.toString() + " in database:");

		if (text.getLayerContainer() != null) {
			dataTableLayerContainer.createOrUpdate(text.getLayerContainer(),
					text);
		}

		Logger.getLogger(getClass().getName()).log(Level.INFO, "Done.");
		// TODO: save visualizations
		return text.getId();
	}
	
	/**
	 * This method updates a text object that already exists in the 
	 * database. This method is only used for Text.rebuild().
	 * 
	 * @param text
	 * @throws SQLException
	 */
	public void updateText(final Text text) throws SQLException {
		dataTableText.update(text);
		if(text.getLayerContainer() != null) {
			dataTableLayerContainer.update(text.getLayerContainer());
		}
	}

	/**
	 * Increses the progress steps by one.
	 * 
	 * @param text
	 *            - Text, to which the progess belongs.
	 */
	protected void increaseProgress(Text text) {
		Integer[] step = progress.get(text);

		if (step == null || step[1] == 1) {
			final LayerContainer container = text.getLayerContainer();

			int maxSteps = 1;
			if (container.getActLayer() != null) {
				maxSteps += container.getActLayer().getActs().size();
			}
			if (container.getEventLayer() != null) {
				maxSteps += container.getEventLayer().getEvents().size();
			}
			if (container.getInteractionLayer() != null) {
				maxSteps += container.getInteractionLayer().getInteractions()
						.size();
			}
			if (container.getTalksAboutLayer() != null) {
				maxSteps += container.getTalksAboutLayer().getMentions().size();
			}
			if (container.getPresenceLayer() != null) {
				maxSteps += container.getPresenceLayer().getPresences().size();
			}
			if (container.getSceneLayer() != null) {
				maxSteps += container.getSceneLayer().getScenes().size();
			}
			if (container.getSentenceLayer() != null) {
				maxSteps += container.getSentenceLayer().getSentences().size();
			}
			if (container.getTokenLayer() != null) {
				maxSteps += container.getTokenLayer().getTokens().size();
			}
			if (container.getTypeOfTextLayer() != null) {
				maxSteps += container.getTypeOfTextLayer().getSections().size();
			}
			if (container.getSpeakerLayer() != null) {
				maxSteps += container.getSpeakerLayer().getSpeakers().size();
			}

			step = new Integer[] { 1, maxSteps };
			progress.put(text, step);
		} else {
			step[0]++;
			progress.put(text, step);
		}
	}

	/**
	 * Gets the progress in steps.
	 * 
	 * @param text
	 *            - Text, to which the progress shall be checked.
	 * @return {@link Integer}
	 */
	public Integer[] getProgress(Text text) {
		return progress.get(text);
	}

	/**
	 * This method saves a given layerContainer to the database and returns its
	 * id.
	 * 
	 * @param layerContainer
	 * @return
	 * @throws SQLException
	 */
	public int saveLayerContainer(final LayerContainer layerContainer)
			throws SQLException {
		dataTableLayerContainer.createOrUpdate(layerContainer);
		return layerContainer.getId();
	}

	/**
	 * This method saves the data of the given user to the database. It doesn't
	 * save projects of the user, it only refers to the datatable "users".
	 * 
	 * @param user
	 *            to save
	 * @throws SQLException
	 */
	public void saveUser(final User user) throws SQLException {
		dataTableUser.createOrUpdate(user);
	}

	/**
	 * This method saves all the data of the given user. It includes all project
	 * data too.
	 * 
	 * @param user
	 *            to save to database
	 * @throws Exception
	 */
	public void saveAllUserData(final User user) throws SQLException {
		// TODO: ArrayList<Project> usersReceivedShares =
		// user.getReceivedShares();

		dataTableUser.createOrUpdate(user);
		if (user.getOwnedProjects() != null) {
			for (final Project project : user.getOwnedProjects()) {
				saveProject(project);
			}
		}
	}

	/**
	 * This function loads a user with all the data belonging to the user. This
	 * comprises projects, corpora, texts, text specific data and the user.
	 * 
	 * @param email
	 *            of user to load
	 * @return user
	 * @throws Exception
	 */
	public User loadUserWithData(final String email) throws SQLException {
		User user = null;
		final String lowerEmail = email.toLowerCase();
		user = dataTableUser.queryForId(lowerEmail);
		// load projects, corpora, texts, text specific data
		// and add it to the user
		if (user != null) {
			loadProjectsOfUser(user);
		}
		return user;
	}

	/**
	 * This method loads the project of the indicated id from the database.
	 * Could be used for lazy loading if suddenly all the data of a project is
	 * needed.
	 * 
	 * @param id
	 *            of project
	 * @return project of indicated id
	 * @throws Exception
	 */
	public User loadProjectsOfUser(final User user) throws SQLException {
		final String columnName = DBTableColumns.OWNEREMAIL.toString();
		final String userEmail = user.getEmail();
		// get all the projects of the user from the db and load their content
		final ArrayList<Project> projects = (ArrayList<Project>) dataTableProject
				.selectFromThisAllWhere().eq(columnName, userEmail).query();
		for (final Project project : projects) {
			project.setCorpora(loadCorporaOfProject(project.getId()));
			for (final Corpus corpus : project.getCorpora()) {
				corpus.setTexts(loadTextsOfCorpus(corpus.getId()));
				for (final Text text : corpus.getTexts()) {
					loadTextContent(text);
				}
			}
		}
		user.setOwnedProjects(projects);
		return user;
	}

	/**
	 * Loads all the corpora belonging to the project with the indicated id.
	 * 
	 * @param projectId
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Corpus> loadCorporaOfProject(final int projectId)
			throws SQLException {
		final String columnName = DBTableColumns.PROJECTID.toString();
		return (ArrayList<Corpus>) dataTableCorpus.selectFromThisAllWhere()
				.eq(columnName, projectId).query();
	}

	/**
	 * Loads all the texts belonging to the corpus with the indicated id.
	 * 
	 * @param corpusId
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<Text> loadTextsOfCorpus(final int corpusId)
			throws SQLException {
		final String columnName = DBTableColumns.CORPUSID.toString();
		return (ArrayList<Text>) dataTableText.selectFromThisAllWhere()
				.eq(columnName, corpusId).query();
	}

	/**
	 * Loads all the layers and visualizations belonging to the given text
	 * object.
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public Text loadTextContent(final Text text) throws SQLException {
		// TODO: add visualizations
		dataTableLayerContainer.loadLayerContainer(text);
		return text;
	}

	/**
	 * This method checks if the email adress already exists in the database
	 * 
	 * @param email
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean checkIfEmailExists(final String email) throws SQLException {
		return dataTableUser.checkIfEmailExists(email);
	}

	/**
	 * This method deletes a layer and its content from the database.
	 * 
	 * @param layer
	 * @throws SQLException
	 */
	public void deleteLayer(final Layer layer) throws SQLException {
		dataTableLayerContainer.deleteLayer(layer);
	}

	/**
	 * This method deletes in the database the project with the given name from
	 * the indicated user.
	 * 
	 * @param ownerEmail
	 * @param projectName
	 * @throws Exception
	 */
	public void deleteProject(Project project) throws SQLException {
		final ArrayList<Corpus> corpora = project.getCorpora();
		for (final Corpus corpus : corpora) {
			deleteCorpus(corpus);
		}
		dataTableProject.delete(project);
	}

	/**
	 * Deletes the corpus with its content from the database.
	 * 
	 * @param corpus
	 * @throws SQLException
	 */
	public void deleteCorpus(Corpus corpus) throws SQLException {
		for (final Text text : corpus.getTexts()) {
			deleteText(text);
		}
		dataTableCorpus.delete(corpus);
	}

	/**
	 * Deletes the text with its content from the database.
	 * 
	 * @param text
	 * @throws SQLException
	 */
	public void deleteText(Text text) throws SQLException {
		deleteVisualizations(text);
		deleteLayerContainer(text);
		dataTableText.delete(text);
	}

	/**
	 * Deletes the layerContainer in the text. It is deleted with all its
	 * database entries.
	 * 
	 * @param text
	 * @throws Exception
	 */
	private void deleteLayerContainer(final Text text) throws SQLException {
		final LayerContainer layerContainer = text.getLayerContainer();
		dataTableLayerContainer.delete(layerContainer);
	}

	/**
	 * Deletes all the visualizations contained in the text object from the
	 * database.
	 * 
	 * @param text
	 */
	public void deleteVisualizations(final Text text) {
		// not implemented yet.
		// delete from database
		// delete from object
	}

	/**
	 * Deletes the user and its data from the database.
	 * 
	 * @param user
	 * @throws SQLException
	 */
	public void deleteUser(User user) throws SQLException {
		final ArrayList<Project> projects = user.getOwnedProjects();
		// user.getReceivedSharedProjects()
		for (final Project project : projects) {
			deleteProject(project);
		}
		dataTableUser.delete(user);
	}

	/**
	 * This method creates all the necessary tables in the database.
	 * 
	 * @throws SQLException
	 */
	private void createTablesIfNotExists() throws SQLException {
		TableUtils.createTableIfNotExists(connectionSource, User.class);
		TableUtils.createTableIfNotExists(connectionSource, Project.class);
		TableUtils.createTableIfNotExists(connectionSource, Corpus.class);
		TableUtils.createTableIfNotExists(connectionSource, Text.class);
		TableUtils.createTableIfNotExists(connectionSource,
				LayerContainer.class);
		TableUtils.createTableIfNotExists(connectionSource,
				TypeOfTextLayer.class);
		TableUtils.createTableIfNotExists(connectionSource, TokenLayer.class);
		TableUtils
				.createTableIfNotExists(connectionSource, SentenceLayer.class);
		TableUtils
				.createTableIfNotExists(connectionSource, PresenceLayer.class);
		TableUtils.createTableIfNotExists(connectionSource,
				TalksAboutLayer.class);
		TableUtils.createTableIfNotExists(connectionSource,
				InteractionLayer.class);
		TableUtils.createTableIfNotExists(connectionSource, EventLayer.class);
		TableUtils.createTableIfNotExists(connectionSource, ActLayer.class);
		TableUtils.createTableIfNotExists(connectionSource, SceneLayer.class);
		TableUtils.createTableIfNotExists(connectionSource, SpeakerLayer.class);

		// TableUtils.createTableIfNotExists(connectionSource, Mention.class);
		TableUtils.createTableIfNotExists(connectionSource, ActWrapper.class);
		TableUtils.createTableIfNotExists(connectionSource, EventWrapper.class);
		TableUtils.createTableIfNotExists(connectionSource,
				InteractionWrapper.class);
		TableUtils.createTableIfNotExists(connectionSource,
				TalksAboutInstanceWrapper.class);
		TableUtils.createTableIfNotExists(connectionSource,
				PresenceWrapper.class);
		TableUtils.createTableIfNotExists(connectionSource,
				SentenceWrapper.class);
		TableUtils.createTableIfNotExists(connectionSource, TokenWrapper.class);
		TableUtils.createTableIfNotExists(connectionSource,
				SectionWrapper.class);
		TableUtils.createTableIfNotExists(connectionSource, SceneWrapper.class);
		TableUtils.createTableIfNotExists(connectionSource,
				SpeakerWrapper.class);

	}

	/**
	 * only for testing
	 * 
	 * @return
	 */
	public DataTableUsers getDataTableUsers() {
		return this.dataTableUser;
	}

	/**
	 * returns the max number needed to save the unique project name with the
	 * format "new shared project digit".
	 * 
	 * @param project
	 *            the given project the user wants to share
	 * @param owner
	 *            the owner the shared project is going to
	 * @param newCloneString
	 *            the getLocal of a new shared project
	 * @return return the max number needed to form a unique project name
	 */
	public void setNewProjectName(Project project, User owner,
			String newCloneString) {
		try {
			owner = loadProjectsOfUser(owner);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int maxNumber = 0;
		int currNumber = 0;

		while (owner.getProject(project.getProjectName()) != null) {
			if (currNumber == 0) {
				project.setProjectName(newCloneString + " 1");
				currNumber += 1;
			} else {
				currNumber = Integer.parseInt(project.getProjectName()
						.replaceAll("\\D+", ""));

				if (currNumber > maxNumber) {
					maxNumber = currNumber;

				}

				project.setProjectName(newCloneString + " " + (maxNumber + 1));
			}
		}

		maxNumber = 0;
		currNumber = 0;

	}

	/**
	 * only for testing
	 * 
	 * @return
	 */
	public DataTableProject getDataTableProject() {
		return this.dataTableProject;
	}

	/**
	 * only for testing
	 * 
	 * @return
	 */
	public DataTableLayercontainer getDataTableLayercontainer() {
		return this.dataTableLayerContainer;
	}
}
