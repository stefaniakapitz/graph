package de.unistuttgart.ims.fictionnet.gui.ui;

//import org.junit.experimental.runners.Enclosed;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;

import de.unistuttgart.ims.fictionnet.gui.ui.tabs.AbstractTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.ImportTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.LoginTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.LogoutTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.ProjectsTab;
import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;
import de.unistuttgart.ims.fictionnet.gui.util.Path;
import de.unistuttgart.ims.fictionnet.gui.util.TabFactory;
import de.unistuttgart.ims.fictionnet.users.Role;
import de.unistuttgart.ims.fictionnet.users.User;

/**
 * Main {@link TabSheet} of FictionNet. This class is the root element for each session, that is added to the FictionUI
 * directly.
 * 
 * @author Roman, Erik-Felix Tinsel
 */
public class MainTabSheet extends TabSheet {

	/**
	 * Standard Constructor.
	 */
	public MainTabSheet() {
		super();

		setImmediate(true);
		setSizeFull();
	}

	@Override
	public void attach() {
		super.attach();

		updateTabs();

		// Add Listeners
		FictionUI.getCurrent().getPage().addUriFragmentChangedListener(new UriFragmentChangedListener() {
			@Override
			public void uriFragmentChanged(UriFragmentChangedEvent event) {
				checkUriFragment();
			}

		});

		addSelectedTabChangeListener(new SelectedTabChangeListener() {

			@Override
			public void selectedTabChange(SelectedTabChangeEvent event) {

				final User user = FictionManager.getUser();

				if (user == null) {
					final AbstractTab currentTab = (AbstractTab) getSelectedTab();

					if (!(currentTab instanceof LoginTab)) {
						// Don't set Uri Fragment! Else bookmarking wouldn't work.
						setSelectedTab(TabFactory.getTab(Path.LOGIN, FictionUI.getCurrent()));
					}
				} else {
					final AbstractTab newTab = (AbstractTab) event.getTabSheet().getSelectedTab();

					if (newTab instanceof LogoutTab) {
						FictionManager.logout();
					} else {

						if (newTab instanceof ProjectsTab) {
							FictionUI.getCurrent().getPage().setUriFragment(Path.PROJECTS.toString(), true);
						} else if (newTab instanceof ImportTab) {
							// FIXME: When Vaadin fixes Upload bugs, reloadUploader() can be removed, including this
							// call.
							FictionUI.getCurrent().getPage().setUriFragment(Path.IMPORT.toString(), false);
							((ImportTab) newTab).reloadUploader();
						} else {
							FictionUI.getCurrent().getPage().setUriFragment(newTab.getPath().toString(), false);
						}
					}
				}
			}

		});

		checkUriFragment();
	}

	/**
	 * Checks the URI on each (re)load of the UI and on tabchange. Sets the selected tab of this {@link TabSheet}.
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	private void checkUriFragment() {

		String uriFragment = FictionUI.getCurrent().getPage().getUriFragment();

		if (uriFragment == null) {
			uriFragment = Path.LOGIN.toString();
		}

		for (final Component tab : this) {
			if (uriFragment.startsWith(((AbstractTab) tab).getPath().toString())) {
				setSelectedTab(tab);
			}
		}
	}

	/**
	 * Sets those Tabs visible, that are allowed for the current User.
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void updateTabs() {

		removeAllComponents();

		Role role;
		if (FictionManager.getUser() == null) {
			role = Role.NONE;
		} else {
			role = FictionManager.getUser().getRole();
		}

		for (final Path path : Path.valuesMainTabSheet()) {
			if (path.getRole().equals(role)) {
				addTab(TabFactory.getTab(path, FictionUI.getCurrent()));
			}
		}

		markAsDirty();
	}

}
