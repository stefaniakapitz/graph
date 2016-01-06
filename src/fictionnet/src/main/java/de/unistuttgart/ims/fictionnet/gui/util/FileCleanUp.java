package de.unistuttgart.ims.fictionnet.gui.util;

import de.unistuttgart.ims.fictionnet.imp.TextImportProcess;
import de.unistuttgart.ims.fictionnet.processes.ProcessManager;
import de.unistuttgart.ims.fictionnet.processes.Process;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A singleton that runs a background thread which cleans up unassigned
 * temporary files
 *
 * @author Erol Aktay
 */
public class FileCleanUp {

	private static FileCleanUp theInstance;

	// private for singleton pattern
	private FileCleanUp() {
		new CleanUpThread().start();
	}

	/**
	 * This method returns the only instance of this class.
	 *
	 * @return theInstance
	 */
	public synchronized static FileCleanUp getTheInstance() {
		if (theInstance == null) {
			theInstance = new FileCleanUp();
		}
		return theInstance;
	}

	public class CleanUpThread extends Thread {
		@Override
		public void run() {
			File temp = new File(FictionManager.getBasepath() +
					File.separator + "temp");
			for (;;) {
				try {
					sleep(3600000); // an hour
				} catch (InterruptedException ex) {
					Logger.getLogger(FileCleanUp.class.getName()).log(Level.SEVERE, null, ex);
				}
				List<File> usedFiles = getAllUsedFiles();
				for (File file : temp.listFiles()) {
					if (!file.getName().startsWith(".")
							&& !usedFiles.contains(file)) {
						file.delete();
					}
				}
			}
		}

		private List<File> getAllUsedFiles() {
			ArrayList<File> files = new ArrayList<>();
			for (Process proc : ProcessManager.getTheInstance().getProcesses()) {
				switch (proc.getStatus()) {
					case PROCESSING:
					case SAVING:
					case WAITING:
					case READY:
						if (proc instanceof TextImportProcess) {
							files.add(((TextImportProcess) proc).getFile());
						}
					default:
						break;
				}
			}
			return files;
		}
	}
}
