package de.unistuttgart.ims.fictionnet.gui.components.graph;

import java.util.List;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.StyleSheet;
import com.vaadin.ui.AbstractJavaScriptComponent;

@JavaScript({ "https://cdnjs.cloudflare.com/ajax/libs/d3/3.5.5/d3.min.js",
		"DiagramConnector_v5.js", "http://code.jquery.com/jquery-2.1.0.min.js" })
@StyleSheet({ "bar.css", "lineChart.css", "style.css" })
/**
 * Implements which javascript libraries and sources are to use,
 * AbstractJavaScriptComponent is used to interact with the JavaScriptConnector
 * @author Estefania, Günay, Erik-Felix Tinsel
 *
 */
public class Diagram extends AbstractJavaScriptComponent {

	/**
	 * calls the export function in the diagrammconnector javascript to save
	 * selected graph.
	 */
	public void exportPNG() {
		callFunction("exportGraphToPNG");
	}


	/**
	 * Set the value of the graph, if true a graph is visualized
	 * 
	 * @param value
	 */
	public void setGraph(boolean value) {
		getState().chartblue = value;
	}

	/**
	 * Get a String to choose the graph
	 * 
	 * @param graph
	 */
	public void setSelectedGraph(String graph) {
		getState().diagram = graph;
	}

	/**
	 * 
	 * @param act
	 */
	public void setChapter(int[] act) {
		getState().chapters = act;
	}

	public void setPerson(int[] person) {
		getState().persons = person;
	}

	public void setChapter2(int[] act) {
		getState().chapters2 = act;
	}

	public void setPerson2(int[] person) {
		getState().persons2 = person;
	}

	public void setConversationObject(String[] conversation) {
		getState().setConvPersons(conversation);
	}

	public void setGroups(int[] group) {
		getState().setGroups(group);
	}

	public void setSource(int[] source) {
		getState().setSource(source);
	}

	public void setTarget(int[] target) {
		getState().setTarget(target);
	}

	public void setValue(int[] value) {
		getState().setValue(value);
	}

	/**
	 * Get the state of the Diagram
	 */
	@Override
	public DiagramState getState() {
		return (DiagramState) super.getState();
	}

}
