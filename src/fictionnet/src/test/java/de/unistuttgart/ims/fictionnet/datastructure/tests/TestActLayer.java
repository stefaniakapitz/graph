package de.unistuttgart.ims.fictionnet.datastructure.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import de.unistuttgart.ims.fictionnet.datastructure.layers.ActLayer;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;

public class TestActLayer {

	@Test
	public void testActLayer() {
		ArrayList<Act>act = new ArrayList<Act>();
		act.add(new Act());
		ActLayer actLayer = new ActLayer(act);
		assertEquals(act, actLayer.getActs());
		
		
	}

}
