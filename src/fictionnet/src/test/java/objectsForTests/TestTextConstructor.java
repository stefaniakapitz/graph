package objectsForTests;

import de.unistuttgart.ims.fictionnet.datastructure.Text;

/**
 * 
 * @author Lukas Rieger
 * @version 15-11-2015
 *
 * This is how we can use a protocted constructor in a test case.
 * So we can use it for test and ensure information hiding at the same time.
 *
 */
public class TestTextConstructor extends Text{
	public TestTextConstructor() {
		super("x", "x", "x", "x");
	}
}
