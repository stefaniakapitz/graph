package de.unistuttgart.ims.fictionnet.processes;

import de.unistuttgart.ims.fictionnet.users.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import objectsForTests.TestUserConstructor;

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
public class ProcessManagerTest {
	User testUser;
	User testUser2;
	Process testProcess;
	Process testProcess2;
	ProcessManager instance;
	
	public ProcessManagerTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
		testUser = new TestUserConstructor();
		testUser2 = new TestUserConstructor();
		testProcess = new ProcessImplementation();
		testProcess2 = new ProcessImplementation();
		instance = ProcessManager.getTheInstance();
	}
	
	@After
	public void tearDown() throws NoSuchFieldException, IllegalAccessException {
		// Use annotations to kill the singleton after every test
		Field field = ProcessManager.class.getDeclaredField("theInstance");
		field.setAccessible(true);
		field.set(ProcessManager.getTheInstance(), null);
		field.setAccessible(false);
	}

	/**
	 * Test of getTheInstance method, of class ProcessManager.
	 */
	@Test
	public void testGetTheInstance() {
		assertSame(instance, ProcessManager.getTheInstance());
	}

	/**
	 * Test of addProcess method, of class ProcessManager.
	 */
	@Test
	public void testAddProcess() {
		instance.addProcess(testProcess, testUser);
		instance.addProcess(testProcess2, testUser);
		instance.addProcess(testProcess, testUser2);
		assertEquals(new ArrayList<>(Arrays.asList(testProcess, testProcess2)),
				instance.getUsersProcesses(testUser));
		assertEquals(new ArrayList<>(Arrays.asList(testProcess)),
				instance.getUsersProcesses(testUser2));
	}

	/**
	 * Test of getUsersProcesses method, of class ProcessManager.
	 */
	@Test
	public void testGetUsersProcesses() {
		assertNull(instance.getUsersProcesses(null));
		assertNull(instance.getUsersProcesses(testUser));
		instance.addProcess(testProcess, testUser);
		assertEquals(new ArrayList<>(Arrays.asList(testProcess)),
				instance.getUsersProcesses(testUser));
	}
	
}
