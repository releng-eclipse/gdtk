package com.ganz.eclipse.gtdk.internal.ui.view.dependencies;

import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

public class ContentProvider implements IGraphEntityContentProvider {

	////////////////////////////////////////////////////////////
	// IGraphEntityContentProvider

	@Override
	public Object[] getElements(Object element) {
		if (element == null) {
			return new Object[0];
		}
		Node node = (Node) element;

		return node.getElements();
		// List<>
		// List<Node> elements = Arrays.asList(filter(node.getElements()));
		// Collections.sort(elements, new NodeComparator());

	}

	@Override
	public Object[] getConnectedTo(Object element) {
		return new Object[0];
		// return filter(((Node) element).getDependencies());
	}

	private Node[] filter(Node[] elements) {
		Node[] result = elements;
		// add filtering
		return result;

	}

}
