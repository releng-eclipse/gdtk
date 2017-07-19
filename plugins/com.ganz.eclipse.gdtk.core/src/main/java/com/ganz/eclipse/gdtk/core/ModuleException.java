package com.ganz.eclipse.gdtk.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;

public class ModuleException extends CoreException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8816526773283842903L;

	public ModuleException(IStatus status) {
		super(status);
	}

}
