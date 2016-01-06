package objectsForTests;

import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
/**
 * @author Lukas Rieger
 * Only for testing. This is how we can use the protected constructor
 * of the Corpus class in a test case and ensure information hiding at
 * the same time.
 *
 */
public class TestCorpusConstructor extends Corpus {
	public TestCorpusConstructor() {
		super("newCorpus");
	}
}
