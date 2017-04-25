package com.ganz.eclipse.gdtk.internal.ivy;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.module.descriptor.Artifact;
import org.apache.ivy.core.module.descriptor.DefaultDependencyDescriptor;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.apache.ivy.core.report.ArtifactDownloadReport;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.IvyNode;
import org.apache.ivy.core.resolve.ResolveData;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.apache.ivy.core.resolve.ResolvedModuleRevision;
import org.apache.ivy.plugins.report.XmlReportParser;
import org.apache.ivy.plugins.resolver.DependencyResolver;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;

import com.ganz.eclipse.gdtk.core.ModuleCore;

public class Resolver {
	private static String DEBUG_OPTION_NAME = ModuleCore.PLUGIN_ID + "/debug/ivy/resolver";
	private static boolean VERBOSE = true;

	private final IProject project;

	private boolean useExtendedResolveId = false;
	private boolean useCacheOnly = true;
	private boolean transitive = true;

	public Resolver(IProject project) {
		this.project = project;
	}

	public IProject getProject() {
		return project;
	}

	public IStatus resolve(Ivy ivy, ModuleDescriptor md, IProgressMonitor monitor, int step) {

		try {
			ivy.pushContext();
			trace("Resolving " + md.toString());

			ResolveListener listener = new ResolveListener(monitor, step);
			ivy.getEventManager().addIvyListener(listener);
			monitor.setTaskName("Resolve");

			ResolveResult result = new ResolveResult();

			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(Resolver.class.getClassLoader());
			try {
				result = doResolve(ivy, md, md.getConfigurationsNames());
			} catch (Exception e) {
				String message = String.format("Failed to resolve '%s' module.", md.getModuleRevisionId().getName());
				return new Status(IStatus.ERROR, ModuleCore.PLUGIN_ID, IStatus.ERROR, message, e);
			} finally {
				Thread.currentThread().setContextClassLoader(cl);
				ivy.getEventManager().removeIvyListener(listener);
			}
			if (!result.getErrorMessages().isEmpty()) {
				MultiStatus status = new MultiStatus(ModuleCore.PLUGIN_ID, IStatus.ERROR, "Failed to resolve ", null);
				for (String message : result.getErrorMessages()) {
					status.add(new Status(IStatus.ERROR, ModuleCore.PLUGIN_ID, IStatus.ERROR, message, null));
				}
				return status;
			}
			return Status.OK_STATUS;
		} catch (Throwable t) {
			String message = String.format("Failed to resolve '%s' module.", md.getModuleRevisionId().getName());
			return new Status(IStatus.ERROR, ModuleCore.PLUGIN_ID, IStatus.ERROR, message, t);
		} finally {
			ivy.popContext();
		}

	}

	private ResolveResult doResolve(Ivy ivy, ModuleDescriptor md, String[] confs) throws ParseException, IOException {
		trace("Attempting to use previous resolve reports");

		ResolveResult result = new ResolveResult();
		try {
			for (int i = 0; i < confs.length; i++) {
				File report = ivy.getResolutionCacheManager()
						.getConfigurationResolveReportInCache(Util.buildResolveId(md, useExtendedResolveId), confs[i]);
				trace("Fetching '%s' configuration resolve report (%s)", confs[i], report);
				if (!report.exists()) {
					throw new IOException(String.format("Report '%s' does not exist.", report));
				}
				trace("Parsing '%s' resolve report.", report);
				XmlReportParser parser = new XmlReportParser();
				parser.parse(report);
				result.addArtifactReports(parser.getArtifactReports());

				// figure out the artifacts provided by each dependency
				ModuleRevisionId[] mrids = parser.getDependencyRevisionIds();
				trace("Successfully parsed '%s' resolve report. Fetching artifacts for %d dependencies.", report, mrids.length);
				for (i = 0; i < mrids.length; i++) {
					DependencyResolver resolver = ivy.getSettings().getResolver(mrids[i]);
					DefaultDependencyDescriptor descriptor = new DefaultDependencyDescriptor(mrids[i], false);
					ResolveOptions opts = new ResolveOptions();
					opts.setRefresh(true);
					opts.setUseCacheOnly(true);
					trace("Fetching dependency %s", mrids[i]);
					ResolveData resolveData = new ResolveData(ivy.getResolveEngine(), opts);
					ResolvedModuleRevision dependency = resolver.getDependency(descriptor, resolveData);
					if (dependency != null) {
						Artifact[] artifacts = dependency.getDescriptor().getAllArtifacts();
						result.setDependencyArtifacts(mrids[i], artifacts);
					}
				}
			}

		} catch (IOException | ParseException e) {
			result = doFullResolve(ivy, md, confs);
		}
		return result;
	}

	private ResolveResult doFullResolve(Ivy ivy, ModuleDescriptor md, String[] confs) throws ParseException, IOException {

		ResolveOptions opts = new ResolveOptions();
		opts.setConfs(confs);
		opts.setValidate(ivy.getSettings().doValidate());
		opts.setUseCacheOnly(useCacheOnly);
		opts.setTransitive(transitive);
		opts.setResolveId(Util.buildResolveId(md, false));

		ResolveReport report = ivy.getResolveEngine().resolve(md, opts);

		String message = report.hasError() ? "Failed to resolve '%s' module." : "Succesfully resolved '%s' module.";
		trace(message, md);

		ArtifactDownloadReport[] artifactReports = report.getArtifactsReports(null, false);

		// TODO add workspace artifacts reports
		ResolveResult result = new ResolveResult(report);

		result.addArtifactReports(artifactReports);
		// figure out the artifacts provided by each dependency
		for (Object node : report.getDependencies()) {
			IvyNode dependency = (IvyNode) node;
			if (dependency.getDescriptor() != null) {
				ModuleRevisionId mrid = dependency.getResolvedId();
				Artifact[] artifacts = dependency.getAllArtifacts();
				result.setDependencyArtifacts(mrid, artifacts);
			}
		}
		return result;
	}

	public void postResolve() {

	}

	private void trace(String message, Object... args) {
		if (!VERBOSE) {
			return;
		}
		System.out.printf("[" + this.getClass().getSimpleName() + "] " + message + System.lineSeparator(), args);
	}
}
