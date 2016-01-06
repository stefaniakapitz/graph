package de.unistuttgart.ims.fictionnet.gui.ui.tabs;

import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.renderers.HtmlRenderer;
import de.unistuttgart.ims.fictionnet.datastructure.Synonym;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Sentence;
import de.unistuttgart.ims.fictionnet.gui.util.Path;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This tab is for reviewing and editing of synonyms of a text
 *
 * @author Erol Aktay
 */
public class SynonymViewTab extends AbstractAnalyzingTab {

	private Text text = null;
	private String castMember = null;

	private final Grid castSelect;
	private final TextField filterField;
	private final TextField synonymFilterField;
	private final Grid synonymGrid;
	private final HorizontalLayout synonymAddLayout;
	private final HorizontalLayout synonymSaveLayout;
	private final Button confirmButton;
	private final Button revertButton;
	private final Button saveButton;
	private final TextField newSynonymField;
	private final Button addButton;

	public SynonymViewTab(Path path) {
		super(path, "NO_TEXT_SELECTED");
		
		IndexedContainer container = new IndexedContainer();
		container.addContainerProperty(getLocal("ROOT_ENTITY"), String.class, null);
		container.addContainerProperty(getLocal("SYNONYMS"), String.class, null);
		castSelect = new Grid(container);
		castSelect.getColumn(getLocal("ROOT_ENTITY")).setExpandRatio(1);
		castSelect.getColumn(getLocal("ROOT_ENTITY")).setWidth(250.0);
		castSelect.getColumn(getLocal("SYNONYMS")).setExpandRatio(3);
		castSelect.addSelectionListener(new CastSelectListener());
		castSelect.setSizeFull();

		filterField = new TextField(getLocal("SEARCH"));
		filterField.addTextChangeListener(new FilterTextChangeListener());

		synonymGrid = new Grid();
		synonymGrid.addColumn(getLocal("SYNONYM_ASSIGNMENT"), String.class);
		synonymGrid.addColumn(getLocal("SYNONYM"), SynonymField.class);
		synonymGrid.addColumn(getLocal("SYNONYM_FIRST_APPEARANCE"), String.class);
		synonymGrid.addColumn(getLocal("SYNONYM_CONFIDENCE"), String.class);
		Column col = synonymGrid.addColumn(getLocal("SYNONYM_CONTEXT"), String.class);
		col.setRenderer(new HtmlRenderer());
		col.setExpandRatio(1);
		synonymGrid.setSelectionMode(Grid.SelectionMode.MULTI);
		synonymGrid.addSelectionListener(new SynonymSelectListener());
		synonymGrid.setSizeFull();
		synonymGrid.setHeightMode(HeightMode.ROW);
		synonymGrid.setHeightByRows(10.0);
		
		synonymFilterField = new TextField(getLocal("SEARCH"));
		synonymFilterField.addTextChangeListener(new SynonymFilterTextChangeListener());

		synonymAddLayout = new HorizontalLayout();
		newSynonymField = new TextField();
		newSynonymField.addTextChangeListener(new AddChangeListener());

		addButton = new Button(getLocal("ADD"));
		addButton.setEnabled(false);
		addButton.addClickListener(new AddClickListener());
		synonymAddLayout.addComponent(newSynonymField);
		synonymAddLayout.addComponent(addButton);

		synonymSaveLayout = new HorizontalLayout();
		confirmButton = new Button(getLocal("CONFIRM_SYNONYMS"));
		confirmButton.setEnabled(false);
		confirmButton.addClickListener(new ConfirmClickListener());
		revertButton = new Button(getLocal("REVERT"));
		revertButton.setEnabled(false);
		revertButton.addClickListener(new RevertClickListener());
		saveButton = new Button(getLocal("SAVE"));
		saveButton.setEnabled(false);
		saveButton.addClickListener(new SaveClickListener());
		synonymSaveLayout.addComponent(confirmButton);
		synonymSaveLayout.addComponent(revertButton);
		synonymSaveLayout.addComponent(saveButton);
	}

	public void setText(Text text) {
		this.text = text;
		super.reloadComponents();
		reload();
	}

	public void reload() {
		clear();
		castSelect.getContainerDataSource().removeAllItems();
		synonymGrid.getContainerDataSource().removeAllItems();
		confirmButton.setEnabled(false);

		if (text != null) {
			for (String cast : text.getCastList()) {
				castSelect.addRow(cast, String.join(", ", text.getStringSynonymsFor(cast)));
			}
			addComponent(filterField);
			addComponent(castSelect);
			addComponent(synonymFilterField);
			addComponent(synonymGrid);
			addComponent(synonymAddLayout);
			addComponent(synonymSaveLayout);
		}
		reloadComponents();
	}

	/**
	 * Finds the act and scene of given text positions
	 * 
	 * @param start
	 * @param end
	 * @return act and scene as human-readable localized string
	 */
	private String getLocation(int start, int end) {
			for (Scene scene : text.getLayerContainer().getSceneLayer().getScenes()) {
				if (start >= scene.getStart() 
						&& end <= scene.getEnd()) {
					return String.format("%s %d, %s %d",
							getLocal("ACT"), scene.getAct().getActNumber(),
							getLocal("SCENE"), scene.getSceneNumber());
				}
			}
		return "-";
	}
	
	/**
	 * Returns some context to given text positions
	 * 
	 * @param start
	 * @param end
	 * @return HTML-formatted context
	 */
	private String getContext(int start, int end) {
		/*int sstart, send;
		StringBuilder result = new StringBuilder();
		/*for (Sentence sentence : text.getLayerContainer().getSentenceLayer().getSentences()) {
			sstart = sentence.getStart();
			send = sentence.getEnd();
			if (start >= sstart && end <= send) {
				result.append(text.getSourceText().substring(sstart, start));
				result.append("<b>");
				result.append(text.getSourceText().substring(start, end));
				result.append("</b>");
				result.append(text.getSourceText().substring(end, send));
				return result.toString();
			}
		}*/
		int len = text.getSourceText().length();
		if (start < end && end < len) {
			return String.format("...%s<b>%s</b>%s...",
					text.getSourceText().substring(Math.max(start - 20, 0), start),
					text.getSourceText().substring(start, end),
					text.getSourceText().substring(end, Math.min(end+20, len)));
		} else {
			return "-";
		}
	}
	
	private void reloadSynonyms() {
		// FIXME: WORKAROUND for Vaadin issue #16195
		// deselect all selected items before removing items
		for (Object id : synonymGrid.getSelectedRows()) {
			synonymGrid.deselect(id);
		}

		synonymGrid.getContainerDataSource().removeAllItems();

		if (castMember == null) {
			return;
		}

		// TODO: localization
		NumberFormat percentFormatter = NumberFormat.getPercentInstance();

		// add all synonyms for the cast member pre-selected
		for (Synonym synonym : text.getSynonymsFor(castMember)) {
			Object id = synonymGrid.addRow(getLocal("SYNONYM_ASSIGNMENT_ACTIVE"),
					new SynonymField(synonym),
					getLocation(synonym.getStart(), synonym.getEnd()),
					percentFormatter.format(synonym.getConfidence()),
					getContext(synonym.getStart(), synonym.getEnd()));
			synonymGrid.select(id);
		}
		for (Entry<String, int[]> entry : text.getNotIdentifiedEntitie().entrySet()) {
			Synonym synonym = new Synonym(entry.getKey(), 0,
					entry.getValue()[0], entry.getValue()[1]);
			synonymGrid.addRow(getLocal("SYNONYM_ASSIGNMENT_NONE"),
				new SynonymField(synonym),
				getLocation(synonym.getStart(), synonym.getEnd()),
				percentFormatter.format(synonym.getConfidence()),
				getContext(synonym.getStart(), synonym.getEnd()));
		}

		// add rest of all synonyms
		for (String cast : text.getCastList()) {
			if (!cast.equals(castMember)) {
				for (Synonym synonym : text.getSynonymsFor(cast)) {
					synonymGrid.addRow(getLocal("SYNONYM_ASSIGNMENT_OTHER"),
				new SynonymField(synonym),
				getLocation(synonym.getStart(), synonym.getEnd()),
				String.format("%s (%s)", percentFormatter.format(synonym.getConfidence()), castMember),
				getContext(synonym.getStart(), synonym.getEnd()));
				}
			}
		}
		confirmButton.setEnabled(true);
		revertButton.setEnabled(false);
		saveButton.setEnabled(false);
	}

	private void saveSynonyms() {
		text.getSynonymsFor(castMember).clear();
		for (Object id : synonymGrid.getSelectedRows()) {
			Synonym synonym = ((SynonymField) synonymGrid
					.getContainerDataSource()
					.getContainerProperty(id, getLocal("SYNONYM")).getValue())
					.getSynonym();
			text.addSynonym(castMember, synonym);
		}

		try {
			text.rebuild();
		} catch (SQLException ex) {
			Logger.getLogger(SynonymViewTab.class.getName()).log(Level.SEVERE, null, ex);
		}

		reload();
	}

	class SynonymField implements Serializable {

		private final Synonym synonym;

		public SynonymField(Synonym synonym) {
			this.synonym = synonym;
		}

		public SynonymField(String name) {
			this.synonym = new Synonym(name, (float) 1.0, 0, 0);
		}

		public Synonym getSynonym() {
			return synonym;
		}

		@Override
		public String toString() {
			return synonym.getName();
		}
	}

	class SynonymFieldConverter implements Converter<String, SynonymField> {

		@Override
		public SynonymField convertToModel(String value,
				Class<? extends SynonymField> targetType, Locale locale)
				throws Converter.ConversionException {
			return new SynonymField(value);
		}

		@Override
		public String convertToPresentation(SynonymField value,
				Class<? extends String> targetType, Locale locale)
				throws Converter.ConversionException {
			return value.toString();
		}

		@Override
		public Class<SynonymField> getModelType() {
			return SynonymField.class;
		}

		@Override
		public Class<String> getPresentationType() {
			return String.class;
		}
	}

	class CastSelectListener implements SelectionListener {

		@Override
		public void select(SelectionEvent event) {
			if (castSelect.getSelectedRow() != null) {
				castMember = castSelect.getContainerDataSource()
						.getItem(castSelect.getSelectedRow())
						.getItemProperty(getLocal("ROOT_ENTITY")).getValue().toString();
			}
			reloadSynonyms();
		}

	}

	class FilterTextChangeListener implements TextChangeListener {

		@Override
		public void textChange(FieldEvents.TextChangeEvent event) {
			((IndexedContainer) castSelect.getContainerDataSource()).removeAllContainerFilters();

			if (!event.getText().isEmpty()) {
					Object pid = castSelect.getColumn(getLocal("ROOT_ENTITY")).getPropertyId();
					((IndexedContainer) castSelect.getContainerDataSource())
							.addContainerFilter(new SimpleStringFilter(pid, event.getText(), true, false));
			}
		}

	}
	
	class SynonymFilterTextChangeListener implements TextChangeListener {

		@Override
		public void textChange(FieldEvents.TextChangeEvent event) {
			((IndexedContainer) synonymGrid.getContainerDataSource()).removeAllContainerFilters();

			if (!event.getText().isEmpty()) {
					Object pid = synonymGrid.getColumn(getLocal("Synonym")).getPropertyId();
					((IndexedContainer) synonymGrid.getContainerDataSource())
							.addContainerFilter(new SimpleStringFilter(pid, event.getText(), true, false));
			}
		}

	}

	class SynonymSelectListener implements SelectionListener {

		@Override
		public void select(SelectionEvent event) {
			confirmButton.setEnabled(!synonymGrid.getSelectedRows().isEmpty());
			saveButton.setEnabled(true);
			revertButton.setEnabled(true);
		}

	}

	class ConfirmClickListener implements Button.ClickListener {

		@Override
		public void buttonClick(Button.ClickEvent event) {
			for (Object id : synonymGrid.getSelectedRows()) {
				((SynonymField) synonymGrid.getContainerDataSource().getItem(id)
						.getItemProperty(getLocal("SYNONYM")).getValue())
						.getSynonym().setConfidence((float) 1.0);
			}
			reloadSynonyms();
		}

	}
	
	class RevertClickListener implements Button.ClickListener {

		@Override
		public void buttonClick(Button.ClickEvent event) {
			reloadSynonyms();
		}

	}

	class SaveClickListener implements Button.ClickListener {

		@Override
		public void buttonClick(Button.ClickEvent event) {
			saveSynonyms();
			revertButton.setEnabled(false);
			saveButton.setEnabled(false);
		}

	}

	class AddChangeListener implements TextChangeListener {
		@Override
		public void textChange(FieldEvents.TextChangeEvent event) {
			if (event.getText().isEmpty()) {
				addButton.setEnabled(false);
			} else {
				addButton.setEnabled(true);
			}
		}

	}

	class AddClickListener implements Button.ClickListener {

		@Override
		public void buttonClick(Button.ClickEvent event) {
			Object id = synonymGrid.addRow(
					getLocal("SYNONYM_ASSIGNMENT_OWN"),
					new SynonymField(newSynonymField.getValue()),
					"-",
					"100%",
					"-");
			synonymGrid.select(id);
			revertButton.setEnabled(true);
			saveButton.setEnabled(true);
			addButton.setEnabled(false);
		}

	}
}
