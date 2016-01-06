package de.unistuttgart.ims.fictionnet.gui.ui.tabs;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import de.unistuttgart.ims.fictionnet.gui.util.Path;


/**
 * Logout Tab to logout.
 * 
 * @author Erik-Felix Tinsel
 */
@SuppressWarnings("serial")
@Theme("valo")
@Widgetset("de.unistuttgart.ims.fictionnet.gui.MyAppWidgetset")
public class LogoutTab extends AbstractTab {

	/**
	 * Placeholder.
	 * @param path given path
	 */
	public LogoutTab(Path path) {
		super(path);

	}
}
