package com.ganz.eclipse.gdtk.internal.core;

import org.eclipse.core.resources.IProject;

import com.ganz.eclipse.gdtk.core.IModule;
import com.ganz.eclipse.gdtk.core.IModuleRevision;
import com.ganz.eclipse.gdtk.internal.core.module.Descriptor;
import com.ganz.eclipse.gdtk.internal.core.module.Revision;

public class Module implements IModule {

	private Revision revision;
	private Descriptor descriptor;

	public Module(Revision revision, Descriptor descriptor) {
		this.revision = revision;
		this.descriptor = descriptor;
	}

	@Override
	public IModule[] getAllRealCallers() {
		return new IModule[0];
	}

	@Override
	public IModule[] getAllCallers() {
		return new IModule[0];
	}

	@Override
	public IModule[] getEvictingModules() {
		return new IModule[0];
	}

	@Override
	public IModule[] getEvictedModules() {
		return new IModule[0];
	}

	@Override
	public IModule[] getDependencies() {
		return new IModule[0];
	}

	@Override
	public IProject getProject() {
		return null;
	}

	@Override
	public IModuleRevision getResolvedRevision() {
		// TODO Auto-generated method stub
		return null;
	}

	public IModuleRevision getRevision() {
		return revision;
	}

}
