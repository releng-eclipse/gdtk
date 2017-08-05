package com.ganz.eclipse.gdtk.internal.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.IvyNode;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.ivy.plugins.parser.ModuleDescriptorParserRegistry;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.ganz.eclipse.gdtk.core.IModule;
import com.ganz.eclipse.gdtk.core.IModuleRevision;
import com.ganz.eclipse.gdtk.core.ModuleCore;
import com.ganz.eclipse.gdtk.core.ModuleException;
import com.ganz.eclipse.gdtk.internal.core.module.Descriptor;
import com.ganz.eclipse.gdtk.internal.core.module.Revision;
import com.ganz.eclipse.gdtk.internal.job.ResolveJob;

public class ModuleModelManager {
	static private ModuleModelManager instance = new ModuleModelManager();

	protected Map<IProject, PerProjectInfo> perProjectInfos;
	private DeltaProcessingState deltaState;

	private ModuleModelManager() {
		perProjectInfos = new HashMap<IProject, PerProjectInfo>(5);
		deltaState = new DeltaProcessingState();
	}

	public static ModuleModelManager getInstance() {
		return instance;
	}

	/**
	 * Returns the cached info for the given project.
	 * 
	 * @param project
	 * @param create
	 * @return
	 */
	public PerProjectInfo getPerProjectInfo(IProject project, boolean create) {
		synchronized (perProjectInfos) {
			PerProjectInfo info = perProjectInfos.get(project);
			if (info == null && create) {
				info = new PerProjectInfo(project);
				perProjectInfos.put(project, info);
			}
			return info;
		}
	}

	public IModule getModuleProject(IResource resource) {
		switch (resource.getType()) {
		case IResource.FOLDER:
			return new ModuleProject(((IFolder) resource).getProject(), this);
		case IResource.FILE:
			return new ModuleProject(((IFile) resource).getProject(), this);
		case IResource.PROJECT:
			return new ModuleProject((IProject) resource, this);
		default:
			throw new IllegalArgumentException("Invalid resource type");
		}
	}

	public void start() throws CoreException {
		try {
			final IWorkspace workspace = ResourcesPlugin.getWorkspace();
			workspace.addResourceChangeListener(this.deltaState,
					IResourceChangeEvent.PRE_BUILD | IResourceChangeEvent.POST_BUILD | IResourceChangeEvent.POST_CHANGE
							| IResourceChangeEvent.PRE_DELETE | IResourceChangeEvent.PRE_CLOSE
							| IResourceChangeEvent.PRE_REFRESH);
		} catch (RuntimeException e) {
			stop();
		}

	}

	public void stop() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.removeResourceChangeListener(deltaState);
	}

	public static class PerProjectInfo {

		private IProject project;
		private ResolveReport resolveReport;
		public boolean writingDescriptor;

		private Ivy ivy;
		private ModuleDescriptor md;

		private HashMap<IModuleRevision, Module> dependencies;

		public PerProjectInfo(IProject project) {
			this.project = project;
			this.dependencies = new HashMap<IModuleRevision, Module>();
			writingDescriptor = false;
		}

		public synchronized void readAndCacheDescriptor() throws ModuleException {
			IFolder folder = project.getFolder("ivy");
			IResource descriptor = folder.findMember("metadata.xml");

			URL descriptorURL = null;
			try {
				descriptorURL = descriptor.getLocationURI().toURL();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ModuleDescriptor md = null;
			try {
				md = ModuleDescriptorParserRegistry.getInstance().parseDescriptor(getIvyInstance().getSettings(),
						descriptorURL, true);
				this.md = md;
				ResolveJob.getInstance().addModule(ModuleCore.create(project));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		public Ivy getIvyInstance() throws ModuleException {
			if (ivy != null) {
				return ivy;
			}
			IvySettings settings = createSettings();
			ivy = Ivy.newInstance(settings);
			try {
				ivy.configureDefault();
			} catch (ParseException e) {
				// FIXME
			} catch (IOException e) {
				// FIXME
			}
			return ivy;
		}

		private IvySettings createSettings() {
			IvySettings settings = new IvySettings();
			return settings;
		}

		public ModuleDescriptor getModuleDescriptor() throws ModuleException {
			IFolder folder = project.getFolder("ivy");
			IResource descriptor = folder.findMember("metadata.xml");

			IFile descriptorFile = project.getFile("ivy/metadata.xml");

			URL descriptorURL = null;
			try {
				descriptorURL = descriptor.getLocationURI().toURL();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ModuleDescriptor md = null;
			try {
				md = ModuleDescriptorParserRegistry.getInstance().parseDescriptor(getIvyInstance().getSettings(),
						descriptorURL, true);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return md;
		}

		// public IModule[] getDependencies() {
		//
		// }

		public synchronized void setResolveReport(final ResolveReport report, IProgressMonitor monitor) {
			resolveReport = report;
			dependencies.clear();
			List<IvyNode> nodes = report.getDependencies();
			for (IvyNode node : nodes) {
				if (node.getDescriptor() != null) {
					Revision revision = new Revision(node.getResolvedId());
					Descriptor descriptor = new Descriptor(node.getDescriptor());
					Module dependency = new Module(revision, descriptor);
					dependencies.put(dependency.getRevision(), dependency);
				}
			}

			// IJavaProject p = JavaCore.create(this.project);
			// try {
			// IClasspathEntry[] oldEntries = p.getRawClasspath();
			// List<IClasspathEntry> newEntries = new
			// ArrayList<IClasspathEntry>();
			// for (final IClasspathEntry entry : oldEntries) {
			// newEntries.add(entry);
			// }
			//
			// ArtifactDownloadReport[] artifactReports =
			// resolveReport.getArtifactsReports(null, false);
			//
			// for (ArtifactDownloadReport artifactReport : artifactReports) {
			// Artifact artifact = artifactReport.getArtifact();
			// if (artifact.isMetadata()) {
			// continue;
			// }
			// File file = artifactReport.getLocalFile();
			// File t = file;
			// // IClasspathEntry entry = JavaCore.newLibraryEntry(path,
			// // sourceAttachmentPath,
			// // sourceAttachmentRootPath);
			// // newEntries.add(entry);
			// }
			//
			// p.setRawClasspath(newEntries.toArray(new
			// IClasspathEntry[newEntries.size()]), monitor);
			// } catch (JavaModelException e) {
			// e.printStackTrace();
			// }
		}

		public synchronized ResolveReport resolve(ModuleProject moduleProject) throws ModuleException {
			// TODO
			return null;
		}

	}
}
