package de.unistuttgart.ims.fictionnet.gui.components;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;
import de.unistuttgart.ims.fictionnet.users.Role;
import de.unistuttgart.ims.fictionnet.users.User;

/**
 * Panel for selecting/managing Projects and their contents.
 * 
 * @author Roman
 */
@SuppressWarnings("PMD.LongVariable")
public class ProjectMenu extends AbstractLocalizedCustomComponent {

	private static final String GENERIC_EXCEPTION = "GENERIC_EXCEPTION";

	private final Tree projectTree;
	private final transient ProjectMenuBar projectMenuBar;

	private final Button downloadInvisibleButton;

	/**
	 * Constructor creates a projectmenu with a toolbar and a tree.
	 */
	public ProjectMenu() {
		super();

		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		final VerticalLayout panelLayout = new VerticalLayout();
		panelLayout.setSpacing(true);
		panelLayout.setSizeUndefined();

		// Create components
		projectMenuBar = new ProjectMenuBar(this);
		projectMenuBar.setSizeUndefined();
		projectTree = new Tree();
		projectTree.setMultiSelect(false);

		// Add to layout
		layout.addComponent(projectMenuBar);
		panelLayout.addComponent(projectTree);

		// Used in a workaround to download from menuitems, see export command
		downloadInvisibleButton = new Button();
		downloadInvisibleButton.setId("DownloadButtonId");
		downloadInvisibleButton.addStyleName("InvisibleButton");
		FictionUI.getCurrent().getPage().getStyles().add(".InvisibleButton { display: none; }");
		layout.addComponent(downloadInvisibleButton);

		final Panel panel = new Panel(panelLayout);
		panel.setSizeFull();
		layout.addComponent(panel);
		layout.setExpandRatio(panel, 1);

		setWidth("318px");
		setHeight("100%");
		setCompositionRoot(layout);

	}

	@Override
	public void attach() {
		super.attach();
		projectMenuBar.fillToolbar();
	}

	/**
	 * Getter for invisible button.
	 * 
	 * @return {@link Button}
	 */
	public Button getDownloadInvisibleButton() {
		return downloadInvisibleButton;
	}

	/**
	 * Getter for projectTree.
	 * 
	 * @return tree : Tree
	 */
	public Tree getProjectTree() {
		return projectTree;
	}

	/**
	 * Reloads all projects, copora and texts and puts them into the tree.
	 */
	// TODO: Lazy loading. Should fix complexity.
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void reloadTree() {
		projectTree.removeAllItems();
		final User user = FictionManager.getUser();

		if (user == null) {
			// No user is logged in, so don't load a projectTree.
			return;
		}

		final ArrayList<User> users = new ArrayList<User>();

		if (user.getRole() == Role.USER) {
			users.add(user);
		} else {
			try {
				users.addAll(DBAccessManager.getTheInstance().getAllUsersWithData());
			} catch (final SQLException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE,
						"Could not get users for ProjectManagement projectTree.", e);
				Notification.show(getLocal(GENERIC_EXCEPTION), Notification.Type.ERROR_MESSAGE);
			}
		}

		// Add users
		for (final User owner : users) {

			if (owner.getRole() == Role.ADMIN) {
				continue;
			}

			projectTree.addItem(owner);

			// Add Projects
			addItems(owner, owner.getOwnedProjects().toArray());
			for (final Project project : owner.getOwnedProjects()) {
				// Add Corpora
				addItems(project, project.getCorpora().toArray());

				for (final Corpus corpus : project.getCorpora()) {
					addItems(corpus, corpus.getTexts().toArray());
				}
			}

			// Remove owner for normal users.
			if (owner.equals(user)) {
				projectTree.removeItem(owner);
			}
		}
	}

	/**
	 * Adds new Items to the tree.
	 * 
	 * @param father
	 *            - father object, which should already be in the tree.
	 * @param children
	 *            - Objects, that should be added to tree as children of father object.
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	private void addItems(final Object father, final Object... children) {

		if (children.length > 0) {
			projectTree.setChildrenAllowed(father, true);
		} else {
			projectTree.setChildrenAllowed(father, false);
		}

		for (final Object child : children) {
			projectTree.addItem(child);
			projectTree.setChildrenAllowed(child, false);
			projectTree.setParent(child, father);
		}
	}

}
