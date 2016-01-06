package de.unistuttgart.ims.fictionnet.gui.components.graph;

import java.util.List;

import com.vaadin.shared.ui.JavaScriptComponentState;
/**
 * This class holds the properties which are accessible from the server/client side.
 * Provides getters/setters to interact with the client
 * @author Estefania
 *
 */
public class DiagramState extends JavaScriptComponentState  {
	 private List<Integer> coords;
	 public boolean chartblue;
	 public String diagram;
	 public int[]chapters;
	 public int[]persons;
	 public int[]chapters2;
	 public int[]persons2;
	 private String[] convPersons;
	 private int[] groups;
	 private int[] source;
	 private int [] target;
	 private int [] value;
	 

	 public int[] getSource() {
		return source;
	}
	public void setSource(int[] source) {
		this.source = source;
	}
	public int[] getTarget() {
		return target;
	}
	public void setTarget(int[] target) {
		this.target = target;
	}
	public int[] getValue() {
		return value;
	}
	public void setValue(int[] value) {
		this.value = value;
	}
	public int[] getGroups() {
		return groups;
	}
	public void setGroups(int[] group) {
		this.groups = group;
	}
	public String[] getConvPersons() {
		return convPersons;
	}
	public void setConvPersons(String[] convPerson) {
		this.convPersons = convPerson;
	}
	//	 public int[] getPersons2() {
//		return persons2;
//	}
//	public void setPersons2(int[] person2) {
//		this.persons2 = person2;
//	}
//	
//	 public int[] getChapters2() {
//		return chapters2;
//	}
//	public void setChapter2(int[] act2) {
//		this.chapters2 = act2;
//	}
	// Mehr Methoden implementieren für ein 2tes Datenset	 
	/**
	 * @return chapter
	 */
	 public int[] getChapter() {
		return chapters;
	}
	 /**
	  * 
	  * @param chapter
	  */
	public void setChapter(int[] chapter) {
		this.chapters = chapter;
	}
	/**
	 * 
	 * @return person
	 */
	public int[] getPerson() {
		return persons;
	}
	/**
	 * 
	 * @param person
	 */
	public void setPerson(int[] person) {
		this.persons = person;
	}
	/**
	  * Return a boolean to decide if the graph is to visualize
	  * @return chartblue
	  */
	 public boolean getGraph(){
		 return chartblue;
	 }
	 /**
	  * Get a boolean to choose if the graph is selected
	  * @param chartblue
	  */
	 public void setGraph(final boolean chartblue){
		 this.chartblue = chartblue;
	 }
	 /**
	  * Return the coordinates for the diagram
	  * @return coords
	  */
	    public List<Integer> getCoords() {
	        return coords;
	    }
	 /**
	  * Set the coordinates
	  * @param coords
	  */
	    public void setCoords(final List<Integer> coords) {
	        this.coords = coords;
	    }
	    /**
	     * Get the choosen graph
	     * @return diagram
	     */
	    public String getSelectedGraph(){
	    	return diagram;
	    }
	    /**
	     * Set a graph
	     * @param diagram
	     */
	    public void setSelectedGraph(final String diagram){
	    	this.diagram=diagram;
	    }
}

