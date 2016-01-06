package de.unistuttgart.ims.fictionnet.databaseaccess.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * Tests for class DBAccessManager.
 * 
 * @author Lukas Rieger
 * @version 22.09.2015
 *
 */
public class TestDBAccessManagerText {
	@Test
	public void testSaveAndLoadText() throws Exception {
		DBAccessManager db = DBAccessManager.getTheInstance();
		UserManagement userManagement = UserManagement.getTheInstance();		
		
		db.connect("jdbc:mariadb://localhost:3306/fictionnet?user=root&password=root");
		
		User user = userManagement.createUser("test1@uni-stuttgart.de", "gurken");
		Project project = user.createProject("theProject");
		Corpus corpus = project.createCorpus("theCorpus");
		Text text = corpus.createText("Das Sams", "Paul Paulmann", "12.12.89", "Test text text.");
		
		HashSet<String> castList = new HashSet<String>();
		castList.add("Der König");
		castList.add("Der Knecht");
		castList.add("Die Magd");
		
		Map<String, List<String>> synonyms = new HashMap<String, List<String>>();
		ArrayList<String> castMembers = new ArrayList<String>();
		castMembers.add("Franz");
		castMembers.add("Horst");
		castMembers.add("Dieter");
		synonyms.put("die drei", castMembers);
		
		text.setCastList(castList);
		
		db.saveText(text);
		
		
	}

}
