package de.unistuttgart.ims.fictionnet.processes;

/**
 * A basic Process implementation for testing purposes
 * 
 * @author Erol Aktay
 */
public class ProcessImplementation extends Process {
	public boolean keepRunning = true;

	public Thread getThread() {
		return thread;
	}

	@Override
	public void run() {
		while (keepRunning) {
		}
		setStatus(ProcessStatusType.FINISHED, "Test");
		setProgress((float) 1.0);
	}

}
