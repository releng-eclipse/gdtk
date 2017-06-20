package com.ganz.eclipse.gdtk.internal;

import java.io.File;
import java.net.URL;
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
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.junit.Before;
import org.junit.Test;

import com.ganz.eclipse.gdtk.TestCase;
import com.ganz.eclipse.gdtk.core.IFragmentAdapter;
import com.ganz.eclipse.gdtk.core.ModuleCore;

public class ResolverTest extends TestCase {
	private static final String SOURCE_FOLDER = "src/test/java";
	private static final IProgressMonitor NULL_PROGRESS_MONITOR = new NullProgressMonitor();

	@Override
	@Before
	public void setUp() throws CoreException {

	}

	@Test
	public void phpAddToBuildpath() {
		try {
			IProject project = createPhpProject("sample-php");
			IScriptProject phpProject = DLTKCore.create(project);

			File file = new File(
					"O:\\work\\ganz\\repo\\.git\\releng\\eclipse\\gdtk\\plugins\\com.ganz.eclipse.gdtk.core.tests\\src\\test\\resources\\phar\\wxcore.jar");
			Path path = new Path(file.toString());
			IBuildpathEntry entry = DLTKCore.newLibraryEntry(path);

			IBuildpathEntry[] oldEntries = phpProject.getRawBuildpath();
			IBuildpathEntry[] newEntries = new IBuildpathEntry[oldEntries.length + 1];
			System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
			newEntries[oldEntries.length] = entry;
			phpProject.setRawBuildpath(newEntries, NULL_PROGRESS_MONITOR);
		} catch (CoreException e) {
			String t = e.getMessage();
		}
	}

	@Test
	public void javaAddToClassPath() {
		try {
			IProject project = createJavaProject("sample");
			IJavaProject javaProject = JavaCore.create(project);
			URL url = this.getClass().getClassLoader().getResource("resources/jar/commons-cli-1.0.jar");
			URL sourceAttachmentUrl = this.getClass().getClassLoader().getResource("resources/jar/commons-cli-1.0-sources.jar");
			File file = new File(
					"O:\\work\\ganz\\repo\\.git\\releng\\eclipse\\gdtk\\plugins\\com.ganz.eclipse.gdtk.core.tests\\src\\test\\resources\\jar\\commons-cli-1.0.jar");
			File sourceAttachmentFile = new File(
					"O:\\work\\ganz\\repo\\.git\\releng\\eclipse\\gdtk\\plugins\\com.ganz.eclipse.gdtk.core.tests\\src\\test\\resources\\jar\\commons-cli-1.0.jar");
			Path path = new Path(file.toString());
			Path sourceAttachmentPath = new Path(sourceAttachmentFile.toString());
			Path sourceAttachmentRootPath = null;

			IClasspathEntry entry = JavaCore.newLibraryEntry(path, sourceAttachmentPath, sourceAttachmentRootPath, false);

			IClasspathEntry[] oldClasspath = javaProject.getRawClasspath();
			IClasspathEntry[] newClasspath = new IClasspathEntry[oldClasspath.length + 1];
			System.arraycopy(oldClasspath, 0, newClasspath, 0, oldClasspath.length);
			newClasspath[oldClasspath.length] = entry;
			IProgressMonitor monitor = new NullProgressMonitor();
			javaProject.setRawClasspath(newClasspath, monitor);
		} catch (CoreException e) {

		}
	}

	@SuppressWarnings("restriction")
	protected IProject createPhpProject(final String name) throws CoreException {
		IProject project = createGenericProject(name);

		IFragmentAdapter adapter = ModuleCore.getAdapter("php");
		adapter.addNature(project);
		// phpNature.setProject(project);
		// phpNature.configure();
		return project;
	}

	protected IProject createGenericProject(final String name) throws CoreException {
		final IWorkspaceRoot wrkspRoot = ResourcesPlugin.getWorkspace().getRoot();
		final IProject project = wrkspRoot.getProject(name);
		project.create(NULL_PROGRESS_MONITOR);
		project.open(NULL_PROGRESS_MONITOR);
		return project;
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
