package com.ganz.eclipse.gdtk.internal.old.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.ganz.eclipse.gdtk.core.IModule;
import com.ganz.eclipse.gdtk.core.ModuleCore;
import com.ganz.eclipse.gdtk.internal.core.SolutionManager.PerModuleState;

public class Module implements IModule {

	protected IProject project;

	public Module(IProject project, Solution parent) {
		this.project = project;
	}

	@Override
	public IProject getProject() {
		return this.project;
	}

	public PerModuleState getPerModuleState() throws SolutionException {
		return SolutionManager.getInstance().getPerModuleState(project);
	}

	public IStatus resolve() {
		// PerModuleState state = Solution.getInstance().getPerModuleState();
		return new Status(IStatus.OK, ModuleCore.PLUGIN_ID, "");
	}

}
