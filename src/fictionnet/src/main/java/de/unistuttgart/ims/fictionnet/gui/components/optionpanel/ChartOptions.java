package de.unistuttgart.ims.fictionnet.gui.components.optionpanel;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.TwinColSelect;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.VisualizationViewTab;
import java.util.List;
import java.util.Set;

/**
 * Additional visualization options, specifically for charts
 *
 * @author Erol Aktay
 */
public class ChartOptions extends AbstractVisualizationOptions {

	private final TwinColSelect actSel;
	private Text text;

	/**
	 * Initialize the options with the currently selected text
	 *
	 * @param text
	 */
	public ChartOptions(Text text) {
		Layout layout = new HorizontalLayout();

		actSel = new TwinColSelect(getLocal("ACT"));
		actSel.setRows(5);
		actSel.setMultiSelect(true);
		actSel.setWidth("220px");

		layout.addComponent(actSel);
		
		setCompositionRoot(layout);

		setText(text);

		actSel.addValueChangeListener(new ActSelListener());
		
	}

	@Override
	public final void setText(Text text) {
		this.text = text;
		
		actSel.removeAllItems();
		if (text != null) {
			List<Act> acts = text.getLayerContainer().getActLayer().getActs();
			for (Act act : acts) {
				actSel.addItem(act);
				actSel.setItemCaption(act, act.getContent());
				actSel.select(act);
			}
			
		} else {
			actSel.setVisible(false);
		}
	}

	@Override
	public void filterUpdate() {
		setText(text);
	}

	public class ActSelListener implements ValueChangeListener {

		@Override
		public void valueChange(Property.ValueChangeEvent event) {
			VisualizationViewTab vis = getCurrentView();
			if (vis == null) {
				return;
			}
			vis.setActs((Set<Act>) event.getProperty().getValue());
		}

	}
}
