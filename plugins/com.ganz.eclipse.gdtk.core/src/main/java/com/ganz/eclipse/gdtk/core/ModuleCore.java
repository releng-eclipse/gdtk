package com.ganz.eclipse.gdtk.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.PerformanceStats;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.pde.internal.core.PDECore;
import org.osgi.framework.BundleContext;

import com.ganz.eclipse.gdtk.internal.core.ModuleModelManager;
import com.ganz.eclipse.gdtk.internal.core.Solution;
import com.ganz.eclipse.gdtk.internal.util.Logger;

public class ModuleCore extends Plugin {
	public static final String PLUGIN_ID = "com.ganz.eclipse.gdtk.core"; //$NON-NLS-1$
	public static final String NATURE_ID = PLUGIN_ID + ".modulenature"; //$NON-NLS-1$
	private static BundleContext ctx;
	private static ModuleCore plugin;
	private static Logger logger;

	static BundleContext getContext() {
		return ctx;
	}

	public static ModuleCore getDefault() {
		return plugin;
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
		plugin = this;
		Logger.getInstance().info("Starting ModuleCore");
		if (PerformanceStats.ENABLED) {
			Logger.getInstance().info("PerformanceStats is enabled.");
		}
		Solution.getInstance().open();
		//JavaCore.getJavaCore();
		// org.eclipse.jdt.ui.CompilationUnitEditor
		//PDECore t;
	
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext ctx) throws Exception {
		Logger.getInstance().info("Stopping ModuleCore");
		Solution.getInstance().close();
		plugin = null;
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

	/**
	 * Returns the Module project corresponding to the given project.
	 * <p>
	 * 
	 * @param project
	 * @return the Module project corresponding to the given project, or null if
	 *         the given project is null.
	 */
	public static IModuleProject create(IProject project) {
		if (project == null) {
			return null;
		}
		return ModuleModelManager.getInstance().getModuleProject(project);

	}

}
