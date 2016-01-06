package de.unistuttgart.ims.fictionnet.gui.ui.tabs;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.gui.components.UserRoleChanger;
import de.unistuttgart.ims.fictionnet.gui.components.UserWithDelete;
import de.unistuttgart.ims.fictionnet.gui.util.Path;
import de.unistuttgart.ims.fictionnet.users.Role;
import de.unistuttgart.ims.fictionnet.users.User;
import de.unistuttgart.ims.fictionnet.users.UserChangeListener;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * Creates and manages the usermanagement tab. Use reload() to automatically fill in the table of users.
 * 
 * @author Roman, Erol Aktay
 */
public class UserManagementTab extends AbstractTableTab implements UserChangeListener {

	private static final String SQL_ERROR = "SQL_ERROR";
	private transient Map<String, Object> rowsThroughEmail = new HashMap<>();

	/**
	 * Default constructor.
	 * 
	 * @param path
	 *            - Path that leads to this tab.
	 */
	@SuppressWarnings("PMD.ConstructorCallsOverridableMethod")
	public UserManagementTab(Path path) {
		super(path);
		
		/* FIXME: Is there way to make custom components work with SimpleFilter?
				have a getValue() method or something?
		*/
		addColumn("USER", UserWithDelete.class, "NOT_AVAILABLE");
		addColumn("ROLE", UserRoleChanger.class, "NOT_AVAILABLE");
		addColumn("REGISTRATION_DATE", String.class, "NOT_AVAILABLE");
		addColumn("LAST_LOGIN", String.class, "NOT_AVAILABLE");
		
		reload();
		
		UserManagement.getTheInstance().addUserChangeListener(this);
	}

	@Override
	@SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
	public void reload() {

		clearData();
		rowsThroughEmail = new HashMap<>();

		Iterator<User> iterator;
		try {
			// FIXME: Provide trough UserManagement, instead of calling DBAccessManager directly.
			iterator = DBAccessManager.getTheInstance().iterator();

			while (iterator.hasNext()) {
				final User user = iterator.next();
				rowsThroughEmail.put(user.getEmail(),
						addEntry(new Object[] { new UserWithDelete(user),
							new UserRoleChanger(user),
							user.getRegistrationDateString(),
							user.getLastLoginDateString() }));
			}
		} catch (SQLException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE,
					"SQL Error in UserManagementTab. Could not get iterator.", e);
			Notification.show(getLocal(SQL_ERROR), Type.ERROR_MESSAGE);
		}
	}

	
	@Override
	public void onRoleChange(User user, Role oldRole, Role newRole) {
		// Nothing to do here, since the combobox is already updated anyway
	}

	@Override
	public void onDeletion(String email) {
		removeEntry(rowsThroughEmail.get(email));
	}
}
