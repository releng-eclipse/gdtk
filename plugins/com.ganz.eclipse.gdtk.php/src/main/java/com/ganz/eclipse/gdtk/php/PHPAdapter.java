package com.ganz.eclipse.gdtk.php;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.ui.IStartup;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.ganz.eclipse.gdtk.core.IFragmentAdapter;
import com.ganz.eclipse.gdtk.core.ModuleCore;

public class PHPAdapter implements IStartup, IFragmentAdapter {
	protected static final IProgressMonitor NULL_PROGRESS_MONITOR = new NullProgressMonitor();

	private static PHPAdapter instance;

	public String getNatureId() {
		// TODO very that the org.eclipse.php.core plugin is loaded
		return org.eclipse.php.internal.core.project.PHPNature.ID;
	}

	protected void addNature(String natureId, IProject project, IProgressMonitor monitor) throws CoreException {
		if (monitor.isCanceled()) {
			throw new OperationCanceledException();
		}
		if (project.hasNature(natureId)) {
			monitor.worked(1);
			return;
		}
		IProjectDescription description = project.getDescription();
		String[] oldNatures = description.getNatureIds();
		String[] newNatures = new String[oldNatures.length + 1];
		System.arraycopy(oldNatures, 0, newNatures, 0, oldNatures.length);
		newNatures[oldNatures.length] = natureId;
		description.setNatureIds(newNatures);
		project.setDescription(description, monitor);
		monitor.worked(1);
	}

	public void addNature(IProject project, IProgressMonitor monitor) throws CoreException {
		String natureId = getNatureId();
		addNature(natureId, project, monitor);
	}

	public void addNature(IProject project) throws CoreException {
		addNature(project, NULL_PROGRESS_MONITOR);
	}

	public static IFragmentAdapter getInstance() {
		if (instance == null) {
			instance = new PHPAdapter();
		}
		return instance;
	}

	@Override
	public void earlyStartup() {
		BundleContext ctx = FrameworkUtil.getBundle(PHPAdapter.class).getBundleContext();

		ModuleCore.registerAdapter(PHPAdapter.getInstance());
	}

	@Override
	public String getFragmentKind() {
		return "php";
	}

}
