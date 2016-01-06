package de.unistuttgart.ims.fictionnet.gui.ui.tabs;

import com.vaadin.ui.Layout;

import de.unistuttgart.ims.fictionnet.gui.components.AbstractLocalizedCustomComponent;
import de.unistuttgart.ims.fictionnet.gui.util.Path;
import de.unistuttgart.ims.fictionnet.users.Role;

/**
 * Superclass for all Tabs. Note, that CustomTab expands CustomComponent, and not {@link Tab}.
 * 
 * @author Roman
 */
public abstract class AbstractTab extends AbstractLocalizedCustomComponent {

	private Path path;

	/**
	 * Sets up a CustomTab. Adds layout to CompositionRoot, sets size full and sets the caption to the paths caption.
	 * 
	 * @param path
	 *            - {@link Path} that leads to this tab.
	 */
	protected AbstractTab(Path path) {
		super();

		this.path = path;

		setSizeFull();
		if (!Path.NONE.equals(path)) {
			setCaption(getLocal(path.getName()));
		}
	}

	/**
	 * Gets the {@link Path} that leads to this Tab.
	 * 
	 * @return {@link Path}
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * Sets size of layout and composition root of the tab.
	 * 
	 * @param layout
	 *            - Layout, that shall be set as composite root of this {@link CustomComponent}.
	 */
	protected void initLayout(Layout layout) {
		layout.setSizeFull();
		setCompositionRoot(layout);

	}

	/**
	 * Sets path to this tab.
	 * 
	 * @param path
	 *            - {@link Path}
	 */
	public void setPath(Path path) {
		this.path = path;
	}

	/**
	 * Returns true, if current {@link User} meets the requiered {@link Role} for this Page.
	 * 
	 * @param role
	 *            - {@link User}
	 * @return boolean
	 */
	public boolean validateRole(Role role) {
		return path.getRole().equals(role);
	}
}
