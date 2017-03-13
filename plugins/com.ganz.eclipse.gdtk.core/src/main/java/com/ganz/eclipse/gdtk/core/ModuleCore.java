package com.ganz.eclipse.gdtk.core;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class ModuleCore extends Plugin {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void start(BundleContext ctx) throws Exception {
		super.start(ctx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext ctx) throws Exception {
		super.stop(ctx);
	}

}
