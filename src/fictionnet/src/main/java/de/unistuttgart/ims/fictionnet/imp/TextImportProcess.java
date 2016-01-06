package de.unistuttgart.ims.fictionnet.imp;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.LayerContainer;
import de.unistuttgart.ims.fictionnet.datastructure.Synonym;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.ActLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.EventLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.InteractionLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.PresenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SceneLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SentenceLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SpeakerLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TalksAboutLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TokenLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TypeOfTextLayer;
import de.unistuttgart.ims.fictionnet.imp.analyzer.PresencesAnalyzer;
import de.unistuttgart.ims.fictionnet.imp.analyzer.TalksAboutAnalyzer;
import de.unistuttgart.ims.fictionnet.imp.analyzer.TalksWithAnalyzer;
import de.unistuttgart.ims.fictionnet.imp.tei_import.ImportException;
import de.unistuttgart.ims.fictionnet.imp.tei_import.TagProcessor_TeiFile;
import de.unistuttgart.ims.fictionnet.imp.util.In;
import de.unistuttgart.ims.fictionnet.imp.util.Methods;
import de.unistuttgart.ims.fictionnet.processes.Process;
import de.unistuttgart.ims.fictionnet.processes.ProcessStatusType;

/**
 * @author Rafael Harth, Erik-Felix Tinsel
 * @version 11-06-2015
 * 
 *          The class responsible to import dramas as .tei files. It must be given a file and a corresponding corpus
 *          when instantiated; the start() method will cause the file to be imported and the text object to be
 *          generated.
 */
public class TextImportProcess extends Process {
	private Corpus corpus; // corpus to import into
	private final File file; // file to import from
	private IMSInteractionObject iO;
	private Text text;
	private long duration;
	private TeiText teiText;

	public TextImportProcess(File file, Corpus corpus) {
		this.file = file;
		this.corpus = corpus;
	}

	@Override
	public void run() {
		final String lines = Methods.concat(In.readFromFile(file));
		final TagProcessor_TeiFile processor = new TagProcessor_TeiFile(lines);

		ProcessStatusType status;
		String message;

		try {
			processor.transcribe();
			processor.interpret();
			
			teiText = processor.getTeiText();

			iO = TeiText.createInteractionObject(teiText);
			iO = IMSAnalyzer.getInstance().analyze(iO);
			createText();

			setStatus(ProcessStatusType.SAVING, "Saving text to database.");
			DBAccessManager.getTheInstance().saveText(text);

			status = ProcessStatusType.FINISHED;
			message = "Done.";

		} catch (final ImportException e) {
			status = ProcessStatusType.INPUT_ERROR;
			message = "Error during import: " + e.getDetails();
			
			// revert changes
						try {
							DBAccessManager.getTheInstance().deleteText(text);
							DBAccessManager.getTheInstance().deleteCorpus(corpus);
						} catch (final SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
		} catch (final SQLException e) {
			status = ProcessStatusType.INTERNAL_ERROR;
			message = "Error during import: Couldn't save text to database";
		} catch (final Exception e) {
			status = ProcessStatusType.ERROR;
			message = "Unknown Error during import";
			
			// revert changes
			try {
				DBAccessManager.getTheInstance().deleteText(text);
				DBAccessManager.getTheInstance().deleteCorpus(corpus);
			} catch (final SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} finally {
			// file.delete();
		}

		setStatus(status, message);
	}

	/**
	 * Get duration.
	 * 
	 * @return Duration in milliseconds.
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * Creates a Text object out of the
	 * 
	 * @param iO
	 *        the IMSInteractionObject instance (should have already been filled)
	 * @return a Text instance equivalent
	 * @throws SQLException
	 */
	private void createText() throws SQLException {
		// sets attributes of text.
		// It is important to set first the synonyms and then the layers
		text = corpus.createText(teiText.getTitle(), teiText.getAuthor(), teiText.getPublicationDate(),
				teiText.getText());
		text.setCastList((HashSet<String>) teiText.getCastList());
		text.setNumberOfChars(text.getSourceText().length());

		// transmits synonyms from the iO's mentionsClusters to the synonyms of the text
		initializeSynonyms(text, teiText, iO);
		final LayerContainer layers = text.getLayerContainer();
		layers.setLayer(new ActLayer(teiText.getActs()));
		layers.setLayer(new SceneLayer(teiText.getScenes()));
		layers.setLayer(new SentenceLayer(iO.getSentences()));
		layers.setLayer(new TokenLayer(iO.getTokens()));
		layers.setLayer(new EventLayer(iO.getEvents()));
		layers.setLayer(new TypeOfTextLayer(teiText.getSections()));
		
		final PresencesAnalyzer presencesAnalyzer = new PresencesAnalyzer(text);
		presencesAnalyzer.analyze();
		layers.setLayer(new PresenceLayer(presencesAnalyzer.getResults()));

		final TalksAboutAnalyzer talksAboutAnalyzer = new TalksAboutAnalyzer(text);
		talksAboutAnalyzer.analyze();
		layers.setLayer(new TalksAboutLayer(talksAboutAnalyzer.getResults()));
		layers.setLayer(new SpeakerLayer(talksAboutAnalyzer.getSpeakers()));
		
		final TalksWithAnalyzer talksWithAnalyzer = new TalksWithAnalyzer(text);
		talksWithAnalyzer.analyze();
		layers.setLayer(new InteractionLayer(talksWithAnalyzer.getResults()));

		text.setLayerContainer(layers);
	}
	
	/**
	 * This function sets the synonyms in a text. It extracts them from the iO's mentionCluster.
	 * 
	 * @param text
	 * @param teiText
	 * @param iO
	 */
	private void initializeSynonyms(Text text, TeiText teiText, IMSInteractionObject iO) {
		final HashMap<String, ArrayList<Synonym>> synonyms = new HashMap<String, ArrayList<Synonym>>();
		
		for (final MentionCluster mentionCluster : iO.getMentionClusters()) {
			// these variables have to be empty for each new castmember
			final ArrayList<Synonym> synonymsOfCastMember = new ArrayList<Synonym>();
			final ArrayList<String> usedSynonyms = new ArrayList<String>();
			final String castMember = mentionCluster.getOrigin();
			if(synonyms.containsKey(castMember)) {
				continue;
			}
			final Set<MentionEntity> associations = mentionCluster.getAssociations();
			for (final MentionEntity mentionEntity : associations) {
				final int start = mentionEntity.mention.getStart();
				final int end = mentionEntity.mention.getEnd();
				final float confidence = mentionEntity.confidence;
				final String synonymString = teiText.getText().substring(start, end);
				// avoid doublettes in the synonyms list of a castmember
				if(usedSynonyms.contains(synonymString)) {
					continue;
				}
				// remember that this synonym now exists for this castmember
				usedSynonyms.add(synonymString);
				final Synonym newSynonym = new Synonym(synonymString, confidence,
						start, end);
				synonymsOfCastMember.add(newSynonym);
			}
			synonyms.put(castMember, synonymsOfCastMember);
		}
		text.setSynonyms(synonyms);
	}

	public File getFile() {
		return file;
	}

	public Corpus getCorpus() {
		return corpus;
	}

	public void setCorpus(Corpus corpus) {
		this.corpus = corpus;
	}

	/**
	 * Set duration.
	 * 
	 * @param duration
	 *        in milliseconds.
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	public Text getText() {
		return text;
	}

	public IMSInteractionObject getIMSInteractionObject() {
		return iO;
	}

	public void setIMSInteractionObject(IMSInteractionObject iO) {
		this.iO = iO;
	}
}
