package com.ganz.eclipse.gtdk.internal.ui.view.dependencies;

import java.util.Arrays;
import java.util.List;

import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

public class ContentProvider implements IGraphEntityContentProvider {

	////////////////////////////////////////////////////////////
	// IGraphEntityContentProvider

	@Override
	public Object[] getElements(Object element) {

		// if (element instanceof Object[]) {
		// return (Object[]) element;
		// }
		// if (element instanceof Collection) {
		// return ((Collection) element).toArray();
		// }
		// return new Object[0];

		if (element == null) {
			return new Object[0];
		}
		Node node = (Node) element;

		// List<>
		List<Node> elements = Arrays.asList(filter(node.getElements()));
		// Collections.sort(elements, new NodeComparator());
		return elements.toArray();

	}

	@Override
	public Object[] getConnectedTo(Object element) {
		return filter(((Node) element).getDependencies());
	}

	private Node[] filter(Node[] elements) {
		Node[] result = elements;
		// add filtering
		return result;

	}

}
