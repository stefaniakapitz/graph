package de.unistuttgart.ims.fictionnet.imp.tei_import.tree;

import java.util.HashSet;
import java.util.Set;

import de.unistuttgart.ims.fictionnet.imp.tei_import.ImportException;

/**
 * @author Rafael Harth
 * @version 10-23-2015
 * 
 *          This class is the representation of a Node used to model a tree representing the structure of a .TEI file
 *          (see TeiTree.java for clarification). Each node corresponds to a tag and tag layer.
 * 
 *          This class is purely used for module-internal implementation of the import procedure and thus its contents
 *          don't need to to be hidden.
 */
public abstract class Node {
	// indicates whether or not the corresponding tag layer includes relevant data (always true for leaf nodes)
	private final String requiredTag; // directs which tag will be recognized as this Node.
	private Node parent;
	private Set<Node> children = new HashSet<>();
	private boolean requireFullMatch;

	private final boolean isLeaf;

	/**
	 * @param parent
	 *        the node's parent. the current node will be added to the parent's children if the parent is not null
	 * @param requiredTag
	 *        the requested tag start or full tag for this node (e.g. "div subtype")
	 * @param isLeaf
	 *        indicates whether or not this node is a leaf and contains data
	 */
	public Node(Node parent, String requiredTag, boolean isLeaf) {
		this.setParent(parent);
		this.requiredTag = requiredTag;
		this.isLeaf = isLeaf;

		if (parent != null) {
			parent.children.add(this);
		}
	}

	/**
	 * The structure of our tei tree requires some nodes to have multiple possibilities for parents. To account for
	 * that, the right parent has to be selected upon entering. Only does something if this class is an instance of the
	 * MultiParent Node.
	 * 
	 * @param parent
	 *        the now active parent
	 * @throws ImportException
	 *         in case the parent is not recognized
	 */
	public void validateParent(Node parent) throws ImportException {
		if (this instanceof Node_MultiParent) {
			((Node_MultiParent) this).setActiveParent(parent);
		}
	}

	/**
	 * Attempts a match with this node and a tag in the file.
	 * 
	 * @param tag
	 *        the found tag
	 * @return true IF requireFullMatch is true and the tag equals the node's required tag OR requireFullMatch is false
	 *         and the tag starts with the node's required tag
	 */
	public boolean attemptMatch(String tag) {
		return requireFullMatch ? tag.equals(requiredTag) : tag.startsWith(requiredTag);
	}

	/**
	 * Adds a child to the current set of children.
	 * 
	 * @param child
	 *        the node to add
	 */
	public void addChild(Node child) {
		children.add(child);
	}

	/**
	 * @return the data as a single string. If it is a list, it will be concated.
	 * @throws ImportException
	 */
	public abstract String dataAsString();

	/**
	 * Abstract because the structure of the data (single String, list of strings, ...) may vary.
	 * 
	 * @param data
	 *        any new data to add
	 * @throws ImportException
	 *         if is is attempted to call this method multiple times on a node that should only exist once
	 */
	public abstract void addData(String data) throws ImportException;

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder(requiredTag);
		String data = dataAsString();

		if (data != null && !data.isEmpty()) {
			string.append(" (").append(data).append(")");
		}
		return string.toString();
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public boolean hasData() {
		return isLeaf;
	}

	public void requestFullMatch() {
		requireFullMatch = true;
	}

	public Set<Node> getChildren() {
		return children;
	}

	public String getRequiredTag() {
		return requiredTag;
	}

	public boolean requireFullMatch() {
		return requireFullMatch;
	}
}