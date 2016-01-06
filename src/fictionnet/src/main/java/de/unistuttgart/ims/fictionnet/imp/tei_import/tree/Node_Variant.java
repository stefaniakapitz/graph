package de.unistuttgart.ims.fictionnet.imp.tei_import.tree;

import de.unistuttgart.ims.fictionnet.imp.tei_import.Contents;

/**
 * @author Rafael Harth
 * @version 11-05-2015
 * 
 *          Special node that is responsible to handle the two different structural versions of the tei tree. castName,
 *          castItems, and coverInformation have to be re-assigned if the current file does indeed follow this variant.
 *          If so, it will be recognized after a successful match, and the switchToVariantTo() method will be called in
 *          Contents.java
 */
public class Node_Variant extends Node_Single {
	// Since this is a unique node, this information can be hard coded.
	private static final String REQUIRED_TAG = "div type=\"Dramatis_Personae\"";
	private static final boolean NO_LEAF = false;

	private Contents nc;
	private Node_Poly castItems;
	private Node_Poly coverInformation;

	/**
	 * @param parent
	 *        the node's parent. if it is not null, the current node will be added to its list of children
	 * @param nodeContent
	 *        a link to the contents instance to pass on the variant switch
	 */
	public Node_Variant(Node parent, Contents nodeContent) {
		super(parent, REQUIRED_TAG, NO_LEAF);
		this.nc = nodeContent;
	}

	@Override
	public boolean attemptMatch(String tag) {
		final boolean doesMatch = tag.equals(REQUIRED_TAG);

		if (doesMatch) {
			nc.switchToVariantTwo(castItems, coverInformation);
		}

		return doesMatch;
	}

	public void setCastItems(Node_Poly castItems) {
		this.castItems = castItems;
	}

	public void setCoverInformation(Node_Poly coverInformation) {
		this.coverInformation = coverInformation;
	}
}