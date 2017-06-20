package com.ganz.eclipse.gdtk.internal.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

import com.ganz.eclipse.gdtk.core.IModuleProject;

public class ModuleModelManager {
	static private ModuleModelManager instance = new ModuleModelManager();

	public static ModuleModelManager getInstance() {
		return instance;
	}

	public IModuleProject getModuleProject(IResource resource) {
		switch (resource.getType()) {
		case IResource.FOLDER:
			return new ModuleProject(((IFolder) resource).getProject(), this);
		case IResource.FILE:
			return new ModuleProject(((IFile) resource).getProject(), this);
		case IResource.PROJECT:
			return new ModuleProject((IProject) resource, this);
		default:
			throw new IllegalArgumentException("Invalid resource type");
		}
	}
}
