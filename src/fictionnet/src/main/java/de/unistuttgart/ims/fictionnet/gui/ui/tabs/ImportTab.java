package de.unistuttgart.ims.fictionnet.gui.ui.tabs;

import java.io.File;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.ChangeEvent;
import com.vaadin.ui.Upload.ChangeListener;
import com.vaadin.ui.Upload.FinishedListener;
import com.vaadin.ui.Upload.StartedEvent;
import com.vaadin.ui.Upload.StartedListener;
import com.vaadin.ui.VerticalLayout;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.gui.util.CorpusReceiver;
import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;
import de.unistuttgart.ims.fictionnet.gui.util.Path;
import de.unistuttgart.ims.fictionnet.imp.TextImportProcess;
import de.unistuttgart.ims.fictionnet.processes.Process;
import de.unistuttgart.ims.fictionnet.processes.ProcessManager;
import de.unistuttgart.ims.fictionnet.processes.ProcessStatusType;

/**
 * Tab for the visual representation of the import process.
 *
 * @author Roman, Erol Aktay, Erik-Felix Tinsel
 */
@SuppressWarnings("PMD.TooManyMethods")
public class ImportTab extends AbstractTab {

	private final Object GUI_INITIALIZATION_MONITOR = new Object();
	private boolean pauseThreadFlag = false;
	private static final String ESTIMATED_DURATION = "ESTIMATED_DURATION";
	private static final String DURATION = "DURATION";
	private static final String STATUS = "STATUS";
	private static final String PROJECT = "PROJECT";
	private static final String CORPUS = "CORPUS";
	private static final String IMPORT_BUTTON = "IMPORT_BUTTON";
	private static final String NEW_PROJECT = "NEW_PROJECT";
	private static final String NEW_CORPUS = "NEW_CORPUS";
	private static final String PROCESSING = "PROCESSING";
	private static final String DESTINATION = "DESTINATION";
	private static final String OPTIONS = "OPTIONS";
	private static final String REMOVE_ENTRY = "REMOVE_ENTRY";
	private static final String REMOVE = "REMOVE";
	private static final String PAUSE_ENTRY = "PAUSE_ENTRY";
	private static final String PAUSE = "PAUSE";
	private static final String FILE = "FILE";
	@SuppressWarnings("PMD.LongVariable")
	private transient ComboBox projectDestination;
	private transient ComboBox corpusDestination;
	private transient Button importButton;
	private final transient HorizontalLayout buttonLayout;
	private final transient VerticalLayout overallLayout;
	private transient Upload uploader;
	private transient Table table;
	private static String newProjectName;
	private static String newCorpusName;
	private static int maxProjectNameLength = 35;

	/**
	 * Default Constructor.
	 *
	 * @param path
	 *            - {@link Path} that leads to this tab.
	 */
	public ImportTab(final Path path) {
		super(path);

		final HorizontalLayout buttonLayout = new HorizontalLayout();
		final VerticalLayout overallLayout = new VerticalLayout();
		initLayout(overallLayout);

		overallLayout.setMargin(true);
		overallLayout.setSpacing(true);

		this.buttonLayout = buttonLayout;
		this.overallLayout = overallLayout;

	}

	/**
	 *
	 */
	private void addUploaderListener() {
		uploader.addChangeListener(new ChangeListener() {
			@Override
			public void filenameChanged(final ChangeEvent event) {
				updateForms(event.getFilename());
			}
		});

		uploader.addStartedListener(new StartedListener() {

			@Override
			public void uploadStarted(StartedEvent event) {
				updateForms("");
			}
		});

		// FIXME: see reloadUploader
		uploader.addFinishedListener(new FinishedListener() {
			@Override
			public void uploadFinished(Upload.FinishedEvent event) {
				reloadUploader();
			}
		});
	}

	@Override
	public void attach() {
		super.attach();

		initComponents();
		initListeners();
	}

	/**
	 * Returns the extension without the dot of a file.
	 *
	 * @param filename
	 *            String
	 * @return Extension of a file.
	 */
	private String getExtension(final String filename) {
		final int lastSeparator = filename.lastIndexOf('.');
		final String name = filename.substring(lastSeparator + 1);

		return name;
	}

	/**
	 * calculates the maximal number to append to string "New project" by
	 * iterating over the combobox list.
	 *
	 * @return the max digit of the found string that matches "New project
	 *         digit"
	 */
	private int getNewProjectNumber() {
		int maxNumber = 0;
		int currNumber;
		for (final Object comboboxItems : projectDestination.getItemIds()) {
			if (comboboxItems.toString().matches("^" + getLocal(NEW_PROJECT) + "\\s?\\d+?$")) {
				if (comboboxItems.toString().equals(getLocal(NEW_PROJECT))) {
					currNumber = 1;
				} else {
					currNumber = Integer.parseInt(comboboxItems.toString().replaceAll("\\D+", ""));
				}
				if (currNumber > maxNumber) {
					maxNumber = currNumber;
				}
			}
		}
		maxNumber += 1;
		return maxNumber;
	}

	/**
	 * calculates the maximal number to append to string "New corpus" by
	 * iterating over the combobox list.
	 *
	 * @return the max digit of the found string that matches "New corpus digit"
	 */
	private int getNewCorpusNumber(Collection<Corpus> corporaCollection) {
		int maxNumber = 0;
		int currNumber;
		for (final Object comboboxItems : corporaCollection) {
			if (comboboxItems.toString().matches("^" + getLocal(NEW_CORPUS) + "\\s?\\d+?$")) {
				if (comboboxItems.toString().equals(getLocal(NEW_CORPUS))) {
					currNumber = 1;
				} else {
					currNumber = Integer.parseInt(comboboxItems.toString().replaceAll("\\D+", ""));
				}
				if (currNumber > maxNumber) {
					maxNumber = currNumber;
				}
			}
		}
		maxNumber += 1;
		return maxNumber;
	}

	/**
	 * Initialize components of the import tab.
	 */
	private void initComponents() {

		uploader = new Upload(getLocal(FILE), new CorpusReceiver());
		uploader.addSucceededListener((CorpusReceiver) uploader.getReceiver());
		uploader.setButtonCaption(null);
		overallLayout.addComponent(uploader);

		// project Combobox Setup
		projectDestination = new ComboBox(getLocal(PROJECT));
		projectDestination.setNewItemsAllowed(true);
		projectDestination.setNullSelectionAllowed(false);
		projectDestination.setEnabled(false);
		projectDestination.addItems(FictionManager.getUser().getOwnedProjects());
		projectDestination.addItems(FictionManager.getUser().getReceivedSharedProjects());

		// get the number for a new project and at it on top of the list
		final int maxNumber = getNewProjectNumber();
		newProjectName = getLocal(NEW_PROJECT) + " " + maxNumber;
		projectDestination.addItem(newProjectName);
		projectDestination.select(newProjectName);

		buttonLayout.addComponent(projectDestination);

		// corpus Combobox Setup
		corpusDestination = new ComboBox(getLocal(CORPUS));
		corpusDestination.setNewItemsAllowed(true);
		corpusDestination.setNullSelectionAllowed(false);
		corpusDestination.setEnabled(false);

		// put New Corpus 1 on top of the list
		newCorpusName = getLocal(NEW_CORPUS) + " " + 1;
		corpusDestination.addItem(newCorpusName);
		corpusDestination.select(newCorpusName);

		buttonLayout.addComponent(corpusDestination);

		overallLayout.addComponent(buttonLayout);

		importButton = new Button(getLocal(IMPORT_BUTTON));
		importButton.setEnabled(false);
		overallLayout.addComponent(importButton);

		// table Setup
		table = new Table(getLocal(STATUS));
		table.setImmediate(true);
		table.setSizeFull();
		overallLayout.addComponent(table);
		// Set Table Columns
		table.addContainerProperty(getLocal(FILE), String.class, "");
		table.addContainerProperty(getLocal(DESTINATION), String.class, "");

		table.addContainerProperty(getLocal(PROCESSING), String.class, ProcessStatusType.ERROR.toString());
		table.addContainerProperty(getLocal(DURATION), String.class, "");
		table.addContainerProperty(getLocal(ESTIMATED_DURATION), String.class, "");
		table.addContainerProperty(getLocal(OPTIONS), HorizontalLayout.class, "");
		reloadImports();

		// Give table all free space
		overallLayout.setExpandRatio(table, 1);
	}

	/**
	 *
	 */
	private void initListeners() {
		// Check, if file is valid and whats the extension.
		addUploaderListener();

		projectDestination.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				final Object value = projectDestination.getValue();

				// Add all corpora to corpus destinations.
				if (value == null) {
					corpusDestination.setEnabled(false);
					importButton.setEnabled(false);
				} else {
					Project project = null;

					if (!(value instanceof Project)) {

						// entry is a new project
						corpusDestination.removeAllItems();
						// newCorpusName = getLocal(NEW_CORPUS) + " " + 1;
						corpusDestination.addItem(newCorpusName);
						corpusDestination.select(newCorpusName);
						corpusDestination.setEnabled(true);
					} else {

						// entry is valid project
						project = FictionManager.getUser().getProject(value.toString());

						final List<Corpus> projectCorporas = project.getCorpora();
						final Collection<Corpus> corporaCollection = projectCorporas;
						// get the max number for a new corpus and at it on top
						// of the list
						/*
						 * final int maxNumber =
						 * getNewCorpusNumber(corporaCollection); newCorpusName
						 * = getLocal(NEW_CORPUS) + " " + maxNumber;
						 */
						corpusDestination.removeAllItems();
						corpusDestination.addItem(newCorpusName);
						corpusDestination.select(newCorpusName);

						corpusDestination.addItems(projectCorporas);

						corpusDestination.setEnabled(true);
					}

				}
			}

		});

		corpusDestination.addBlurListener(new BlurListener() {

			@Override
			public void blur(BlurEvent event) {

				final Object valueCorpus = corpusDestination.getValue();

				if (valueCorpus == null) {
					importButton.setEnabled(false);
				}

			}
		});

		// Add Button Listener
		importButton.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(final ClickEvent event) {

				final Object value = projectDestination.getValue();
				Project project = FictionManager.getUser().getProject(value.toString());

				if (project == null) {
					// project does not exist in db create new

					try {
						// create
						project = FictionManager.getUser().createProject(value.toString());

						// add new item to combobox
						projectDestination.removeItem(value);
						projectDestination.addItem(project);

						if (value.equals(newProjectName)) {
							// generic name used

							// add top element for new item creation
							newProjectName = getLocal(NEW_PROJECT) + " " + getNewProjectNumber();
							projectDestination.addItem(newProjectName);
							projectDestination.select(newProjectName);

						}
					} catch (SQLException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Could not create project on import window.", e);
					}

				}

				final Object valueCorpus = corpusDestination.getValue();

				// Add all corpora to corpus destinations.
				Corpus corpus = FictionManager.getUser().getProject(project.getId())
						.getCorpus(corpusDestination.getValue().toString());
				if (valueCorpus instanceof Corpus) {
					corpus = (Corpus) valueCorpus;
				}
				if (corpus == null) {
					try {

						corpus = project.createCorpus(valueCorpus.toString());
						corpusDestination.removeItem(valueCorpus);
						corpusDestination.addItem(corpus);

						if (valueCorpus.equals(newCorpusName)) {

							// put New Corpus 1 on top of the list
							newCorpusName = getLocal(NEW_CORPUS) + " " + 1;
							corpusDestination.addItem(newCorpusName);
							corpusDestination.select(newCorpusName);

						}
					} catch (SQLException e) {
						Logger.getLogger(getClass().getName()).log(Level.SEVERE,
								"Could not create corpus on import window.", e);
					}
				}

				((CorpusReceiver) uploader.getReceiver()).setCorpus(corpus);

				uploader.submitUpload();

			}
		});
	}

	/**
	 * Reloads the content of the import table.
	 */
	@SuppressWarnings({ "PMD.DataflowAnomalyAnalysis", "PMD.AvoidInstantiatingObjectsInLoops" })
	public void reloadImports() {
		table.removeAllItems();

		for (final Process process : ProcessManager.getTheInstance().getUsersProcesses(FictionManager.getUser())) {
			if (process instanceof TextImportProcess) {
				final TextImportProcess textProcess = (TextImportProcess) process;

				updateProcess(textProcess);

			} else {
				table.addItem(process.toString());
			}
		}
	}

	/**
	 * FIXME: Ugly hack that creates a new Upload component and replaces the old
	 * to work around Upload stopping reacting to events forever after an upload
	 * has begun (which seems to be caused by Upload never registering an upload
	 * as finished even when successful).
	 */
	public void reloadUploader() {
		final Upload newUploader = new Upload(getLocal(FILE), new CorpusReceiver());
		overallLayout.replaceComponent(uploader, newUploader);
		uploader = newUploader;
		uploader.addSucceededListener((CorpusReceiver) uploader.getReceiver());
		uploader.setButtonCaption(null);

		updateForms("");
		addUploaderListener();
	}

	/**
	 * Update forms based on selected file to import.
	 *
	 * @param fileName
	 *            - Name of file.
	 */
	private void updateForms(final String fileName) {
		if (fileName.isEmpty()) {
			// No File is selected. Disable all.
			projectDestination.setEnabled(false);
			corpusDestination.setEnabled(false);
			importButton.setEnabled(false);
		} else if (getExtension(fileName).equals("fiction")) {
			// Project file is selected. TODO: Not implemented yet.
			projectDestination.setEnabled(false);
			corpusDestination.setEnabled(false);
			// FIXME: Enable, when ProjectImportProcess is implemented.
			importButton.setEnabled(false);
		} else if (getExtension(fileName).equals("xml")) {
			// TEI-File is selected. Enable project, if it's not already
			// enabled.
			projectDestination.setEnabled(true);
			corpusDestination.setEnabled(true);
			corpusDestination.removeItem(newCorpusName);

			// split the filename into pieces, that the corpusname will be the
			// filename
			
			newCorpusName = fileName.split("\\.")[0];
			newCorpusName = newCorpusName
					.substring(newCorpusName.lastIndexOf(File.separator, newCorpusName.length()) + 1);
			if (newCorpusName.length() > maxProjectNameLength) {
				newCorpusName = newCorpusName.substring(0, maxProjectNameLength);
			}
			corpusDestination.addItem(newCorpusName);
			corpusDestination.select(newCorpusName);
			importButton.setEnabled(true);
		} else {
			// Not supported file extension. Disable all.
			Notification.show("WRONG_FILE_TYPE", Type.WARNING_MESSAGE);
			projectDestination.setEnabled(false);
			corpusDestination.setEnabled(false);
			importButton.setEnabled(false);
		}

		// Reload projects, if projectDestination is enabled.
		if (projectDestination.isEnabled()) {
			projectDestination.removeAllItems();
			projectDestination.addItem(newProjectName);
			projectDestination.select(newProjectName);
			projectDestination.addItems(FictionManager.getUser().getOwnedProjects());
			projectDestination.addItems(FictionManager.getUser().getReceivedSharedProjects());

		}
	}

	/**
	 * Updates a single process. Only works, if process was already added to
	 * table.
	 *
	 * @param process
	 *            - {@link TextImportProcess}
	 */
	@SuppressWarnings({ "unchecked", "PMD.DoNotUseThreads" })
	public void updateProcess(final TextImportProcess process) {

		if (process == null) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"Process of ImportTab.updateProcess(TextImportProcess) is null!");
			return;
		}

		for (UI ui : VaadinSession.getCurrent().getUIs()) {

			if (ui.isAttached()) {
				ui.access(new Runnable() {
					@Override
					public void run() {
						FictionUI.getCurrent().setPollInterval(500); // 0.5
																		// seconds.

						Item item = table.getItem(process.toString());

						if (item == null) {
							item = table.addItem(process.toString());
							item.getItemProperty(getLocal(FILE)).setValue(process.getFile().getName());
							item.getItemProperty(getLocal(DESTINATION)).setValue(process.getCorpus().toString());
							setupOptionsPanel(item);
						}
						final Integer[] progress = DBAccessManager.getTheInstance().getProgress(process.getText());
						final String processing = item.getItemProperty(getLocal(PROCESSING)).getValue().toString();
						final int start = processing.lastIndexOf(' ');
						final int end = processing.lastIndexOf(':');

						if (start != -1 && end != -1) {
							final Integer lastProgress = Integer.parseInt(processing.substring(start + 1, end));

							if (progress[0] - lastProgress != 0) {
								final String estimatedDuration = getTime((long) process.getDuration()
										+ ((progress[1] - progress[0]) / (progress[0] - lastProgress)) * 1000);

								item.getItemProperty(getLocal(ESTIMATED_DURATION)).setValue(estimatedDuration);
							}
						}

						if (progress == null) {
							item.getItemProperty(getLocal(PROCESSING))
									.setValue(getLocal(process.getStatus().toString()));
						} else if (process.getStatus() == ProcessStatusType.FINISHED) {
							item.getItemProperty(getLocal(PROCESSING))
									.setValue(String.format(getLocal(process.getStatus().toString()),
											process.getText().getLayerContainer().getActLayer().getActs().size(),
											process.getText().getLayerContainer().getSceneLayer().getScenes().size(),
											process.getText().getCastList().size()));
						} else {
							item.getItemProperty(getLocal(PROCESSING)).setValue(
									getLocal(process.getStatus().toString()) + " " + progress[0] + ":" + progress[1]);
						}

						final String duration = getTime((long) process.getDuration());

						item.getItemProperty(getLocal(DURATION)).setValue(duration);

						// Disable polling
						FictionUI.getCurrent().setPollInterval(30000); // 30
						// seconds.
					}

					/**
					 * sets up the options panel in the importlist
					 * 
					 * @param item
					 *            the column where the options take place in
					 */
					private void setupOptionsPanel(Item item) {

						HorizontalLayout buttonLayout = new HorizontalLayout();

						// remove button
						Button remove = new NativeButton();
						Page.getCurrent().getStyles()
								.add(".v-nativebutton-removeFromImportList {border: none; background: transparent}");
						remove.addStyleName("removeFromImportList");
						remove.setIcon(FictionManager.getIconResource(REMOVE), getLocal(REMOVE_ENTRY));
						remove.addClickListener(new ClickListener() {

							@Override
							public void buttonClick(ClickEvent event) {
								table.removeItem(process.toString());

							}
						});

						buttonLayout.addComponent(remove);
						item.getItemProperty(getLocal(OPTIONS)).setValue(buttonLayout);

					}

					/**
					 * Formats a
					 *
					 * @param process
					 * @return
					 */
					@SuppressWarnings({ "PMD.UseStringBufferForStringAppends", "PMD.AvoidReassigningParameters" })
					private String getTime(long milliseconds) {

						final long days = TimeUnit.MILLISECONDS.toDays(milliseconds);
						milliseconds -= TimeUnit.DAYS.toMillis(days);
						final long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
						milliseconds -= TimeUnit.HOURS.toMillis(hours);
						final long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
						milliseconds -= TimeUnit.MINUTES.toMillis(minutes);
						final long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);

						final StringBuilder duration = new StringBuilder();
						if (days != 0) {
							duration.append(days).append("d ");
						}
						if (hours != 0) {
							duration.append(hours).append("h ");
						}
						if (minutes != 0) {
							duration.append(minutes).append("m ");
						}
						if (seconds != 0) {
							duration.append(seconds).append('s');
						}

						return duration.toString();
					}
				});
			}

		}

	}
}
