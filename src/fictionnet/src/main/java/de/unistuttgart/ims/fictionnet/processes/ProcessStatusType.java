package de.unistuttgart.ims.fictionnet.processes;

/**
 * Statuses for the ProcessStatus class.
 *
 * @author Lukas Rieger, Erol Aktay
 * @version 07-09-15
 */
public enum ProcessStatusType {
	/**
	 * Process is active and still processing
	 */
	PROCESSING("PROCESSING"),
	/**
	 * Process is active and saving to database
	 */
	SAVING("SAVING"),
	/**
	 * Process has been canceled externally
	 */
	CANCELED("CANCELED"),
	/**
	 * Process is waiting for user input
	 */
	WAITING("WAITING"),
	/**
	 * Process finished succesfully
	 */
	FINISHED("FINISHED"),
	/**
	 * Process is loaded and ready to start processing
	 */
	READY("READY"),
	/**
	 * Process encountered problems with the user input and stopped
	 */
	INPUT_ERROR("INPUT_ERROR"),
	/**
	 * Process encountered an internal error and stopped
	 */
	INTERNAL_ERROR("INTERNAL_ERROR"),
	/**
	 * Process encountered an error of unknown type
	 */
	ERROR("ERROR");

	private final String statusName;

	private ProcessStatusType(String statusName) {
		this.statusName = statusName;
	}

	@Override
	public String toString() {
		return statusName;
	}
}
