package de.unistuttgart.ims.fictionnet.imp.tei_import.tree;

import java.util.List;
import de.unistuttgart.ims.fictionnet.imp.util.Methods;

public class Node_TextPart_Header extends Node_Poly {
	private static final boolean IS_LEAF = true;
	private static final String REQUIRED_TAG = "div";
	private final String tag;
	
	private List<String> sourceText;
	
	public Node_TextPart_Header(Node parent, List<String> sourceText, boolean isAct) {
		super(parent, REQUIRED_TAG, IS_LEAF);
		this.sourceText = sourceText;
		tag = isAct ? "<aa>" : "<ss>";
		requestFullMatch();
	}
	
	@Override
	public void addData(String data) {
		sourceText.add(tag + Methods.substring(data, "<title>", "</title>") + "</close>");
	}

}
