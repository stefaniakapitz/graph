package de.unistuttgart.ims.fictionnet.gui.components;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import com.wcs.wcslib.vaadin.widget.recaptcha.ReCaptcha;
import com.wcs.wcslib.vaadin.widget.recaptcha.shared.ReCaptchaOptions;

import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.export.ProjectExportProcess;
import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.TextViewTab;
import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;
import de.unistuttgart.ims.fictionnet.gui.util.Path;
import de.unistuttgart.ims.fictionnet.gui.util.SendEmail;
import de.unistuttgart.ims.fictionnet.gui.util.TabFactory;
import de.unistuttgart.ims.fictionnet.users.Role;
import de.unistuttgart.ims.fictionnet.users.User;

/**
 * Creates a Menubar for the project Menu. FIXME: Check complexity and god
 * class.
 * 
 * @author Roman, Erik-Felix Tinsel
 */
@SuppressWarnings({ "PMD.LongVariable", "PMD.CyclomaticComplexity",
		"PMD.StdCyclomaticComplexity", "PMD.TooManyMethods", "PMD.GodClass",
		"PMD.ModifiedCyclomaticComplexity", "PMD.ExcessiveImports" })
class ProjectMenuBar extends AbstractLocalizedCustomComponent {

	
	// Language strings
	private static final String NUMBER_OF_PROJECTS = "NUMBER_OF_PROJECTS";
	private static final String USER = "USER";
	private static final String EMAILS = "EMAILS";
	private static final String ADDEMAILS = "ADDEMAILS";
	private static final String REMOVE_EMAILS = "REMOVEEMAILS";
	private static final String EMAILSINFO = "EMAILSINFO";
	private static final String EMAILINVALID = "EMAILINVALID";
	private static final String OWNEMAILERROR = "OWNEMAILERROR";
	private static final String ADMINEMAILERROR = "ADMINEMAILERROR";
	private static final String USERNOTEXISTENT = "USERNOTEXISTENT";
	private static final String CAPTCHA_WRONG = "CAPTCHA_WRONG";
	private static final String SELECTEDEMAILS = "SELECTEDEMAILS";
	private static final String AUTHOR = "AUTHOR";
	private static final String PROJECT_CREATION_DATE = "PROJECT_CREATION_DATE";
	private static final String OWNER = "OWNER";
	private static final String TEXT_NAME = "TEXT_NAME";
	private static final String CORPUS_NAME = "CORPUS_NAME";
	private static final String INFO_WINDOW = "INFO_WINDOW";
	private static final String PROJECT_NAME_REQUIRED = "PROJECT_NAME_REQUIRED";
	private static final String CORPUS_NAME_REQUIRED = "CORPUS_NAME_REQUIRED";
	private static final String EXPORT = "EXPORT";
	private static final String EXPORT_PROJECT = "EXPORT_PROJECT";
	private static final String SHARE_PROJECT = "SHARE_PROJECT";
	private static final String INFO = "INFO";
	private static final String REMOVE_PROJECT = "REMOVE_PROJECT";
	private static final String INVALID_PROJECT_NAME = "INVALID_PROJECT_NAME";
	private static final String SQL_ERROR = "SQL_ERROR";
	private static final String CREATE_PROJECT = "CREATE_PROJECT";
	private static final String PROJECT_NAME = "PROJECT_NAME";
	private static final String NEW_PROJECT = "NEW_PROJECT";
	private static final String NEW_SHARED_PROJECT = "NEW_SHARED_PROJECT";
	private static final String CLONE_PROJECT_EMAIL_TITLE = "CLONE_PROJECT_EMAIL_TITLE";
	private static final String CLONE_PROJECT_EMAIL_TEXT = "CLONE_PROJECT_EMAIL_TEXT";
	private static final String PLEASE_CONFIRM = "PLEASE_CONFIRM";
	private static final String DELETE_CONFIRMATION_1 = "DELETE_CONFIRMATION_1";
	private static final String DELETE_CONFIRMATION_2 = "DELETE_CONFIRMATION_2";
	private static final String BASE_PROJECT = "BASE_PROJECT";
	private static final String BASE_CORPUS = "BASE_CORPUS";
	private static final String BASE_TEXT = "BASE_TEXT";
	private static final String YES = "YES";
	
	// Images
	private static final String RECYCLE_BIN = "RECYCLE_BIN";
	private static final String ADD_EMAIL = "ADD_EMAIL";
	
	@SuppressWarnings("PMD.ShortVariable")
	private static final String NO = "NO";

	private transient MenuItem add;
	private transient MenuItem remove;
	private transient MenuItem info;
	private transient MenuItem share;
	private transient MenuItem export;
	private final transient MenuBar menubar;
	private transient FileDownloader downloader;
	private final transient ProjectMenu projectMenu;

	/**
	 * Default Constructor.
	 * 
	 * @param projectMenu
	 *            - {@link ProjectMenu}
	 */
	ProjectMenuBar(final ProjectMenu projectMenu) {
		super();
		this.projectMenu = projectMenu;
		menubar = new MenuBar();

		setCompositionRoot(menubar);
		setSizeFull();

	}

	/**
	 * Fills the Toolbar with buttons.
	 */
	protected void fillToolbar() {

		// Add new Project.
		initAddProject();

		add.setCheckable(true);
		add.setDescription(getLocal(NEW_PROJECT));
		// Remove existing Project.
		initRemove();
		remove.setDescription(getLocal(REMOVE_PROJECT));

		// Show info to project, corpus, text or user.
		initInfo();

		info.setCheckable(true);
		info.setDescription(getLocal(INFO));

		// Share project with other user.
		initShare();

		share.setCheckable(true);
		share.setEnabled(false);
		share.setDescription(getLocal(SHARE_PROJECT));

		// Export project.
		initExport();

		export.setDescription(getLocal(EXPORT_PROJECT));

		projectMenu.getProjectTree().addValueChangeListener(
				new Property.ValueChangeListener() {

					@Override
					public void valueChange(
							final Property.ValueChangeEvent event) {
						toggleMenuItemEnabled();
					}

				});

		// Enable MenuItems
		toggleMenuItemEnabled();

		markAsDirty();
	}

	/**
	 * Creates a pop-up window and wait for user input to create a new
	 * {@link Project}. Pop-up closes automatic, when project was created or
	 * user closes it manually. *
	 */
	@SuppressWarnings("PMD.ExcessiveMethodLength")
	// FIXME
	private void initAddProject() {
		add = menubar.addItem("", FictionManager.getIconResource(NEW_PROJECT),
				new MenuBar.Command() {

					@Override
					public void menuSelected(final MenuBar.MenuItem selectedItem) {
						FictionUI.getCurrent().closeWindows();

						if (selectedItem.isChecked()) {
							// Create layout and pop-up window
							final FormLayout layout = new FormLayout();
							final Window newProject = new Window(
									getLocal(NEW_PROJECT), layout);
							newProject.getContent().setSizeUndefined();
							newProject.setPosition(50, 50);

							newProject
									.addCloseListener(new Window.CloseListener() {

										@Override
										public void windowClose(
												final Window.CloseEvent event) {
											add.setChecked(false);
										}
									});

							FictionUI.getCurrent().addWindow(newProject);

							layout.setMargin(true);
							layout.setSpacing(true);

							// Create Textfield name
							final TextField name = new TextField(
									getLocal(PROJECT_NAME));
							name.focus();
							name.addBlurListener(new BlurListener() {

								@Override
								public void blur(final BlurEvent event) {
									name.setRequired(true);
									name.setRequiredError(getLocal(PROJECT_NAME_REQUIRED));
									name.setValidationVisible(true);
								}
							});
							name.addValidator(new Validator() {

								@Override
								public void validate(final Object value)
										throws InvalidValueException {
									if (FictionManager.getUser().getProject(
											(String) name.getValue()) != null) {
										// Notification.show(getLocal(INVALID_PROJECT_NAME),
										// Type.WARNING_MESSAGE);
										throw new InvalidValueException(
												getLocal(INVALID_PROJECT_NAME));
									}
								}
							});

							name.setImmediate(true);
							name.setValidationVisible(true);

							layout.addComponent(name);

							final Button createProject = new Button(
									getLocal(CREATE_PROJECT));
							createProject.setClickShortcut(KeyCode.ENTER);
							layout.addComponent(createProject);

							createProject
									.addClickListener(new Button.ClickListener() {

										@Override
										public void buttonClick(
												final Button.ClickEvent event) {

											try {

												name.setRequired(true);
												name.setRequiredError(getLocal(PROJECT_NAME_REQUIRED));
												name.validate();

												final Project project = FictionManager
														.getUser()
														.createProject(
																name.getValue());

												projectMenu.getProjectTree()
														.addItem(project);
												projectMenu.getProjectTree()
														.setChildrenAllowed(
																project, false);
												newProject.close();

											} catch (
													UnsupportedOperationException
													| SQLException e) {
												Logger.getLogger(
														getClass().getName())
														.log(Level.SEVERE,
																"Could not create new project.",
																e);
												Notification
														.show(getLocal(SQL_ERROR),
																Notification.Type.ERROR_MESSAGE);

											} catch (final Validator.InvalidValueException ive) {
												Notification.show(
														ive.getMessage(),
														Notification.Type.WARNING_MESSAGE);
											}

										}
									});
						}

					}
				});

	}

	/**
	 * Removes an item from the projectTree and database.
	 */
	private void initRemove() {
		remove = menubar.addItem("",
				FictionManager.getIconResource(REMOVE_PROJECT),
				new MenuBar.Command() {

					@Override
					@SuppressWarnings("PMD.AvoidCatchingGenericException")
					// FIXME: Throw specific Exception, when exception is
					// changed in Project.deleteCorpus()
					public void menuSelected(final MenuBar.MenuItem selectedItem) {
						FictionUI.getCurrent().closeWindows();

						final Object item = projectMenu.getProjectTree()
								.getValue();

						if (item != null) {

							if (item instanceof Project) {

								showConfirmationDialog("project",
										(Project) item, null, null);

							} else if (item instanceof Corpus) {

								showConfirmationDialog("corpus", null,
										(Corpus) item, null);

							} else if (item instanceof Text) {

								showConfirmationDialog("text", null, null,
										(Text) item);

							}

						}
					}

				});
	}

	/**
	 * create dialogs for deletion.
	 * 
	 * @param type
	 *            determines which case to delete project,corpus,text
	 * @param project
	 *            the given project, null if none
	 * @param corpus
	 *            the given corpus, null if none
	 * @param text
	 *            the given text, null if none
	 */
	private void showConfirmationDialog(final String type,
			final Project project, final Corpus corpus, final Text text) {
		switch (type) {
		case "project":
			final String projectName = project.getProjectName();
			// Using custom captions.
			ConfirmDialog.show(FictionUI.getCurrent(),
					getLocal(PLEASE_CONFIRM), getLocal(DELETE_CONFIRMATION_1)
							+ " " + getLocal(BASE_PROJECT) + " " + projectName
							+ " " + getLocal(DELETE_CONFIRMATION_2),
					getLocal(YES), getLocal(NO), new ConfirmDialog.Listener() {

						public void onClose(final ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								final User user = FictionManager.getUser();
								try {
									user.deleteProject(project);
									// Reload projectTree to remove deleted
									// items.
									projectMenu.reloadTree();
									// remove text from textview tab
									final TextViewTab textViewTab = (TextViewTab) TabFactory
											.getTab(Path.TEXTVIEW,
													FictionUI.getCurrent());
									textViewTab.clear();
									textViewTab.reloadComponents();

								} catch (final SQLException e) {
									Notification.show(getLocal(SQL_ERROR),
											Notification.Type.ERROR_MESSAGE);
									Logger.getLogger(getClass().getName()).log(
											Level.SEVERE,
											"Project deletion failure.", e);
								}

							}
						}
					});
			break;
		case "corpus":
			// Using custom captions.
			ConfirmDialog.show(
					FictionUI.getCurrent(),
					getLocal(PLEASE_CONFIRM),
					getLocal(DELETE_CONFIRMATION_1) + " "
							+ getLocal(BASE_CORPUS) + " " + " "
							+ corpus.getCorpusName() + " "
							+ getLocal(DELETE_CONFIRMATION_2), getLocal(YES),
					getLocal(NO), new ConfirmDialog.Listener() {

						public void onClose(final ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								final Project project = (Project) projectMenu
										.getProjectTree().getParent(corpus);
								try {
									project.deleteCorpus((Corpus) corpus);
									// Reload projectTree to remove deleted
									// items.
									projectMenu.reloadTree();
									// remove text from textview tab
									final TextViewTab textViewTab = (TextViewTab) TabFactory
											.getTab(Path.TEXTVIEW,
													FictionUI.getCurrent());
									textViewTab.clear();
									textViewTab.reloadComponents();
								} catch (final SQLException e) {
									Notification.show(getLocal(SQL_ERROR),
											Notification.Type.ERROR_MESSAGE);
									Logger.getLogger(getClass().getName()).log(
											Level.SEVERE,
											"Corpus deletion failure.", e);
								}

							}
						}
					});
			break;
		case "text":
			// Using custom captions.
			ConfirmDialog.show(FictionUI.getCurrent(),
					getLocal(PLEASE_CONFIRM),
					getLocal(DELETE_CONFIRMATION_1) + " " + getLocal(BASE_TEXT)
							+ " " + " " + text.getTextName() + " "
							+ getLocal(DELETE_CONFIRMATION_2), getLocal(YES),
					getLocal(NO), new ConfirmDialog.Listener() {

						public void onClose(final ConfirmDialog dialog) {
							if (dialog.isConfirmed()) {
								final Corpus corpus = (Corpus) projectMenu
										.getProjectTree().getParent(text);
								try {
									corpus.deleteText((Text) text);
									// Reload projectTree to remove deleted
									// items.
									projectMenu.reloadTree();
									// remove text from textview tab
									final TextViewTab textViewTab = (TextViewTab) TabFactory
											.getTab(Path.TEXTVIEW,
													FictionUI.getCurrent());
									textViewTab.clear();
									textViewTab.reloadComponents();
								} catch (final SQLException e) {
									Notification.show(getLocal(SQL_ERROR),
											Notification.Type.ERROR_MESSAGE);
									Logger.getLogger(getClass().getName()).log(
											Level.SEVERE,
											"Text deletion failure.", e);
								}

							}
						}
					});

			break;
		default:
			// Todo
		}

	}

	/**
	 * Creates a new popup window, that shows information to the selected item.
	 */
	@SuppressWarnings("PMD.ModifiedCyclomaticComplexity")
	private void initInfo() {
		info = menubar.addItem("", FictionManager.getIconResource(INFO),
				new MenuBar.Command() {

					@Override
					public void menuSelected(final MenuBar.MenuItem selectedItem) {
						FictionUI.getCurrent().closeWindows();

						if (selectedItem.isChecked()) {

							final Object object = projectMenu.getProjectTree()
									.getValue();

							// Check, if Object is selected in projectMenu.
							if (object == null) {
								return;
							}

							// Init Layout
							final FormLayout layout = new FormLayout();
							layout.setMargin(true);
							layout.setSpacing(true);

							// Init pop up
							final Window infoWindow = new Window(
									getLocal(INFO_WINDOW), layout);
							infoWindow.getContent().setSizeUndefined();
							infoWindow.setPosition(150, 50);
							infoWindow
									.addCloseListener(new Window.CloseListener() {
										@Override
										public void windowClose(
												final Window.CloseEvent event) {

											info.setChecked(false);
										}
									});

							FictionUI.getCurrent().addWindow(infoWindow);

							// Init project, corpus and text

							if (object instanceof User) {
								final User user = (User) object;
								// Add user name.
								final Label userNameLabel = new Label(user
										.getEmail());
								userNameLabel.setCaption(getLocal(USER));
								layout.addComponent(userNameLabel);

								// To prevend nullpointer
								int size;
								if (projectMenu.getProjectTree().hasChildren(
										user)) {
									size = projectMenu.getProjectTree()
											.getChildren(user).size();
								} else {
									size = 0;
								}

								// Add number of projects.
								final Label numberOfProjects = new Label(
										Integer.toString(size));
								numberOfProjects
										.setCaption(getLocal(NUMBER_OF_PROJECTS));
								layout.addComponent(numberOfProjects);

							} else if (object instanceof Text) {
								final Text text = (Text) object;
								final Corpus corpus = (Corpus) projectMenu
										.getProjectTree().getParent(text);
								final Project project = (Project) projectMenu
										.getProjectTree().getParent(corpus);
								initInfoPopup(layout, project, corpus, text);

							} else if (object instanceof Corpus) {
								final Corpus corpus = (Corpus) object;
								final Project project = (Project) projectMenu
										.getProjectTree().getParent(corpus);
								initInfoPopup(layout, project, corpus, null);

							} else if (object instanceof Project) {
								final Project project = (Project) object;
								initInfoPopup(layout, project, null, null);

							} else {
								return;
							}

						}
					}

				});
	}

	/**
	 * Initializes the info popup labels.
	 * 
	 * @param layout
	 *            - Layout of the popup.
	 * @param project
	 *            - {@link Project}
	 * @param corpus
	 *            - {@link Corpus} or null
	 * @param text
	 *            - {@link Text} or null
	 */
	private void initInfoPopup(final FormLayout layout, final Project project,
			final Corpus corpus, final Text text) {

		// Add project name
		if (project != null) {
			initRenameProject(layout, project);
		}

		// Add corpus name
		if (corpus != null) {
			initRenameCorpus(layout, corpus, project);
		}

		if (text != null) {
			// Add text name
			final Label textNameLabel = new Label(text.getTextName());
			textNameLabel.setCaption(getLocal(TEXT_NAME));
			layout.addComponent(textNameLabel);

			// Add author name
			final Label authorLabel = new Label(text.getAuthorName());
			authorLabel.setCaption(getLocal(AUTHOR));
			layout.addComponent(authorLabel);

			// Add publishing date
			final Label publishingDateLabel = new Label(
					text.getPublishingDate());
			publishingDateLabel.setCaption(getLocal("PUBLISHING_DATE"));
			layout.addComponent(publishingDateLabel);
		}

		if (project != null) {
			// Add owner
			final Label ownerLabel = new Label(project.getOwnerEmail());
			ownerLabel.setCaption(getLocal(OWNER));
			layout.addComponent(ownerLabel);

			// Add creation date
			final Label creationDateLabel = new Label(
					project.getCreationDateString());
			layout.addComponent(creationDateLabel);
			creationDateLabel.setCaption(getLocal(PROJECT_CREATION_DATE));
		}
	}

	/**
	 * Initializes the rename textfield and button in the info popup.
	 * 
	 * @param layout
	 *            - Layout of the popup.
	 * @param corpus
	 *            the given {@link Corpus}.
	 * @param project
	 *            - The given {@link Project}.
	 */
	private void initRenameCorpus(final FormLayout layout, final Corpus corpus,
			final Project project) {

		final TextField corpusNameField = new TextField(getLocal(CORPUS_NAME));
		corpusNameField.setValue(corpus.getCorpusName());
		corpusNameField.setRequired(true);
		corpusNameField.setRequiredError(getLocal(CORPUS_NAME_REQUIRED));
		corpusNameField.addBlurListener(new BlurListener() {

			@Override
			public void blur(final BlurEvent event) {

				if (!(corpusNameField.getValue().equals(corpus.getCorpusName()))) {

					// projectname was changed
					try {

						corpusNameField.validate();
						corpusNameField.setImmediate(true);
						FictionManager.getUser().renameCorpus(project.getId(),
								corpusNameField.getValue(), corpus.getId());
						corpusNameField.setValidationVisible(false);
						projectMenu.reloadTree();

					} catch (UnsupportedOperationException | SQLException e) {
						Logger.getLogger(getClass().getName()).log(
								Level.SEVERE, "Could not rename new project.",
								e);
						Notification.show(getLocal(SQL_ERROR),
								Notification.Type.ERROR_MESSAGE);

					} catch (final Validator.InvalidValueException ive) {
						corpusNameField.setValidationVisible(true);
						Notification.show(ive.getMessage(),
								Notification.Type.WARNING_MESSAGE);
					}
				}
			}
		});

		corpusNameField.setValidationVisible(false);
		// Validate if given projectname is unique
		corpusNameField.addValidator(new Validator() {

			@Override
			public void validate(final Object value)
					throws InvalidValueException {

				if (FictionManager.getUser().getCorpus(project.getId(),
						(String) corpusNameField.getValue()) != null) {
					corpusNameField.setValidationVisible(true);
					throw new InvalidValueException(
							getLocal(INVALID_PROJECT_NAME));
				}
			}
		});

		layout.addComponent(corpusNameField);

	}

	/**
	 * Initializes the rename textfield and button in the info popup.
	 * 
	 * @param layout
	 *            - Layout of the popup.
	 * @param project
	 *            - {@link Project}
	 */
	private void initRenameProject(final FormLayout layout,
			final Project project) {

		final TextField projectNameField = new TextField(getLocal(PROJECT_NAME));
		projectNameField.setValue(project.getProjectName());
		projectNameField.setRequired(true);
		projectNameField.setRequiredError(getLocal(PROJECT_NAME_REQUIRED));
		projectNameField.addBlurListener(new BlurListener() {

			@Override
			public void blur(final BlurEvent event) {

				if (!(projectNameField.getValue().equals(project
						.getProjectName()))) {

					// projectname was changed
					try {

						projectNameField.validate();
						projectNameField.setImmediate(true);
						FictionManager.getUser().renameProject(
								projectNameField.getValue(), project.getId());
						projectNameField.setValidationVisible(false);
						projectMenu.reloadTree();

					} catch (UnsupportedOperationException | SQLException e) {
						Logger.getLogger(getClass().getName()).log(
								Level.SEVERE, "Could not rename new project.",
								e);
						Notification.show(getLocal(SQL_ERROR),
								Notification.Type.ERROR_MESSAGE);

					} catch (final Validator.InvalidValueException ive) {
						projectNameField.setValidationVisible(true);
						Notification.show(ive.getMessage(),
								Notification.Type.WARNING_MESSAGE);
					}
				}
			}
		});

		projectNameField.setValidationVisible(false);
		// Validate if given projectname is unique
		projectNameField.addValidator(new Validator() {

			@Override
			public void validate(final Object value)
					throws InvalidValueException {

				if (FictionManager.getUser().getProject(
						(String) projectNameField.getValue()) != null) {
					projectNameField.setValidationVisible(true);
					throw new InvalidValueException(
							getLocal(INVALID_PROJECT_NAME));
				}
			}
		});

		layout.addComponent(projectNameField);

	}

	/**
	 * Initializes share menu.
	 */
	@SuppressWarnings("PMD.ExcessiveMethodLength")
	// FIXME
	private void initShare() {
		share = menubar.addItem("",
				FictionManager.getIconResource(SHARE_PROJECT),
				new MenuBar.Command() {

					@Override
					public void menuSelected(final MenuBar.MenuItem selectedItem) {
						FictionUI.getCurrent().closeWindows();

						if (selectedItem.isChecked()) {

							// Init Layout
							final HorizontalLayout emailLayout = new HorizontalLayout();
							final HorizontalLayout selectLayout = new HorizontalLayout();
							final FormLayout layout = new FormLayout();
							layout.setMargin(true);
							layout.setSpacing(true);

							// Init pop up
							final Window shareProjectWindow = new Window(
									getLocal(SHARE_PROJECT), layout);
							shareProjectWindow.getContent().setSizeUndefined();
							shareProjectWindow.setPosition(150, 50);
							shareProjectWindow
									.addCloseListener(new Window.CloseListener() {
										@Override
										public void windowClose(
												final Window.CloseEvent event) {

											share.setChecked(false);
										}
									});

							FictionUI.getCurrent()
									.addWindow(shareProjectWindow);

							// Add Elements

							// Project name Label
							final Project project = (Project) projectMenu
									.getProjectTree().getValue();
							final Label labelProjectName = new Label(project
									.getProjectName());

							// Textfield Emails
							final TextField emailsField = new TextField(
									getLocal(EMAILS));

							emailsField.setWidth("300px");
							emailsField.setImmediate(true);
							emailsField.focus();
							// Emailslist
							final ListSelect select = new ListSelect(
									getLocal(SELECTEDEMAILS));
							select.setNullSelectionAllowed(true);
							select.setMultiSelect(true);
							select.setImmediate(true);
							select.setWidth("370px");
							select.setVisible(false);
							// List for removing Elements
							final ArrayList<User> obj = new ArrayList<User>();

							select.addValueChangeListener(new ValueChangeListener() {

								@SuppressWarnings("unchecked")
								@Override
								public void valueChange(
										final ValueChangeEvent event) {
									// add selected items to first pos in array
									// TODO FIX if better solution
									Collection<? extends User> test = (Collection<? extends User>) event
											.getProperty().getValue();
									obj.addAll(test);

								}

							});

							// ReCaptcha
							final ReCaptcha captcha = new ReCaptcha(
									"6LfyJhMTAAAAAFlGZXsDvc-JYEnk4zQ6HQjYMecE",
									"6LfyJhMTAAAAAFQNAbeODGDrMiTzy04q0UscXxHH",
									new ReCaptchaOptions() {
										{
											theme = "white";
										}
									});
							captcha.setVisible(false);

							// share project Button

							final Button shareButton = new Button(
									getLocal(SHARE_PROJECT));
							shareButton.setVisible(false);
							shareButton
									.addClickListener(new Button.ClickListener() {

										@Override
										public void buttonClick(
												final ClickEvent event) {

											try {
												if (captcha.validate()) {
													FictionManager
															.getUser()
															.cloneProject(
																	project,
																	(List<User>) select
																			.getItemIds(),
																	getLocal(NEW_SHARED_PROJECT));

													// send email
													SendEmail.sendMail(
															(List<User>) select
																	.getItemIds(),
															getLocal(CLONE_PROJECT_EMAIL_TITLE),
															getLocal(CLONE_PROJECT_EMAIL_TEXT), FictionManager.getUser());

													// close dialog
													shareProjectWindow.close();
												} else {
													// Captcha was wrong!
													Notification
															.show(getLocal(CAPTCHA_WRONG),
																	Notification.Type.ERROR_MESSAGE);
													captcha.reload();
												}
											} catch (Exception e) {
												// TODO Auto-generated catch
												// block
												e.printStackTrace();
											}

										}

									});

							// remove Emails Button

							final Button removeEmailsButton = new Button("");
							removeEmailsButton.setIcon(
									FictionManager.getIconResource(RECYCLE_BIN),
									getLocal(REMOVE_EMAILS));
							Page.getCurrent()
									.getStyles()
									.add(".v-slot-recycleicon {padding-top: 27px;};.recycleicon {padding: 0px;};");
							removeEmailsButton.addStyleName("recycleicon");
							removeEmailsButton.setVisible(false);
							removeEmailsButton
									.addClickListener(new Button.ClickListener() {

										@Override
										public void buttonClick(
												final ClickEvent event) {

											removeEmails(removeEmailsButton,
													shareButton, emailsField,
													select, obj, captcha);

										}

									});

							// Add Emails Button
							final Button addEmailsButton = new Button("");
							addEmailsButton.setIcon(FictionManager.getIconResource(ADD_EMAIL) , getLocal(getLocal(ADDEMAILS)));
							addEmailsButton.setClickShortcut(KeyCode.ENTER);
							addEmailsButton
									.addClickListener(new Button.ClickListener() {

										@Override
										public void buttonClick(
												final ClickEvent event) {

											validateEmails(
													emailsField.getValue(),
													select);
											if (!(select.getItemIds().isEmpty())) {
												removeEmailsButton
														.setVisible(true);
												select.setVisible(true);
												shareButton.setVisible(true);
												captcha.setVisible(true);
												emailsField.focus();
											}
										}

									});

							// Infotext
							final Label emailInfo = new Label(
									getLocal(EMAILSINFO));

							// Add Elements to Layout and Set up Layout
							emailLayout.addComponent(emailsField);
							emailLayout.addComponent(addEmailsButton);
							emailLayout.setWidth("100%");
							emailLayout.setComponentAlignment(addEmailsButton,
									Alignment.BOTTOM_CENTER);
							emailLayout.setSpacing(true);
							selectLayout.addComponent(select);
							selectLayout.addComponent(removeEmailsButton);
							layout.addComponent(labelProjectName);
							layout.addComponent(emailLayout);
							layout.addComponent(emailInfo);
							layout.addComponent(selectLayout);
							layout.addComponent(captcha);
							layout.addComponent(shareButton);

						}
					}
				});
	}

	/**
	 * validates given emails and adds them to a list for the user.
	 * 
	 * @param value
	 *            the emailstring
	 * @param select
	 *            the list which is populated by the emails
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	private void validateEmails(final String value, final ListSelect select) {
		// add given Emails to List
		List<String> emailList;
		emailList = Arrays.asList(value.split("\\s*;\\s*"));
		final EmailValidator validator = new EmailValidator(
				getLocal(EMAILINVALID));
		for (final String email : emailList) {
			try {
				// check if given email is valid email string
				validator.validate(email);

				// check if user exists on fictionnet
				User user = FictionManager.getUser().getUser(email);

				if (user == null) {
					Notification.show(email + " " + getLocal(USERNOTEXISTENT),
							Notification.Type.WARNING_MESSAGE);
				} else {
					if (user.getEmail().equals(
							FictionManager.getUser().getUserEmail())) {
						Notification.show(getLocal(OWNEMAILERROR),
								Notification.Type.WARNING_MESSAGE);
					} else {
						if (user.getRole() == Role.ADMIN) {
							Notification.show(getLocal(ADMINEMAILERROR),
									Notification.Type.WARNING_MESSAGE);
						} else {
							// TODO workaround, since select.getItem(user) does
							// always
							// return null
							Boolean exists = false;
							final Collection<?> coll = select.getItemIds();
							for (final Object item : coll) {
								if (item.toString().equals(user.getEmail())) {
									exists = true;
								}
							}
							if (!exists) {
								// add item to list
								select.addItem(user);
							}
						}
					}
				}
			} catch (final Validator.InvalidValueException ive) {
				Notification.show(ive.getMessage(),
						Notification.Type.WARNING_MESSAGE);
				continue;
			}
		}

	}

	/**
	 * Removes the emails selected in the select {@link ListSelect}.
	 * 
	 * @param select
	 *            the ListSelect in the GUI
	 * @param obj
	 *            the items selected (given by obj from ValueChangeListener)
	 */
	private void removeEmails(Button removeEmailsButton, Button shareButton,
			TextField emailsField, ListSelect select, ArrayList<User> obj,
			ReCaptcha captcha) {

		@SuppressWarnings("unchecked")
		final ArrayList<User> oldList = new ArrayList<User>(
				(Collection<? extends User>) select.getItemIds());
		oldList.removeAll(obj);
		select.removeAllItems();
		select.addItems(oldList);

		if (select.getItemIds().isEmpty()) {
			removeEmailsButton.setVisible(false);
			select.setVisible(false);
			shareButton.setVisible(false);
			captcha.setVisible(false);
			emailsField.focus();
		}
	}

	/**
	 * 
	 */
	private void initExport() {
		export = menubar.addItem("", FictionManager.getIconResource(EXPORT),
				new MenuBar.Command() {

					@Override
					public void menuSelected(final MenuBar.MenuItem selectedItem) {
						// This registers a downloader to an invisible button
						// because you can't register a downloader for a menu
						// item
						// Then a click on this button is simulated using
						// injected JS

						FictionUI.getCurrent().closeWindows();

						final StreamResource resource = newResource();
						if (downloader == null) {
							downloader = new FileDownloader(resource);
							downloader.extend(projectMenu
									.getDownloadInvisibleButton());

						} else {
							downloader.setFileDownloadResource(resource);
						}

						FictionUI
								.getCurrent()
								.getPage()
								.getJavaScript()
								.execute(
										"document.getElementById('DownloadButtonId').click();");
					}

					/**
					 * @return
					 */
					private StreamResource newResource() {
						return new StreamResource(
								new StreamResource.StreamSource() {
									@Override
									public InputStream getStream() {
										// this is an expensive operation, so we
										// do it on demand
										final ProjectExportProcess export = new ProjectExportProcess();
										try {
											return export
													.write((Project) projectMenu
															.getProjectTree()
															.getValue());
										} catch (final IOException ex) {
											Logger.getLogger(
													ProjectMenu.class.getName())
													.log(Level.SEVERE, null, ex);
											return null;
										}
									}
								}, projectMenu.getProjectTree().getValue()
										.toString()
										+ ".fiction");
						// To differentiate between text and project on upload,
						// don't use XML extension.
					}
				});
	}

	/**
	 * Sets enables {@link MenuItem}s, depending on the {@link Role} of the
	 * {@link User} and the selected value of the {@link Tree}.
	 */
	private void toggleMenuItemEnabled() {
		final boolean isRoleUser = FictionManager.getUser().getRole() == Role.USER;
		final boolean isNotNull = projectMenu.getProjectTree().getValue() != null;
		boolean isProject;
		if (isNotNull) {
			isProject = projectMenu.getProjectTree().getValue() instanceof Project;
		} else {
			isProject = false;
		}
		final boolean isUserSelected = projectMenu.getProjectTree().getValue() instanceof User;

		add.setEnabled(isRoleUser);
		remove.setEnabled(isNotNull && !isUserSelected);
		info.setEnabled(isNotNull);
		share.setEnabled(isRoleUser && isProject);
		export.setEnabled(isRoleUser && isProject);

	}
}