package com.ganz.eclipse.gdtk.internal.core;

import java.util.HashMap;
import java.util.Map;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.report.ResolveReport;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;

import com.ganz.eclipse.gdtk.core.IModule;
import com.ganz.eclipse.gdtk.core.IModuleDescriptor;
import com.ganz.eclipse.gdtk.core.ModuleException;

public class ModuleModelManager {
	static private ModuleModelManager instance = new ModuleModelManager();

	protected Map<IProject, PerProjectInfo> perProjectInfos;
	private DeltaProcessingState deltaState;

	private ModuleModelManager() {
		perProjectInfos = new HashMap<IProject, PerProjectInfo>(5);
		deltaState = new DeltaProcessingState();
	}

	public static ModuleModelManager getInstance() {
		return instance;
	}

	/**
	 * Returns the cached info for the given project.
	 * 
	 * @param project
	 * @param create
	 * @return
	 */
	public PerProjectInfo getPerProjectInfo(IProject project, boolean create) {
		synchronized (perProjectInfos) {
			PerProjectInfo info = perProjectInfos.get(project);
			if (info == null && create) {
				info = new PerProjectInfo(project);
				perProjectInfos.put(project, info);
			}
			return info;
		}
	}

	public IModule getModuleProject(IResource resource) {
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

	public void start() throws CoreException {
		try {
			final IWorkspace workspace = ResourcesPlugin.getWorkspace();
			workspace.addResourceChangeListener(this.deltaState,
					IResourceChangeEvent.PRE_BUILD | IResourceChangeEvent.POST_BUILD | IResourceChangeEvent.POST_CHANGE
							| IResourceChangeEvent.PRE_DELETE | IResourceChangeEvent.PRE_CLOSE
							| IResourceChangeEvent.PRE_REFRESH);
		} catch (RuntimeException e) {
			stop();
		}

	}

	public void stop() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.removeResourceChangeListener(deltaState);
	}

	public static class PerProjectInfo {

		private IProject project;
		private ResolveReport resolveReport;
		public boolean writingDescriptor;

		public PerProjectInfo(IProject project) {
			this.project = project;
			writingDescriptor = false;
		}

		public Ivy getIvyInstance() throws ModuleException {
			// XXX
			return null;
		}

		public IModuleDescriptor getModuleDescriptor() throws ModuleException {
			return null;
		}

		public void setResolveReport(final ResolveReport report) {
			resolveReport = report;
		}

		public synchronized ResolveReport resolve(ModuleProject moduleProject) throws ModuleException {
			// TODO
			return null;
		}

	}
}
