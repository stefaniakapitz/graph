package de.unistuttgart.ims.fictionnet.imp.tei_import.tree;

/**
 * @author Rafael Harth
 * @version 11-05-2015
 * 
 *          This is a special type of single node, for when two nodes appear on the same level of the tree but with
 *          different content. The node will remember whether it is the first or second to be entered, and will react
 *          accordingly when a match is attempted.
 */
public class Node_Pair extends Node_Single {
	private final boolean isFirst;
	private boolean hasMatched;
	private Node_Pair partner;

	/**
	 * The constructor without a partner argument should be called for the first instance of a pair.
	 * 
	 * @param parent
	 *        the node's parent. the current node will be added to the parent's children if the parent is not null
	 * @param requiredTag
	 *        the requested tag or tag start for this node, e.g. "div type="h4""
	 * @param isLeaf
	 *        indicates whether or not this node is a leaf and contains data
	 */
	public Node_Pair(Node parent, String requiredTag, boolean isLeaf) {
		super(parent, requiredTag, isLeaf);
		isFirst = true;
	}

	/**
	 * The constructor with a partner argument should be called for the second instance of a pair.
	 * 
	 * @param parent
	 *        the node's parent. the current node will be added to the parent's children if the parent is not null
	 * @param requiredTag
	 *        the requested tag or tag start for this node, e.g. "div type="h4""
	 * @param isLeaf
	 *        indicates whether or not this node is a leaf and contains data
	 * @param partner
	 *        a link to the other part of the pair
	 */
	public Node_Pair(Node parent, String requiredTag, boolean isLeaf, Node partner) {
		super(parent, requiredTag, isLeaf);
		isFirst = false;
		this.partner = (Node_Pair) partner;
	}

	@Override
	public String dataAsString() {
		return null;
	}

	/**
	 * Attempts a match. Only accepts if either it is the first to be matched and has not matched already, or if it is
	 * not the first and the other part of the pairs has matched already.
	 */
	@Override
	public boolean attemptMatch(String tag) {
		if (isFirst) {
			if (hasMatched) {
				return false;
			}
			final boolean result = super.attemptMatch(tag);
			hasMatched = result;
			return result;
		} else {
			if (partner.hasMatched) {
				final boolean result = super.attemptMatch(tag);
				hasMatched = result;
				return result;
			} else {
				return false;
			}
		}
	}
}