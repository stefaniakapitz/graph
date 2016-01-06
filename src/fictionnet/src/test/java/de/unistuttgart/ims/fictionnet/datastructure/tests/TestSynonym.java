package de.unistuttgart.ims.fictionnet.datastructure.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.unistuttgart.ims.fictionnet.datastructure.Synonym;

public class TestSynonym {

	@Test
	public void testSynonym() {
		Synonym testSynonym = new Synonym("hallo", 1287, 0, 0);
		assertEquals("hallo", testSynonym.getName());
		assertEquals(1287, testSynonym.getConfidence(), 0);
	}
}
