package de.unistuttgart.ims.fictionnet.gui.components.optionpanel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import de.unistuttgart.ims.fictionnet.analysis.Result;
import de.unistuttgart.ims.fictionnet.datastructure.Text;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Act;
import de.unistuttgart.ims.fictionnet.datastructure.layers.content.Scene;
import de.unistuttgart.ims.fictionnet.gui.components.AbstractLocalizedCustomComponent;
import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.TableViewTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.TextViewTab;
import de.unistuttgart.ims.fictionnet.gui.ui.tabs.VisualizationViewTab;
import de.unistuttgart.ims.fictionnet.gui.util.Path;
import de.unistuttgart.ims.fictionnet.gui.util.TabFactory;

/**
 * The right-hand panel providing options for the current display mode and text.
 * 
 *
 */
public class OptionPanel extends AbstractLocalizedCustomComponent {
	private static final String CONTENT = "CONTENT";
	private static final int FILTERPANEL_INDEX = 1;
	private static final String DISPLAY_MODE = "DISPLAY_MODE";
	private static final String CORPUS = "CORPUS";
	private static final String TEXTVIEW = "TEXTVIEW";
	private static final String CORPUS_NONE = "CORPUS_NONE";
	private transient Label corpusTitle;
	private transient Label viewMode;
	private transient FilterPanel filterPanel;
	private final transient FormLayout infoLayout;
	private final transient VerticalLayout layout;
	private transient Component tabOptions;
	@SuppressWarnings("PMD.RedundantFieldInitializer")
	private transient Text text = null;

	private Panel jumpPanel;
	private Label content;

	/**
	 * 
	 * Sets the text, the filterpanel should use. Enables/Deactivates the FilterPanel as needed.
	 *
	 * @param text
	 *            - {@link Text} that should be analyzed.
	 */
	public void setText(final Text text) {
                this.text = text;

		if (tabOptions instanceof AbstractVisualizationOptions) {
			((AbstractVisualizationOptions) tabOptions).setText(text);
		}
		// Remove panel, if it is already added.
		if (filterPanel != null) {
			layout.removeComponent(filterPanel);
		}

		// Add filter panel, if text not null.
		if (text != null) {
			filterPanel = new FilterPanel(text, this);

			layout.addComponent(filterPanel, FILTERPANEL_INDEX);
			reloadContent();
		}

		if (tabOptions != null) {
			if (text == null) {
				tabOptions.setVisible(false);
			} else {
				tabOptions.setVisible(true);
			}
		} else {
			tabOptions = new VisualizationOptions(text);
		}

	}

	/**
	 * Default Constructor.
	 */
	public OptionPanel() {
		super();

		layout = new VerticalLayout();

		infoLayout = new FormLayout();
		infoLayout.setMargin(true);
		layout.addComponent(infoLayout);

		final Panel panel = new Panel(layout);
		panel.setSizeFull();

		setHeight("100%");
		setWidth("318px");
		setCompositionRoot(panel);
	}

	@Override
	public void attach() {
		super.attach();

		// Add corpus title
		corpusTitle = new Label(getLocal(CORPUS_NONE));
		corpusTitle.setCaption(getLocal(CORPUS));
		infoLayout.addComponent(corpusTitle);

		// Add viewmode
		viewMode = new Label(getLocal(TEXTVIEW));
		viewMode.setCaption(getLocal(DISPLAY_MODE));
		infoLayout.addComponent(viewMode);

		// Add Jump-Panel
		jumpPanel = new Panel();
		jumpPanel.setCaption(getLocal(CONTENT));

		content = new Label();
		content.setContentMode(ContentMode.HTML);
		content.setWidth("100%");

		jumpPanel.setContent(content);
		layout.addComponent(jumpPanel);
	}

	/**
	 * Sets the title of the current corpus.
	 * 
	 *
	 * @param title
	 *            - Name of Project, Corpus or Text.
	 */
	public void setCorpusTitle(String title) {
		corpusTitle.setValue(title);
	}

	/**
	 * Sets the view mode.
	 * 
	 *
	 * @param path
	 *            - Current path.
	 * @return false if the panel doesn't support the given mode
	 */
	public boolean setMode(Path path) {
		if (tabOptions != null) {
			layout.removeComponent(tabOptions);
		}

		jumpPanel.setVisible(false);

		switch (path) {
		case VISUALIZATIONVIEW:
			if (tabOptions != null)
				layout.addComponent(tabOptions);
			layout.addComponent(filterPanel, FILTERPANEL_INDEX);
			break;
		case ANNOTATIONVIEW:
		case TABLEVIEW:
		case TEXTVIEW:
			if (tabOptions != null) {
				layout.removeComponent(tabOptions);
			}
			layout.addComponent(filterPanel, FILTERPANEL_INDEX);
			jumpPanel.setVisible(true);
			reloadContent();

			break;
		case SYNONYMVIEW:

			if (filterPanel != null) {
				layout.removeComponent(filterPanel);
			}
			break;
		default:
			return false;
		}

		viewMode.setValue(getLocal(path.getName()));
		return true;
	}

	private void reloadContent() {
		StringBuilder links = new StringBuilder();

		String baseUri = Path.TEXTVIEW.toString() + "/" + text.getId() + "/";

		String uri = "";
		for (Scene scene : text.getLayerContainer().getSceneLayer().getScenes()) {

			for (Act act : text.getLayerContainer().getActLayer().getActs()) {
				if (act.getStart() < scene.getStart() && scene.getEnd() <= act.getEnd()) {

					String checkUri;
					try {
						checkUri = URLEncoder.encode(act.toString(), "UTF-8");
						if (!uri.equals(checkUri)) {
							uri = checkUri;
							links.append("<a href=\"#" + baseUri + uri.trim() + "\">" + act.toString() + "</a><br>");
						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					break;
				}
			}

			try {
				String sceneUri = URLEncoder.encode(scene.toString(), "UTF-8");

				links.append("<a href=\"#" + baseUri + uri.trim() + "/" + sceneUri.trim()
						+ "\"> -  " + scene.toString() + "</a><br>");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		content.setValue(links.toString());
	}

	/**
	 * Applies the filter result on all tabs.
	 * 
	 *
	 * @param result
	 *            - Result, which shall be visualized.
	 */
	protected void applyFilter(final Result result) {
		if (tabOptions instanceof AbstractVisualizationOptions) {
			((AbstractVisualizationOptions) tabOptions).filterUpdate();
		}

		// Apply on TextViewTab.
		((TextViewTab) TabFactory.getTab(Path.TEXTVIEW, FictionUI.getCurrent())).setResult(result, text);

		// Apply on VisualizationViewTab.
		((VisualizationViewTab) TabFactory.getTab(Path.VISUALIZATIONVIEW, FictionUI.getCurrent())).setResult(result);

		// Apply on TableViewTab.
		final TableViewTab tableViewTab = (TableViewTab) TabFactory.getTab(Path.TABLEVIEW, FictionUI.getCurrent());
		tableViewTab.reload(result, text);

		// TODO: Apply on AnnotationViewTab.
	}

}
