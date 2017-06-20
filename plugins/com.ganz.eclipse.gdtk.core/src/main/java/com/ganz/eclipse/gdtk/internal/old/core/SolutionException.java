package com.ganz.eclipse.gdtk.internal.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

public class SolutionException extends CoreException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5193507817229230115L;

	public SolutionException(IStatus status) {
		super(status);
		// TODO Auto-generated constructor stub
	}

}
