package de.unistuttgart.ims.fictionnet.users.tests;

import static org.junit.Assert.*;
import objectsForTests.TestUserConstructor;

import org.junit.Test;

import de.unistuttgart.ims.fictionnet.users.Role;
import de.unistuttgart.ims.fictionnet.users.User;

/**
 * Tests for the user class.
 * Use this for regression testing.
 * 
 * @author Lukas Rieger
 * @version 30-10-2015
 *
 */
public class TestUser{

	@Test
	public void test() {
		User user = new TestUserConstructor("newUserEmail@gmx.de");
		assertNotNull(user.getLastLoginDate());
		assertNotNull(user.getRegistrationDate());
		assertEquals("newUserEmail@gmx.de", user.getEmail());
		assertEquals(Role.USER, user.getRole());
	}

}
