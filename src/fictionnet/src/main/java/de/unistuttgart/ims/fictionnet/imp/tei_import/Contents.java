package de.unistuttgart.ims.fictionnet.imp.tei_import;

import java.util.List;
import java.util.Set;

import de.unistuttgart.ims.fictionnet.datastructure.layers.ActLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SceneLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.SectionWithType;
import de.unistuttgart.ims.fictionnet.imp.TeiText;
import de.unistuttgart.ims.fictionnet.imp.tei_import.tree.NodeCollection;
import de.unistuttgart.ims.fictionnet.imp.tei_import.tree.Node_Poly;
import de.unistuttgart.ims.fictionnet.imp.tei_import.tree.Node_Single;
import de.unistuttgart.ims.fictionnet.imp.util.Converter;
import de.unistuttgart.ims.fictionnet.imp.util.Methods;

/**
 * @author Rafael Harth
 * @version 11-08-2015
 * 
 *          This class bundles the functionality to interpret and convert the contents after part one of the the import
 *          process is completed, during which parts of the source text are purely transcribed and archived. It contains
 *          all nodes with valuable content as fields.
 *
 */
public class Contents {
	private NodeCollection castList;
	private Node_Poly coverInformation; // Contains author and title
	private Node_Single creationDate;
	private Node_Single titleAndReleaseDate;
	private Node_Single notes; // further notes

	private ActLayer actLayer = new ActLayer();
	private SceneLayer sceneLayer = new SceneLayer();
	private List<SectionWithType> sections;
	private String text;

	/**
	 * Starts off the extraction process. All necessary information will be gathered from the class fields which must
	 * have already been set during the first part of the import process before this method is called.
	 * 
	 * @return a TeiText instance that contains all collected information
	 * @throws ImportException
	 *         in case of an error
	 */
	public TeiText interpret(List<String> sourceText) throws ImportException {
		final TeiText teiText = new TeiText();
	
		buildText(sourceText);

		teiText.setText(text);
		teiText.setSections(sections);
		teiText.setAuthor(extractAuthor());
		teiText.setPublicationDate(extractPublicationDate());
		teiText.setTitle(extractTitle());
		teiText.setCastList(extractCastList());
		teiText.setActs(actLayer.getActs());
		teiText.setScenes(sceneLayer.getScenes());
		teiText.setNotes(extractNotes());

		return teiText;
	}

	/**
	 * Attempts to extract the name of the drama's author.
	 * 
	 * @return the name as a String, or "unknown" if the attempt failed
	 */
	private String extractAuthor() {
		try {
			return new TagProcessor_General(coverInformation.dataAsString()).extractLines().get(0).trim();
		} catch (final Exception e) {
			return "unknown";
		}
	}

	/**
	 * Attempts to extract the name of the drama's title. Said name appears twice in the usual .tei files, so there are
	 * two ways to determine it, which are implemented in extractAuthorVar1() and extractAuthorVar2(). This method will
	 * try to use var1 first, and it it fails, use var2.
	 * 
	 * @return the name as a String, or "unknown" if both methods fail.
	 */
	private String extractTitle() {
		String title = extractTitleVar1(); // try var1
		title = title == null ? extractTitleVar2() : title; // if it failed, try var2

		if (title == null) { // both failed
			return "unknown";
		} else {
			return title;
		}

	}

	/**
	 * @return the drama's title as a String, or null if extraction failed.
	 */
	private String extractTitleVar1() {
		List<String> lines;

		try {
			lines = Methods.mapTrim(new TagProcessor_General(coverInformation.dataAsString()).extractLines());
			return lines.get(1);
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * @return the drama's title as a String, or null if extraction failed.
	 */
	private String extractTitleVar2() {
		List<String> lines;

		try {
			lines = Methods.mapTrim(new TagProcessor_General(titleAndReleaseDate.dataAsString()).extractLines());
			return lines.get(0);
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * Attempts to extract the drama's cast list.
	 * 
	 * @return the cast list as a set of strings
	 * @throws ImportException
	 *         in case of an error
	 */
	private Set<String> extractCastList() throws ImportException {
		final List<String> content = new TagProcessor_General(castList.getData()).extractLines(1);

		return Converter.toStringSet(Tools.purifyCastList(content));
	}

	/**
	 * Attempts to extract the drama's publication date. It can both be single date or a radius.
	 * 
	 * @return the date or radius of dates as a string
	 * @throws ImportException
	 *         in case of an error
	 */
	private String extractPublicationDate() throws ImportException {
		try {
			final String content = creationDate.dataAsString();

			if (content.contains("notBefore")) {
				final String from = Methods.substring(content, "notBefore=\"", "\"");
				final String to = Methods.substring(content, "notAfter=\"", "\"");

				return from + " - " + to;
			} else {
				return new TagProcessor_General(content).extractLines().get(0).trim();
			}
		} catch (Exception e) {
			return "unknown";
		}
	}

	/**
	 * Attempts to extract further release notes that may be included in the file description.
	 * 
	 * @return the notes as a single string or "No notes found" if extraction failed
	 */
	private String extractNotes() {
		try {
			return Tools.concatLinesInTagStructure(notes.dataAsString());
		} catch (Exception e) {
			return "No notes found";
		}
	}

	/**
	 * Builds the final source text out of the TextPart objects. Also creates SectionWithType and Paragraph lists on the
	 * way.
	 * 
	 * @param parts
	 *        the text parts that include all stage rends, speakers, and spoken text
	 * @return the text
	 * @throws ImportException
	 *         if a tag never closes
	 */
	private void buildText(final List<String> parts) throws ImportException {

		final TagProcessor_SP spC = new TagProcessor_SP(parts);
		spC.transcribe();

		actLayer.setActs(spC.getActs());
	
		sceneLayer.setScenes(spC.getScenes());
		sections = spC.getSections();
		text = spC.getContent();

	}

	/**
	 * Handles with the potential alternative structure of .tei files that doesn't fit into the model. See the class
	 * comment in TeiTree.java for clarification.
	 * 
	 * @param castName
	 *        new node for cast name
	 * @param castItems
	 *        new node for cast items
	 * @param coverInf
	 *        new node for cover information
	 */
	public void switchToVariantTwo(final Node_Poly castItems, final Node_Poly coverInf) {
		castList = new NodeCollection(castItems);
		coverInformation = coverInf;
	}

	// Basic Setters ↓
	public void setCastList(NodeCollection castList) {
		this.castList = castList;
	}

	public void setCoverInformation(Node_Poly coverInformation) {
		this.coverInformation = coverInformation;
	}

	public void setCreationDate(Node_Single creationDate) {
		this.creationDate = creationDate;
	}

	public void setTitleAndReleaseDate(Node_Single titleAndReleaseDate) {
		this.titleAndReleaseDate = titleAndReleaseDate;
	}

	public void setNotes(Node_Single notes) {
		this.notes = notes;
	}
}