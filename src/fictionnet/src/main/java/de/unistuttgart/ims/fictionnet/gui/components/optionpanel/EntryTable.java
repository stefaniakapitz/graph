package de.unistuttgart.ims.fictionnet.gui.components.optionpanel;

import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.AbstractTableTab;
import de.unistuttgart.ims.fictionnet.gui.util.Path;


/**
 * EntryTable for FilterWindow.
 *
 * @author Roman, Erol Aktay
 */
public class EntryTable extends AbstractTableTab {

	protected static final String ROOT_ENTITY = "ROOT_ENTITY";
	private final transient Text text;

	/**
	 * Creates a table fitted for FilterWindow.
	 *
	 * @param text - Selected Text.
	 */
	protected EntryTable(Text text) {
		// FIXME: Ugly. Extract table from AbstractTableTab to a component.
		super(Path.NONE);

		this.text = text;

		setSelectable(true, true);

		//addColumn(ENTITY, String.class, "");
		addColumn(ROOT_ENTITY, String.class, "");

		getTable().setColumnExpandRatio(getLocal(ROOT_ENTITY), 2);
	}

	@Override
	public void attach() {
		super.attach();
		reload();
	}

	@Override
	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	public void reload() {

		for (final String castMember : text.getCastList()) {
			addEntry(castMember); 
		}

	}
}
