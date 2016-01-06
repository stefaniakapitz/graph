package de.unistuttgart.ims.fictionnet.gui.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.JavaScript;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import de.unistuttgart.ims.fictionnet.databaseaccess.DBAccessManager;
import de.unistuttgart.ims.fictionnet.gui.util.FictionManager;
import de.unistuttgart.ims.fictionnet.gui.util.FileCleanUp;
import de.unistuttgart.ims.fictionnet.gui.util.Localization;
import de.unistuttgart.ims.fictionnet.gui.util.Path;
import de.unistuttgart.ims.fictionnet.users.Role;
import de.unistuttgart.ims.fictionnet.users.UserManagement;

/**
 * Provides the basic functionality the FictionNet UI needs, that is: Setting up database connection, Localization and
 * User access.
 * 
 * @author Roman, Erol, Erik-Felix Tinsel
 */
@Push(transport = Transport.WEBSOCKET)
@Theme("valo")
@Widgetset("de.unistuttgart.ims.fictionnet.gui.MyAppWidgetset")
@PreserveOnRefresh
@JavaScript("http://www.google.com/recaptcha/api/js/recaptcha_ajax.js")
public final class FictionUI extends UI {

	/**
	 * Servlet, on which the application is running. The init() is called only once, when a webpage is loaded for the
	 * first time.
	 * 
	 * @author Roman
	 */
	@WebServlet(name = "FictionNet", urlPatterns = { "/*", "/" }, initParams = { @WebInitParam(
			name = "org.atmosphere.useWebSocketAndServlet3", value = "true") }, asyncSupported = true,
			loadOnStartup = 0)
	@VaadinServletConfiguration(widgetset = "de.unistuttgart.ims.fictionnet.gui.MyAppWidgetset",
			productionMode = true, ui = FictionUI.class)
	public static class Servlet extends VaadinServlet {
		private static final String ADMIN_PWD = "admin_pwd";
		private static final String ADMIN = "admin";
		private static Properties config;

		@Override
		protected void servletInitialized() throws ServletException {
			super.servletInitialized();

			// Try to load server config file
			final String basepath = getService().getBaseDirectory().getAbsolutePath();
			initConfig(basepath);
			Logger.getLogger(getClass().getName()).log(Level.INFO, "Server config was loaded.");

			// Setup database connection if needed
			initDB();
			Logger.getLogger(getClass().getName()).log(Level.INFO, "Database initialized.");

			// Create main admin account if needed
			initAdmin();
			Logger.getLogger(getClass().getName()).log(Level.INFO, "Default admin created.");
			
			// start clean up thread
			FileCleanUp.getTheInstance();
		}

		/**
		 * Loads the server config file to the current VaadinSession.
		 * 
		 * @param basepath
		 *            - Basepath of the server.
		 */
		private void initConfig(final String basepath) {
			config = new Properties();
			try (FileInputStream fileInputStream =
					new FileInputStream(basepath + File.separator + "WEB-INF" + File.separator + "config.properties")) {
				config.load(fileInputStream);
				// TODO
			} catch (final FileNotFoundException ex) {
				Logger.getLogger(FictionUI.class.getName()).log(Level.SEVERE, null, ex);
			} catch (final IOException ex) {
				Logger.getLogger(FictionUI.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		/**
		 * Sets up the connection to the Database.
		 */
		private void initDB() {
			final DBAccessManager dbaccess = DBAccessManager.getTheInstance();
			if (!dbaccess.isConnected()) {
				final String dbUrl = getConfig("db_url");
				// TODO Was machen mit Exception?
				try {
					dbaccess.connect(dbUrl);
				} catch (final SQLException e) {
					Logger.getLogger(FictionUI.class.getName()).log(Level.SEVERE, "Cannot connect to Database.", e);
				}
			}
		}

		/**
		 * Initializes a default admin.
		 */
		private void initAdmin() {
			try {
				UserManagement.getTheInstance().createUser(getConfig(ADMIN), getConfig(ADMIN_PWD));
				UserManagement.getTheInstance().changeUserRole(getConfig(ADMIN), Role.ADMIN);
			} catch (SQLException ex) {
				/*
				 * FIXME: Need a way to discern if caused by already existing admin/ wrong password or some problem with
				 * the database
				 */
				Logger.getLogger(FictionUI.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		/**
		 * Returns value of given key from the server configuration.
		 * 
		 * @param key
		 *            - String
		 * @return congig value
		 */
		public static String getConfig(String key) {
			return config.getProperty(key);
		}
	}

	@Override
	protected void init(final VaadinRequest vaadinRequest) {

		// Init Uri
		if (getPage().getUriFragment() == null) {
			getPage().setUriFragment(Path.LOGIN.toString(), false);
		}

		// Get User's language setting
		initLanguage();

		// Init UI
		setPollInterval(30000); // 30 seconds.#

		// FIXME: Remove after testing
//		try {
//			FictionManager.setUser(UserManagement.getTheInstance().login("foo@bar.com", "foo"));
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		final MainTabSheet mainTab = new MainTabSheet();
		setContent(mainTab);
	}

	/**
	 * Reloads the mainTabSheet.
	 */
	public void reloadTabSheet() {
		((MainTabSheet) getContent()).updateTabs();
	}

	/**
	 * Closes UI and creates a new one. Same as refresh, if "PreserveOnRefresh" would not be active.
	 */
	public void reloadUI() {
		MainTabSheet mainTabSheet = new MainTabSheet();
		setContent(mainTabSheet);
	}

	/**
	 * Get the currently used FictionUI.
	 * 
	 * @return The current UI, or null if unavailable.
	 */
	public static FictionUI getCurrent() {
		return (FictionUI) UI.getCurrent();
	}

	/**
	 * Inits the language files and saves a {@link Localization} object in the VaadinSession.
	 */
	private void initLanguage() {
		String lang = FictionManager.readLanguageCookie();
		if (lang.isEmpty()) { // Fallback to default
			lang = Servlet.getConfig("lang");
		}
		final Localization loc = new Localization(FictionManager.getBasepath() + "/WEB-INF/lang/");
		loc.setLanguage(lang);

		FictionManager.setLocalization(loc);
	}

	/**
	 * Closes all currently open popup {@link Window}s.
	 */
	public void closeWindows() {
		final Iterator<Window> iterator = getWindows().iterator();

		while (iterator.hasNext()) {
			final Window window = iterator.next();
			window.close();
		}
	}

	/**
	 * Closes all open windows, before adding the new one.
	 */
	@Override
	public void addWindow(Window window) throws IllegalArgumentException, NullPointerException {
		closeWindows();
		super.addWindow(window);
	}
}
