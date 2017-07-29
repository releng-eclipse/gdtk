package com.ganz.eclipse.gdtk.internal.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;

import com.ganz.eclipse.gdtk.core.ModuleCore;
import com.ganz.eclipse.gdtk.core.ModuleException;
import com.ganz.eclipse.gdtk.internal.core.ModuleModelManager.PerProjectInfo;

//@see org.eclipse.jdt.internal.core.DeltaProcessor
public class DeltaProcessor {
	private static boolean VERBOSE = true;
	private static String DEBUG_OPTION_NAME = ModuleCore.PLUGIN_ID + "debug/deltaprocesing";

	public DeltaProcessor(DeltaProcessingState state, ModuleModelManager manager) {

	}

	/**
	 * Inspects the changed resources and determines if and how to update the
	 * internal structures of the <code>ModuleModelManage</code>.
	 * 
	 * @param event
	 */
	public void resourceChanged(IResourceChangeEvent event) {

		int eventType = event.getType();
		IResourceDelta delta = event.getDelta();
		switch (eventType) {
		case IResourceChangeEvent.POST_CHANGE:
			checkProjectsAndDescriptorChanges(delta);
			break;
		}
	}

	private void checkProjectsAndDescriptorChanges(IResourceDelta delta) {
		IResource resource = delta.getResource();
		IResourceDelta[] children = null;
		ModuleProject moduleProject = null;

		switch (resource.getType()) {
		case IResource.ROOT:
			children = delta.getAffectedChildren();
			break;
		case IResource.PROJECT:
			int kind = delta.getKind();
			switch (delta.getKind()) {
			case IResourceDelta.CHANGED:
				children = delta.getAffectedChildren();
				break;
			}

			break;
		case IResource.FOLDER:
			if (delta.getKind() == IResourceDelta.CHANGED) {
				children = delta.getAffectedChildren();
			}
			break;
		case IResource.FILE:
			IFile file = (IFile) resource;
			// FIXME query the module project to find out descriptor name
			if (file.getName().contentEquals("metadata.xml")) {
				switch (delta.getKind()) {
				case IResourceDelta.CHANGED:
					int flags = delta.getFlags();
					// ignore changes other than content changes; otherwise fall
					// through
					if ((flags & IResourceDelta.CONTENT) == 0) {
						break;
					}
					//$FALL-THROUGH$
				case IResourceDelta.ADDED:
				case IResourceDelta.REMOVED:
					moduleProject = (ModuleProject) ModuleCore.create(file.getProject());
					readRawDescriptor(moduleProject);
					break;
				}
			}
			break;
		}
		if (children != null) {
			for (IResourceDelta child : children) {
				checkProjectsAndDescriptorChanges(child);
			}
		}
	}

	private void readRawDescriptor(ModuleProject moduleProject) {
		trace("'%s' descriptor has changed.", moduleProject);
		try {
			PerProjectInfo info = moduleProject.getPerProjectInfo();
			info.readAndCacheDescriptor();
		} catch (ModuleException e) {
			// FIXME
		}
	}

	/**
	 * @deprecated
	 * @param moduleProject
	 */
	private void resolve(ModuleProject moduleProject) {
		try {
			PerProjectInfo perProjectInfo = moduleProject.getPerProjectInfo();
			if (!perProjectInfo.writingDescriptor) {
				perProjectInfo.resolve(moduleProject);
			}
		} catch (ModuleException e) {
			// TODO some sort of reporting

		}

	}

	private void trace(String message, Object... args) {
		if (!VERBOSE) {
			return;
		}
		System.out.printf("[" + this.getClass().getSimpleName() + "] " + message + System.lineSeparator(), args);
	}

}
