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
public class ProcessTest {

	Process instance;
	ProcessImplementation waitingInstance;
	ProcessStatusChangedEvent testEvent;

	boolean makeMeComeTrue;
	ProcessListener makeTrueListener;
	ProcessListener testEventListener;

	public ProcessTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		instance = new ProcessImplementation();
		waitingInstance = new ProcessImplementation();
		makeMeComeTrue = false;
		makeTrueListener = new ProcessListener() {
			@Override
			public void dispatch(ProcessStatusChangedEvent event) {
				makeMeComeTrue = true;
			}
		};
		testEvent = new ProcessStatusChangedEvent(ProcessStatusType.INPUT_ERROR,
				"testEvent");
		testEventListener = new ProcessListener() {
			@Override
			public void dispatch(ProcessStatusChangedEvent event) {
				assertEquals(ProcessStatusType.INPUT_ERROR,
						event.getProcessStatus());
				assertEquals("testEvent",
						event.getMessage());
			}
		};
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of addListener method, of class Process.
	 */
	@Test
	public void testAddListener() {
		instance.addListener(makeTrueListener);
		instance.fire(testEvent);
		assertTrue(makeMeComeTrue);
	}

	/**
	 * Test of removeListener method, of class Process.
	 */
	@Test
	public void testRemoveListener() {
		instance.addListener(makeTrueListener);
		instance.fire(testEvent);
		assertTrue(makeMeComeTrue);
		makeMeComeTrue = false;
		instance.removeListener(makeTrueListener);
		instance.fire(testEvent);
		assertFalse(makeMeComeTrue);
	}

	/**
	 * Test of fire method, of class Process.
	 */
	@Test
	public void testFire() {
		instance.addListener(makeTrueListener);
		instance.fire(testEvent);
		assertTrue(makeMeComeTrue);
	}

	/**
	 * Test of getStatus method, of class Process.
	 */
	@Test
	public void testGetStatus() {
		assertEquals(instance.getStatus(), ProcessStatusType.READY);
	}

	/**
	 * Test of setStatus method, of class Process.
	 */
	@Test
	public void testSetStatus() {
		assertEquals(ProcessStatusType.READY, instance.getStatus());
		// setStatus also fires up an event
		instance.addListener(makeTrueListener);
		instance.addListener(testEventListener);
		instance.setStatus(ProcessStatusType.INPUT_ERROR, "testEvent");

		assertEquals(ProcessStatusType.INPUT_ERROR, instance.getStatus());
		assertTrue(makeMeComeTrue);
	}

	/**
	 * Test of getProgress method, of class Process.
	 */
	@Test
	public void testGetProgress() {
		assertEquals(0.0, instance.getProgress(), 0.001);
		instance.setProgress((float) 0.5);
		assertEquals(0.5, instance.getProgress(), 0.001);
	}

	/**
	 * Test of setProgress method, of class Process.
	 */
	@Test
	public void testSetProgress() {
		assertEquals(0.0, instance.getProgress(), 0.001);
		instance.setProgress((float) 0.5);
		assertEquals(0.5, instance.getProgress(), 0.001);
	}

	/**
	 * Test of cancel method, of class Process.
	 */
	@Test
	public void testCancel() {
		waitingInstance.start();
		waitingInstance.cancel();
		try {
			/* FIXME: Should wait a while to give thread a chance to die
			 *			Currently cancel() isn't implemented and it would just
			 *			slow down the tests
			 */
			waitingInstance.getThread().join(1);
		} catch (InterruptedException ex) {
		}
		assertFalse(waitingInstance.getThread().isAlive());
		assertEquals(ProcessStatusType.CANCELED, waitingInstance.getStatus());
	}

	/**
	 * Test of start method, of class Process.
	 *
	 * @throws java.lang.InterruptedException
	 */
	@Test
	public void testStart() throws InterruptedException {
		assertEquals(ProcessStatusType.READY, waitingInstance.getStatus());
		assertEquals(0.0, instance.getProgress(), 0.001);
		waitingInstance.start();
		assertEquals(ProcessStatusType.PROCESSING, waitingInstance.getStatus());
		waitingInstance.keepRunning = false;
		waitingInstance.getThread().join();
		assertEquals(ProcessStatusType.FINISHED, waitingInstance.getStatus());
		assertEquals(1.0, waitingInstance.getProgress(), 0.001);
	}

}
