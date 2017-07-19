package com.ganz.eclipse.gdtk.internal.core;

import org.eclipse.core.resources.IProject;

import com.ganz.eclipse.gdtk.core.IModule;
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
		// TODO Auto-generated method stub
		return null;
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
	public IRevisionId getResolvedRevisionId() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PerProjectInfo getPerProjectInfo() {
		return ModuleModelManager.getInstance().getPerProjectInfo(project, true);
	}

}
