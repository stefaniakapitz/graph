package de.unistuttgart.ims.fictionnet.analysis.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.j256.ormlite.table.TableUtils;

import java.io.File;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.ims.fictionnet.analysis.ActionTypes;
import de.unistuttgart.ims.fictionnet.analysis.Analyzer;
import de.unistuttgart.ims.fictionnet.analysis.Conjunction;
import de.unistuttgart.ims.fictionnet.analysis.Filter;
import de.unistuttgart.ims.fictionnet.analysis.Result;
import de.unistuttgart.ims.fictionnet.analysis.SingleResult;
import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
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
import de.unistuttgart.ims.fictionnet.datastructure.layers.TalksAboutLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TokenLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.TypeOfTextLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Interaction;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.InteractionType;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Presence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Sentence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Speaker;
import de.unistuttgart.ims.fictionnet.export.ProjectExportProcess;
import de.unistuttgart.ims.fictionnet.imp.TextImportProcess;
import de.unistuttgart.ims.fictionnet.users.Role;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;
import objectsForTests.TestUserConstructor;

/**
 * 
 * @author Domas Mikalkinas
 *
 */
public class AnalyzerTest {
	public DBAccessManager db = DBAccessManager.getTheInstance();
	public UserManagement userManagement = UserManagement.getTheInstance();
	public User testUser1;
	public User testUser2;

	@Before
	public void initialize() {
		db = DBAccessManager.getTheInstance();
		userManagement = UserManagement.getTheInstance();

		try {
			db.connect("jdbc:mariadb://localhost:3306/fictionnet?user=root&password=");
			
			TableUtils.clearTable(db.getConnectionSource(), User.class);
			TableUtils.clearTable(db.getConnectionSource(), Project.class);
			TableUtils.clearTable(db.getConnectionSource(), Corpus.class);

			userManagement.createUser("testUser1@fictionnet.de", "katze");
			userManagement.createUser("testUser2@fictionnet.de", "hund");

			testUser1 = userManagement.login("testUser1@fictionnet.de", "katze");
			testUser2 = userManagement.login("testUser2@fictionnet.de", "hund");
			userManagement.changeUserRole("testUser1@fictionnet.de", Role.ADMIN);

			assertNotNull(testUser1);
			assertNotNull(testUser2);

		} catch (Exception e) {
			System.out.println("ERROR initialize(): " + e.getMessage());
		}
	}


//	@Test
//	public void testTeiImportAnalysis() {
//		File f = new File("D:/Git2/FictionNet/doc/Anleitung Alpha/Beispiel TEI Texte/WilhelmTell.xml");
//		User user = new TestUserConstructor("test@test.com");
//		Project project = null;
//		Project importedProject = null;
//		try {
//			DBAccessManager db = DBAccessManager.getTheInstance();
//			db.connect("jdbc:mariadb://localhost:3306/fictionnet?user=root&password=");
//			project = user.createProject("project");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//
//		Corpus corpus= null;
//		try {
//			corpus = project.createCorpus("myCorpus");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		TextImportProcess t = new TextImportProcess(f, corpus);
//		t.run();
//		corpus = t.getCorpus();
//		Filter filter = new Filter();
//		List<String> subject = new ArrayList<>();
//		List<String> object = new ArrayList<>();
//		
////		subject.add("duncan.");
//		
////		object.add("Wilhelm Tell");
//		filter.setActionObject(object);
//		filter.setActionSubject(subject);
//		filter.setActionType(ActionTypes.TALKS_ABOUT);
//		filter.setObjectConjunction(Conjunction.AND);
//		filter.setSubjectConjunction(Conjunction.AND);
//		
//		System.out.println("Name "+corpus.getTexts().get(0).getCastList().iterator().next());
//		
//		Analyzer analyzer = new Analyzer();
//		Iterator<String> it= corpus.getTexts().get(0).getCastList().iterator();
//		while(it.hasNext()){
//			System.out.println(it.next());
//		
//		}
//		System.out.println();
//		String castname= corpus.getTexts().get(0).getCastMembersForSynonym("walter fürst").get(0);
//		String castname2= "Walter Fürst";
//		String objectName="Friesshardt";
//		String objectName2= "Ruodi, der Fischer, aus Uri";
//		System.out.println(castname2);
//		subject.add(castname);
//		subject.add(castname2);
//
//		object.add(objectName);
//		object.add(objectName2);
//		System.out.println(corpus.getTexts().get(0).getStringSynonymsFor("Ruodi, der Fischer, aus Uri"));
//		ArrayList<String> liste= corpus.getTexts().get(0).getStringSynonymsFor("Konrad Hunn");
//				for(int i=0;i<liste.size();i++){
//					System.out.println("Synonyme für Ruodi: "+ liste.get(i));
//				}
//		System.out.println("Wie viele Akte: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().size());
//		System.out.println("Aktstart erster Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(0).getStart());
//		System.out.println("Aktende erster Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(0).getEnd());
//
//		System.out.println("Aktstart zweiter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(1).getStart());
//		System.out.println("Aktende zweiter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(1).getEnd());
//
//		System.out.println("Aktstart dritter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(2).getStart());
//		System.out.println("Aktende dritter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(2).getEnd());
//		
//		System.out.println("Aktstart vierter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(3).getStart());
//		System.out.println("Aktende vierter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(3).getEnd());
//		
//		System.out.println("Aktstart fünfter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(4).getStart());
//		System.out.println("Aktende fünfter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(4).getEnd());
//		
//		System.out.println("Aktstart sechster Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(5).getStart());
//		System.out.println("Aktende sechster Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(5).getEnd());
//		
////		System.out.println("CastListe: "+ corpus.getTexts().get(0).getCastList().size());
////		System.out.println("Synonyme" +corpus.getTexts().get(0).getSynonyms().size() );
//		Result result = analyzer.analyze(corpus.getTexts().get(0), filter);
//		System.out.println(result.getResults().size());
//		for(int i=0; i<result.getResults().size();i++){
//			System.out.println(result.getResults().size());
//			System.out.println("Akt: "+result.getResults().get(i).getAct().getActNumber());
//			System.out.println("Aktstart: "+result.getResults().get(i).getAct().getStart());
//			System.out.println("Aktende: "+result.getResults().get(i).getAct().getEnd());
//			System.out.println("Szene: "+result.getResults().get(i).getScene().getSceneNumber());
//			System.out.println("Szenenstart: "+result.getResults().get(i).getScene().getStart());
//			System.out.println("Sprecher: "+result.getResults().get(i).getSpeaker().iterator().next());
//			System.out.println("Wie viele Anwesenden: "+result.getResults().get(i).getConversationObjects().size());
//			System.out.println("Erster Anwesender: "+result.getResults().get(i).getConversationObjects().iterator().next());
//		}
//		
//		//filter.setActionSubject(actionSubject);
//	}
	@Test
	public void testTalksWithAnalyzer() throws SQLException {
		File f = new File("D:/Git2/FictionNet/doc/Anleitung Alpha/Beispiel TEI Texte/WilhelmTell.xml");
		User user = new TestUserConstructor("test@test.com");
		Project project = null;
		Project importedProject = null;
		try {
			DBAccessManager db = DBAccessManager.getTheInstance();
			db.connect("jdbc:mariadb://localhost:3306/fictionnet?user=root&password=");
			project = user.createProject("project");
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		Corpus corpus= null;
		try {
			corpus = project.createCorpus("myCorpus");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		TextImportProcess t = new TextImportProcess(f, corpus);
		t.run();
		
		corpus = t.getCorpus();
		
		Text text2 = db.loadTextsOfCorpus(1).get(0);
		text2 = db.loadTextContent(text2);
		
		for(int i=0;i<text2.getLayerContainer().getLayers().size();i++){
			Layer layer= text2.getLayerContainer().getLayers().get(i);

			if (layer != null) {
				if (layer instanceof ActLayer) {
					ActLayer actLayer = (ActLayer) layer;

					System.out.println(layer.getClass() + " "+((ActLayer) layer).getActs().size());

				} else if (layer instanceof TokenLayer) {
					TokenLayer tokenLayer = (TokenLayer) layer;

					System.out.println(layer.getClass() + " "+((TokenLayer) layer).getTokens().size());

				} else if (layer instanceof SentenceLayer) {
					SentenceLayer sentenceLayer = (SentenceLayer) layer;

					System.out.println(layer.getClass() + " "+((SentenceLayer) layer).getSentences().size());
				} else if (layer instanceof TypeOfTextLayer) {
					TypeOfTextLayer typeOfTextLayer = (TypeOfTextLayer) layer;

					System.out.println(layer.getClass() + " "+((TypeOfTextLayer) layer).getSections().size());
				} else if (layer instanceof PresenceLayer) {
					PresenceLayer presenceLayer = (PresenceLayer) layer;

					System.out.println(layer.getClass() + " "+((PresenceLayer) layer).getPresences().size());
				} else if (layer instanceof InteractionLayer) {

					InteractionLayer interactionLayer = (InteractionLayer) layer;
					System.out.println(layer.getClass() + " "+((InteractionLayer) layer).getInteractions().size());
				} else if (layer instanceof EventLayer) {
					EventLayer eventLayer = (EventLayer) layer;

					System.out.println(layer.getClass() + " "+((EventLayer) layer).getEvents().size());
				} else if (layer instanceof TalksAboutLayer) {
					TalksAboutLayer talksAboutLayer = (TalksAboutLayer) layer;

					System.out.println(layer.getClass() + " "+((TalksAboutLayer) layer).getMentions().size());
				} else if (layer instanceof SceneLayer) {
					SceneLayer sceneLayer = (SceneLayer) layer;

					System.out.println(layer.getClass() + " "+((SceneLayer) layer).getScenes().size());
				} else if (layer instanceof TypeOfTextLayer) {
					TypeOfTextLayer typeOfTextLayer = (TypeOfTextLayer) layer;

					System.out.println(layer.getClass() + " "+((TypeOfTextLayer) layer).getSections().size());
				} else if (layer instanceof SpeakerLayer) {
					SpeakerLayer speakerLayer = (SpeakerLayer) layer;

					System.out.println(layer.getClass() + " "+((SpeakerLayer) layer).getSpeakers().size());
				} else {
					throw new InvalidParameterException();
				}
			}
		}
		Filter filter = new Filter();
		List<String> subject = new ArrayList<>();
		List<String> object = new ArrayList<>();
		
//		subject.add("duncan.");
		
//		object.add("Wilhelm Tell");
		
		filter.setActionType(ActionTypes.TALKS_ABOUT);
		filter.setObjectConjunction(Conjunction.OR);
		filter.setSubjectConjunction(Conjunction.OR);
		
		System.out.println("Name "+corpus.getTexts().get(0).getCastList().iterator().next());
		
		Analyzer analyzer = new Analyzer();
//		Iterator<String> it= corpus.getTexts().get(0).getCastList().iterator();
//		while(it.hasNext()){
//			System.out.println(it.next());
//		
//		}
//		System.out.println();
//		String castname2= corpus.getTexts().get(0).getCastMembersForSynonym("Stauffacher").get(0);
		String castname2= "Walter Fürst";
//		String objectName="Friesshardt";
		String objectName2= "Gertrud, Stauffachers Gattin";
//		System.out.println(castname2);
//		subject.add(castname);
		subject.add(castname2);
//
//		object.add(objectName);
		object.add(objectName2);
		filter.setActionObject(object);
		filter.setActionSubject(subject);
//		System.out.println(corpus.getTexts().get(0).getStringSynonymsFor("Ruodi, der Fischer, aus Uri"));
//		ArrayList<String> liste= corpus.getTexts().get(0).getStringSynonymsFor("Konrad Hunn");
//				for(int i=0;i<liste.size();i++){
//					System.out.println("Synonyme für Ruodi: "+ liste.get(i));
//				}
//		System.out.println("Wie viele Akte: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().size());
//		System.out.println("Aktstart erster Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(0).getStart());
//		System.out.println("Aktende erster Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(0).getEnd());
//
//		System.out.println("Aktstart zweiter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(1).getStart());
//		System.out.println("Aktende zweiter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(1).getEnd());
//
//		System.out.println("Aktstart dritter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(2).getStart());
//		System.out.println("Aktende dritter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(2).getEnd());
//		
//		System.out.println("Aktstart vierter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(3).getStart());
//		System.out.println("Aktende vierter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(3).getEnd());
//		
//		System.out.println("Aktstart fünfter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(4).getStart());
//		System.out.println("Aktende fünfter Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(4).getEnd());
//		
//		System.out.println("Aktstart sechster Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(5).getStart());
//		System.out.println("Aktende sechster Akt: "+corpus.getTexts().get(0).getLayerContainer().getActLayer().getActs().get(5).getEnd());
//		
//		InteractionLayer il= corpus.getTexts().get(0).getLayerContainer().getInteractionLayer();
//		for( int i=0; i<il.getInteractions().size();i++){
//			System.out.println("Interaction "  + il.getInteractions().get(i).getProtagonist().iterator().next() + il.getInteractions().get(i).getProtagonist().size()+ " "+ il.getInteractions().get(i).getInvolvedCastMembers().iterator().next() + " " +il.getInteractions().get(i).getInvolvedCastMembers().size());
//			
//			
//		}
//		System.out.println("CastListe: "+ corpus.getTexts().get(0).getCastList().size());
//		System.out.println("Synonyme" +corpus.getTexts().get(0).getSynonyms().size() );
		Result result = analyzer.analyze(text2, filter);
		System.out.println(result.getResults().size());
		for(int i=0; i<result.getResults().size();i++){
			System.out.println(result.getResults().size());
			System.out.println("Akt: "+result.getResults().get(i).getAct().getActNumber());
			System.out.println("Aktstart: "+result.getResults().get(i).getAct().getStart());
			System.out.println("Aktende: "+result.getResults().get(i).getAct().getEnd());
			System.out.println("Szene: "+result.getResults().get(i).getScene().getSceneNumber());
			System.out.println("Szenenstart: "+result.getResults().get(i).getScene().getStart());
			System.out.println("Sprecher: "+result.getResults().get(i).getSpeaker().iterator().next());
			Iterator<String> it= result.getResults().get(i).getConversationObjects().iterator();
			while(it.hasNext()){
				System.out.println("Anwesender " +it.next());
			}
			System.out.println("Wie viele Anwesenden: "+result.getResults().get(i).getConversationObjects().size());
			System.out.println("Erster Anwesender: "+result.getResults().get(i).getConversationObjects().iterator().next());
		}
		
//		filter.setActionSubject(actionSubject);
	}
	
}
