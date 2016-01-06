package de.unistuttgart.ims.fictionnet.processes;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author Erol Aktay
 */
public class ProcessStatusChangedEventTest {
	
	ProcessStatusChangedEvent instance;
	static final ProcessStatusType testType = ProcessStatusType.INPUT_ERROR;
	static final String testMessage = "testMessage";

	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
		instance = new ProcessStatusChangedEvent(testType, testMessage);
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of getProcessStatus method, of class ProcessStatusChangedEvent.
	 */
	@Test
	public void testGetProcessStatus() {
		assertEquals(testType, instance.getProcessStatus());		
	}

	/**
	 * Test of getMessage method, of class ProcessStatusChangedEvent.
	 */
	@Test
	public void testGetMessage() {
		assertEquals(testMessage, instance.getMessage());	
	}
	
}
