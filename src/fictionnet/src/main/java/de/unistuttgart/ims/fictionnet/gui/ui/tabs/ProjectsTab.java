package de.unistuttgart.ims.fictionnet.gui.ui.tabs;

import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Container.ItemSetChangeListener;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Page.UriFragmentChangedEvent;
import com.vaadin.server.Page.UriFragmentChangedListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeListener;

import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.gui.components.ProjectMenu;
import de.unistuttgart.ims.fictionnet.gui.components.optionpanel.OptionPanel;
import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.gui.ui.WorkspacePanel;
import de.unistuttgart.ims.fictionnet.gui.util.Path;
import de.unistuttgart.ims.fictionnet.gui.util.TabFactory;

/**
 * Class for the Project Tab.
 *
 * @author Roman
 */
public class ProjectsTab extends AbstractTab {

	private final transient HorizontalLayout layout;
	private transient ProjectMenu projectMenu;
	private transient WorkspacePanel workspace;
	private transient OptionPanel optionPanel;

	/**
	 * Getter for worcspace panel.
	 *
	 * @return {@link WorkspacePanel}
	 */
	public WorkspacePanel getWorkspace() {
		return workspace;
	}

	/**
	 * Creates the project tab with a path bound and a horizontal layout.
	 *
	 * @param path : Path that is bound to Tab
	 */
	public ProjectsTab(Path path) {
		super(path);

		this.layout = new HorizontalLayout();
		initLayout(layout);
	}

	/**
	 * Adds the Listeners to the class.
	 */
	private void addListeners() {
		// Listen for Item Click on project tree
		projectMenu.getProjectTree().addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				final Object item = projectMenu.getProjectTree().getValue();

				if (item != null) {
					workspace.addTexts(item);
					optionPanel.setCorpusTitle(item.toString());

					// FIXME: too much repetition
					final VisualizationViewTab vis
							= (VisualizationViewTab) TabFactory.getTab(
									Path.VISUALIZATIONVIEW,
									FictionUI.getCurrent());
					final SynonymViewTab syn
							= (SynonymViewTab) TabFactory.getTab(
									Path.SYNONYMVIEW,
									FictionUI.getCurrent());
					if (item instanceof Text) {
						optionPanel.setText((Text) item);

						if (vis != null) {
							vis.setText((Text) item);
						}
						if (syn != null) {
							syn.setText((Text) item);
						}
					} else {
						optionPanel.setText(null);
						if (vis != null) {
							vis.setText(null);
						}
						if (syn != null) {
							syn.setText(null);
						}
					}
				}
			}
		});

		projectMenu.getProjectTree().addItemSetChangeListener(new ItemSetChangeListener() {

			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				// TODO Auto-generated method stub

			}

		});

		// Listen for Tab Change on workspace tabsheet
		workspace.getTabSheet().addSelectedTabChangeListener(new SelectedTabChangeListener() {
			@Override
			public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
				final AbstractTab newTab = (AbstractTab) event.getTabSheet().getSelectedTab();

				FictionUI.getCurrent().getPage().setUriFragment(newTab.getPath().toString(), false);
				optionPanel.setMode(newTab.getPath());
			}

		});

		// Add Listener
		FictionUI.getCurrent().getPage().addUriFragmentChangedListener(new UriFragmentChangedListener() {
			@Override
			public void uriFragmentChanged(UriFragmentChangedEvent event) {
				checkUriFragment();
			}

		});
	}

	@Override
	public void attach() {
		super.attach();
		layout.setSizeFull();

		projectMenu = new ProjectMenu();
		projectMenu.reloadTree();
		layout.addComponent(projectMenu);

		workspace = new WorkspacePanel();
		layout.addComponent(workspace);

		optionPanel = new OptionPanel();
		layout.addComponent(optionPanel);

		// Listeners
		addListeners();

		// Check starting location
		checkUriFragment();

		// Give all additional space to workspace
		layout.setExpandRatio(workspace, 1);

	}

	/**
	 * Checks the current UriFragment to load the correct tab.
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void checkUriFragment() {
		final String uriFragment = FictionUI.getCurrent().getPage().getUriFragment();
		
		if (uriFragment == null || uriFragment.isEmpty()) {
			FictionUI.getCurrent().getPage().setUriFragment(Path.LOGIN.toString(), true);
		}

		for (final Component tab : workspace.getTabSheet()) {

			if (uriFragment.startsWith(((AbstractTab) tab).getPath().toString())) {
				workspace.getTabSheet().setSelectedTab(tab);
			} else if (uriFragment.equals(Path.PROJECTS.toString())) {
				projectMenu.reloadTree();
				final AbstractTab selectedTab = (AbstractTab) workspace.getTabSheet().getSelectedTab();
				FictionUI.getCurrent().getPage().setUriFragment(selectedTab.getPath().toString(), true);
			}
		}
		workspace.getTabSheet().markAsDirty();

	}
}
