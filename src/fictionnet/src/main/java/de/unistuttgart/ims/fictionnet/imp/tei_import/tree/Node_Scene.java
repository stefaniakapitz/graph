package de.unistuttgart.ims.fictionnet.imp.tei_import.tree;

/**
 * @author Rafael Harth
 * @version 11-05-2015
 * 
 *          A poly node that corresponds to a scene. Does not have additional functionality.
 */
public class Node_Scene extends Node_Poly {
	/**
	 * @param parent
	 *        the node's parent. the current node will be added to the parent's children if the parent is not null
	 * @param requiredTagStart
	 *        the requested tag start for this node, e.g. "div subtype"
	 * @param isLeaf
	 *        indicates whether or not this node is a leaf and contains data.
	 */
	public Node_Scene(Node parent, String requiredTagStart, boolean isLeaf) {
		super(parent, requiredTagStart, isLeaf);
	}
}