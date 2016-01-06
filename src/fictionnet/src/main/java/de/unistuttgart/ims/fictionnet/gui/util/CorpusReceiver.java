package de.unistuttgart.ims.fictionnet.gui.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.ImportTab;
import de.unistuttgart.ims.fictionnet.imp.TextImportProcess;
import de.unistuttgart.ims.fictionnet.processes.ProcessListener;
import de.unistuttgart.ims.fictionnet.processes.ProcessManager;
import de.unistuttgart.ims.fictionnet.processes.ProcessStatusChangedEvent;
import de.unistuttgart.ims.fictionnet.processes.ProcessStatusType;

/**
 * Custom receiver.
 *
 * @author Roman, Erol Aktay
 */
@SuppressWarnings({ "PMD.AtLeastOneConstructor", "PMD.CyclomaticComplexity", "PMD.DataflowAnomalyAnalysis" })
public class CorpusReceiver implements Receiver, SucceededListener {

	private static final String COULD_NOT_UPLOAD = "COULD_NOT_UPLOAD";
	private static final String TEMP_DIRECTORY = "temp";
	private transient Corpus corpus;
	private transient File file;

	/**
	 * Sets the destination project, the corpus should be added to.
	 *
	 * @param corpus
	 *            {@link Corpus}
	 */
	public void setCorpus(final Corpus corpus) {
		this.corpus = corpus;
	}

	@Override
	public void uploadSucceeded(final SucceededEvent event) {

		// Start time for duration
		final long start = Calendar.getInstance().getTimeInMillis();

		// Init analyzing process.
		final TextImportProcess process = new TextImportProcess(file, corpus);

		// Add process to manager..
		ProcessManager.getTheInstance().addProcess(process, FictionManager.getUser());
		updateProcess(process);

		// Add listener to process.
		process.addListener(new ProcessListener() {

			@Override
			public void dispatch(final ProcessStatusChangedEvent event) {
				/*
				 * FIXME This could stay alive through browser sessions so references to objects in this very instance
				 * would be kept alive (or maybe not). After a new browser session, a new instance of everything would
				 * be created but only the old instance would be updated
				 */

				// Delete temp file, if process status is FINNISHED or INPUT_ERROR.
				// not other errors, as we may want the user try to repeat the import
				if (event.getProcessStatus() == ProcessStatusType.INPUT_ERROR
						|| event.getProcessStatus() == ProcessStatusType.FINISHED
						|| event.getProcessStatus() == ProcessStatusType.ERROR
						|| event.getProcessStatus() == ProcessStatusType.INTERNAL_ERROR
						|| event.getProcessStatus() == ProcessStatusType.CANCELED) {
					file.delete();
				}

				// Change status of process in upload table.
				updateProcess(process);
			}
		});

		// Start process.
		process.start();

		// Start process time counter.
		@SuppressWarnings("PMD.DoNotUseThreads")
		final Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (process.getStatus() == ProcessStatusType.PROCESSING
						|| process.getStatus() == ProcessStatusType.SAVING) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Interupted upload duration counter.",
								e);
					}

					final long duration = (Calendar.getInstance().getTimeInMillis() - start);

					process.setDuration(duration);
					updateProcess(process);

				}

			}

		});

		thread.start();
	}

	/**
	 * Updates all UIs.
	 * 
	 * @param process
	 *            - Process to update.
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	private void updateProcess(final TextImportProcess process) {
		for (final UI ui : VaadinSession.getCurrent().getUIs()) {
			((ImportTab) TabFactory.getTab(Path.IMPORT, ui)).updateProcess(process);
		}
	}

	@Override
	@SuppressWarnings({ "PMD.AvoidInstantiatingObjectsInLoops", "PMD.OnlyOneReturn" })
	public OutputStream receiveUpload(final String filename, final String mimeType) {
		try {
			// Set path to temp directory of user.
			final StringBuilder tempDirectory =
					new StringBuilder(FictionManager.getBasepath() + File.separator + TEMP_DIRECTORY);

			// Create temp directory, if it doesn't exist
			if (!new File(tempDirectory.toString()).exists()) {
				new File(tempDirectory.toString()).mkdir();
			}
			/*tempDirectory.append(File.separator + FictionManager.getUser());
			// Create user directory, if it doesn't exist
			if (!new File(tempDirectory.toString()).exists()) {
				new File(tempDirectory.toString()).mkdir();
			}*/

			file = new File(tempDirectory + File.separator + filename);
			// make sure no existing file gets used
			while (file.exists()) {
				file = new File(file.getAbsoluteFile() + "-");
			}
			file.createNewFile();

			return new FileOutputStream(file);

		} catch (final IOException e) {
			Notification.show(FictionManager.getLocalization().getValue(COULD_NOT_UPLOAD), e.getMessage(),
					Notification.Type.ERROR_MESSAGE);
			Logger.getLogger(getClass().getName()).log(Level.WARNING, "IOException on upload.", e);
			file.delete();
			return null;
		}
	}
}