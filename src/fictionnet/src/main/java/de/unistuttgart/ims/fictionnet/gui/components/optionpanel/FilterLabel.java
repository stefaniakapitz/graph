package de.unistuttgart.ims.fictionnet.gui.components.optionpanel;

import java.util.Set;

import com.vaadin.ui.Label;

import de.unistuttgart.ims.fictionnet.gui.components.AbstractLocalizedCustomComponent;

/**
 * FilterLabel for Object and Subject labels in FilterPanel.
 * 
 * @author Roman
 */
class FilterLabel extends AbstractLocalizedCustomComponent {

	protected static final String ANY_ONE = "ANY_ONE";
	private final transient Label label;
	private transient Set<Object> ids;

	/**
	 * Filter Label for FilterPanel.
	 * 
	 * @param ids
	 *            - IDs to table rows in FilterWindow.
	 * @param startValue
	 *            - Initial value for Label. If none is given, sets empty message. Will separate Strings with "; ".
	 */
	protected FilterLabel(Set<Object> ids, String... startValue) {
		super();

		label = new Label();
		label.setWidth("100%");

		setValue(ids, (Object[]) startValue);
		// setCaption(getLocal(captionKey));
		setSizeFull();
		setCompositionRoot(label);
	}

	/**
	 * Sets the Value of the label.
	 * 
	 * @param ids
	 *            - IDs of table rows, corresponding to the values.
	 * @param values
	 *            - Values, that should be set. Will be separated with "; ".
	 */
	protected void setValue(Set<Object> ids, Object... values) {
		this.ids = ids;

		final StringBuilder builder = new StringBuilder();

		if (values == null || values.length == 0) {
			builder.append(getLocal(ANY_ONE));
		} else {
			for (int i = 0; i < values.length; i++) {
				builder.append((String) values[i]);
				if (i != values.length - 1) {
					builder.append("; ");
				}
			}
		}

		label.setValue(builder.toString());
	}

	/**
	 * Getter for values.
	 * 
	 * @return Label value, splitted by "; ".
	 */
	protected String[] getValues() {
		return label.getValue().split("; ");
	}

	/**
	 * Getter for table ids.
	 * 
	 * @return Selected IDs for table.
	 */
	protected Set<Object> getIds() {
		return ids;
	}

	/**
	 * Returns the label text as shown.
	 * 
	 * @return String
	 */
	public String getText() {
		// TODO Auto-generated method stub
		return label.getValue();
	}
}
