package de.unistuttgart.ims.fictionnet.gui.ui.tabs;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.gui.util.Path;

/**
 * Superclass for {@link TextViewTab} and {@link VisualizationViewTab}.
 * 
 * @author Roman
 */
abstract class AbstractAnalyzingTab extends AbstractTab {

	@SuppressWarnings("PMD.LongVariable")
	private final transient String noComponentSelected;
	private final transient List<Component> components;
	private final transient VerticalLayout layout;

	/**
	 * Default Constructor.
	 * 
	 * @param path
	 *            - {@link Path} that leads to this tab.
	 * @param noComponentSelected
	 *            - Label message, if no item from project tree is selected.
	 */
	@SuppressWarnings("PMD.LongVariable")
	protected AbstractAnalyzingTab(Path path, final String noComponentSelected) {
		super(path);

		this.noComponentSelected = noComponentSelected;
		
		setSizeFull();

		final VerticalLayout layout = new VerticalLayout();
		initLayout(layout);

		layout.setMargin(true);
		layout.setSpacing(true);

		layout.setWidth("100%");
		layout.setHeight(null); // Enables vertical scrolling
		
		final Panel scrollPanel = new Panel();
		setCompositionRoot(scrollPanel);
		scrollPanel.setSizeFull();
		scrollPanel.setContent(layout);
		scrollPanel.setStyleName(ValoTheme.PANEL_BORDERLESS);

		this.layout = layout;

		components = new ArrayList<Component>();
	}

	/**
	 * Adds a new {@link Text} to the TextView.
	 * 
	 * @param component
	 *            - Adds {@link Component} to {@link AbstractAnalyzingTab}.
	 */
	public void addComponent(final Component component) {

		components.add(component);
	}

	@Override
	public void attach() {
		super.attach();
		reloadComponents();
	}

	/**
	 * Empties the list that stores the labels.
	 */
	public void clear() {
		components.clear();
	}

	/**
	 * Reloads all {@link Label}s that are stored. If none are stored, a message is displayed to the User.
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void reloadComponents() {
		layout.removeAllComponents();

		for (final Component component : components) {
			layout.addComponent(component);
		}

		if (layout.getComponentCount() == 0) {
			layout.addComponent(new Label(getLocal(noComponentSelected)));
		}
	}

}