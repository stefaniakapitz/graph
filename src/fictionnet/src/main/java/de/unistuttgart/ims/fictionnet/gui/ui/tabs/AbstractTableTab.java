package de.unistuttgart.ims.fictionnet.gui.ui.tabs;

import java.util.Set;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.Page;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.unistuttgart.ims.fictionnet.gui.util.Path;

/**
 * Super class for all tabs, which contain only one table.
 * 
 * @author Roman
 */
public abstract class AbstractTableTab extends AbstractTab {

	private static final String SEARCH = "SEARCH";
	private final transient Table table;
	private final transient IndexedContainer data;

	/**
	 * Default constructor.
	 * 
	 * @param path
	 *            - {@link Path} that leads to this tab.
	 */
	protected AbstractTableTab(final Path path) {
		super(path);

		final VerticalLayout layout = new VerticalLayout();
		initLayout(layout);

		layout.setMargin(true);
		layout.setSpacing(true);

		final TextField filterField = new TextField();
		filterField.setCaption(getLocal(SEARCH));
		layout.addComponent(filterField);

		table = new Table();
		table.setSizeFull();
		Page.getCurrent().getStyles().add(
				".wordwrap-table .v-table-cell-wrapper {\n" +
				"   white-space: normal;\n" +
				"	margin:5px;\n" +
				"	line-height: 1.5\n}" +
				"   overflow: hidden;}");
		table.setStyleName("wordwrap-table");

		data = new IndexedContainer();

		filterField.addTextChangeListener(new TextChangeListener() {

			@Override
			public void textChange(TextChangeEvent event) {
				applyFilter(event.getText());
			}

		});

		table.addItemClickListener(new ItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void itemClick(ItemClickEvent event) {
				if (table.isSelectable() && ((Set<Object>) table.getValue()).size() == 1
						&& ((Set<Object>) table.getValue()).contains(event.getItemId())) {
					table.setValue(null);
				}

			}
		});

		table.setContainerDataSource(data);
		layout.addComponent(table);

		layout.setExpandRatio(table, 1);
	}

	/**
	 * Filters rows, that contain the filterText in at least one column.
	 * 
	 * @param filterText
	 *            Text, that should be filtered
	 */
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	private void applyFilter(final String filterText) {
		final Filterable filterable = (Filterable) table.getContainerDataSource();

		// Remove old filter
		filterable.removeAllContainerFilters();

		final String[] columnIds = table.getColumnHeaders();
		Filter[] filters = new Filter[columnIds.length];

		for (int i = 0; i < columnIds.length; i++) {
			final SimpleStringFilter filter = new SimpleStringFilter(columnIds[i], filterText, true, true);

			filters[i] = filter;
		}

		final Or orFilter = new Or(filters);

		filterable.addContainerFilter(orFilter);
	}

	/**
	 * Sets the caption for the {@link Table} of this tab.
	 * 
	 * @param key
	 *            - Language key of the caption.
	 */
	protected void setTableCaption(final String key) {
		table.setCaption(getLocal(key));
	}

	/**
	 * Adds a new column to the table.
	 * 
	 * @param key
	 *            - Language key for the column name.
	 * @param classOfColumn
	 *            - Class of ojects, that are stored in this column.
	 * @param defaultValue
	 *            - Default value for the column cells.
	 * @return True, if column was added.
	 */
	protected boolean addColumn(final String key, final Class<?> classOfColumn, final String defaultValue) {

		final String value = getLocal(key);
		final boolean success = table.addContainerProperty(value, classOfColumn, defaultValue);

		return success;
	}

	/**
	 * Removes a column, which name matches.
	 * 
	 * @param key
	 *            - Language key for the column name.
	 * @return True, if column was removed.
	 */
	protected boolean removeColumn(final String key) {
		return table.removeContainerProperty(getLocal(key));
	}

	/**
	 * Adds a new row to the table.
	 * 
	 * @param cells
	 *            - Row, that should be added.
	 * @return Row-Id or null, if it failed.
	 */
	protected Object addEntry(final Object... cells) {
		final Object rowId = table.addItem(cells, null);
		table.sort();
		return rowId;
	}

	/**
	 * Removes item with specified itemID.
	 * 
	 * @param itemId
	 *            - ID of the item to be removed.
	 * @return True, if item was removed.
	 */
	protected boolean removeEntry(final Object itemId) {
		return table.removeItem(itemId);
	}

	/**
	 * Gets the item with the given ID.
	 * 
	 * @param itemId
	 *            - ID of the cell, that shall be collected.
	 * @return {@link Item} the item from the container.
	 */
	protected Item getEntry(final Object itemId) {
		return table.getItem(itemId);
	}

	/**
	 * Reloads the data for the table.
	 */
	public abstract void reload();

	/**
	 * Removes all items from a table.
	 */
	protected void clearData() {
		table.removeAllItems();
		data.removeAllItems();
	}

	/**
	 * Sets how selection should be handled.
	 * 
	 * @param selectable
	 *            - True, if items should be selectable.
	 * @param multiSelect
	 *            - True, if multiple items should be selectable.
	 */
	protected void setSelectable(boolean selectable, boolean multiSelect) {
		table.setSelectable(selectable);
		table.setMultiSelect(multiSelect);
	}

	/**
	 * Getter for Table.
	 * 
	 * @return {@link Table}
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * Hides a column.
	 * 
	 * @param propertyId
	 *            - ID of the column, which shall be hidden.
	 * @param collapsed
	 *            - True, if column shall be hidden, false, if column shall be shown.
	 */
	protected void collapseColumn(final Object propertyId, final Boolean collapsed) {
		table.setColumnCollapsingAllowed(true);
		table.setColumnCollapsed(propertyId, collapsed);
	}

}
