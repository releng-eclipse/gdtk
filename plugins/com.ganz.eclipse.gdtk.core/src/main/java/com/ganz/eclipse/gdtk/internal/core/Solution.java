package com.ganz.eclipse.gdtk.internal.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.ganz.eclipse.gdtk.core.IModule;
import com.ganz.eclipse.gdtk.core.ModuleCore;

public class Solution {

	public IModule[] getModules() throws CoreException {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		IModule[] modules = new IModule[projects.length];
		int index = 0;
		for (int i = 0; i < projects.length; i++) {
			IProject project = projects[i];
			if (project.hasNature(ModuleCore.NATURE_ID)) {
				modules[index++] = getModule(project);
			}
		}
		if (index < projects.length) {
			System.arraycopy(modules, 0, modules = new IModule[index], 0, index);
		}
		return modules;
	}

	public IModule getModule(IResource resource) throws CoreException {
		switch (resource.getType()) {
		case IResource.FOLDER:
			return null;
		// TODO implementation
		// throw new CoreException();
		case IResource.FILE:
			// TODO implementation
			return null;
		// throw new CoreException();
		case IResource.PROJECT:
			return new Module((IProject) resource, this);
		default:
			// TODO add detail message
			// throw new IllegalArgumentException();
			return null;
		}

	}
}
