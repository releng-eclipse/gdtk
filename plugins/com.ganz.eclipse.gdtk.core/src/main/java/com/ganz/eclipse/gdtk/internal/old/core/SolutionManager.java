package com.ganz.eclipse.gdtk.internal.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.settings.IvySettings;
import org.apache.ivy.plugins.parser.ModuleDescriptorParserRegistry;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;

public class SolutionManager {
	public class PerModuleState {
		private IProject project;
		private Ivy ivy;
		IPath moduleSettingsPath;
		IPath moduleMetadataPath;
		private ResolveReport report;

		public PerModuleState(IProject project) {
			this.project = project;
		}

		public IProject getProject() {
			return project;
		}

		public Ivy getIvy() {
			if (ivy != null) {
				return ivy;
			}

			IvySettings settings = createSettings();
			ivy = Ivy.newInstance(settings);
			try {
				ivy.configureDefault();
			} catch (ParseException e) {
				// TODO
			} catch (IOException ioe) {

			}
			return ivy;
		}

		public ModuleDescriptor getModuleDescriptor() {
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
				md = ModuleDescriptorParserRegistry.getInstance().parseDescriptor(getIvy().getSettings(), descriptorURL, true);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return md;
		}

		private IvySettings createSettings() {
			IvySettings settings = new IvySettings();
			return settings;
		}

		public void setResolveReport(ResolveReport report) {
			this.report = report;
		}

		public ResolveReport getResolveReport() {
			return report;
		}

	}

	private static SolutionManager instance;
	private static Solution solution;

	private HashMap<IProject, PerModuleState> perModuleStates;

	public static SolutionManager getInstance() {
		if (instance == null) {
			instance = new SolutionManager();
			instance.perModuleStates = new HashMap<>(5);
		}
		return instance;
	}

	public Solution getSolution() {
		if (solution == null) {
			solution = new Solution();
		}
		return solution;
	}

	public PerModuleState getPerModuleState(IProject project) throws SolutionException {
		PerModuleState state = getPerModuleState(project, false);
		if (state == null) {
			// TODO check for module nature and throw exception if not
			state = getPerModuleState(project, true);

		}
		return state;
	}

	public PerModuleState getPerModuleState(IProject project, boolean create) {
		synchronized (perModuleStates) {
			PerModuleState state = perModuleStates.get(project);
			if (state == null && create) {
				state = new PerModuleState(project);
				perModuleStates.put(project, state);
			}
			return state;
		}
	}
}
