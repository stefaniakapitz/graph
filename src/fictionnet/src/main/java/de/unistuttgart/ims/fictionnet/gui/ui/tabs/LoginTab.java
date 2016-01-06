package de.unistuttgart.ims.fictionnet.gui.ui.tabs;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.unistuttgart.ims.fictionnet.gui.components.LanguageSelection;
import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI.Servlet;
import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;
import de.unistuttgart.ims.fictionnet.gui.util.Path;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * The initial page, allowing users to log in or register.
 * 
 * @author Erol, Roman, Erik-Felix Tinsel
 */
@Theme("valo")
@Widgetset("de.unistuttgart.ims.fictionnet.gui.MyAppWidgetset")
public class LoginTab extends AbstractTab {
	private static final String EMAIL_NOT_UNIQUE = "EMAIL_NOT_UNIQUE";
	private static final String SQL_ERROR = "SQL_ERROR";
	// Language constants.
	private static final String INVALID_LOGIN = "INVALID_LOGIN";
	private static final String WELCOME = "WELCOME";
	private static final String PASSWORD_FORGOT = "PASSWORD_FORGOT";
	@SuppressWarnings("PMD.ShortVariable")
	private static final String OR = "OR";
	private static final String PASSWORD_REPEAT = "PASSWORD_REPEAT";
	private static final String NEWS = "NEWS";
	private static final String EMAIL_REQUIRED = "EMAIL_REQUIRED";
	private static final String EMAIL_INVALID = "EMAIL_INVALID";
	private static final String EMAIL = "EMAIL";
	private static final String PASSWORD_REQUIRED = "PASSWORD_REQUIRED";
	private static final String PASSWORD = "PASSWORD";
	private static final String LOGIN_CAPTION = "LOGIN";
	private static final String PASSWORD_DIFFERS = "PASSWORD_DIFFERS";
	private static final String REGISTER_CAPTION = "REGISTER";

	private transient TextField emailField;
	private transient PasswordField passwordField;
	private transient PasswordField passwordRepeat;
	private final transient VerticalLayout layout;
	@SuppressWarnings("PMD.LongVariable")
	private transient Button registrationButton;
	private transient Button loginButton;

	/**
	 * Constructor for LoginTab.
	 * 
	 * @param path
	 *            - Path that leads to login tab.
	 */
	public LoginTab(final Path path) {
		super(path);

		final VerticalLayout layout = new VerticalLayout();
		initLayout(layout);

		layout.setMargin(true);
		layout.setSpacing(true);

		this.layout = layout;
	}

	@Override
	public void attach() {
		super.attach();

		// Create LanguageSelection
		final LanguageSelection languageSelection = new LanguageSelection();

		layout.addComponent(languageSelection);
		layout.setComponentAlignment(languageSelection, Alignment.TOP_RIGHT);

		// Create Main-Panel		
		final Panel panelContent = new Panel("<font size=\"20\"><b>" + getLocal(WELCOME) + "</b></font>");
		panelContent.setStyleName(ValoTheme.PANEL_BORDERLESS);
		panelContent.setCaptionAsHtml(true);
		layout.addComponent(panelContent);

		// Create Form-Layout for TextFields.
		final FormLayout loginLayout = new FormLayout();
		loginLayout.setMargin(true);

		// Create email TextField and set Validator.
		initEmailField();
		loginLayout.addComponent(emailField);

		// Create passwort PasswordField and set Validator.
		initPassword();
		loginLayout.addComponent(passwordField);
		//loginLayout.addComponent(new Button(getLocal(PASSWORD_FORGOT)));

		// Create loginButton and add ClickListener.
		loginButton = new Button(getLocal(LOGIN_CAPTION));
		loginLayout.addComponent(loginButton);

		// Add OR Label
		loginLayout.addComponent(new Label(getLocal(OR)));

		// Add passwordRepeat PasswordField.
		passwordRepeat = new PasswordField(getLocal(PASSWORD_REPEAT));
		loginLayout.addComponent(passwordRepeat);

		// Add Registration Button
		registrationButton = new Button(getLocal(REGISTER_CAPTION));
		loginLayout.addComponent(registrationButton);

		// Add News Layout. Only used for proper margin.
		final VerticalLayout newsLayout = new VerticalLayout();
		newsLayout.setMargin(true);
		newsLayout.setSizeFull();

		// Add news Panel.
		final Panel newsPanel = new Panel(getLocal(NEWS));
		newsPanel.setWidth(15, Unit.CM);
		newsPanel.setHeight(8, Unit.CM);
		newsLayout.addComponent(newsPanel);

		// Add marginLayout.
		final VerticalLayout marginLayout = new VerticalLayout();
		marginLayout.setMargin(true);
		marginLayout.setSizeFull();
		newsPanel.setContent(marginLayout);
		/*
		final String basepath = VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath();
		FileResource resource = new FileResource(new File(basepath +
                "/WEB-INF/images/LOGO.png"));
		final Image image = new Image("", resource);
		image.setWidth("100px");
		image.setHeight("100px");
		marginLayout.addComponent(image);
		marginLayout.setComponentAlignment(image, Alignment.TOP_CENTER);
		 */
		// get News from config
		// TODO implement better
		String news = Servlet.getConfig("news_de");
		if (getLocalization().getLanguageString().equals("de")) {
			news = Servlet.getConfig("news_de");
		} else {
			if (getLocalization().getLanguageString().equals("en")) {
				news = Servlet.getConfig("news_en");
			}
		}
		final Label newsLabel = new Label(news, ContentMode.HTML);
		newsLabel.setWidth("100%");
		newsLabel.setCaptionAsHtml(true);
		marginLayout.addComponent(newsLabel);
		newsPanel.getContent().setSizeUndefined();
		// Add Login and News Components to Horizontal Layout.
		final HorizontalLayout contentLayout = new HorizontalLayout();
		contentLayout.addComponent(loginLayout);
		contentLayout.addComponent(newsLayout);

		// Set content of the main Panel.
		panelContent.setContent(contentLayout);

		layout.setExpandRatio(panelContent, 1);

		initListeners();
	}

	/**
	 * Initiates the emailField.
	 */
	private void initEmailField() {
		emailField = new TextField(getLocal(EMAIL));
		emailField.focus();
		emailField.addValidator(new EmailValidator(getLocal(EMAIL_INVALID)));
		emailField.setInvalidAllowed(true);
		emailField.setRequired(true);
		// If Focus is lost and Field is empty send a warning
		emailField.addBlurListener(new FieldEvents.BlurListener() {
			@Override
			public void blur(FieldEvents.BlurEvent event) {
				emailField.setRequiredError(getLocal(EMAIL_REQUIRED));
			}
		});

	}

	/**
	 * Initiates the passwordField.
	 */
	private void initPassword() {
		passwordField = new PasswordField(getLocal(PASSWORD));
		passwordField.setRequired(true);
		// If Focus is lost and Field is empty send a warning
		passwordField.addBlurListener(new FieldEvents.BlurListener() {
			@Override
			public void blur(FieldEvents.BlurEvent event) {
				passwordField.setRequiredError(getLocal(PASSWORD_REQUIRED));
			}
		});

	}

	/**
	 * Adds a Listener to the buttons.
	 */
	private void initListeners() {

		registrationButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				register();
			}
		});

		loginButton.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(final Button.ClickEvent event) {
				login();
			}
		});
		loginButton.setClickShortcut(KeyCode.ENTER);

	}

	/**
	 * Performs login. Checks if email and password are valid.
	 */
	private void login() {

		// Get loc for current language values.

		try {
			emailField.validate();
			passwordField.validate();

			final User user = UserManagement.getTheInstance().login(emailField.getValue(), passwordField.getValue());

			if (user == null) {
				Notification.show(getLocal(INVALID_LOGIN), Notification.Type.WARNING_MESSAGE);
			} else {
				getSession().setAttribute(User.class, user);
				if (user.getLang() != null)
					FictionManager.getLocalization().setLanguage(user.getLang());

				// Reload UI, so that MainTab resets visibility
				FictionUI.getCurrent().reloadTabSheet();

			}

		} catch (final Validator.InvalidValueException ive) {
			Notification.show(ive.getMessage(), Notification.Type.WARNING_MESSAGE);
			passwordField.setValue("");
		} catch (final SQLException ex) {
			Logger.getLogger(LoginTab.class.getName()).log(Level.SEVERE, "SQL Error on login.", ex);
			Notification.show(getLocal(SQL_ERROR), Notification.Type.ERROR_MESSAGE);
		}

	}

	/**
	 * Creates a new user and logs in afterwards.
	 */
	protected void register() {
		// TODO: Captcha einfügen, Bestätigungsemail versenden.

		try {
			emailField.validate();
			passwordField.validate();
			passwordRepeat.validate();
		} catch (Validator.InvalidValueException ive) {
			Notification.show(ive.getMessage(), Notification.Type.WARNING_MESSAGE);
			return;
		}

		if (passwordField.getValue().equals(passwordRepeat.getValue())) {
			try {
				if (UserManagement.getTheInstance().isEmailUnique(emailField.getValue())) {
					// Everything fine, create User.
					try {
						final User user =
								UserManagement.getTheInstance().createUser(emailField.getValue(),
										passwordField.getValue());
						user.setLang(FictionManager.getLocalization().getLanguageString());
						FictionManager.setUser(user);

						FictionUI.getCurrent().reloadTabSheet();
					} catch (SQLException e) {
						Logger.getLogger(LoginTab.class.getName()).log(Level.SEVERE, "Could not create new User.", e);
						Notification.show(getLocal(SQL_ERROR), Notification.Type.ERROR_MESSAGE);
					}
				} else {
					// Email isn't unique.
					Notification.show(getLocal(EMAIL_NOT_UNIQUE), Notification.Type.WARNING_MESSAGE);

				}
			} catch (SQLException e) {
				Logger.getLogger(LoginTab.class.getName()).log(Level.SEVERE, "SQL Error on registration.", e);
				Notification.show(getLocal(SQL_ERROR), Notification.Type.ERROR_MESSAGE);
			}
		} else {
			// Passwords don't match1
			Notification.show(getLocal(PASSWORD_DIFFERS), Notification.Type.WARNING_MESSAGE);
			passwordField.setValue("");
			passwordRepeat.setValue("");

		}
	}
}
