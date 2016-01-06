package de.unistuttgart.ims.fictionnet.imp.tei_import.tree;

import de.unistuttgart.ims.fictionnet.imp.tei_import.ImportException;

/**
 * @author Rafael Harth
 * @version 11-05-2015
 * 
 *          Node that's meant to appear exactly once.
 */
public class Node_Single extends Node {
	private String data;

	/**
	 * @param parent
	 *        the node's parent. the current node will be added to the parent's children if the parent is not null
	 * @param requiredTag
	 *        the requested tag or tag start for this node, e.g. "CastGroup"
	 * @param isLeaf
	 *        indicates whether or not this node is a leaf and contains data
	 */
	public Node_Single(Node parent, String requiredTag, boolean isLeaf) {
		super(parent, requiredTag, isLeaf);
	}

	/**
	 * Should only be called once.
	 */
	@Override
	public void addData(String data) throws ImportException {
		if (this.data != null) {
			throw new ImportException("Attempt to add data multiple times to a single node\n(" + data + ")");
		}
		this.data = data;
	}

	@Override
	public String dataAsString() {
		return data;
	}
}