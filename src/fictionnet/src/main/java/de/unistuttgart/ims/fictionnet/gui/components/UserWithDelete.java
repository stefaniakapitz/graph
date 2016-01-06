package de.unistuttgart.ims.fictionnet.gui.components;

import java.sql.SQLException;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;

import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;
import de.unistuttgart.ims.fictionnet.users.Role;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * Displays email of the user and a button to delete him next to it
 * 
 * @author Erol Aktay, Roman
 */
public class UserWithDelete extends AbstractLocalizedCustomComponent implements ClickListener {

	private final User user;
	private final Button deleteButton;

	/**
	 * Create with the user object to display/delete
	 * 
	 * @param user
	 */
	public UserWithDelete(User user) {
		super();

		this.user = user;

		final HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();

		layout.addComponent(new Label(user.getEmail()));
		deleteButton = new Button(getLocal("USER_DELETE"));

		layout.addComponent(deleteButton);
		setCompositionRoot(layout);
		
		init();
	}
	
	/**
	 * Initialize widgets after object construction
	 */
	private void init() {
		// Don't allow main administrator to be deleted
		if (user.getEmail().equals(FictionUI.Servlet.getConfig("admin"))
				|| user.getEmail().equals(FictionManager.getUser().getEmail())) {
			deleteButton.setEnabled(false);
		} else {
			deleteButton.addClickListener(this);
		}
	}

	/**
	 * Add a ClickListener to its delete button. NOTE: This component will already delete the user by default
	 * 
	 * @param listener
	 */
	public void addClickListener(ClickListener listener) {
		deleteButton.addClickListener(listener);
	}

	/**
	 * Remove a ClickListener to its delete button NOTE: To remove the default ClickListener simply pass its own
	 * instance
	 * 
	 * @param listener
	 */
	public void removeClickListener(ClickListener listener) {
		deleteButton.removeClickListener(listener);
	}

	/**
	 * Default action on clicking delete
	 */
	@Override
	public void buttonClick(Button.ClickEvent event) {
		// Make extra sure current user is admin
		if (FictionManager.getUser().getRole() != Role.ADMIN) {
			Notification.show(getLocal("ADMIN_ONLY_ACTION"), Notification.Type.ERROR_MESSAGE);
		} else {
			try {
				UserManagement.getTheInstance().deleteUser(user);
			} catch (SQLException ex) {
				Notification.show(getLocal("SQL_ERROR"), Notification.Type.ERROR_MESSAGE);
			}
		}
	}

	@Override
	public String toString() {
		return user.toString();
	}

}
