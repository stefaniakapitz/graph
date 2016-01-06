package de.unistuttgart.ims.fictionnet.imp.tei_import.tree;

import java.util.LinkedList;
import java.util.List;

import de.unistuttgart.ims.fictionnet.imp.util.Methods;

/**
 * @author Rafael Harth
 * @version 11-05-2015
 * 
 *          Node that's meant to appear multiple times.
 */
public class Node_Poly extends Node {
	private List<String> data = new LinkedList<String>();

	/**
	 * @param parent
	 *        the node's parent. the current node will be added to the parent's children if the parent is not null
	 * @param requiredTag
	 *        the requested tag or requested tag start for this node, e.g. "div type = "h4""
	 * @param isLeaf
	 *        indicates whether or not this node is a leaf and contains data
	 */
	public Node_Poly(Node parent, String requiredTag, boolean isLeaf) {
		super(parent, requiredTag, isLeaf);
	}

	@Override
	public void addData(String data) {
		this.data.add(data);
	}

	@Override
	public String dataAsString() {
		return Methods.concat(data);
	}
}