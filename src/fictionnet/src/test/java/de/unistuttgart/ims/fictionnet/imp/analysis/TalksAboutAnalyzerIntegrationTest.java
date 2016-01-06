package de.unistuttgart.ims.fictionnet.imp.analysis;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.unistuttgart.ims.fictionnet.analysis.ActionTypes;
import de.unistuttgart.ims.fictionnet.analysis.Analyzer;
import de.unistuttgart.ims.fictionnet.analysis.Conjunction;
import de.unistuttgart.ims.fictionnet.analysis.Filter;
import de.unistuttgart.ims.fictionnet.analysis.Result;
import de.unistuttgart.ims.fictionnet.analysis.SingleResult;
import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.layers.SpeakerLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TalksAboutLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Speaker;
import de.unistuttgart.ims.fictionnet.imp.TextImportProcess;
import de.unistuttgart.ims.fictionnet.users.User;
import objectsForTests.TestUserConstructor;

public class TalksAboutAnalyzerIntegrationTest {
	
	private static File f = new File("C:/Users/David/git/FictionNet/doc/Anleitung Alpha/Beispiel TEI Texte/WilhelmTell.xml");
	private static User user = new TestUserConstructor("test@test.com");
	private static Project project = null;
	private static Corpus corpus = null;
	private static TalksAboutLayer talksAboutLayer= null;
	private SpeakerLayer speakerLayer = null;
	
	@BeforeClass
	public static void initDB() {
		try {
			final DBAccessManager db = DBAccessManager.getTheInstance();
			db.connect("jdbc:sqlite::memory:");
			project = user.createProject("project");
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
	
	@Before
	public void setUp(){
				
		try {
			corpus = project.createCorpus("myCorpus");
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		final TextImportProcess t = new TextImportProcess(f, corpus);
		t.run();
		talksAboutLayer = corpus.getTexts().get(0).getLayerContainer().getTalksAboutLayer();
		speakerLayer = corpus.getTexts().get(0).getLayerContainer().getSpeakerLayer();
	}
	
	
	
	@Test
	public void testAnalysis() {
		/*Iterator<String> i = talksAboutLayer.getMentions().get(0).getMentionedCastMembers().iterator();
		assertEquals("Jenni", i.next());
		i = talksAboutLayer.getMentions().get(35).getMentionedCastMembers().iterator();
		assertEquals("Herberg", i.next());*/
		
		List<String> subject = new ArrayList<>();
		List<String> object = new ArrayList<>();
		
		subject = new ArrayList<String>(corpus.getTexts().get(0).getCastList());
		object = new ArrayList<String>(corpus.getTexts().get(0).getCastList());
		//object.add("MACBETH");
		//subject.add("MACBETH");
//		subject.add("duncan.");
		
//		object.add("Wilhelm Tell");
		final Filter filter = new Filter();
		filter.setActionObject(object);
		filter.setActionSubject(subject);
		filter.setActionType(ActionTypes.TALKS_ABOUT);
		filter.setObjectConjunction(Conjunction.OR);
		filter.setSubjectConjunction(Conjunction.OR);
		
		final Analyzer a = new Analyzer();
		final Result r = a.analyze(corpus.getTexts().get(0), filter);
		final List<SingleResult>arr = r.createSingleResults(r);
		
		for (final Speaker speaker : speakerLayer.getSpeakers()) {
			System.out.println(corpus.getTexts().get(0).getSourceText().substring(speaker.getStart(), speaker.getEnd()));
		}
		
	}
	
	
}
