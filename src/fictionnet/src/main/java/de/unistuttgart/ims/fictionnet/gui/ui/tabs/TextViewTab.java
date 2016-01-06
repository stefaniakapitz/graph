package de.unistuttgart.ims.fictionnet.gui.ui.tabs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.codehaus.plexus.interpolation.SingleResponseValueSource;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

import de.unistuttgart.ims.fictionnet.analysis.Result;
import de.unistuttgart.ims.fictionnet.analysis.SingleResult;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Sentence;
import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;
import de.unistuttgart.ims.fictionnet.gui.util.Path;

/**
 * This class is used to create the content in the TextViewTab.
 * 
 * @author Roman
 *
 */
public class TextViewTab extends AbstractAnalyzingTab {

	private static final String NO_TEXT_SELECTED = "NO_TEXT_SELECTED";
	private static final String FOUND_RESULTS = "FOUND_RESULTS";

	private Result filterResult = null;

	/**
	 * @return the filterResult
	 */
	public Result getFilterResult() {
		return filterResult;
	}

	/**
	 * @param filterResult
	 *            the filterResult to set
	 */
	public void setResult(Result filterResult, Text text) {
		this.filterResult = filterResult;
		clear();

		StringBuilder builder = new StringBuilder();

		if (filterResult == null || filterResult.getResults().isEmpty()) {
			addText(text);
		} else {

			for (final Act act : text.getLayerContainer().getActLayer().getActs()) {
				final List<SingleResult> singleResults = new ArrayList<SingleResult>();

				for (final SingleResult singleResult : filterResult.getResults()) {
					if (singleResult.getAct().toString().equals(act.toString())) {
						singleResults.add(singleResult);
					}
				}

				builder.append("<p>" + singleResults.size() + " " + getLocal(FOUND_RESULTS) + " " + act.toString()
						+ "</p>");

				for (final Scene scene : text.getLayerContainer().getSceneLayer().getScenes()) {

					if (scene.getAct().toString().equals(act.toString())) {

						final List<Sentence> sentencesInScene = new ArrayList<Sentence>();

						for (final SingleResult singleResult : singleResults) {
							if (singleResult.getScene().toString().equals(scene.toString())) {
								sentencesInScene.add(singleResult.getSentences());
							}
						}

						if (!sentencesInScene.isEmpty()) {
							builder.append(text.getHtmlText(scene, sentencesInScene));
						}
					}

				}
			}

			if (!builder.toString().isEmpty()) {
				final Label label = new Label(builder.toString());
				label.setContentMode(ContentMode.HTML);
				label.setCaptionAsHtml(true);
				addComponent(label);
			}

		}

		reloadComponents();
	}

	/**
	 * Constructor for TextViewTab.
	 * 
	 * @param path
	 *            - Path that leads to this tab.
	 */
	public TextViewTab(Path path) {
		super(path, NO_TEXT_SELECTED);
	}

	/**
	 * Adds a new text to the textview.
	 * 
	 * @param text
	 *            {@link Text}
	 */
	public void addText(final Text text) {
		final Label label = new Label(text.getHtmlText());
		label.setContentMode(ContentMode.HTML);
		addComponent(label);
	}
}
