package de.unistuttgart.ims.fictionnet.gui.ui.tabs;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Button.ClickEvent;

import de.unistuttgart.ims.fictionnet.analysis.Result;
import de.unistuttgart.ims.fictionnet.analysis.SingleResult;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.gui.components.graph.DiagramCreator;
import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;
import de.unistuttgart.ims.fictionnet.gui.util.Path;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Class for the visual representation of graphs.
 * 
 * @author Roman, Stefania, Erol Aktay
 */
public class VisualizationViewTab extends AbstractAnalyzingTab {

	// parameters of the contained diagram
	private static final String SAVE = "SAVE";
	private static final String SAVE_TEXT = "SAVE_TEXT";
	private Text text = null;
	private GraphType type = null;
	private List<SingleResult> results = null;
	private Set<Act> acts = null;


	private static final String NO_GRAPH_SELECTED = "NO_GRAPH_SELECTED";

	/**
	 * Default Constructor.
	 * 
	 * @param path
	 *            - {@link Path}, that leads to this tab.
	 */
	public VisualizationViewTab(Path path) {
		super(path, NO_GRAPH_SELECTED);

	}

	/**
	 * Adds a new graph to the VisualizationView. Probably not necessary.
	 * 
	 * @param graph
	 *            - Graph component.
	 */
	public void reloadGraph() {
		clear();
		final DiagramCreator newGraph = new DiagramCreator(type, text, acts, results);
		Button exportButton = new Button();
		exportButton.setIcon(
				FictionManager.getIconResource(SAVE),
				getLocal(SAVE_TEXT));
		exportButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				newGraph.getDiagram().exportPNG();				
			}
		});
	
		addComponent(exportButton);
		addComponent(newGraph);
		
		reloadComponents();
	}

	/**
	 * Set the text of the diagram.
	 * 
	 * @param text
	 */
	public void setText(Text text) {
		this.text = text;
		results = null;

		clear();
		reloadComponents();
	}

	/**
	 * Set the type of diagram/graph to display.
	 * 
	 * @param type
	 */
	public void setGraph(GraphType type) {
		this.type = type;
		reloadGraph();
	}

	/**
	 * Sets the acts in the visualization (does nothing for network graphs).
	 * 
	 * @param acts
	 */
	public void setActs(Set<Act> acts) {
		this.acts = acts;
		reloadGraph();
	}

	/**
	 * Sets the current filter results.
	 * 
	 * @param result
	 */
	public void setResult(Result result) {
		if (result != null) {
			if (result.getResults().isEmpty()) {
				this.results = null;
			} else {
				this.results = result.getResults();
			}
		}
		
		reloadGraph();
	}

	/**
	 * The type of diagram/graph.
	 */
	public enum GraphType {
		BAR("BarChart"), LINE("LineChart"), NETWORK("ForceDirectedGraph");

		private final String name;

		GraphType(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
