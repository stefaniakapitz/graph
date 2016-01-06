package de.unistuttgart.ims.fictionnet.users;

/**
 * Interface for listeners on user change events
 * 
 * @author Erol Aktay
 */
public interface UserChangeListener {
	/**
	 * Called when a role changed
	 * 
	 * @param user	affected user
	 * @param oldRole	role before changes
	 * @param newRole	new role that triggered this call
	 */
	public void onRoleChange(User user, Role oldRole, Role newRole);
	/**
	 * Called when a user gets deleted
	 * 
	 * @param email email address of the old user
	 */
	public void onDeletion(String email);
}
