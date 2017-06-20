package com.ganz.eclipse.gdtk;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.junit.Before;

public class TestCase {
	private static final String SOURCE_FOLDER = "src/test/java";
	private static final IProgressMonitor NULL_PROGRESS_MONITOR = new NullProgressMonitor();

	@Before
	public void setUp() throws CoreException {

	}

	@SuppressWarnings("restriction")
	private IProject createJavaProject(final String name) throws CoreException {
		final IWorkspaceRoot wroot = ResourcesPlugin.getWorkspace().getRoot();
		final IProject proj = wroot.getProject(name);
		proj.create(NULL_PROGRESS_MONITOR);
		proj.open(NULL_PROGRESS_MONITOR);
		org.eclipse.jdt.internal.ui.wizards.buildpaths.BuildPathsBlock.addJavaNature(proj,
				new SubProgressMonitor(NULL_PROGRESS_MONITOR, 1));
		final IJavaProject javaProject = JavaCore.create(proj);
		final List<IClasspathEntry> entries = new ArrayList<>();
		for (final IClasspathEntry entry : javaProject.getRawClasspath()) {
			if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
				((org.eclipse.jdt.internal.core.ClasspathEntry) entry).path = Path.fromPortableString(SOURCE_FOLDER);
			}
			entries.add(entry);
		}
		entries.add(JavaRuntime.getDefaultJREContainerEntry());
		javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), NULL_PROGRESS_MONITOR);
		return proj;
	}
}
