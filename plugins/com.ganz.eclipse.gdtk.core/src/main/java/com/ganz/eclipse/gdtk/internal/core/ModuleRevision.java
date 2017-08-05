package com.ganz.eclipse.gdtk.internal.core;

import org.apache.ivy.core.module.id.ModuleRevisionId;

import com.ganz.eclipse.gdtk.core.IModuleRevision;

public class ModuleRevision implements IModuleRevision {
	// FIXME remove
	private String name;

	private ModuleRevisionId revision;

	public ModuleRevision(String name) {
		this.name = name;
	}

	public ModuleRevision(ModuleRevisionId rev) {
		revision = rev;
	}

	public String getName() {
		return name;
	}

}
