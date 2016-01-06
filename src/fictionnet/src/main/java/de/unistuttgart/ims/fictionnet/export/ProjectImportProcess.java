package de.unistuttgart.ims.fictionnet.export;


import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXB;

import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.users.User;

public class ProjectImportProcess {
	public Project importProject(InputStream input, User user) throws IOException {
		Project importedProject = JAXB.unmarshal(input, Project.class);
		importedProject.setOwner(user);
		
		return importedProject;		
	}
	
}
