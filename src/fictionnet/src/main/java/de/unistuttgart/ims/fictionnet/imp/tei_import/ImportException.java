package de.unistuttgart.ims.fictionnet.imp.tei_import;

/**
 * @author Rafael Harth
 * @version 10-28-2015
 *
 *          Error for when a tag layer never closes. This was the most common error I encountered, and it can be caused
 *          by a multitude of different mistakes.
 */
public class ImportException extends Exception {
	private final String details;

	/**
	 * @param details optional context for where the error occured
	 */
	public ImportException(String details) {
		this.details = details;
	}
	
	public String getDetails() {
		return details;
	}
}