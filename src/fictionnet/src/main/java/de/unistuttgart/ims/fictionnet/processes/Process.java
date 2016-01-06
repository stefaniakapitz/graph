package de.unistuttgart.ims.fictionnet.processes;

import java.util.ArrayList;

/**
 * Provides a thread for import and export activities. Can be observed by a
 * listener.
 * 
 * @author Lukas Rieger, Erol Aktay
 * @version 24-10-2015
 */
public abstract class Process implements Runnable {

	private ProcessStatusType status;

	private float progress;
	private final ArrayList<ProcessListener> listeners = 
			new ArrayList<>();
	protected Thread thread;

	public Process() {
		setStatus(ProcessStatusType.READY, null);
		setProgress(0);
		thread = new Thread(this);
	}
	/**
	 * Add a listener to observe thread activity.
	 *
	 * @param listener
	 */
	public void addListener(ProcessListener listener) {
		listeners.add(listener);
	}

	/**
	 * Unregister a listener.
	 *
	 * @param listener
	 */
	public void removeListener(ProcessListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Informs all the listeners of the process about a change.
	 *
	 * @param event
	 */
	protected void fire(ProcessStatusChangedEvent event) {
		for (ProcessListener listener : listeners) {
			listener.dispatch(event);
		}
	}

	/**
	 * Returns the current status of the process.
	 *
	 * @return status
	 */
	public ProcessStatusType getStatus() {
		return status;
	}

	/**
	 * Sets the new status and informs the listeners
	 *
	 * @param status
	 * @param message	Additional information for the new status, can be NULL
	 */
	public final void setStatus(ProcessStatusType status, String message) {
		this.status = status;
		ProcessStatusChangedEvent event = new ProcessStatusChangedEvent(status, message);
		this.fire(event);
	}

	/**
	 * Get current progress 
	 *
	 * @return progress From 0.0 (no progress at all) to 1.0 (finished)
	 */
	public float getProgress() {
		return progress;
	}

	/**
	 * Set current progress
	 *
	 * @param progress From 0.0 (no progress at all) to 1.0 (finished)
	 */
	public final void setProgress(float progress) {
		this.progress = progress;
	}
	
	/**
	 * Cancel current process
	 */
	public void cancel() {
		thread.interrupt();
		setStatus(ProcessStatusType.CANCELED, null);
	}
	
	/**
	 * Start the process asynchronously
	 */
	public void start() {
		setStatus(ProcessStatusType.PROCESSING, null);
		thread.start();
	}
	
	/**
	 * Actions to be run in the background
	 * Note that progress, status has to be updated in the overridden method,
	 * e.g. set to FINISHED at the end.
	 */
	@Override
	public abstract void run();
}
