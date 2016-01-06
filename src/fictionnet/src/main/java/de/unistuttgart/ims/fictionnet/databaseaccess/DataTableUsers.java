package de.unistuttgart.ims.fictionnet.databaseaccess;

import java.sql.SQLException;

import de.unistuttgart.ims.fictionnet.users.User;

/**
 * @author Lukas Rieger
 * @version 17-10-2015
 * 
 * Table for the users.
 *
 */
public class DataTableUsers extends AbstractDataTable<User, String> {
	/**
	 * public only for testing purposes.
	 */
	public DataTableUsers() {
		super(User.class);
	}
	
	/**
	 * This method checks if the email adress already exists in the database.
	 * 
	 * @param email
	 * @return boolean
	 * @throws SQLException 
	 */
	public boolean checkIfEmailExists(final String email) throws SQLException {
		boolean result = false;
		final String columnName = DBTableColumns.USEREMAIL.toString();
		int count = 1;
		count = selectFromThisAllWhere().eq(columnName, email).query().size();
		result = count == 1;
		return result;
	}

}
