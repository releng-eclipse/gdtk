package com.ganz.eclipse.gtdk.internal.ui.view.dependencies;

import org.eclipse.zest.core.viewers.EntityConnectionData;

public class LabelProvider extends org.eclipse.jface.viewers.LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof Node) {
			Node node = (Node) element;
			return node.getName() + "s";

		}

		// if(element instanceof MyConnection) {
		//
		// }
		if (element instanceof EntityConnectionData) {
			return "";
		}

		throw new RuntimeException("Wrong type");
	}
}
