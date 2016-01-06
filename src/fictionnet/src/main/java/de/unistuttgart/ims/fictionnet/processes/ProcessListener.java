package de.unistuttgart.ims.fictionnet.processes;

/**
 * Interface for process listener
 * 
 * @author Lukas Rieger, Erol Aktay
 * @version 14-10-2015
 */
public interface ProcessListener {
	/**
	 * The method that gets called when the process status changes
	 * 
	 * @param event The event that caused the call
	 */
	public void dispatch(ProcessStatusChangedEvent event);
}
