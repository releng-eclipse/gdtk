package com.ganz.eclipse.gdtk.internal.ivy;

import java.io.IOException;
import java.text.ParseException;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.eclipse.core.runtime.IProgressMonitor;

import com.ganz.eclipse.gdtk.core.ModuleCore;
import com.ganz.eclipse.gdtk.internal.core.ModuleProject;
import com.ganz.eclipse.gdtk.internal.core.SolutionManager.PerModuleState;

public class ModuleResolver {

	private static String DEBUG_OPTION_NAME = ModuleCore.PLUGIN_ID + "/debug/ivy/resolver";
	private static boolean VERBOSE = true;

	public void resolve(ModuleProject module, IProgressMonitor monitor, int monitorStep) {
		try {
			PerModuleState state = module.getPerModuleState();
			Ivy ivy = state.getIvy();
			ModuleDescriptor md = state.getModuleDescriptor();
			ivy.pushContext();

			ResolveListener listener = new ResolveListener(monitor, monitorStep);
			ivy.getEventManager().addIvyListener(listener);
			monitor.setTaskName("Resolve");

			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(ModuleResolver.class.getClassLoader());

			try {
				ResolveReport resolveReport = doResolve(ivy, md, md.getConfigurationsNames());
				state.setResolveReport(resolveReport);
			} catch (Exception e) {

			} finally {
				Thread.currentThread().setContextClassLoader(cl);
				ivy.getEventManager().removeIvyListener(listener);

			}
		} catch (Throwable t) {

		} finally {

		}
	}

	private ResolveReport doResolve(Ivy ivy, ModuleDescriptor md, String[] confs) throws ParseException, IOException {
		return doFullResolve(ivy, md, confs);
	}

	private ResolveReport doFullResolve(Ivy ivy, ModuleDescriptor md, String[] confs) throws ParseException, IOException {
		ResolveOptions opts = new ResolveOptions();
		opts.setConfs(confs);
		opts.setValidate(ivy.getSettings().doValidate());

		opts.setResolveId(Util.buildResolveId(md, false));

		ResolveReport report = ivy.getResolveEngine().resolve(md, opts);

		return report;
		// ArtifactDownloadReport[] artifactReports =
		// report.getArtifactsReports(null, false);

	}

	private void trace(String message, Object... args) {
		if (!VERBOSE) {
			return;
		}
		System.out.printf("[" + this.getClass().getSimpleName() + "] " + message + System.lineSeparator(), args);
	}
}
