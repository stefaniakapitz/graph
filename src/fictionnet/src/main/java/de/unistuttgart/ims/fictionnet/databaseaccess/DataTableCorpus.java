package de.unistuttgart.ims.fictionnet.databaseaccess;

import de.unistuttgart.ims.fictionnet.datastructure.Corpus;

/**
 * @author Lukas Rieger
 * @version 17-10-2015
 * 
 * Table for the corpora.
 *
 */
public class DataTableCorpus extends AbstractDataTable<Corpus, Integer> {
	// reset to private
	/**
	 * public only for testing purposes.
	 * Should be reset to private.
	 */
	public DataTableCorpus() {
		super(Corpus.class);
	}

}
