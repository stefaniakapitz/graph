package de.unistuttgart.ims.fictionnet.gui.ui.tabs;

import java.util.ArrayList;
import java.util.LinkedList;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.HorizontalLayout;

import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.gui.components.ProjectMenu;
import de.unistuttgart.ims.fictionnet.gui.util.Path;

/**
 * Tab for projectmanagement.
 * 
 * @author Roman
 */
public class ProjectManagementTab extends AbstractTab {

	private final transient HorizontalLayout layout;

	/**
	 * Default constructor.
	 * 
	 * @param path
	 *            - Path that leads to this tab.
	 */
	public ProjectManagementTab(Path path) {
		super(path);
		layout = new HorizontalLayout();
		initLayout(layout);
	}

	@Override
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void attach() {
		super.attach();

		final ProjectMenu projectMenu = new ProjectMenu();
		layout.addComponent(projectMenu);

		final TextViewTab tab = new TextViewTab(Path.PROJECTMANAGEMENT);
		layout.addComponent(tab);

		layout.setExpandRatio(tab, 1);

		projectMenu.getProjectTree().addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				final Object item = projectMenu.getProjectTree().getValue();

				if (item == null) {
					return;
				}

				

				tab.clear();
				final LinkedList<Object> stack = new LinkedList<Object>();
				stack.add(item);

				while (!stack.isEmpty()) {
					final Object element = stack.removeFirst();

					if (element instanceof Project) {

						stack.addAll(((Project) element).getCorpora());

					} else if (element instanceof Corpus) {
						final ArrayList<Text> texts = ((Corpus) element).getTexts();
						for (final Text text : texts) {
							tab.addText(text);
						}

					} else if (element instanceof Text) {
						tab.addText((Text) element);
					}
				}

				tab.reloadComponents();
			}
		});

		projectMenu.reloadTree();
	}

}
