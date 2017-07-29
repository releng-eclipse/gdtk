package com.ganz.eclipse.gtdk.internal.ui.view.dependencies;

import com.ganz.eclipse.gdtk.core.IModuleRevision;

public class Node {
	private IModuleRevision revision;

	public Node(IModuleRevision rev, boolean evicted) {
		revision = rev;
	}

	public Node(IModuleRevision rev) {
		this(rev, false);
	}

	public Node[] getChildren() {
		return null;
	}

	/**
	 * Returns the name of the module represented by this Node.
	 * 
	 * @return
	 */
	public String getName() {
		// TODO
		return revision.getName();
	}

	/**
	 * Returns the <code>IModuleRevision</code> of the module represented by
	 * this node.
	 * 
	 * @return
	 */
	public IModuleRevision getRevision() {
		// TODO
		return null;
	}

	public Node[] getElements() {
		// TODO
		return new Node[] { this };
	}

}
