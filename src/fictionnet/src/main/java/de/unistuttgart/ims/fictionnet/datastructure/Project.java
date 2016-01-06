package de.unistuttgart.ims.fictionnet.datastructure;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.users.User;

/**
 * @author Lukas Rieger
 * @version 17-10-15
 * 
 *          The project class is a container that contains copora with texts and
 *          visualizations. A project always belongs to a user. Corpora can
 *          never exist without a project where they belong to.
 *
 */
@XmlRootElement 
@DatabaseTable(tableName = "projects")
public class Project {
	@DatabaseField(generatedId = true)
	private int id;
	@DatabaseField
	private String projectName;
	private User owner;
	@DatabaseField
	private String ownerEmail;
	@DatabaseField
	private Date lastChangeDate;
	@DatabaseField
	private Date creationDate;
	@DatabaseField
	private boolean isLocked;
	private Set<User> sharedWith;
	private ArrayList<Corpus> corpora = new ArrayList<Corpus>();
	private DBAccessManager db = DBAccessManager.getTheInstance();
	
	private static final long serialVersionUID = 1L;

	/**
	 * empty for serialization
	 */
	protected Project() {}

	/**
	 * Constructor for project object. Should only be called from the
	 * createProject() function.
	 * 
	 * @param projectName
	 */
	protected Project(final String projectName) {
		this.projectName = projectName;
		this.sharedWith = new TreeSet<User>();
		this.creationDate = new Date();
		this.lastChangeDate = new Date();
		this.isLocked = false;
		this.id = 0;
	}

	/**
	 * Constructor: don't use this independently. Create projects via
	 * user.createProject(projectName)
	 * 
	 * @param projectName
	 * @param owner
	 */
	protected Project(final String projectName, final User owner) {
		this.projectName = projectName;
		this.sharedWith = new TreeSet<User>();
		this.corpora = new ArrayList<Corpus>();
		this.creationDate = new Date();
		this.lastChangeDate = new Date();
		this.isLocked = false;
		this.owner = owner;
		this.ownerEmail = owner.getEmail();
		this.id = 0;
	}

	/**
	 * 
	 * @param owner
	 */
	public void setOwner(final User owner) {
		this.owner = owner;
		this.ownerEmail = owner.getEmail();
	}

	/**
	 * 
	 * @return user owning the project
	 */
	@XmlTransient
	public User getOwner() {
		return this.owner;
	}

	/**
	 * 
	 * @return user's email
	 */
	public String getOwnerEmail() {
		return this.ownerEmail;
	}

	/**
	 * 
	 * @return projectName
	 */
	public String getProjectName() {
		return this.projectName;
	}

	/**
	 * 
	 * @param name
	 */
	public void setName(final String name) {
		if (!name.equals("")) {
			this.projectName = name;
		}
	}

	/**
	 * 
	 * @return id of project
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * 
	 * @return returns the creation date as a formated string
	 */
	public String getCreationDateString() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(creationDate);
	}

	/**
	 * Locks and unlocks the project. This is a boolean variable switch.
	 */
	public void lockProject() {
		if (isLocked) {
			this.isLocked = false;
		} else {
			this.isLocked = true;
		}
	}

	/**
	 * 
	 * @return corpora of project
	 */
	@XmlElement(name= "corpora")
	public ArrayList<Corpus> getCorpora() {
		return corpora;
	}

	/**
	 * Adds a corpus to the project object. This doesn't use the database.
	 * 
	 * @param corpus
	 * @return boolean
	 * @throws SQLException
	 */
	public boolean addCorpus(final Corpus corpus) throws SQLException {
		return this.corpora.add(corpus);
	}

	/**
	 * Adds an ArrayList of corpora to the project. It doesn't use the database.
	 * 
	 * @param corpora
	 * @return
	 * @throws SQLException
	 */
	public boolean addCorpora(final ArrayList<Corpus> corpora) {
		return this.corpora.addAll(corpora);
	}

	/**
	 * This method adds an ArrayList of corpora to the project. It creates
	 * clones and creates a new database entry for each of them.
	 * 
	 * @param corpora
	 * @return
	 * @throws Exception
	 */
	public boolean addClonesOfCorpora(final ArrayList<Corpus> corpora)
			throws Exception {
		Corpus newCorpus;
		for (final Corpus currentCorpus : corpora) {
			newCorpus = new Corpus(currentCorpus.getCorpusName());
			newCorpus.setProjectId(this.id);
			newCorpus.addClonesOfTexts(currentCorpus.getTexts());
			db.saveCorpus(newCorpus);
		}
		return true;
	}

	/**
	 * This method creates a new corpus object in this project. It also creates
	 * a new database entry for the corpus. Returns NULL if corpusName is not
	 * unique!
	 * 
	 * @param corpusName
	 * @return corpus created
	 * @throws Exception
	 */
	public Corpus createCorpus(final String corpusName) throws SQLException {
		if (isUniqueCorpusName(corpusName)) {
			final Corpus newCorpus = new Corpus(corpusName);
			newCorpus.setProjectId(this.id);
			db.saveCorpus(newCorpus);
			this.corpora.add(newCorpus);
			return newCorpus;
		}
		return null;
	}

	/**
	 * This method checks if the corpusName already exists in this project.
	 * 
	 * @param corpusName
	 * @return boolean
	 */
	private boolean isUniqueCorpusName(final String corpusName) {
		if (this.corpora == null || this.corpora.size() == 0) {
			return true;
		}
		for (Corpus currentCorpus : this.corpora) {
			if (currentCorpus.getCorpusName().equals(corpusName)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Deletes a corpus from the project. It also deletes the corpus's database
	 * entry.
	 * 
	 * @param corpus
	 * @return success
	 * @throws SQLException
	 */
	public boolean deleteCorpus(final Corpus corpus) throws SQLException {
		db.deleteCorpus(corpus);
		return this.corpora.remove(corpus);
	}

	/**
	 * Deletes a corpus from the project. It also deletes the corpus's database
	 * entry.
	 * 
	 * @param corpus
	 * @return success
	 * @throws SQLException
	 */
	public boolean deleteCorpus(final int id) throws Exception {
		for (final Corpus c : corpora) {
			if (c.getId() == id) {
				db.deleteCorpus(c);
				return this.corpora.remove(c);
			}
		}
		return false;
	}

	/**
	 * 
	 * @param name
	 * @return corpus
	 */
	public Corpus getCorpus(final String name) {
		for (final Corpus c : this.corpora) {
			if (c.getCorpusName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	/**
	 * 
	 */
	@Override
	public String toString() {
		return projectName;
	}

	/**
	 * 
	 * @param id
	 * @return corpus
	 */
	public Corpus getCorpus(final int id) {
		for (final Corpus c : this.corpora) {
			if (c.getId() == id) {
				return c;
			}
		}
		return null;
	}

	/**
	 * This method sets the database connection. Is used for serialization.
	 * 
	 * @param db
	 */
	public void refreshDatabaseAccess() {
		this.db = DBAccessManager.getTheInstance();
	}

	/**
	 * @return the lastChangeDate
	 */
	public Date getLastChangeDate() {
		return lastChangeDate;
	}

	/**
	 * @param lastChangeDate
	 *            the lastChangeDate to set
	 */
	public void setLastChangeDate(final Date lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}

	/**
	 * @return the creationDate
	 */
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            the creationDate to set
	 */
	public void setCreationDate(final Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return the isLocked
	 */
	public boolean isLocked() {
		return isLocked;
	}

	/**
	 * @param isLocked
	 *            the isLocked to set
	 */
	public void setLocked(final boolean isLocked) {
		this.isLocked = isLocked;
	}

	/**
	 * @return the sharedWith
	 */
	@XmlTransient
	public Set<User> getSharedWith() {
		return sharedWith;
	}

	/**
	 * @param sharedWith
	 *            the sharedWith to set
	 */
	public void setSharedWith(final Set<User> sharedWith) {
		this.sharedWith = sharedWith;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(final int id) {
		this.id = id;
	}

	/**
	 * @param projectName
	 *            the projectName to set
	 */
	public void setProjectName(final String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @param ownerEmail
	 *            the ownerEmail to set
	 */
	public void setOwnerEmail(final String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	/**
	 * @param corpora
	 *            the corpora to set
	 */
	public void setCorpora(final ArrayList<Corpus> corpora) {
		this.corpora = corpora;
	}

}
