package de.unistuttgart.ims.fictionnet.gui.util;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;

import de.unistuttgart.ims.fictionnet.gui.ui.MainTabSheet;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.AbstractTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.AnnotationViewTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.ImportTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.LoginTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.LogoutTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.ProjectManagementTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.ProjectsTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.SettingsTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.SynonymViewTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.TableViewTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.TextViewTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.UserManagementTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.VisualizationViewTab;

/**
 * Factory Class for all {@link AbstractTab}s.
 * 
 * @author Roman, Erik-Felix Tinsel
 */
@SuppressWarnings({ "PMD.CyclomaticComplexity", "PMD.StdCyclomaticComplexity" })
public final class TabFactory {

	/**
	 * Disable public constructor.
	 */
	private TabFactory() {
	};

	/**
	 * Factory method to get a new instance of a child of AbstractTab.
	 * 
	 * @param path
	 *            - {@link Path} to which a new tab shall be instantiated.
	 * @return {@link AbstractTab}
	 */
	@SuppressWarnings("PMD.OnlyOneReturn")
	private static AbstractTab createTab(Path path) {
		switch (path) {
		case ANNOTATIONVIEW:
			return new AnnotationViewTab(path);
		case IMPORT:
			return new ImportTab(path);
		case LOGIN:
			return new LoginTab(path);
		case PROJECTMANAGEMENT:
			return new ProjectManagementTab(path);
		case PROJECTS:
			return new ProjectsTab(path);
		case SETTINGS_USER:
			return new SettingsTab(path);
		case SETTINGS_ADMIN:
			return new SettingsTab(path);
		case TABLEVIEW:
			return new TableViewTab(path);
		case TEXTVIEW:
			return new TextViewTab(path);
		case USERMANAGEMENT:
			return new UserManagementTab(path);
		case VISUALIZATIONVIEW:
			return new VisualizationViewTab(path);
		case SYNONYMVIEW:
			return new SynonymViewTab(path);
		case LOGOUT:
			return new LogoutTab(path);
		default:
			return null;

		}
	}

	/**
	 * Returns an existing tab of the current UI, or a new created. Returns null, if path is invalid.
	 * 
	 * @param path
	 *            - Path to tab.
	 * @return {@link AbstractTab}
	 */
	public static AbstractTab getTab(final Path path, final UI ui) {

		AbstractTab tab;

		if (path.isMainTabSheet()) {
			final TabSheet tabsheet = (MainTabSheet) ui.getContent();
			tab = getTab(path, tabsheet);
		} else {
			tab = getTab(path, ((ProjectsTab) getTab(Path.PROJECTS, ui)).getWorkspace().getTabSheet());
		}

		if (tab == null) {
			tab = createTab(path);
		}

		return tab;
	}

	/**
	 * Returns a tab a specific class, if it is visible.
	 * 
	 * @param path
	 *            - {@link Path} to which a tab shall be found.
	 * @param tabsheet
	 *            - {@link TabSheet} which contains the searched tab.
	 * @return {@link AbstractTab} from {@link TabSheet}, which {@link Path} matches the parameter.
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	private static AbstractTab getTab(Path path, final TabSheet tabsheet) {

		for (final Component tab : tabsheet) {
			if (((AbstractTab) tab).getPath() == path) {
				return (AbstractTab) tab;
			}
		}

		return null;
	}

}
