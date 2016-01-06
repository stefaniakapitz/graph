package de.unistuttgart.ims.fictionnet.imp.tei_import.tree;

import java.util.HashSet;
import java.util.Set;

public class NodeCollection {
	Set<Node> nodes = new HashSet<Node>();
	
	
	public NodeCollection(final Node... nodes) {
		for (Node node : nodes) {
			this.nodes.add(node);
		}
	}
	
	public String getData() {
		for (Node node : nodes) {
			if (!node.dataAsString().isEmpty()) {
				return node.dataAsString();
			}
		}
		return null;
	}
}
