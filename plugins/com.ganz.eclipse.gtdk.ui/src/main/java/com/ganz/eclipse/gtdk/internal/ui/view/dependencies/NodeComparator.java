package com.ganz.eclipse.gtdk.internal.ui.view.dependencies;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

	@Override
	public int compare(Node node1, Node node2) {
		if (node1.getDepth() > node2.getDepth()) {
			return -1;
		}
		if (node1.getDepth() < node2.getDepth()) {
			return 1;
		}
		return node1.getModuleRevisionId().toString().compareTo(node2.getModuleRevisionId().toString());

	}
}
