package com.ganz.eclipse.gdtk.core;

import org.eclipse.core.resources.IProject;

public interface IModule {
	public static final String NATURE_ID = ModuleCore.PLUGIN_ID + ".modulenature"; //$NON-NLS-1$

	//TODO what is the difference between getAllRealCallers and getAllCallers
	public IModule[] getAllRealCallers();
	
	public IModule[] getAllCallers();
	
	public IModule[] getEvictingModules();
	
	public IModule[] getEvictedModules();
	
	public IModule[] getDependencies();
	
	/**
	 * Returns the <code>IProject</code> on which this <code>IModule</code> was
	 * created.
	 * 
	 * @return the <code>IProject</code> on which this <code>IModule</code> was
	 *         created.
	 */
	public IProject getProject();
	
	public IRevisionId getResolvedRevisionId();
	
		
	static public interface IRevisionId {
		
	}
}
