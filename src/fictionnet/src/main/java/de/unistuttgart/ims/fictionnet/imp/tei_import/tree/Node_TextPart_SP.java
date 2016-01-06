package de.unistuttgart.ims.fictionnet.imp.tei_import.tree;

import java.util.List;

/**
 * @author Rafael Harth
 * @version 11-11-2015
 *
 *          The Node that contains the source text transcriptions. This node is really several nodes (such as stage
 *          rends and spoken text parts), but treating it as one greatly simplifies the implementation. What specific
 *          Node each instance is can later be determined based on its content.
 */
public class Node_TextPart_SP extends Node_MultiParent {
	// Since this class covers several nodes, it has several possible (static) matches.
	private static final String[] POSSIBLE_MATCHES = { "stage rend", "sp", "p rend", "head type=\"h4\"" };
	private static final boolean IS_LEAF = true;
	
	private List<String> sourceText;
	
	public Node_TextPart_SP(List<String> sourceText, Node... parents) {
		super(null, IS_LEAF, parents);
		this.sourceText = sourceText;
	}
		
	@Override
	public void addData(String data) {
		sourceText.add(data);
	}
	
	@Override
	public boolean attemptMatch(String tag) {
		for (final String match : POSSIBLE_MATCHES) {
			if (tag.startsWith(match)) {
				return true;
			}
		}
		return false;
	}
}
