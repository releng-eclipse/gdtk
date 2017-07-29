package com.ganz.eclipse.gdtk.internal.core;

import org.eclipse.core.resources.IProject;

import com.ganz.eclipse.gdtk.core.IModule;
import com.ganz.eclipse.gdtk.core.IModuleRevision;
import com.ganz.eclipse.gdtk.internal.core.ModuleModelManager.PerProjectInfo;

public class ModuleProject implements IModule {
	IProject project;

	public ModuleProject(IProject project, ModuleModelManager parent) {
		this.project = project;
	}

	public IModule[] getDependencies() {
		return new IModule[0];
	}

	@Override
	public IProject getProject() {
		return project;
	}

	@Override
	public IModule[] getAllRealCallers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IModule[] getAllCallers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IModule[] getEvictingModules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IModule[] getEvictedModules() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IModuleRevision getResolvedRevision() {
		// TODO Auto-generated method stub
		return new ModuleRevision(this.getProject().getName());
	}

	public PerProjectInfo getPerProjectInfo() {
		return ModuleModelManager.getInstance().getPerProjectInfo(project, true);
	}

	public String toString() {
		return this.getProject().getName();
	}

}
