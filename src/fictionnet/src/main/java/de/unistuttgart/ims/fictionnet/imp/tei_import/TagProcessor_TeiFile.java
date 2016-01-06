package de.unistuttgart.ims.fictionnet.imp.tei_import;

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ims.fictionnet.imp.TeiText;
import de.unistuttgart.ims.fictionnet.imp.tei_import.tree.Node;
import de.unistuttgart.ims.fictionnet.imp.tei_import.tree.TeiTree;

/**
 * @author Rafael Harth
 * @version 10-18-2015
 * 
 *          This class is responsible for the import of a TEI file. The structure of such a file is determined by a
 *          formal tree defined in TeiTree.java, which each relevant tag and tag layer corresponding to a node. This
 *          class will process a file according to the tree. For this purpose, it will go along the nodes of the tree
 *          while the import takes place, starting with the tree's root. Whenever a sub tag is reached, if it is
 *          recognized as a child of the current node, it will become the new current node; if not it will be skipped.
 *          If a current node is marked as containing relevant information, then the contents of the corresponding tag
 *          layer will be copied into the node's body. Whenever the current tag layer closes, the current node will
 *          shift to its parent.
 * 
 *          See TeiTree.java and TeiStructure.png for clarification.
 */
public class TagProcessor_TeiFile extends TagProcessor {
	private List<String> sourceText = new ArrayList<String>();
	private TeiTree teiTree = new TeiTree(sourceText);
	private Node node = teiTree.getRoot(); // see TeiTree.java
	private TeiText teiText;

	/**
	 * @param teiContents
	 *        the entire source code of a .TEI file in a single string
	 */
	public TagProcessor_TeiFile(String teiContents) {
		source = teiContents;
	}

	/**
	 * The import process. Relevant data will be stored in the respective notes (see NodeTree.java), but not yet
	 * interpreted.
	 * 
	 * @throws ImportException
	 *         if something goes wrong
	 */
	public void transcribe() throws ImportException {
		for (; index < source.length(); index++) {
			setCurrentAndNextLetter();
			progressIndexTo('<'); // sometimes there are white-spaces between tags, skip them

			final String tag = getCurrentTag(); // extracts current tag ('<...>')

			if (nextLetter == '/') {
				quitNode();
			} else if (progressNode(tag)) { // found suitable child for current node
				if (node.hasData()) { // it has relevant data -> record
					node.addData(recordTagLayer());
					quitNode();
				} // if it has no relevant data -> do nothing
			} else if (!tag.endsWith("/")) { // didn't find one -> skip entire tag layer, unless it's <.../> tag
				skipTagLayer();
			}
			progressIndexTo('>'); // does nothing if tag layer was skipped or recorded
		}
	}



	public void interpret() throws ImportException {
		teiText = teiTree.getContents().interpret(sourceText);
	}
	
	/**
	 * Overrides current node with its parent.
	 **/
	private void quitNode() {
		node = node.getParent();
	}

	/**
	 * For a given tag, tries to find a child of the current node that fits the tag. If one was found, overrides the
	 * current node with it.
	 * 
	 * @param tag
	 *        any tag
	 * @return true IF a fitting child was found
	 * @throws Exception
	 *         if something goes wrong
	 */
	private boolean progressNode(final String tag) throws ImportException {
		for (final Node child : getNode().getChildren()) {
			if (child.attemptMatch(tag)) {
				child.validateParent(getNode()); // necessary if the node can have multiple parents
				setNode(child);

				return true;
			}
		}
		return false; // No match was found
	}
	// Basic Getters and Setters
	public Contents getContents() {
		return teiTree.getContents();
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public TeiTree getTeiTree() {
		return teiTree;
	}

	public void setTeiTree(TeiTree teiTree) {
		this.teiTree = teiTree;
	}

	public TeiText getTeiText() {
		return teiText;
	}
}