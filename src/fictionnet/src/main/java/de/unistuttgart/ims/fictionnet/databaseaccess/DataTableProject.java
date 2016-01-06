package de.unistuttgart.ims.fictionnet.databaseaccess;

import de.unistuttgart.ims.fictionnet.datastructure.Project;

/**
 * @author Lukas Rieger
 * @version 17-10-2015
 * 
 * The table for all the projects.
 *
 */
public class DataTableProject extends AbstractDataTable<Project, Integer> {
/**
 * protected constructor
 * This class should only be instantiated in the DBAccessManager.
 */
	protected DataTableProject() {
		super(Project.class);
	}

}
