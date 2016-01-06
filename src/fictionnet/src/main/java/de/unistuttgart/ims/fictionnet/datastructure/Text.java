package de.unistuttgart.ims.fictionnet.datastructure;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.annotation.XmlElement;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.layers.PresenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TalksAboutLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.SectionWithType;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Sentence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Token;
import de.unistuttgart.ims.fictionnet.gui.util.Path;
import de.unistuttgart.ims.fictionnet.imp.analyzer.PresencesAnalyzer;
import de.unistuttgart.ims.fictionnet.imp.analyzer.TalksAboutAnalyzer;

/**
 * @author Lukas Rieger, Roman Stercken
 * @version 19-09-15
 * 
 *          The Text class models a text with all its attributes. The data for analysis is contained in the layer
 *          container object. The results of the analysis are contained in the visualizations array. The class must be
 *          serializable so it can be exported with a project.
 * 
 *          Example for synonyms:
 * 
 *          <pre>
 *         castMember   | synonyms
 *         ---------------------------------------------------- 
 *         Paul         | DER RITTER, Paul, DER BAUER
 *         ----------------------------------------------------
 *         Macbeth      | MACBETH, MAC, bitch
 * </pre>
 */
@DatabaseTable(tableName = "texts")
public class Text {
	private static final String NEW_LINE = "<br>";
	private static final String TEXT_CAPTION_FORMAT_CLOSE = "</u></b></h1>";
	private static final String TEXT_CAPTION_FORMAT = "<h1><b><u>";
	@DatabaseField(generatedId = true)
	private transient int id;
	@DatabaseField
	private transient int corpusId;
	@DatabaseField
	private String textName;
	@DatabaseField
	private String authorName;
	@DatabaseField
	private String publishingDate;
	@DatabaseField(columnDefinition = "MEDIUMTEXT")
	private String sourceText;
	@DatabaseField
	private int numberOfChars;
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private HashMap<String, ArrayList<Synonym>> synonyms;
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private HashSet<String> castList = new HashSet<String>();
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private final HashMap<String, int[]> notIdentifiedEntitie = new HashMap<String, int[]>();
	private LayerContainer layerContainer;

	@DatabaseField
	private int numberOfActs;
	@DatabaseField
	private int numberOfScenes;

	/**
	 * empty constructor for serialization
	 */
	protected Text() {
	}

	/**
	 * This constructor should not be called independently. Text objects should be created via corpus.createText(String
	 * textName)
	 * 
	 * @param name
	 * @param author
	 * @param publishingDate
	 * @param sourceText
	 */
	protected Text(String name, String author, String publishingDate, String sourceText) {
		if ("".equals(name)) {
			name = "unnamed_" + new Date().toString();
		}
		this.textName = name;
		this.authorName = author;
		this.publishingDate = publishingDate;
		this.sourceText = sourceText;
		this.layerContainer = new LayerContainer();
		synonyms = new HashMap<String, ArrayList<Synonym>>();
	}

	/**
	 * This method is supposed to be called after the user manually corrected the synonyms.
	 * 
	 * @throws SQLException
	 *             if something goes wrong
	 */
	public void rebuild() throws SQLException {
		final List<Sentence> sentences = layerContainer.getSentenceLayer().getSentences();
		final List<Token> tokens = layerContainer.getTokenLayer().getTokens();
		final List<SectionWithType> sections = layerContainer.getTypeOfTextLayer().getSections();

		final PresencesAnalyzer presencesAnalyzer = new PresencesAnalyzer(this);
		presencesAnalyzer.analyze();
		layerContainer.setLayer(new PresenceLayer(presencesAnalyzer.getResults()));

		final TalksAboutAnalyzer talksAboutAnalyzer = new TalksAboutAnalyzer(this);
		talksAboutAnalyzer.analyze();
		layerContainer.setLayer(new TalksAboutLayer(talksAboutAnalyzer.getResults()));

		DBAccessManager.getTheInstance().updateText(this);
	}

	/**
	 * Sets the id of the layercontainer in this text.
	 */
	protected void setLayerContainersTextId() {
		this.layerContainer.setTextId(this.id);
	}

	/**
	 * This method adds a synonym to the set of synonyms. It avoids emtpy entries.
	 *
	 * @param name
	 * @param synonym
	 * @return
	 */
	public void addSynonym(String castListName, final Synonym synonym) {
		ArrayList<Synonym> synonymsOfCastMember;
		if (synonyms.keySet().contains(castListName)) {
			synonymsOfCastMember = synonyms.get(castListName);
		} else {
			synonymsOfCastMember = new ArrayList<Synonym>();
		}
		synonymsOfCastMember.add(synonym);
		synonyms.put(castListName, synonymsOfCastMember);
	}

	/**
	 * This method deletes a synonym from the set of synonyms.
	 * 
	 * @param key
	 * @param castListMember
	 * @return
	 */
	public boolean deleteSynonym(String castListMember, final Synonym synonym) {
		if (synonyms.containsKey(castListMember)) {
			synonyms.get(castListMember).remove(synonym);
			return true;
		}
		return false;
	}

	/**
	 * This method returns an ArrayList<String> containing all the known synonyms for the indicated person.
	 *
	 * @param personsName
	 * @return synonyms as ArrayList<String>
	 */
	public ArrayList<String> getStringSynonymsFor(String personsName) {
		final ArrayList<String> castListMembersSynonyms = new ArrayList<String>();
		if (synonyms.containsKey(personsName)) {
			for (final Synonym synonym : synonyms.get(personsName)) {
				castListMembersSynonyms.add(synonym.getName());
			}
		}
		return castListMembersSynonyms;
	}

	/**
	 * This method returns an ArrayList<Synonym> containing all the known synonyms for the indicated person.
	 *
	 * @param personsName
	 * @return synonyms as ArrayList<Synonym>
	 */
	public ArrayList<Synonym> getSynonymsFor(String personsName) {
		if (synonyms.containsKey(personsName)) {
			return synonyms.get(personsName);
		}
		return new ArrayList<Synonym>();
	}

	/**
	 * Returns all the cast members that are referred to by the given synonym.
	 *
	 * @param synonym
	 * @return List<String> castMembers
	 */
	public List<String> getCastMembersForSynonym(final String synonym) {
		final ArrayList<String> results = new ArrayList<String>();
		for (final Entry entry : synonyms.entrySet()) {
			final ArrayList<Synonym> s = (ArrayList<Synonym>) entry.getValue();
			for (final Synonym syno : s) {
				if (syno.getName().equalsIgnoreCase(synonym)) {
					results.add((String) entry.getKey());
				}
			}
		}
		return results;
	}

	/**
	 * Sets id of corpus to which the text belongs.
	 * 
	 * @param id
	 */
	public void setCorpusId(int id) {
		this.corpusId = id;
	}

	/**
	 * 
	 * @return textName
	 */
	public String getTextName() {
		return textName;
	}

	/**
	 * 
	 * @param textName
	 */
	public void setTextName(String textName) {
		this.textName = textName;
	}

	/**
	 * 
	 * @return authorName
	 */
	public String getAuthorName() {
		return authorName;
	}

	/**
	 * 
	 * @param authorName
	 */
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	/**
	 * 
	 * @return publishingDate
	 */
	public String getPublishingDate() {
		return publishingDate;
	}

	/**
	 * 
	 * @param publishingDate
	 */
	public void setPublishingDate(String publishingDate) {
		this.publishingDate = publishingDate;
	}

	/**
	 * 
	 * @return sourceText
	 */
	public String getSourceText() {
		return sourceText;
	}

	/**
	 * 
	 * @param sourceText
	 */
	public void setSourceText(String sourceText) {
		this.sourceText = sourceText;
		this.setNumberOfChars(sourceText.length());
	}

	/**
	 * 
	 * @return HashSet<String> with cast members
	 */
	public HashSet<String> getCastList() {
		return castList;
	}

	/**
	 * 
	 * @param set
	 */
	public void setCastList(HashSet<String> set) {
		this.castList = set;
	}

	/**
	 * 
	 * @return number of chars in source text
	 */
	public int getNumberOfChars() {
		return numberOfChars;
	}

	/**
	 * 
	 * @return the layer container
	 */
	@XmlElement
	public LayerContainer getLayerContainer() {
		return layerContainer;
	}

	/**
	 * 
	 * @param layerContainer
	 */
	public void setLayerContainer(LayerContainer layerContainer) {
		this.layerContainer = layerContainer;
		this.layerContainer.setTextId(this.id);
	}

	/**
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * For clone function
	 * 
	 * @return
	 */
	public LayerContainer getLayerContainerCopy() {
		// return this.layerContainer.getCopy();
		return null;
	}

	/**
	 * Returns the name of the text as String
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		return textName;
	}

	public void setNumberOfChars(int numberOfChars) {
		this.numberOfChars = numberOfChars;
	}

	/**
	 * Getter for synonyms.
	 * 
	 * @return
	 */
	public HashMap<String, ArrayList<Synonym>> getSynonyms() {
		return synonyms;
	}

	/**
	 * Setter for synonyms.
	 * 
	 * @param synonyms
	 */
	public void setSynonyms(HashMap<String, ArrayList<Synonym>> synonyms) {
		this.synonyms = synonyms;
	}

	/**
	 * Returns the source text formatted with HTML tags.
	 * 
	 * @return String
	 */
	public String getHtmlText() {

		final StringBuilder htmlText =
				new StringBuilder(TEXT_CAPTION_FORMAT + getTextName() + TEXT_CAPTION_FORMAT_CLOSE);

		if (layerContainer.getTypeOfTextLayer() != null) {

			for (final Act act : getLayerContainer().getActLayer().getActs()) {

				final LinkedList<SectionWithType> sections = new LinkedList<SectionWithType>();

				for (final SectionWithType section : layerContainer.getTypeOfTextLayer().getSections()) {
					if (section.getStart() >= act.getStart() && section.getStart() < act.getEnd()) {
						sections.add(section);
					}
				}

				if (!sections.isEmpty()) {

					htmlText.append(
							TextConversion.generateHtmlText(
									sourceText.substring(sections.getFirst().getStart(), sections.getLast().getEnd()),
									sections, false, getUriPrefix())).append(NEW_LINE);
				} else {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Empty act on Text.getHtmlText()");
				}

			}

			return htmlText.toString();
		} else {
			// return unformatted if layers are missing
			return getSourceText();
		}
	}

	private String getUriPrefix() {
		final StringBuilder uriPrefix = new StringBuilder(Path.TEXTVIEW.toString());

		uriPrefix.append("/" + getId() + "/");

		return uriPrefix.toString();
	}

	/**
	 * Returns the source text with html formatting.
	 * 
	 * @param scene
	 * @param sentences
	 * @return
	 */
	public String getHtmlText(Scene scene, List<Sentence> sentences) {
		if (layerContainer.getTypeOfTextLayer() != null) {

			final StringBuilder result = new StringBuilder();

			final Iterator<Sentence> iterator = sentences.iterator();
			int lastEnd = scene.getStart();

			while (iterator.hasNext()) {

				final Sentence sentence = iterator.next();
				final LinkedList<SectionWithType> preSections = new LinkedList<SectionWithType>();
				final LinkedList<SectionWithType> sections = new LinkedList<SectionWithType>();
				final LinkedList<SectionWithType> postSections = new LinkedList<SectionWithType>();

				for (final SectionWithType section : layerContainer.getTypeOfTextLayer().getSections()) {

					if (section.getStart() >= lastEnd && section.getStart() < sentence.getStart()) {
						preSections.add(section);
					} else if (section.getStart() >= sentence.getStart() && section.getStart() < sentence.getEnd()) {
						sections.add(section);
					} else if (!iterator.hasNext() && section.getStart() >= sentence.getEnd()
							&& section.getStart() < scene.getEnd()) {
						postSections.add(section);
					}
				}

				// Part before sentences
				if (!preSections.isEmpty()) {
					final String preSentences =
							sourceText.substring(preSections.getFirst().getStart(), preSections.getLast().getEnd());
					result.append(TextConversion.generateHtmlText(preSentences, preSections, false, getUriPrefix()));
				}

				// Sentences
				if (!sections.isEmpty()) {
					final String sourceSentences =
							sourceText.substring(sections.getFirst().getStart(), sections.getLast().getEnd());
					result.append(TextConversion.generateHtmlText(sourceSentences, sections, true, getUriPrefix()));
				} else {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE,
							"No sentence for result in Text.getHtmlText(Scene, List<Sentence>)");
				}

				// End
				if (!postSections.isEmpty()) {
					final String postSentences =
							sourceText.substring(postSections.getFirst().getStart(), postSections.getLast().getEnd());
					result.append(TextConversion.generateHtmlText(postSentences, postSections, false, getUriPrefix()));
				} else {
					lastEnd = sections.getLast().getEnd();
				}
			}

			// else if (

			return result.toString();

		} else {
			return getSourceText();
		}
	}

	public int getNumberOfActs() {
		return numberOfActs;
	}

	public void setNumberOfActs(int numberOfActs) {
		this.numberOfActs = numberOfActs;
	}

	public int getNumberOfScenes() {
		return numberOfScenes;
	}

	public void setNumberOfScenes(int numberOfScenes) {
		this.numberOfScenes = numberOfScenes;
	}

	/**
	 * 
	 * @param name
	 * @param start
	 * @param end
	 */
	public void addNotIdentifiedEntitie(String name, int start, int end) {
		final int[] index = { start, end };
		notIdentifiedEntitie.put(name, index);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap<String, int[]> getNotIdentifiedEntitie() {
		return notIdentifiedEntitie;
	}
}
