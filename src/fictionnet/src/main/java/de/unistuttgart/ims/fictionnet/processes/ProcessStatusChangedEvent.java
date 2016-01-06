package de.unistuttgart.ims.fictionnet.processes;

/**
 * Event for status changes
 * 
 * @author Lukas Rieger, Erol Aktay
 * @version 24-10-2015
 */
public class ProcessStatusChangedEvent {
	private final ProcessStatusType status;
	private final String message;

	/**
	 * Create a new event
	 * 
	 * @param status The new status
	 * @param message An optional additional comment for the status change
	 */
	public ProcessStatusChangedEvent(ProcessStatusType status, String message) {
		this.status = status;
		this.message = message;
	}

	/**
	 * Return the process status type
	 * @return status
	 */
	public ProcessStatusType getProcessStatus() {
		return this.status;
	}

	/**
	 * Return the event message
	 * @return message
	 */
	public String getMessage() {
		return message;
	}
}
