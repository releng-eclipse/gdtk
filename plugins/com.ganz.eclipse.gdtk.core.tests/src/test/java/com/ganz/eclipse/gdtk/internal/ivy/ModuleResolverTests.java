package com.ganz.eclipse.gdtk.internal.ivy;

import java.io.IOException;
import java.text.ParseException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

import com.ganz.eclipse.gdtk.AbstractModuleTests;
import com.ganz.eclipse.gdtk.internal.core.ModuleProject;
import com.ganz.eclipse.gdtk.internal.core.Solution;
import com.ganz.eclipse.gdtk.internal.core.SolutionManager;

public class ModuleResolverTests extends AbstractModuleTests {

	@Test
	public void resolveJavaProject() throws CoreException, ParseException, IOException {
		IProject project = createModuleProject("sample");
		Solution solution = SolutionManager.getInstance().getSolution();
		ModuleProject sample = new ModuleProject(project, solution);

		project = createModuleProject("example");
		ModuleProject example = new ModuleProject(project, solution);
		ResolveJob.resolve(sample);
		ResolveJob.resolve(example);
		// ModuleResolver resolver = new ModuleResolver();
		// resolver.resolve(module, NULL_PROGRESS_MONITOR, 0);
	}

}
