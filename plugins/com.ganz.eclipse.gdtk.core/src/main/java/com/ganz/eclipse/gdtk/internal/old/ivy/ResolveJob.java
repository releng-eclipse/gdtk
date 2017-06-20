package com.ganz.eclipse.gdtk.internal.ivy;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.PerformanceStats;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.ganz.eclipse.gdtk.core.ModuleCore;
import com.ganz.eclipse.gdtk.internal.core.ModuleProject;
import com.ganz.eclipse.gdtk.internal.util.Logger;

public class ResolveJob extends Job {
	private static String DEBUG_OPTION_NAME = ModuleCore.PLUGIN_ID + "/debug/ivy/ResolveJob";
	private static boolean VERBOSE = true;
	private static String PERF_RESOLVE_JOB = ModuleCore.PLUGIN_ID + "/perf/agents";
	static String NAME = "Module Resolve Job";

	private static ResolveJob instance;

	private final List queue = new ArrayList();

	public ResolveJob() {
		super(NAME);
		setUser(false);
		setRule(ResourcesPlugin.getWorkspace().getRuleFactory().buildRule());
	}

	public static ResolveJob getInstance() {
		if (instance == null) {
			instance = new ResolveJob();
		}
		return instance;
	}

	public static void resolve(ModuleProject module) {
		resolve(module, new NullProgressMonitor());
	}

	public static void resolve(ModuleProject module, IProgressMonitor monitor) {
		ResolveJob instance = getInstance();
		synchronized (instance.queue) {
			instance.queue.add(module);
		}
		instance.schedule(1000);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		PerformanceStats stats = PerformanceStats.getStats(PERF_RESOLVE_JOB, this);
		try {
			stats.startRun();
			return doRun(monitor);
		} catch (RuntimeException e) {
			Logger.getInstance().error("Resolve failed.", e);
			// TODO
			return Status.OK_STATUS;
		} finally {
			stats.endRun();
			Logger.getInstance().info("Resolve took " + stats.getRunningTime());
		}
	}

	private IStatus doRun(IProgressMonitor monitor) {
		List<ModuleProject> modules;
		synchronized (queue) {
			modules = new ArrayList<ModuleProject>(queue);
			queue.clear();
		}
		if (modules.isEmpty()) {
			return Status.OK_STATUS;
		}

		trace("Found {0} modules to resolve.", modules.size());

		for (ModuleProject module : modules) {
			boolean canceled = launchResolveThread(module, monitor);
		}
		return Status.OK_STATUS;
	}

	private boolean launchResolveThread(final ModuleProject module, final IProgressMonitor monitor) {
		final IStatus[] status = new IStatus[1];
		ModuleResolver resolver = new ModuleResolver();
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				// try {
				resolver.resolve(module, monitor, 0);
				// } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
		};

		Runner runner = new Runner();
		if (runner.launch(runnable, monitor)) {
			return true;
		}
		return false;
	}

	private void trace(String message, Object... args) {
		if (!VERBOSE) {
			return;
		}
		System.out.printf("[" + this.getClass().getSimpleName() + "] " + message + System.lineSeparator(), args);
	}

}
