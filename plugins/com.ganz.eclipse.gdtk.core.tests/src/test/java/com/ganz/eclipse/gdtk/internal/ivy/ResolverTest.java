package com.ganz.eclipse.gdtk.internal.ivy;

import java.io.IOException;
import java.text.ParseException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.junit.Test;

import com.ganz.eclipse.gdtk.AbstractModuleTest;
import com.ganz.eclipse.gdtk.core.IModule;
import com.ganz.eclipse.gdtk.core.ModuleCore;
import com.ganz.eclipse.gdtk.internal.job.ResolveJob;

public class ResolverTest extends AbstractModuleTest {

	@Test
	public void resolveJavaProject() throws CoreException, ParseException, IOException {
		IProject project = createModuleProject("sample");
		configureAsJavaProject(project);
		IModule module = ModuleCore.create(project);
		ResolveJob.getInstance().addModule(module);

		// Solution solution = SolutionManager.getInstance().getSolution();
		// ModuleProject sample = new ModuleProject(project, solution);

		project = createModuleProject("example");
		// ModuleProject example = new ModuleProject(project, solution);
		// ResolveJob.resolve(sample);
		// ResolveJob.resolve(example);
		// ModuleResolver resolver = new ModuleResolver();
		// resolver.resolve(module, NULL_PROGRESS_MONITOR, 0);
	}

}
