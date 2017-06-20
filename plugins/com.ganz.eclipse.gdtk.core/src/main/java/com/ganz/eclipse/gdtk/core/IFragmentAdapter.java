package com.ganz.eclipse.gdtk.core;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public interface IFragmentAdapter {

	public String getFragmentKind();

	public void addNature(IProject project) throws CoreException;

	public void addNature(IProject project, IProgressMonitor monitor) throws CoreException;

}
