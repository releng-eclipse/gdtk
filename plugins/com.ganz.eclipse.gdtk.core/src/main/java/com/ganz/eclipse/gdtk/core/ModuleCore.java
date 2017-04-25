package com.ganz.eclipse.gdtk.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

public class ModuleCore extends Plugin {
	public static final String PLUGIN_ID = "com.ganz.eclipse.gdtk.core"; //$NON-NLS-1$
	public static final String NATURE_ID = PLUGIN_ID + ".modulenature"; //$NON-NLS-1$
	private static BundleContext ctx;

	static BundleContext getContext() {
		return ctx;
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
		// JavaCore.getJavaCore()
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

	public static void addModuleNature(IProject project, IProgressMonitor monitor) throws CoreException {
		if (monitor != null && monitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		if (!project.hasNature(NATURE_ID)) {
			IProjectDescription description = project.getDescription();
			String[] oldNatureIds = description.getNatureIds();
			String[] newNatureIds = new String[oldNatureIds.length + 1];
			System.arraycopy(oldNatureIds, 0, newNatureIds, 0, oldNatureIds.length);
			newNatureIds[oldNatureIds.length + 1] = NATURE_ID;
			description.setNatureIds(newNatureIds);
			project.setDescription(description, monitor);
		}
		if (monitor != null) {
			monitor.worked(1);
		}
	}

}
