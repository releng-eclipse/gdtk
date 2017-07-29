package com.ganz.eclipse.gdtk.internal.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

public class ProjectUtils {
	/**
	 * Adds the nature identified by the given string to the specified project.
	 * 
	 * @param project
	 * @param natureId
	 * @throws CoreException
	 */
	public static void addNature(IProject project, String natureId) throws CoreException {
		ProjectUtils.addNature(project, natureId, new NullProgressMonitor());
	}

	/**
	 * Adds the nature identified by the given string to the specified project.
	 * 
	 * @param project
	 * @param natureId
	 * @param monitor
	 * @throws CoreException
	 */
	public static void addNature(IProject project, String natureId, IProgressMonitor monitor) throws CoreException {
		if (monitor != null && monitor.isCanceled()) {
			throw new OperationCanceledException();
		}

		if (!project.hasNature(natureId)) {
			IProjectDescription description = project.getDescription();
			String[] oldNatures = description.getNatureIds();
			String[] newNatures = new String[oldNatures.length + 1];
			System.arraycopy(oldNatures, 0, newNatures, 0, oldNatures.length);
			newNatures[oldNatures.length] = natureId;
			description.setNatureIds(newNatures);
			project.setDescription(description, monitor);
		}

		if (monitor != null) {
			monitor.worked(1);
		}
	}

}
