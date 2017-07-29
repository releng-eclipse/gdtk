package com.ganz.eclipse.gdtk.internal.core;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;

//@see org.eclipse.jdt.internal.core.DeltaProcessingState
public class DeltaProcessingState implements IResourceChangeListener {

	private ThreadLocal<DeltaProcessor> deltaProcessors = new ThreadLocal<DeltaProcessor>();

	public DeltaProcessor getDeltaProcessor() {
		DeltaProcessor processor = deltaProcessors.get();
		if (processor == null) {
			processor = new DeltaProcessor(this, ModuleModelManager.getInstance());
			deltaProcessors.set(processor);
		}
		return processor;
	}

	///////////////////////////////////////////////////////
	// IResourceChangeListener implementation
	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		try {
			getDeltaProcessor().resourceChanged(event);
		} finally {
			// TODO
		}

	}

}
