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

	@Override
	public IProject getProject() {
		return project;
	}

	public PerProjectInfo getPerProjectInfo() {
		return ModuleModelManager.getInstance().getPerProjectInfo(project, true);
	}

	public String toString() {
		return this.getProject().getName();
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
	public IModule[] getDependencies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IModuleRevision getResolvedRevision() {
		// TODO Auto-generated method stub
		return null;
	}

}
