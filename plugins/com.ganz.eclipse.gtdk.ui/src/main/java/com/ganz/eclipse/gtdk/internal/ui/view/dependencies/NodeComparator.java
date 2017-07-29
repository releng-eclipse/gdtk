package com.ganz.eclipse.gtdk.internal.ui.view.dependencies;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

	@Override
	public int compare(Node node1, Node node2) {
		return 0;
		// if (node1.getDepth() > node2.getDepth()) {
		// return -1;
		// }
		// if (node1.getDepth() < node2.getDepth()) {
		// return 1;
		// }
		// return
		// node1.getRevisionId().toString().compareTo(node2.getRevisionId().toString());

	}
}
