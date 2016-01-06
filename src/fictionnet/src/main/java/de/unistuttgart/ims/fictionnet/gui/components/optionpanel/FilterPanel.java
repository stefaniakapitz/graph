package de.unistuttgart.ims.fictionnet.gui.components.optionpanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import de.unistuttgart.ims.fictionnet.analysis.ActionTypes;
import de.unistuttgart.ims.fictionnet.analysis.Analyzer;
import de.unistuttgart.ims.fictionnet.analysis.Conjunction;
import de.unistuttgart.ims.fictionnet.analysis.Filter;
import de.unistuttgart.ims.fictionnet.analysis.Result;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.gui.components.AbstractLocalizedCustomComponent;
import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;

/**
 * Panel for text filters.
 * 
 */
public class FilterPanel extends AbstractLocalizedCustomComponent {
	private static final String NO_RESULTS_FOUND = "NO_RESULTS_FOUND";
	private static final String ACTION = "ACTION";
	private static final String APPLY_FILTER = "APPLY_FILTER";
	private static final String FILTER = "FILTER";
	private static final String OBJECT = "OBJECT";
	private static final String SUBJECT = "SUBJECT";

	private static final Resource NEW_WINDOW = FictionManager.getIconResource("NEW_WINDOW");
	private final transient FilterLabel objectLabel;
	private final transient FilterLabel subjectLabel;
	private final transient ComboBox actionBox;
	private final VerticalLayout layout;

	/**
	 * FilterPanel for a specific text.
	 * 
	 * @param text
	 *            - Text, on which the filter should run.
	 * @param optionPanel
	 *            - OptionPanel, which manages the filter.
	 */
	FilterPanel(final Text text, final OptionPanel optionPanel) {

		super();

		layout = new VerticalLayout();
		final Panel panel = new Panel(getLocal(FILTER), layout);
		setCompositionRoot(panel);
		setWidth("100%");

		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setWidth("100%");

		final HorizontalLayout subjectLayout = new HorizontalLayout();
		final HorizontalLayout objectLayout = new HorizontalLayout();

		// Init subjectLabel
		subjectLabel = createFilterLayout(SUBJECT, text, subjectLayout);

		// Init actionBox
		actionBox = createActionBox(subjectLayout, objectLayout);

		// Init objectLabel
		objectLabel = createFilterLayout(OBJECT, text, objectLayout);

		final Button apply = new Button(getLocal(APPLY_FILTER));
		layout.addComponent(apply);

		apply.addClickListener(new ClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void buttonClick(ClickEvent event) {
				// Create Filter object.
				final Filter filter = new Filter();

				filter.setActionType((ActionTypes) actionBox.getValue());

				if (filter.getActionType().hasSubject()) {
					if (subjectLabel.getText().equals(getLocal(FilterLabel.ANY_ONE))) {
						filter.setActionSubject(new ArrayList<String>(text.getCastList()));
						filter.setSubjectConjunction(Conjunction.OR);
					} else {
						filter.setActionSubject(Arrays.asList(subjectLabel.getValues()));
						filter.setSubjectConjunction(Conjunction.OR); // FIXME
					}
				}

				if (filter.getActionType().hasObject()) {
					if (objectLabel.getText().equals(getLocal(FilterLabel.ANY_ONE))) {
						filter.setActionObject(new ArrayList<String>(text.getCastList()));
						filter.setObjectConjunction(Conjunction.OR);
					} else {
						filter.setActionObject(Arrays.asList(objectLabel.getValues()));
						filter.setObjectConjunction(Conjunction.OR); // FIXME
					}
				}

				// Analyze filter.
				final Analyzer analyzer = new Analyzer();

				final Result result = analyzer.analyze(text, filter);

				// Apply result to tabs.
				if (result.getResults().isEmpty()) {
					Notification.show(getLocal(NO_RESULTS_FOUND), Type.WARNING_MESSAGE);
				}
				optionPanel.applyFilter(result);
				FictionUI.getCurrent().closeWindows();
			}
		});

	}

	/**
	 * Creates a new Combobox.
	 * 
	 * @param layout
	 *            - Layout of FilterPanel.
	 * @return {@link ComboBox}
	 */
	private ComboBox createActionBox(final HorizontalLayout subjectLayout, final HorizontalLayout objectLayout) {
		final ComboBox box = new ComboBox(getLocal(ACTION));
		// FIXME: Add all, when they work.
		//box.addItems((Object[]) ActionTypes.values());
		box.addItem(ActionTypes.TALKS_WITH);
		box.addItem(ActionTypes.TALKS_ABOUT);
		box.setNullSelectionAllowed(false);
		layout.addComponent(box);

		box.addValueChangeListener(new ValueChangeListener() {

			@Override
			public void valueChange(ValueChangeEvent event) {
				final ActionTypes type = (ActionTypes) box.getValue();

				if (type != null) {
					subjectLayout.setEnabled(type.hasSubject());
					objectLayout.setEnabled(type.hasObject());
				}
			}

		});

		box.setValue(ActionTypes.TALKS_ABOUT);

		return box;
	}

	/**
	 * Creates a new Layout for filtering.
	 * 
	 * @param caption
	 *            - OBJECT or SUBJECT.
	 * @param noValue
	 *            - NO_OBJECT or NO_SUBJECT
	 * @param text
	 *            - Text, to which this filter belongs.
	 * @param horizontalLayout
	 *            - Layout of this element.
	 * @return {@link FilterLabel}
	 */
	private FilterLabel createFilterLayout(final String caption, final Text text, HorizontalLayout horizontalLayout) {
		horizontalLayout.setSpacing(true);
		horizontalLayout.setSizeFull();
		layout.addComponent(horizontalLayout);

		final Button button = new Button(NEW_WINDOW);
		button.setStyleName("borderless");
		horizontalLayout.addComponent(button);
		horizontalLayout.setComponentAlignment(button, Alignment.MIDDLE_RIGHT);

		final FilterLabel label = new FilterLabel(null);
		horizontalLayout.addComponent(label);
		horizontalLayout.setExpandRatio(label, 1);
		horizontalLayout.setComponentAlignment(label, Alignment.MIDDLE_LEFT);

		// Init Listeners.
		button.addClickListener(new ClickListener() {

			@Override
			@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
			public void buttonClick(ClickEvent event) {

				boolean open = false;
				for (final Window window : FictionUI.getCurrent().getWindows()) {
					open = getLocal(caption).equals(window.getCaption());
				}

				FictionUI.getCurrent().closeWindows();

				if (!open) {
					new FilterWindow(text, caption, label);
				}
			}
		});

		return label;
	}
}
