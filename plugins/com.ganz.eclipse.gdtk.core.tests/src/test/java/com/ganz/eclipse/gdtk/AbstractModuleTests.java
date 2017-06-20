package com.ganz.eclipse.gdtk;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;

public class AbstractModuleTests {
	private static final String SOURCE_FOLDER = "src/test/java";
	public static final IProgressMonitor NULL_PROGRESS_MONITOR = new NullProgressMonitor();

	@SuppressWarnings("restriction")
	protected IProject createJavaProject(final String name) throws CoreException {
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

	protected IProject createModuleProject(final String name) throws CoreException, IOException {
		IProject project = createGenericProject(name);

		IFolder ivyPath = project.getFolder("ivy");
		if (!ivyPath.exists()) {
			ivyPath.create(true, true, NULL_PROGRESS_MONITOR);
		}

		URL descriptorURL = this.getClass().getClassLoader().getResource("xml/ivy-" + name + ".xml");
		InputStream source = descriptorURL.openStream();

		IFile descriptorFile = project.getFile("ivy/metadata.xml");

		if (!descriptorFile.exists()) {
			descriptorFile.create(source, true, NULL_PROGRESS_MONITOR);
		}
		return project;
	}

	protected IProject createGenericProject(final String name) throws CoreException {
		final IWorkspaceRoot wrkspRoot = ResourcesPlugin.getWorkspace().getRoot();
		final IProject project = wrkspRoot.getProject(name);
		project.create(NULL_PROGRESS_MONITOR);
		project.open(NULL_PROGRESS_MONITOR);
		return project;
	}

	protected IFolder createFolder(String path) throws CoreException {
		return createFolder(new Path(path));
	}

	protected IFolder createFolder(IPath path) throws CoreException {
		final IFolder folder = getWorkspaceRoot().getFolder(path);
		getWorkspace().run(new IWorkspaceRunnable() {
			@Override
			public void run(IProgressMonitor monitor) throws CoreException {
				IContainer parent = folder.getParent();
				if (parent instanceof IFolder && !parent.exists()) {
					createFolder(parent.getFullPath());
				}
				folder.create(true, true, monitor);
			}
		}, null);
		return folder;
	}

	protected IWorkspaceRoot getWorkspaceRoot() {
		return getWorkspace().getRoot();
	}

	protected IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}
}
