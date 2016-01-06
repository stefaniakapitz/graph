package de.unistuttgart.ims.fictionnet.imp.tei_import;

import static org.junit.Assert.assertEquals;
import java.util.List;
import org.junit.Test;

public class ToolsTest {
	@Test
	public void testExtractLinesBetweenBrackets() {

		String example1 = "<opening tag 1>content 1 (Vaka)</closing tag 1><opening tag 2>content 2 (Fyrsta)</closing tag 2>";
	
		/*String example1 = "<opening tag 1>content 1 (Vaka)</closing tag 1><opening tag 2>content 2 (Fyrsta)</closing tag 2>";
		List<String> res1 = Tools.extractLinesBetweenBrackets(example1);
>>>>>>> branch 'master' of git@clarin06.ims.uni-stuttgart.de:FictionNet/FictionNet.git
		assertEquals(2, res1.size());
		assertEquals("content 1 (Vaka)", res1.get(0));
		assertEquals("content 2 (Fyrsta)", res1.get(1));

		String example2 = "this doesn't even have any brackets";
<<<<<<< HEAD
		List<String> res2 = Tools.extractLinesBetweenTags(example2);
		assertTrue(res2.isEmpty());
=======
		List<String> res2 = Tools.extractLinesBetweenBrackets(example2);
		assertTrue(res2.isEmpty());*/
	}

	@Test
	public void testExtractLinesInTagStructure() {
		String example1 = "<opening tagLayer 1> redundantText <opening tagLayer2>content 1 (Vaka)<redundantOneTagLayer />"
				+ "</closing tagLayer 2><opening tagLayer 2>content 2 (Fyrsta)</closing tagLayer 1> redundantText</closing tagLayer2>";
		List<String> res1 = null;
		try {
			TagProcessor_General processor = new TagProcessor_General(example1);
			
			res1 = processor.extractLines(2);
		} catch (ImportException e) {
			e.printStackTrace();
		}
		assertEquals(2, res1.size());
		assertEquals("content 1 (Vaka)", res1.get(0));
		assertEquals("content 2 (Fyrsta)", res1.get(1));
	}

}
