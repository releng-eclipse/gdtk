package com.ganz.eclipse.gdtk.internal.core;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceDelta;

import com.ganz.eclipse.gdtk.core.ModuleException;
import com.ganz.eclipse.gdtk.internal.core.ModuleModelManager.PerProjectInfo;

//@see org.eclipse.jdt.internal.core.DeltaProcessor
public class DeltaProcessor {

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
			checkProjectsAndDescriptors(delta);
			break;
		}
	}

	private void checkProjectsAndDescriptors(IResourceDelta delta) {
		IResource resource = delta.getResource();
		IResourceDelta[] children = null;

		switch (resource.getType()) {

		}
		if (children != null) {
			for (IResourceDelta child : children) {
				checkProjectsAndDescriptors(child);
			}
		}
	}

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

}
