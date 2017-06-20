package com.ganz.eclipse.gdtk.internal.ivy;

import org.eclipse.core.runtime.IProgressMonitor;

public class Runner {

	public boolean launch(Runnable runnable, IProgressMonitor monitor) {
		if (monitor.isCanceled()) {
			return true;
		}

		Thread thread = new Thread(runnable);
		thread.setName("Resolve thread");
		thread.start();
		while (true) {
			try {
				thread.join(100);
			} catch (InterruptedException e) {
				return true;
			}
			if (!thread.isAlive()) {
				return false;
			}
			if (monitor.isCanceled()) {
				return true;
			}
		}
	}
}
