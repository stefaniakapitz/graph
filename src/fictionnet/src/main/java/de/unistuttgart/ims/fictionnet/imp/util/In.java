package de.unistuttgart.ims.fictionnet.imp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Rafael Harth
 * @version 10-31-2015
 * 
 *          A collection (?) of methods to read from files.
 */
public final class In {
	/**
	 * No reason to instantiate this class.
	 */
	private In() {
	}

	/**
	 * @param file
	 *        a file
	 * @return a list of strings with all lines in that file OR an empty list if the file was empty OR null if the file
	 *         was not found
	 */
	public static List<String> readFromFile(File file) {
		final List<String> lines = new LinkedList<String>();

		try {
			final String encoding = extractEncoding(file);

//			Logger.getLogger(In.class.getName()).log(Level.INFO, "Encoding: " + encoding);

			final BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));

			String line;
			do {
				line = input.readLine();

				if (line != null) {
					lines.add(line);
				}
			} while (line != null);

			input.close();
		} catch (IOException e) {
			return null;
		}

		return lines;
	}

	private static String extractEncoding(File file) {
		String line;
				
		try {
			final BufferedReader encodingReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			line = encodingReader.readLine();
			encodingReader.close();
		} catch (Exception e) {
			return "UTC-8";
		}
		
		
		String encoding = interpretEncoding(line, "\"");
		
		if (encoding == null) {
			encoding = interpretEncoding(line, "'");
		}
		
		return encoding == null ? "UTC-8" : encoding;
	}
	
	private static String interpretEncoding(String encodingLine, String frame) {
		try {
			return Methods.substring(encodingLine, ("encoding=" + frame), frame);
		} catch (Exception e) {
			return null;
		}
	}
}