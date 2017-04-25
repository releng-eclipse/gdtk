package com.ganz.eclipse.gdtk.core;

import org.eclipse.core.resources.IProject;

public interface IModule {

	/**
	 * Returns the <code>IProject</code> on which this <code>IModule</code> was
	 * created.
	 * 
	 * @return the <code>IProject</code> on which this <code>IModule</code> was
	 *         created.
	 */
	public IProject getProject();
}
