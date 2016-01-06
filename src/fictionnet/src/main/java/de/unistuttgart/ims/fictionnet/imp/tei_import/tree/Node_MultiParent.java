package de.unistuttgart.ims.fictionnet.imp.tei_import.tree;

import java.util.HashSet;
import java.util.Set;

import de.unistuttgart.ims.fictionnet.imp.tei_import.ImportException;

/**
 * @author Rafael Harth
 * @version 10-28-2015
 * 
 *          This is a special node that is used when there is a section in the tree which appears in two different
 *          places. This causes a node to have multiple parents, which is not compatible with the original model. An
 *          example for this is the content of a scene ("div type=text") which can appear either in a scene tag or
 *          directly in an act tag if that act only has one scene.
 */
public class Node_MultiParent extends Node_Poly {
	private final Set<Node> parents = new HashSet<>();
	private Node activeParent;

	/**
	 * Will add itself to the children list of both parents.
	 * 
	 * @param parent1
	 *        the node's first parent
	 * @param parent2
	 *        the node's second parent
	 * @param requiredTagStart
	 *        the requested tag start for this node, e.g. "TEI Xml"
	 * @param isLeaf
	 *        indicates whether or not this node is a leaf and contains data
	 */
	public Node_MultiParent(String requiredTagStart, boolean isLeaf, Node... parents) {
		super(null, requiredTagStart, isLeaf);

		for (Node parent : parents) {
			this.parents.add(parent);
			parent.addChild(this);
		}
	}

	/**
	 * Since the node has two parents, it is necessary to keep track of which is the right one. This method will be
	 * called while processing at the moment the tag layer is entered.
	 * 
	 * @param rightParent
	 *        the now active parent Node
	 * @throws ImportException
	 *         if the right parent is not one of the two available parents
	 */
	public void setActiveParent(Node rightParent) throws ImportException {
		for (Node parent : parents) {
			if (rightParent == parent) {
				activeParent = rightParent;
				return;
			}
		}
		throw new ImportException("Unknown Parent to Multi Parent Node");
	}

	/**
	 * @return Not the basic parent, but the @link active parent
	 */
	@Override
	public Node getParent() {
		return activeParent;
	}
}