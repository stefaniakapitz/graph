package de.unistuttgart.ims.fictionnet.gui.util;

import java.io.File;

import javax.servlet.http.Cookie;

import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;

import de.unistuttgart.ims.fictionnet.gui.ui.FictionUI;
import de.unistuttgart.ims.fictionnet.users.User;

/**
 * This class manages all data, that is saved in the VaadinSession or in cookies.
 * 
 * @author Roman
 */
public final class FictionManager {

	private static final String COOKIE_LANG = "lang";

	/**
	 * Deactivated Constructor.
	 */
	private FictionManager() {
	};

	/**
	 * Get a specific cookie.
	 * 
	 * @param name
	 *            - Name of the cookie.
	 * @return {@link Cookie} or null, if there is no cookie with that name.
	 */
	@SuppressWarnings({ "PMD.OnlyOneReturn", "PMD.DataflowAnomalyAnalysis" })
	private static Cookie getCookieByName(String name) {
		// Fetch all cookies from the request
		final Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

		if (cookies == null) {
			return null;
		}

		// Iterate to find cookie by its name
		for (final Cookie cookie : cookies) {
			if (name.equals(cookie.getName())) {
				return cookie;
			}
		}

		return null;
	}

	/**
	 * Gets the sessions {@link Localization}. To prevent Null-Pointer, don't use this method while the
	 * session/FictionUI is being initialized. Calling it from attach() methods is considered safe.
	 * 
	 * @return {@link Localization}. Or null, if VaadinSession isn't fully initialized.
	 */
	public static Localization getLocalization() {
		return VaadinSession.getCurrent().getAttribute(Localization.class);
	}

	/**
	 * Gets the basepath.
	 * 
	 * @return String
	 */
	public static String getBasepath() {
		return VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
	}

	/**
	 * Gets the sessions {@link User}. To prevent Null-Pointer, don't use this method while the session/FictionUI is
	 * being initialized. Calling it from attach() methods is considered safe.
	 * 
	 * @return {@link User}. Or null, if VaadinSession isn't fully initialized.
	 */
	public static User getUser() {
		return VaadinSession.getCurrent().getAttribute(User.class);
	}

	/**
	 * Restores the language choice of the client.
	 *
	 * @return The language in lower-case ISO 639-1 format (e.g. "de", "en") or "" if no language has been set.
	 */
	@SuppressWarnings("PMD.OnlyOneReturn")
	public static String readLanguageCookie() {
		final Cookie cookie = getCookieByName(COOKIE_LANG);

		if (cookie == null) {
			return "";
		}
		return cookie.getValue();
	}

	/**
	 * Sets the session {@link Localization}.
	 * 
	 * @param loc
	 *            {@link Localization}
	 */
	public static void setLocalization(final Localization loc) {
		VaadinSession.getCurrent().setAttribute(Localization.class, loc);
	}

	/**
	 * Sets the sessions logged in user.
	 * 
	 * @param user
	 *            - {@link User} that shall be set as logged in user.
	 */
	public static void setUser(final User user) {
		VaadinSession.getCurrent().setAttribute(User.class, user);
	}

	/**
	 * Saves the language choice as a cookie on the client, to remember it over multiple VaadinSessions.
	 *
	 * @param lang
	 *            The language to save in lower-case ISO 639-1 format (e.g. "de", "en")
	 */
	public static void writeLanguageCookie(final String lang) {

		final Cookie cookie = new Cookie(COOKIE_LANG, lang);
		cookie.setComment("Cookie for storing the selected language.");

		// Set path
		String path = VaadinService.getCurrentRequest().getContextPath();
		if (path.isEmpty()) {
			// For some reason, Vaadin returns an empty string, if application runs on root.
			path = "/";
		}
		cookie.setPath(path);
		cookie.setMaxAge(3600 * 24 * 365 * 5);

		if (VaadinService.getCurrentResponse() != null) {
			VaadinService.getCurrentResponse().addCookie(cookie);
		} else {
			// FIXME: reintroduced old hack because setting language in a 
			//			logged in user session threw null pointer exception
			Page.getCurrent().getJavaScript().execute(
					String.format("document.cookie = '%s=%s; expires=%s;';",
							cookie.getName(), cookie.getValue(),
							cookie.getMaxAge()));
		}
	}

	/**
	 * Ends the current session and redirects to login page.
	 */
	public static void logout() {
		// Discard current session.
		VaadinSession.getCurrent().close();

		// Discard current UI and load new one, which creates a new session.
		FictionUI.getCurrent().getPage().reload();

		// Set Uri Fragment to match the login path.
		FictionUI.getCurrent().getPage().setUriFragment(Path.LOGIN.toString(), false);
	}

	/**
	 * Gets a resource.
	 * 
	 * @param resourceName
	 *            - Name of resource, without ".png".
	 * @return {@link Resource} to an icon image.
	 */
	public static Resource getIconResource(String resourceName) {
		final FileResource resource =
				new FileResource(new File(VaadinService.getCurrent().getBaseDirectory().getAbsolutePath()
						+ "/WEB-INF/images/" + resourceName + ".png"));
		return resource;
	}
}


