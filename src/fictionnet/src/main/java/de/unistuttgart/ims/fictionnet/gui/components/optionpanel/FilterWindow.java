package de.unistuttgart.ims.fictionnet.gui.components.optionpanel;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.gui.components.AbstractLocalizedCustomComponent;
import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Popup window for changing subject and object of the filter.
 *
 * @author Roman, Erol Aktay
 */
public class FilterWindow extends AbstractLocalizedCustomComponent {

	private final Window window;
	private final EntryTable table;
	private final Text text;
	private final FilterLabel filterLabel;

	/**
	 * Creates a new Popup {@link Window}, to choose filter object or subject.
	 *
	 * @param text
	 *            - {@link Text}, to which filter belongs.
	 * @param captionKey
	 *            - LanguageKey for caption (either OBJECT or SUBJECT).
	 * @param filterLabel
	 *            - {@link FilterLabel}, on which the selected result shall be displayed.
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	protected FilterWindow(final Text text, final String captionKey, final FilterLabel filterLabel) {
		this.text = text;

		window = new Window(getLocal(captionKey));
		window.setWidth("50%");
		window.setHeight("100%");
		window.addCloseListener(new WindowCloseListener());

		final VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();

		window.setContent(layout);

		layout.setMargin(true);
		layout.setSpacing(true);

		table = new EntryTable(text);
		layout.addComponent(table);
		layout.setExpandRatio(table, 1);

		if (filterLabel.getIds() != null) {
			table.getTable().setValue(filterLabel.getIds());
		}

		// Lower buttons
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);

		Button okButton = new Button(getLocal("CONFIRM"));
		okButton.addClickListener(new CloseButtonListener());
		okButton.setSizeUndefined();

		buttonLayout.addComponent(okButton);
		layout.addComponent(buttonLayout);
		layout.setComponentAlignment(buttonLayout, Alignment.BOTTOM_CENTER);

		this.filterLabel = filterLabel;

		window.center();

		FictionUI.getCurrent().addWindow(window);
	}

	/**
	 * Listener for the close button
	 * 
	 */
	private class CloseButtonListener implements Button.ClickListener {

		@Override
		public void buttonClick(Button.ClickEvent event) {
			window.close();
		}
	}

	/**
	 * Handles window closing
	 * 
	 */
	private class WindowCloseListener implements Window.CloseListener {

		private static final String ERROR = "ERROR";

		@Override
		public void windowClose(Window.CloseEvent e) {
			final Set<Object> ids = (Set<Object>) table.getTable().getValue();
			final Set<String> values = new HashSet<String>();

			// FIXME: Find and kill the nullpointer!
			try {
				for (final Object id : ids) {
					values.add((table.getTable().getItem(id)
							.getItemProperty(getLocal(EntryTable.ROOT_ENTITY)).getValue()).toString());
				}
			} catch (NullPointerException exception) {
				Notification.show(getLocal(ERROR), Type.ERROR_MESSAGE);
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Nullpointer on window close in FilterWindow.", exception);
			}

			filterLabel.setValue(ids, values.toArray());
		}
	}
}
