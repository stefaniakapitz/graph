package de.unistuttgart.ims.fictionnet.gui.components.optionpanel;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;
import de.unistuttgart.ims.fictionnet.datastructure.Text;

import de.unistuttgart.ims.fictionnet.gui.ui.tabs.VisualizationViewTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.VisualizationViewTab.GraphType;

/**
 * Specific options for visualizations
 *
 * @author Erol Aktay
 */
public class VisualizationOptions extends AbstractVisualizationOptions
		implements ValueChangeListener {

	private final ComboBox graphSel;
	private AbstractVisualizationOptions subOptions = null;
	private final VerticalLayout layout = new VerticalLayout();
	private Text text;
	private GraphType graphType;

	/**
	 * Initialize the options with the currently selected text
	 * 
	 * @param text 
	 */
	public VisualizationOptions(Text text) {
		super();

		graphSel = new ComboBox(getLocal("CHART_TYPE"));
		graphSel.setNullSelectionAllowed(false);
		graphSel.setSizeFull();

		graphSel.addItem(GraphType.BAR);
		graphSel.setItemCaption(GraphType.BAR, getLocal("CHART_BAR"));
		graphSel.addItem(GraphType.NETWORK);
		graphSel.setItemCaption(GraphType.NETWORK, getLocal("CHART_NETWORK"));
		graphSel.addItem(GraphType.LINE);
		graphSel.setItemCaption(GraphType.LINE, getLocal("CHART_LINE"));

		layout.addComponent(graphSel);
		layout.setMargin(true);
		setCompositionRoot(layout);

		setText(text);
		
		//set bar chart as default
		graphSel.select(GraphType.BAR);
		update(GraphType.BAR);
		
		init();
	}

	/**
	 * Additional initialization after object has been created
	 */
	private void init() {
		graphSel.addValueChangeListener(this);
	}

	/**
	 * Update options based on graph type
	 * 
	 * @param graphType
	 */
	private void update(GraphType graphType) {
		if (graphType != null)
		if (subOptions != null)
			layout.removeComponent(subOptions);
		switch (graphType) {
			case BAR:
			case LINE:
                        case NETWORK:
				subOptions = new ChartOptions(text);
				layout.addComponent(subOptions);				
		}
		
		VisualizationViewTab vis = getCurrentView();
		if (vis == null) {
			return;
		}

		vis.setGraph(graphType);
	}
	
	@Override
	public void valueChange(Property.ValueChangeEvent event) {		
		update((GraphType) event.getProperty().getValue());
	}

	@Override
	public final void setText(Text text) {
		this.text = text;
		if (subOptions != null)
			subOptions.setText(text);
	}
	
	/**
	 * To be called when a new filter is applied
	 */
	@Override
	public void filterUpdate() {
		subOptions.filterUpdate();
	}
}
