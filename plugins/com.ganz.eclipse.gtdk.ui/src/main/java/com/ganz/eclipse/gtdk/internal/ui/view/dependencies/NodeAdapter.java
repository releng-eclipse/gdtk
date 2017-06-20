package com.ganz.eclipse.gtdk.internal.ui.view.dependencies;

import java.util.HashMap;
import java.util.Map;

import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ResolveReport;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.ganz.eclipse.gdtk.internal.core.ModuleProject;
import com.ganz.eclipse.gdtk.internal.core.Solution;
import com.ganz.eclipse.gdtk.internal.core.SolutionException;
import com.ganz.eclipse.gdtk.internal.core.SolutionManager;
import com.ganz.eclipse.gdtk.internal.core.SolutionManager.PerModuleState;
import com.ganz.eclipse.gdtk.internal.ivy.ModuleResolver;

public class NodeAdapter {

	public static Node adapt(IProject project) throws SolutionException {

		if (project == null) {
			return null;
		}
		if (!project.exists() || !project.isOpen()) {
			return null;
		}
		Map<ModuleRevisionId, Node> resolvedNodes = new HashMap<>();
		Solution solution = SolutionManager.getInstance().getSolution();
		ModuleProject module = new ModuleProject(project, solution);

		ModuleResolver resolver = new ModuleResolver();
		resolver.resolve(module, new NullProgressMonitor(), 0);

		PerModuleState state = module.getPerModuleState();

		ResolveReport report = state.getResolveReport();

		Node root = new Node(report.getModuleDescriptor().getModuleRevisionId());
		return root;

	}

}
