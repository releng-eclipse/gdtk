package com.ganz.eclipse.gdtk.internal.old.core;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;

import com.ganz.eclipse.gdtk.core.IModule;

/**
 * Manages the states used during delta processing.
 * 
 * @author Bogdan
 *
 */
public class DeltaProcessingState implements IResourceChangeListener {

	private ThreadLocal<DeltaProcessor> deltaProcessor = new ThreadLocal<>();

	private/* HashMap[] */void getRootStates(boolean usePreviousSession) {
		Solution solution = SolutionManager.getInstance().getSolution();
		IModule[] modules;
		// try {
		// modules = solution.getModules();
		// } catch (SolutionException e) {
		// return;
		// }
		// for (int i = 0, length = modules.length; i < length; i++) {
		// try {
		// Module module = (Module) modules[i];
		// PerModuleState state = module.getPerModuleState();
		// } catch (SolutionException e) {
		// // TODO reporting
		// continue;
		// }
		// }
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {

		try {
			getDeltaProcessor().resourceChanged(event);
		} finally {

		}

	}

	public DeltaProcessor getDeltaProcessor() {
		DeltaProcessor deltaProcessor = this.deltaProcessor.get();
		if (deltaProcessor == null) {
			deltaProcessor = new DeltaProcessor();
			this.deltaProcessor.set(deltaProcessor);
		}
		return deltaProcessor;
	}

}
