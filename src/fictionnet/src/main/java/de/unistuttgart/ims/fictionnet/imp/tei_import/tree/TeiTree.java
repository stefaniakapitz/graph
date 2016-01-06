package de.unistuttgart.ims.fictionnet.imp.tei_import.tree;

import java.util.List;

import de.unistuttgart.ims.fictionnet.imp.tei_import.Contents;

/**
 * @author Rafael Harth
 * @version 10-23-2015
 * 
 *          This class determines the tag-layer structure of a .TEI file. A graphic of this structure can be found at
 *          TeiStructure.png. Each node in this graphic is created here. An empty line after a declaration indicates
 *          that the Node has two or more branches with length > 1. The nodes with relevant content aren't stored in
 *          variables, have its boolean parameter as 'true', and have a comment assigned which shortly explains what is
 *          being stored.
 * 
 *          The name of each node is a representation of its name in the graphics. If the name is not unique, the
 *          parent's name will be included, following the format parentName$currentName.
 * 
 *          The nodes also store which tag they correspond to (second parameter) (:= 'required tag'). Due to
 *          non-distinct and non-unique tags, it is necessary to differentiate between requesting the actual tag to be
 *          identical to the required tag (example: 'teiHeader'), or merely to start with it (example: teiXml; this
 *          corresponds to a non-distinct tag such as 'TEI xml:id="tg3.vn90.0" n= [...]) (fourth parameter).
 * 
 *          The third parameter determines whether or not the node is a leaf.
 * 
 *          The variable name {otherNodeName}$c indicates that the current node is the only child of the other node and
 *          merely serves as a bridge to the remaining subtree, thereby fulfilling no apparent purpose as they could be
 *          merged without changing the tree's structure
 */
public class TeiTree {
	private final Node exit = new Node_Single(null, "--- finished ---", NO_LEAF);
	private final Node start = new Node_Single(exit, "--- start ---", NO_LEAF);
	private final Contents contents;

	// For better readability
	private static final boolean IS_LEAF = true;
	private static final boolean NO_LEAF = false;
	private static final boolean IS_ACT = true;
	private static final boolean IS_SCENE = false;

	/**
	 * Builds the tei tree as shown in TeiStructure.png.
	 * 
	 * @param processor
	 *        a link to the processing routine to give the textPart nodes
	 */
	public TeiTree(List<String> sourceText) {
		contents = new Contents();

		exit.setParent(exit);

		final Node xmlVersion = new Node_Single(start, "?xml version", NO_LEAF);
		final Node teiCorpus = new Node_Single(xmlVersion, "teiCorpus", NO_LEAF);
		final Node tei_xml = new Node_MultiParent("TEI xml", NO_LEAF, xmlVersion, teiCorpus);
		final Node teiHeader = new Node_Single(tei_xml, "teiHeader", NO_LEAF);

		final Node fileDesc = new Node_Single(teiHeader, "fileDesc", NO_LEAF);
		contents.setTitleAndReleaseDate(new Node_Single(fileDesc, "titleStmt", IS_LEAF));
		contents.setNotes(new Node_Single(fileDesc, "notesStmt", IS_LEAF));

		final Node profileDesc = new Node_Single(teiHeader, "profileDesc", NO_LEAF);
		contents.setCreationDate(new Node_Single(profileDesc, "creation", IS_LEAF));

		final Node text = new Node_Single(tei_xml, "text", NO_LEAF);

		final Node front = new Node_Single(text, "front", NO_LEAF);
		final Node front$c = new Node_Single(front, "div type=\"front\"", NO_LEAF);

		final Node coverInformation = new Node_Pair(front$c, "div type=\"h4\"", NO_LEAF);
		contents.setCoverInformation(new Node_Poly(coverInformation, "head type", IS_LEAF));
		final Node castList = new Node_Pair(front$c, "div type=\"h4\"", NO_LEAF, coverInformation);

		// see comment at the bottom
		Node_Poly castListOrConverInf = new Node_Poly(front$c, "head type", IS_LEAF);
		Node_Poly castListItemsOutside = new Node_Poly(castList, "p rend", IS_LEAF);
		final Node body = new Node_Single(text, "body", NO_LEAF);
		final Node acts = new Node_Act(body, "div subtype", NO_LEAF);

		new Node_TextPart_Header(acts, sourceText, IS_ACT);
		

		final Node scenes = new Node_Scene(acts, "div subtype", NO_LEAF);
		
		new Node_TextPart_Header(scenes, sourceText, IS_SCENE);

		/*
		 * The following node & sub-nodes appear in three places in the tree, representing three
		 * alternative structures. This corresponds to speaker roles either being directly
		 * subordinated to their respective act (current), or distributed by scene
		 * (more common), or even directly in the <body> tag layer (very rare). For this
		 * purpose, we use a Node with multiple (three) parents.
		 */
		final Node sceneBody = new Node_MultiParent("div type=\"text\"", NO_LEAF, scenes, acts, body);
		final Node sceneBody$c = new Node_Single(sceneBody, "div type=\"h4\"", NO_LEAF);
		
		/*
		 * Some .tei files think it's funny to put everything in one huge layer. Thankfully, they provide a
		 * way to differentiate this: the div type is now h3 instead of h4.  
		 */
		final Node sceneBody$cOneLayer = new Node_Single(sceneBody, "div type=\"h3\"", NO_LEAF);
		
		final Node cLIOneLayer = new Node_Poly(sceneBody$cOneLayer, "head type", IS_LEAF);

		/*
		 * Create and set a cast list collection object with all three variants 
		 */
		NodeCollection castListItems = new NodeCollection(castListOrConverInf, castListItemsOutside, cLIOneLayer);

		contents.setCastList(castListItems);

		new Node_TextPart_SP(sourceText, sceneBody$c, sceneBody$cOneLayer);

		/*
		 * Sometimes, the cast counter-intuitively appears as the first act instead of in the <front> tag
		 * layer. Rather than expanding the complexity of the current model to account for this, I decided to
		 * deal with it as follows:
		 * 
		 * Per default, we assume the other variant, and the variables in NodeContent who cover for the
		 * cast list (which are nameOfCastList and castListItems) are set to the respective nodes in the <front>
		 * tag layer. If the cast list is in fact treated as an act, it will be recognized as soon as the
		 * below node is entered, and then the NodeContent items will be reassigned (the logic for this
		 * happens in VariantNode.java)
		 * 
		 * To make things even more complicated, the tag for cover information in this variant is indistinguishable
		 * from the cast list tags in variant one of they don't have their own parent tag (which they usually do,
		 * but not always). Since they are the same, they both share a common variable, but it will never be used
		 * for both purposes.
		 */
		final Node_Variant variantNode = new Node_Variant(sceneBody, contents);

		final Node v2$castList = new Node_Single(variantNode, "castList", NO_LEAF);
		final Node v2$castGroup = new Node_Single(v2$castList, "castGroup", NO_LEAF);
		variantNode.setCastItems(new Node_Poly(v2$castGroup, "castItem", IS_LEAF));
		variantNode.setCoverInformation(castListOrConverInf);
	}

	/**
	 * @return the start node
	 */
	public Node getRoot() {
		return start;
	}

	// Basic Getter
	public Contents getContents() {
		return contents;
	}
}