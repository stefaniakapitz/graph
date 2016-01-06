package de.unistuttgart.ims.fictionnet.gui.components.optionpanel;

import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.gui.components.AbstractLocalizedCustomComponent;
import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.VisualizationViewTab;
import de.unistuttgart.ims.fictionnet.gui.util.Path;
import de.unistuttgart.ims.fictionnet.gui.util.TabFactory;

/**
 * Abstract class for option components for the visualizations
 * 
 * @author Erol Aktay
 */
public abstract class AbstractVisualizationOptions
		extends AbstractLocalizedCustomComponent {
	
	/**
	 * Set the currently selected text
	 * 
	 * @param text 
	 */
	abstract public void setText(Text text);
	
	/**
	 * To be called when a new filter is applied
	 */
	abstract public void filterUpdate();
	
	/**
	 * Load current Visualization tab
	 * 
	 * @return the current (for the session) visualization tab
	 */
	protected VisualizationViewTab getCurrentView() {
		return (VisualizationViewTab)TabFactory.getTab(Path.VISUALIZATIONVIEW,
				FictionUI.getCurrent());
	}
}
