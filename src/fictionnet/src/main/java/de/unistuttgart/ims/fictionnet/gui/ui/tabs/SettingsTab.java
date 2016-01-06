package de.unistuttgart.ims.fictionnet.gui.ui.tabs;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;

import de.unistuttgart.ims.fictionnet.gui.components.LanguageSelection;
import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI.Servlet;
import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;
import de.unistuttgart.ims.fictionnet.gui.util.Path;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * Class for the visual representation of the settings.
 * 
 * @author Roman, Erol Aktay
 */
public class SettingsTab extends AbstractTab {

	private static final String LOGOUT_CAPTION = "LOGOUT";
	private static final String USER_CAPTION = "USER";
	private static final String LANGUAGE = "LANGUAGE";
	private static final String GIT_VERSION = "git_version";
	private static final String VERSION = "VERSION";

	/**
	 * Default constructor.
	 * 
	 * @param path
	 *            - {@link Path} to this tab.
	 */
	public SettingsTab(Path path) {
		super(path);

		final FormLayout layout = new FormLayout();
		initLayout(layout);

		layout.setMargin(true);
		layout.setSpacing(true);

		final LanguageSelection languageSelection = new LanguageSelection();
		languageSelection.setCaption(getLocal(LANGUAGE));
		layout.addComponent(languageSelection);

		final User user = FictionManager.getUser();
		if (user != null) {
			final Label userLabel = new Label(user.getEmail());
			userLabel.setCaption(getLocal(USER_CAPTION));
			layout.addComponent(userLabel);
			
			final PasswordField pwdField, RepeatPwdField, oldPwdField;
			oldPwdField = new PasswordField(getLocal("PASSWORD_OLD"));
			pwdField = new PasswordField(getLocal("PASSWORD"));
			RepeatPwdField = new PasswordField(getLocal("PASSWORD_REPEAT"));
			final Button pwdButton = new Button(getLocal("CHANGE_PASSWORD"));
			pwdButton.addClickListener(new ClickListener() {
				@Override
				public void buttonClick(ClickEvent event) {
					if (user.getPassword() == UserManagement.hash(oldPwdField.getValue())
						&& pwdField.getValue().equals(RepeatPwdField.getValue())) {
						if (user.setPassword(pwdField.getValue())) {
						oldPwdField.setValue("");
						pwdField.setValue("");
						RepeatPwdField.setValue("");
						Notification.show(getLocal("PASSWORD_CHANGE_SUCCESS"), Notification.Type.HUMANIZED_MESSAGE);
						} else {
							Notification.show(getLocal("PASSWORD_CHANGE_FAILURE"), Notification.Type.ERROR_MESSAGE);
						}
					} else {
						Notification.show(getLocal("PASSWORD_CHANGE_FAILURE"), Notification.Type.ERROR_MESSAGE);
					}
				}
			});
			layout.addComponent(oldPwdField);
			layout.addComponent(pwdField);
			layout.addComponent(RepeatPwdField);
			layout.addComponent(pwdButton);
		}
		
		final Label versionLabel = new Label(Servlet.getConfig(GIT_VERSION));
		versionLabel.setCaption(getLocal(VERSION));
		layout.addComponent(versionLabel);
		
		final Button logout = new Button(getLocal(LOGOUT_CAPTION));
		logout.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				FictionManager.logout();

			}
		});
		layout.addComponent(logout);

	}
}
