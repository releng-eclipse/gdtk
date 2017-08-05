package com.ganz.eclipse.gdtk.internal.core.module;

import org.apache.ivy.core.module.descriptor.ModuleDescriptor;

import com.ganz.eclipse.gdtk.core.IModuleDescriptor;

public class Descriptor implements IModuleDescriptor {
	ModuleDescriptor wrapped;

	public Descriptor(ModuleDescriptor descriptor) {
		wrapped = descriptor;
	}

}
