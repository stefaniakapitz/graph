package de.unistuttgart.ims.fictionnet.databaseaccess;

import de.unistuttgart.ims.fictionnet.datastructure.Text;

/**
 * @author Lukas Rieger
 * @version 17-10-2015
 * 
 * Table for the texts.
 *
 */
public class DataTableText  extends AbstractDataTable<Text, Integer> {
/**
 * protected constructor
 * This class should only be instantiated in the DBAccessManager.
 */
	protected DataTableText() {
		super(Text.class);
	}

}
