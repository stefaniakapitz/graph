package de.unistuttgart.ims.fictionnet.datastructure;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;

import de.unistuttgart.ims.fictionnet.datastructure.layers.content.SectionWithType;

/**
 * @author Rafael Harth
 * @version 10-31-2015
 * 
 *          This utility class offers two static methods to convert pieces of source text in different ways.
 */
public final class TextConversion {

	private static final String HIGHLIGHT_CLOSE = "</span>";
	private static final String HIGHLIGHT = "<span style=\"color: #ffffff; background-color: #ff0000\">";
	private static final String PARAGRAPH = "<p>";
	private static final String ITALIC_CLOSE = "</i>";
	private static final String ITALIC = "<i>";
	private static final String PARAGRAPH_CLOSE = "</p>";
	private static final String FAT_CLOSE = "</b>";
	private static final String FAT = "<b>";
	private static final String NEW_LINE = "<br>";

	protected static final String ACT_CAPTION_CLOSE = "</u></b></h2>";
	protected static final String ACT_CAPTION = "<h2 id=\"%s%s\"><b><u>";

	private static final String SCENE_CAPTION_CLOSE = "</u></b></h3>";
	private static final String SCENE_CAPTION = "<h3 id=\"%s%s/%s\"><b><u>";

	/**
	 * Don't instantiate this.
	 */
	private TextConversion() {
	}

	/**
	 * Formats a given text by inserting html tags.
	 * 
	 * @param textSection
	 *            a substring of the source text
	 * @param sections
	 *            a descriptor for the sections of the text. See TypeOfTextLayer.java
	 * @param offset
	 *            the number of chars in the source text before the start of the section
	 * @return the text formatted with html tags.
	 * 
	 *         <pre>
	 * ﻿ ＜p＞＜i＞Halle im herzoglichen Palast＜/i＞＜br＞＜/p＞＜p＞＜i＞ Es treten auf der Herzog von Ephesus, Ägeon, der Kerkermeister und Gefolge＜/i＞＜br＞＜/p＞＜p＞＜b＞ÄGEON＜/b＞Fahr' fort, Solin! Sei Förd'rer meines Falles,＜br＞Dein Urteil ende Schmerz und Gram und alles!＜br＞＜/p＞＜p＞＜b＞HERZOG＜/b＞Kaufmann aus Syrakus, hör' auf zu rechten;＜br＞Ich kann parteiisch das Gesetz nicht kürzen＜br＞Die Fehd' und Zwietracht, die uns jüngst erwuchs＜br＞Durch Eures Herzogs tückische Mißhandlung＜br＞Ehrsamer Kaufherrn, meiner Untertanen,＜br＞(Die, Geld entbehrend, um sich loszukaufen,＜br＞Sein hart Gesetz mit ihrem Blut gebüßt) –＜br＞Bannt alle Gnad' aus unserm droh'nden Blick＜br＞Denn seit dem tödlichen und innern Zwist,＜br＞Des Bosheit Eure Stadt von uns getrennt,＜br＞Verbot ein feierlicher Volksbeschluß,＜br＞So bei den Syrakusern wie bei uns,＜br＞
	 * ﻿</pre﻿>
	 */
	public static String generateHtmlText(String textSection, List<SectionWithType> sections, boolean highlight,
			String uriPrefix) {
		final StringBuilder html = new StringBuilder(textSection.length());

		html.append(PARAGRAPH);

		final int offset = sections.get(0).getStart();

		final Iterator<SectionWithType> iterator = sections.iterator();

		String currentAct = "";
		String currentScene = "";

		while (iterator.hasNext()) {
			final SectionWithType section = iterator.next();
			final String value = textSection.substring(section.getStart() - offset, section.getEnd() - offset);

			switch (section.getSectionType()) {
			case SPEAKER_STATEMENT:
				trimNewLine(html);
				html.append(FAT).append(value).append(FAT_CLOSE + NEW_LINE);
				break;
			case SPOKEN_TEXT:
				if (highlight) {
					html.append(HIGHLIGHT);
				}
				html.append(value);

				if (highlight) {
					html.append(HIGHLIGHT_CLOSE);
				}

				html.append(NEW_LINE);

				break;
			case STAGE_INSTRUCTION:
				if (!html.toString().endsWith(PARAGRAPH_CLOSE) && !html.toString().endsWith(PARAGRAPH)) {
					html.append(PARAGRAPH_CLOSE).append(PARAGRAPH);
				}

				html.append(ITALIC).append(value).append(ITALIC_CLOSE).append(NEW_LINE);

				break;
			case PARAGRAPH:
				if (html.toString().endsWith(PARAGRAPH)) {
					break;
				} else {
					trimNewLine(html);
				}
				html.append(PARAGRAPH_CLOSE);

				if (iterator.hasNext()) {
					html.append(PARAGRAPH);
				}
				break;
			case ACT_HEADER:
				try {

					currentAct = URLEncoder.encode(value.trim(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				html.append(String.format(ACT_CAPTION, uriPrefix, currentAct)).append(value.trim())
						.append(ACT_CAPTION_CLOSE);

				break;
			case SCENE_HEADER:
				try {
					currentScene = URLEncoder.encode(value.trim(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				html.append(String.format(SCENE_CAPTION, uriPrefix, currentAct, currentScene)).append(value.trim())
						.append(SCENE_CAPTION_CLOSE);
				break;
			default:
				break;
			}
		}

		trimNewLine(html);

		return html.toString();
	}

	/**
	 * @param html
	 */
	private static void trimNewLine(final StringBuilder html) {
		if (html.toString().endsWith(NEW_LINE)) {
			removeSuffix(html, NEW_LINE);
			html.append(PARAGRAPH_CLOSE);
		}
	}

	/**
	 * @param html
	 */
	private static void removeSuffix(final StringBuilder html, String suffix) {
		html.replace(html.toString().length() - suffix.length(), html.toString().length(), "");
	}

	// /**
	// * Formats the given text to make it more readable.
	// *
	// * @param text
	// * the entire source text
	// * @param sections
	// * a descriptor for the sections of the text. See TypeOfTextLayer.java
	// * @return the formatted text.
	// *
	// * <pre>
	// * -- Es treten auf der Herzog von Ephesus, Ägeon, der Kerkermeister und Gefolge --
	// *
	// *
	// *
	// *
	// * ÄGEON
	// * Fahr' fort, Solin! Sei Förd'rer meines Falles,
	// * Dein Urteil ende Schmerz und Gram und alles!
	// *
	// *
	// *
	// *
	// * HERZOG
	// * Kaufmann aus Syrakus, hör' auf zu rechten;
	// * Ich kann parteiisch das Gesetz nicht kürzen
	// * Die Fehd' und Zwietracht, die uns jüngst erwuchs
	// * Durch Eures Herzogs tückische Mißhandlung
	// * </pre>
	// */
	// public static String generateReadableText(String text, List<SectionWithType> sections) {
	// StringBuilder readable = new StringBuilder();
	//
	// for (SectionWithType section : sections) {
	// String append = text.substring(section.getStart(), section.getEnd());
	//
	// switch (section.getSectionType()) {
	// case SPEAKER_STATEMENT:
	// readable.append(append).append('\n');
	// break;
	// case SPOKEN_TEXT:
	// readable.append(append).append('\n');
	// break;
	// case STAGE_INSTRUCTION:
	// readable.append(" -- ").append(append).append(" --\n");
	// break;
	// case PARAGRAPH:
	// readable.append("\n\n\n\n");
	// default:
	// break;
	// }
	// }
	// return readable.toString();
	// }
}