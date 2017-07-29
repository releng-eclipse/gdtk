package com.ganz.eclipse.gdtk.internal.ivy;

import java.io.IOException;
import java.text.ParseException;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.report.ResolveReport;
import org.apache.ivy.core.resolve.ResolveOptions;
import org.eclipse.core.runtime.IProgressMonitor;

import com.ganz.eclipse.gdtk.core.ModuleCore;
import com.ganz.eclipse.gdtk.internal.core.ModuleModelManager.PerProjectInfo;
import com.ganz.eclipse.gdtk.internal.core.ModuleProject;

public class Proxy {

	private static String DEBUG_OPTION_NAME = ModuleCore.PLUGIN_ID + "/debug/resolve";
	private static boolean VERBOSE = true;

	public void resolve(ModuleProject project, IProgressMonitor monitor) {
		try {
			PerProjectInfo info = project.getPerProjectInfo();
			Ivy ivy = info.getIvyInstance();
			ModuleDescriptor md = info.getModuleDescriptor();

			ivy.pushContext();

			ResolveListener listener = new ResolveListener(monitor);
			ivy.getEventManager().addIvyListener(listener);
			// TODO deal with the name of the task
			monitor.setTaskName("Resolve");

			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(Proxy.class.getClassLoader());

			try {
				ResolveReport report = doResolve(ivy, md, md.getConfigurationsNames());
				info.setResolveReport(report, monitor);
			} catch (Exception e) {
				// FIXME

			} finally {
				Thread.currentThread().setContextClassLoader(cl);
				ivy.getEventManager().removeIvyListener(listener);
			}
		} catch (Exception e) {
			// FIXME

		} finally {

		}
	}

	private ResolveReport doResolve(Ivy ivy, ModuleDescriptor md, String[] confs) throws ParseException, IOException {
		return doFullResolve(ivy, md, confs);
	}

	private ResolveReport doFullResolve(Ivy ivy, ModuleDescriptor md, String[] confs)
			throws ParseException, IOException {
		ResolveOptions opts = new ResolveOptions();
		opts.setConfs(confs);
		opts.setValidate(ivy.getSettings().doValidate());
		opts.setResolveId(Util.buildResolveId(md, false));

		return ivy.getResolveEngine().resolve(md, opts);

	}
}
