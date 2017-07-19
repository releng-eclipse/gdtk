package com.ganz.eclipse.gtdk.internal.ui.view.dependencies;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ResolveReport;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.ganz.eclipse.gdtk.core.IModule;
import com.ganz.eclipse.gdtk.core.IModule.IRevisionId;
import com.ganz.eclipse.gdtk.core.ModuleCore;

import java.util.List;

public class NodeAdapter {

	
	public static Node adapt(IProject project) {
		Map<IRevisionId, Node> nodes = new HashMap<IRevisionId, Node>();
		Map<IRevisionId, Collection<Node>>mrids = new HashMap<IRevisionId, Collection<Node>>();
		IModule module = ModuleCore.create(project);
		Node root = new Node(module.getResolvedRevisionId());
		nodes.put(root.getRevisionId(), root);
		
		// pass 1 
		IModule[] dependencies = module.getDependencies();
		for(IModule dependency : dependencies) {
			if(dependency.getEvictingModules().length != 0) {
				continue;
			}
			Node node = new Node(dependency.getResolvedRevisionId());
			nodes.put(root.getRevisionId(),node);
		}
		
		// pass 2 - relationships
		for(IModule dependency : dependencies) {
			if(dependency.getEvictingModules().length !=0) {
				continue;
			}
			Node node = nodes.get(dependency.getResolvedRevisionId());
			IModule[] callers = dependency.getAllRealCallers();
			for(IModule caller:callers) {
				Node callerNode = nodes.get(caller.getResolvedRevisionId());
				if(callerNode!=null) {
					node.addCaller(callerNode);
				}
			}
		}
		
		// pass 3 - evictions
		IModule[] evictions = module.getEvictedModules();
		for(IModule eviction:evictions) {
			Node evictedNode = new Node(eviction.getResolvedRevisionId());
			evictedNode.setEvicted(true);
			IModule[] callers = eviction.getAllCallers();
			for(IModule caller:callers) {
				Node callerNode = nodes.get(caller.getResolvedRevisionId());
				if(callerNode!=null) {
					evictedNode.addCaller(callerNode);
				}
			}
		}

		//@see org.apache.ivyde.eclipse.resolvevisualizer.model.IvyNodeElementAdapter
		Node[] children = root.getChildren();
		for(Node child:children) {
			if(child.isEvicted()) {
				continue;
			}
			IRevisionId mrid = child.getRevisionId();
			
			if(mrids.containsKey(mrid)) {
				Collection<Node>conflicts = mrids.get(mrid);
				conflicts.add(child);
				for(Node conflictNode:conflicts) {
					conflictNode.setConflicts(conflicts);
				}				
			}else{
				List<Node>conflicts = (List) Arrays.asList(new Node[] {child});
				mrids.put(mrid, new HashSet<Node>(conflicts));
			}
		}
		root.setDepth(0);
		return root;
	}
	


}
