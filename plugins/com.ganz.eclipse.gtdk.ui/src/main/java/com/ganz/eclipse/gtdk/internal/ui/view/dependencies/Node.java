package com.ganz.eclipse.gtdk.internal.ui.view.dependencies;

import java.util.Collection;
import java.util.HashSet;

import org.apache.ivy.core.module.id.ModuleRevisionId;

import com.ganz.eclipse.gdtk.core.IModule;

public class Node {
	IModule.IRevisionId mrid;
	private String name;
	private int depth;

	private Collection<Node> callers;
	private Collection<Node> dependencies;
	private Node[] elements;
	private Collection<Node>conflicts;

	public Node(IModule.IRevisionId mrid, boolean evicted) {
		this.mrid = mrid;
		callers = new HashSet<>();
		dependencies = new HashSet<>();
		conflicts = new HashSet<Node>();
	}
	
	public Node(IModule.IRevisionId mrid) {
		this(mrid, false);
	}
	
	public Node[] getChildren() {
		return null;
	}

	public String getName() {
		// TODO
		return name;
	}

	public IModule.IRevisionId getRevisionId() {
		// TODO
		return null;
	}

	
	public Node[] getConflicts() {
		return (Node[]) conflicts.toArray(new Node[conflicts.size()]);
	}
	
	public void setConflicts(Collection<Node>conflicts) {
		this.conflicts = conflicts;
	}
	
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
		for (Node dependency : dependencies) {
			dependency.setDepth(depth + 1);
		}
	}
	
	public void setEvicted(boolean evicted) {
		
	}
	
	public boolean isEvicted() {
		return false;
	}

	public Node[] getDependencies() {
		return dependencies.toArray(new Node[dependencies.size()]);
	}

	public void addCaller(Node caller) {
		callers.add(caller);
		caller.dependencies.add(this);
	}

	public Node[] getCallers() {
		return callers.toArray(new Node[callers.size()]);
	}

	public Node[] getElements() {
		if (elements == null) {
			elements = getElements(this).toArray(new Node[] {});
		}
		return elements;
	}

	/**
	 * @deprecated ?
	 * @param node
	 * @return
	 */
	private Collection<Node> getElements(Node node) {
		Collection<Node> nodes = new HashSet<>();
		nodes.add(node);
		return nodes;
	}

}
