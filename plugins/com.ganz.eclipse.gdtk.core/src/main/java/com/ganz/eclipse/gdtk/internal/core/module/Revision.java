package com.ganz.eclipse.gdtk.internal.core.module;

import org.apache.ivy.core.module.id.ModuleRevisionId;

import com.ganz.eclipse.gdtk.core.IModuleRevision;

public class Revision implements IModuleRevision {
	// FIXME remove
	private String name;

	private ModuleRevisionId revision;

	public Revision(String name) {
		this.name = name;
	}

	public Revision(ModuleRevisionId rev) {
		revision = rev;
	}

	public String getName() {
		return name;
	}

}
