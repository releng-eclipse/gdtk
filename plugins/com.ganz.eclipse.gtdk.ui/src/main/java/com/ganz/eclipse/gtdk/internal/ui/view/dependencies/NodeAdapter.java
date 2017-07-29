package com.ganz.eclipse.gtdk.internal.ui.view.dependencies;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;

import com.ganz.eclipse.gdtk.core.IModule;
import com.ganz.eclipse.gdtk.core.IModuleRevision;
import com.ganz.eclipse.gdtk.core.ModuleCore;

public class NodeAdapter {

	public static Node adapt(IProject project) {
		Map<IModuleRevision, Node> nodes = new HashMap<IModuleRevision, Node>();
		IModule moduleProject = ModuleCore.create(project);
		Node root = new Node(moduleProject.getResolvedRevision());
		nodes.put(root.getRevision(), root);
		return root;
	}

}
