package com.ganz.eclipse.gdtk.internal.core;

import com.ganz.eclipse.gdtk.core.IModuleRevision;

public class ModuleRevision implements IModuleRevision {
	// FIXME remove
	private String name;

	public ModuleRevision(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
