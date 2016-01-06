package de.unistuttgart.ims.fictionnet.gui.ui.tabs;

import de.unistuttgart.ims.fictionnet.analysis.Result;
import de.unistuttgart.ims.fictionnet.analysis.SingleResult;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Presence;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Speaker;
import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.gui.util.Path;

/**
 * Tab to show the filter results as a table.
 * 
 * @author Roman
 */
public class TableViewTab extends AbstractTableTab {

	private static final String PRESENT_ENTITIES = "PRESENT_ENTITIES";
	private static final String SCENE = "SCENE";
	private static final String ACT = "ACT";
	private static final String SPEAKER = "SPEAKER";
	private static final String TEXT = "TEXT";

	private transient Result result;
	private transient Text text;

	/**
	 * Default constructor.
	 * 
	 * @param path
	 *            - {@link Path} which leads to this tab.
	 */
	public TableViewTab(Path path) {
		super(path);

		addColumn(ACT, String.class, "");
		addColumn(SCENE, String.class, "");
		addColumn(SPEAKER, String.class, "");
		addColumn(PRESENT_ENTITIES, String.class, "");
		addColumn(TEXT, String.class, "");
	}

	@SuppressWarnings("PMD.DataflowAnomalyAnalysis")
	@Override
	public void reload() {
		if (result == null) {
			return;
		}

		clearData();

		for (final SingleResult singleResult : result.getResults()) {
			String speakers = "";
			for (String speaker : singleResult.getSpeaker()) {
				if (!speakers.isEmpty()) {
					speakers += ", ";
				}
				speakers += speaker;
			}
			
			String objects = "";
			for (String object : singleResult.getConversationObjects()) {
				if (!objects.isEmpty()) {
					objects += ", ";
				}
				objects += object;
			}


			addEntry(
					singleResult.getAct().toString(),
					singleResult.getScene().toString(),
					speakers,
					objects,
					text.getSourceText().substring(singleResult.getSentences().getStart(),
							singleResult.getSentences().getEnd()));
		}

	}

	/**
	 * Reloads the table with new result set.
	 * 
	 * @param result
	 *            - Result object, which shall be load into the table.
	 */
	public void reload(final Result result, final Text text) {
		this.result = result;
		this.text = text;

		reload();
	}

}
