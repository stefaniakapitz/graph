package de.unistuttgart.ims.fictionnet.gui.util;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.*;
import de.unistuttgart.ims.fictionnet.users.User;

/***
 * Sends an Email to given Userslist.
 * 
 * @author Erik-Felix Tinsel
 *
 */
public class SendEmail {
	
	/**
	 * Method to send the Email.
	 * @param usersList the list of users.
	 * @param title the title the email should have.
	 * @param text the text the title should contain.
	 * @param sender the given sender of the email. 
	 */
	public static void sendMail(List<User> usersList, String title,
			String text, User sender) {

		final String host = "adler";
		final Properties props = new Properties();
		props.put("mail.smtp.host", host);
		final Session session = Session.getInstance(props, null);
		session.setDebug(false);
		try {
			// create a message
			final MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress("e.tinsel@googlemail.com"));
			String emailCC = "";

			for (int i = 0; i < usersList.size(); i++) {
				if (i == usersList.size() - 1) {
					emailCC += usersList.get(i);
				} else {
					emailCC += usersList.get(i) + ",";
				}

			}
			msg.addRecipients(Message.RecipientType.CC,
					InternetAddress.parse(emailCC));

			msg.setSubject(title);
			// If the desired charset is known, you can use
			// setText(text, charset)

			final String finalText = sender.getEmail() + " " + text + "<br />"
					+ "http://clarin11.ims.uni-stuttgart.de/fictionnet";

			msg.setContent(finalText, "text/html");

			msg.setSentDate(new Date());

			Transport.send(msg);
		} catch (MessagingException mex) {
			System.out.println("\n--Exception handling in msgsendsample.java");

			mex.printStackTrace();
			System.out.println();
			Exception ex = mex;
			do {
				if (ex instanceof SendFailedException) {
					SendFailedException sfex = (SendFailedException) ex;
					Address[] invalid = sfex.getInvalidAddresses();
					if (invalid != null) {
						System.out.println("    ** Invalid Addresses");
						for (int i = 0; i < invalid.length; i++)
							System.out.println("         " + invalid[i]);
					}
					Address[] validUnsent = sfex.getValidUnsentAddresses();
					if (validUnsent != null) {
						System.out.println("    ** ValidUnsent Addresses");
						for (int i = 0; i < validUnsent.length; i++)
							System.out.println("         " + validUnsent[i]);
					}
					Address[] validSent = sfex.getValidSentAddresses();
					if (validSent != null) {
						System.out.println("    ** ValidSent Addresses");
						for (int i = 0; i < validSent.length; i++)
							System.out.println("         " + validSent[i]);
					}
				}
				System.out.println();
				if (ex instanceof MessagingException)
					ex = ((MessagingException) ex).getNextException();
				else
					ex = null;
			} while (ex != null);
		}

	}
}