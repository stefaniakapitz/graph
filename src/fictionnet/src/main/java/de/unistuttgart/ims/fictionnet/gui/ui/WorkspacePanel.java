package de.unistuttgart.ims.fictionnet.gui.ui;

import java.util.ArrayList;
import java.util.LinkedList;

import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;

import de.unistuttgart.ims.fictionnet.datastructure.Corpus;
import de.unistuttgart.ims.fictionnet.datastructure.Project;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.TextViewTab;
import de.unistuttgart.ims.fictionnet.gui.util.Path;
import de.unistuttgart.ims.fictionnet.gui.util.TabFactory;

/**
 * 
 * @author Roman, Erol
 */
public class WorkspacePanel extends Panel {

	private final transient TabSheet tabSheet;

	/**
	 * Constructor for {@link WorkspacePanel}. Content initialized in attach() method.
	 */
	public WorkspacePanel() {
		super();
		setSizeFull();

		tabSheet = new TabSheet();
		tabSheet.setSizeFull();

		setContent(tabSheet);
	}

	/**
	 * Adds all texts to textViewTab.
	 * 
	 * @param item
	 *            - Must be of type Project, Corpus or Text
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void addTexts(final Object item) {

		if (item == null) {
			return;
		}

		final TextViewTab textViewTab = (TextViewTab) TabFactory.getTab(Path.TEXTVIEW, FictionUI.getCurrent());

		textViewTab.clear();
		final LinkedList<Object> stack = new LinkedList<Object>();
		stack.add(item);

		while (!stack.isEmpty()) {
			final Object element = stack.removeFirst();

			if (element instanceof Project) {

				stack.addAll(((Project) element).getCorpora());

			} else if (element instanceof Corpus) {
				final ArrayList<Text> texts = ((Corpus) element).getTexts();
				for (final Text text : texts) {
					textViewTab.addText(text);
				}

			} else if (element instanceof Text) {
				textViewTab.addText((Text) element);
			}
		}

		textViewTab.reloadComponents();
	}

	@Override
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void attach() {
		super.attach();

		for (final Path path : Path.valuesWorkspaceTabSheet()) {
			tabSheet.addTab(TabFactory.getTab(path, FictionUI.getCurrent()));
		}

	}

	/**
	 * Getter for tabsheet.
	 * 
	 * @return {@link TabSheet}
	 */
	public TabSheet getTabSheet() {
		// TODO Auto-generated method stub
		return tabSheet;
	}

}
