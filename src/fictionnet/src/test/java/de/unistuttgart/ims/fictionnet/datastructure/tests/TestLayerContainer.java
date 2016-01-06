package de.unistuttgart.ims.fictionnet.datastructure.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.LayerContainer;
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
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;
/**
 * @author Lukas Rieger
 * @version 30-10-2015
 * 
 * Tests for the basic functions of the layercontainer.
 */
public class TestLayerContainer {
	DBAccessManager db;
	UserManagement um;
	
	@Before
	public void setUp() throws SQLException {
		db = DBAccessManager.getTheInstance();
		db.connect("jdbc:sqlite::memory:");
		um = UserManagement.getTheInstance();
	}
	
	@Test
	public void testLayerContainer() throws SQLException {
		User user = um.createUser("user@email.com", "pw1");
		Text text = user.createProject("test").createCorpus("test").createText("textName", "author", "12.12.12", "lalal");
		
		assertNotNull(text.getLayerContainer());
		db.saveText(text);
		Text t = db.loadTextContent(text);
		assertNotNull(t.getLayerContainer());
	}
	
	@Test
	public void testSetLayer() throws Exception {
		LayerContainer container = new LayerContainer();
		
		ActLayer actLayer = new ActLayer();
		container.setLayer(actLayer);
		assertEquals(actLayer, container.getActLayer());
		//TODO test if layer is in DB
		
		TokenLayer tokenLayer = new TokenLayer();
		container.setLayer(tokenLayer);
		assertEquals(tokenLayer, container.getTokenLayer());

		SentenceLayer sentenceLayer = new SentenceLayer();
		container.setLayer(sentenceLayer);
		assertEquals(sentenceLayer, container.getSentenceLayer());
		
		PresenceLayer presenceLayer = new PresenceLayer();
		container.setLayer(presenceLayer);
		assertEquals(presenceLayer, container.getPresenceLayer());
		
		InteractionLayer interactionLayer = new InteractionLayer();
		container.setLayer(interactionLayer);
		assertEquals(interactionLayer, container.getInteractionLayer());
		
		TalksAboutLayer talksAboutLayer = new TalksAboutLayer();
		container.setLayer(talksAboutLayer);
		assertEquals(talksAboutLayer, container.getTalksAboutLayer());
		
		SceneLayer sceneLayer = new SceneLayer();
		container.setLayer(sceneLayer);
		assertEquals(sceneLayer, container.getSceneLayer());
		
		SpeakerLayer speakerLayer = new SpeakerLayer();
		container.setLayer(speakerLayer);
		assertEquals(speakerLayer, container.getSpeakerLayer());
		
		EventLayer eventLayer = new EventLayer();
		container.setLayer(eventLayer);
		assertEquals(eventLayer, container.getEventLayer());
		
		container.setLayer(null);
		assertEquals(actLayer, container.getActLayer());
		assertEquals(eventLayer, container.getEventLayer());
		assertEquals(speakerLayer, container.getSpeakerLayer());
		assertEquals(talksAboutLayer, container.getTalksAboutLayer());
		assertEquals(sceneLayer, container.getSceneLayer());
		assertEquals(interactionLayer, container.getInteractionLayer());
		assertEquals(presenceLayer, container.getPresenceLayer());
		assertEquals(tokenLayer, container.getTokenLayer());

	}
}
