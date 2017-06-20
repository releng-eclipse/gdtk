package com.ganz.eclipse.gtdk.internal.ui.view.dependencies;

import java.util.Collection;
import java.util.HashSet;

import org.apache.ivy.core.module.id.ModuleRevisionId;

public class Node {
	ModuleRevisionId mrid;
	private String name;
	private int depth;

	private Collection<Node> callers;
	private Collection<Node> dependencies;
	private Node[] elements;

	public Node(ModuleRevisionId mrid) {
		this.mrid = mrid;
		callers = new HashSet<>();
		dependencies = new HashSet<>();

	}

	public String getName() {
		// TODO
		return name;
	}

	public Object getModuleRevisionId() {
		// TODO
		return "heh";
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

	private Collection<Node> getElements(Node node) {
		Collection<Node> nodes = new HashSet<>();
		nodes.add(node);
		return nodes;
	}

}
