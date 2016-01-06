package de.unistuttgart.ims.fictionnet.gui.components;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;

import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;
import de.unistuttgart.ims.fictionnet.users.Role;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * A combo box for changing a user's roles.
 * 
 * @author Erol Aktay, Roman
 */
public class UserRoleChanger extends AbstractLocalizedCustomComponent implements ValueChangeListener {
	private final transient User user;
	private final transient ComboBox roleComboBox;

	/**
	 * Create a new widget for role changing
	 * 
	 * @param user	The user whose roles are to be changed
	 */
	public UserRoleChanger(User user) {
		super();

		this.user = user;
		roleComboBox = new ComboBox();
		roleComboBox.setNullSelectionAllowed(false);
		setCompositionRoot(roleComboBox);
		
		init();
	}
	
	/**
	 * Initialize widgets after object construction
	 */
	private void init() {
		roleComboBox.addItem(Role.ADMIN);
		roleComboBox.addItem(Role.USER);
		roleComboBox.setValue(user.getRole());
		// Don't allow changes to main administrator and the logged in user.
		if (user.getEmail().equals(FictionUI.Servlet.getConfig("admin"))
				|| user.getEmail().equals(FictionManager.getUser().getEmail())) {
			roleComboBox.setEnabled(false);
		} else {
			roleComboBox.addValueChangeListener(this);
		}
	}

	@Override
	public void valueChange(Property.ValueChangeEvent event) {
		if (event.getProperty().getValue() == null) {
			return;
		}

		if (FictionManager.getUser().getRole() == Role.ADMIN) {
			if (!UserManagement.getTheInstance().changeUserRole(user.getEmail(), (Role) event.getProperty().getValue())) {
				Notification.show(getLocal("SQL_ERROR"), Notification.Type.ERROR_MESSAGE);
			}

		} else {
			Notification.show(getLocal("ADMIN_ONLY_ACTION"), Notification.Type.ERROR_MESSAGE);
		}
	}

	@Override
	public String toString() {
		return user.getRole().toString();
	}

}
