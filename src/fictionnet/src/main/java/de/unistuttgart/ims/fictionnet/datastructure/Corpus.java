package de.unistuttgart.ims.fictionnet.datastructure;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;

/**
 * @author Lukas Rieger
 * @version 09-09-15
 * 
 *          This class represents a corpus of a ficitionnet project. It contains text objects. It
 *          can be saved to the database and be serialized.
 *
 */

@DatabaseTable(tableName = "corpus")
public class Corpus {
  @DatabaseField(generatedId = true)
  private int id;
  @DatabaseField
  private String corpusName;
  private ArrayList<Text> texts = new ArrayList<Text>();
  @DatabaseField
  private int projectId;
  private DBAccessManager db = DBAccessManager.getTheInstance();
  private static final long serialVersionUID = 1L;

  // empty constructor for ORMLite and serialization
  protected Corpus() {
  }

  /**
   * Constructor. Should only be called from project.createCorpus(corpusName).
   * 
   * @param name
   */
  protected Corpus(final String name) {
    this.corpusName = name;
  }

  /**
   * 
   * @return String corpusName
   */
  public String getCorpusName() {
    return this.corpusName;
  }

  /**
	 * 
	 */
  @Override
  public String toString() {
    return corpusName;
  }

  /**
   * 
   * @param name
   * @return success
   */
  public boolean setCorpusName(final String name) {
	boolean result = true;
    if (name.equals("")) {
      result = false;
    }
    this.corpusName = name;
    return result;
  }

  /**
   * 
   * @param id
   */
  public void setProjectId(final int id) {
    this.projectId = id;
  }

  /**
   * 
   * @param text
   * @return boolean success
   */
  public boolean addText(final Text text) {
    text.setCorpusId(this.id);
    return this.texts.add(text);
  }

  /**
   * This method creates a new text in the corpus and creates a new database entry for the text.
   * @param name
   * @param author
   * @param publishingDate
   * @param sourceText
   * @return
   * @throws Exception 
   */
  public Text createText(String name, String author, String publishingDate, String sourceText)
      throws SQLException {
	
	name =  makeNameUnique(name);
    Text newText = new Text(name, author, publishingDate, sourceText);
    newText.setCorpusId(this.id);
    db.saveText(newText);
    newText.setLayerContainersTextId();
	db.saveLayerContainer(newText.getLayerContainer());
    this.texts.add(newText);
    return newText;
  }
  
  /**
   * If a text name already exists in this corpus, (Nr) will be added.
   * 
   * @param name
   * @return unique name
   */
	private String makeNameUnique(String name) {
		int i = 0;
		String suffix;
		while (!isNameUnique(name)) {
			suffix = "(" + i + ")";
			if (name.contains(suffix)) {
				name = name.substring(0, name.indexOf(suffix) - 1);
			}
			i++;
			name = name + " (" + i + ")";
		}
		return name;
	}

	/**
	 * Checks if name of text is unique for this corpus.
	 * 
	 * @param name
	 * @return boolean
	 */
	private boolean isNameUnique(String name) {
		boolean result = true;
		for (final Text t : this.texts) {
			if (t.getTextName().equals(name)) {
				result = false;
				break;
			}
		}
		return result;
	}

	/**
	 * 
	 * @return ArrayList<Text>
	 */
	@XmlElement(name = "text2")
	public ArrayList<Text> getTexts() {
		return this.texts;
	}

	/**
	 * This method deletes a text from the corpus and also deletes its database entry.
	 * 
	 * @param text
	 * @return boolean success
	 * @throws Exception
	 */
	public boolean deleteText(final Text text) throws SQLException {
		db.deleteText(text);
		return this.texts.remove(text);
	}

  /**
   * This method deletes a text from the corpus and also deletes its database entry.
   * 
   * @param id
   * @return boolean success
 * @throws Exception 
   */
  public boolean deleteText(final int textId) throws SQLException {
	boolean result = false;
    for (final Text t : texts) {
      if (t.getId() == id) {
        db.deleteText(t);
        result = this.texts.remove(t);
        break;
      }
    }
    return result;
  }

  /**
   * This method deletes a text from the corpus and also deletes its database entry.
   * 
   * @param name
   * @return boolean success
 * @throws Exception 
   */
  public boolean deleteText(final String name) throws SQLException {
	boolean result = false;
    for (final Text t : texts) {
      if (t.getTextName().equals(name)) {
        db.deleteText(t);
        result = this.texts.remove(t);
        break;
      }
    }
    return result;
  }

  /**
   * 
   * @return id
   */
  public int getId() {
    return this.id;
  }

  /**
   * This method adds an ArrayList of text objects to the corpus.
   * It doesn't create any database entries.
   * 
   * @param texts
   */
  public void addTexts(final ArrayList<Text> texts) {
    this.texts.addAll(texts);
  }

  /**
   * This method clones the texts in the given ArrayList.
   * It then adds them to the corpus and
   * creates a new database entry for each one of them.
   * It is needed to clone projects.
   * 
   * @param texts
   * @throws Exception 
   */
  public void addClonesOfTexts(final ArrayList<Text> texts) throws SQLException {
    Text newText;
    String textName;
    String author;
    String publishingDate;
    String sourceText;

    for (final Text currentText : texts) {
      textName = currentText.getTextName();
      author = currentText.getAuthorName();
      publishingDate = currentText.getPublishingDate();
      sourceText = currentText.getSourceText();

      newText = new Text(textName, author, publishingDate, sourceText);
      newText.setCorpusId(this.id);
      newText.setCastList(currentText.getCastList());
      LayerContainer newLayerContainer = currentText.getLayerContainerCopy();
			// copy text specific data
			db.saveText(newText);
			newLayerContainer.setTextId(newText.getId());
			newText.setLayerContainer(newLayerContainer);
			this.texts.add(newText);
		}

	}

	/**
	 * @return the projectId
	 */
	public int getProjectId() {
		return projectId;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param texts
	 *            the texts to set
	 */
	public void setTexts(ArrayList<Text> texts) {
		this.texts = texts;
	}
}
