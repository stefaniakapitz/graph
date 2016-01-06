package de.unistuttgart.ims.fictionnet.gui.util;

import de.unistuttgart.ims.fictionnet.users.Role;

/**
 * Defines the paths URL and required Roles for all windows.
 * 
 * @author Roman, Erik-Felix Tinsel
 */
public enum Path {
	// The order of enums equals the order of tabs.
	NONE("", Role.NONE, ""),
	LOGIN("/login", Role.NONE, "LOGIN"),
	PROJECTS("/projects", Role.USER, "PROJECTS"),
	TEXTVIEW("/projects/textview", Role.USER, "TEXTVIEW"),
	VISUALIZATIONVIEW("/projects/visualizationview", Role.USER, "VISUALIZATIONVIEW"),
	TABLEVIEW("/projects/tableview", Role.USER, "TABLEVIEW"),
	ANNOTATIONVIEW("/projects/annotationview", Role.USER, "ANNOTATIONVIEW"),
	SYNONYMVIEW("/projects/synonyms", Role.USER, "SYNONYMVIEW"),
	IMPORT("/import", Role.USER, "IMPORT"),
	SETTINGS_USER("/settings/user", Role.USER, "SETTINGS"),
	USERMANAGEMENT("/usermanagement", Role.ADMIN, "USERMANAGEMENT"),
	PROJECTMANAGEMENT("/projectmanagement", Role.ADMIN, "PROJECTMANAGEMENT"),
	SETTINGS_ADMIN("/settings/admin", Role.ADMIN, "SETTINGS"),
	LOGOUT("/logout", Role.USER, "LOGOUT");

	private final String path;
	private final Role role;
	private final String name;

	/**
	 * Intern Constructor.
	 * 
	 * @param path
	 *            {@link Path}
	 * @param role
	 *            {@link Role}
	 * @param name
	 *            String for Language-Presentation.
	 */
	Path(String path, Role role, String name) {
		this.path = path;
		this.role = role;
		this.name = name;
	}

	/**
	 * Returns the localization name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the required role to a path.
	 * 
	 * @return role
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Returns the path as a string.
	 * 
	 * @return path
	 */
	@Override
	public String toString() {
		return path;
	}

	/**
	 * Checks if the given role matches the requiered Role for this path.
	 * 
	 * @param role
	 *            {@link Role}
	 * @return boolean
	 */
	public boolean validateRole(Role role) {
		return role == this.role;
	}

	/**
	 * values()-method, which only returns values, that belong to the MainTabSheet.
	 * 
	 * @return Array of {@link Path}.
	 */
	public static Path[] valuesMainTabSheet() {

		return new Path[] { LOGIN, PROJECTS, IMPORT, SETTINGS_USER, PROJECTMANAGEMENT, USERMANAGEMENT, SETTINGS_ADMIN, PROJECTS, LOGOUT };
	}
	
	public boolean isMainTabSheet() {
		for (Path path : valuesMainTabSheet()) {
			if (this.equals(path)) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * values()-method, which only returns values, that belong to the WorkspacePanel TabSheet.
	 * 
	 * @return Array of {@link Path}.
	 */
	public static Path[] valuesWorkspaceTabSheet() {
		return new Path[] { TEXTVIEW, VISUALIZATIONVIEW, TABLEVIEW, SYNONYMVIEW }; //, ANNOTATIONVIEW };
	}
}
