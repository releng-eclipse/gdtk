package com.ganz.eclipse.gdtk.internal.core;

import org.eclipse.core.resources.IProject;

import com.ganz.eclipse.gdtk.core.IModuleProject;

public class ModuleProject implements IModuleProject {
	IProject project;

	public ModuleProject(IProject project, ModuleModelManager parent) {
		this.project = project;
	}

}
