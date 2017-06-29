package com.ganz.eclipse.gdtk.internal.old;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.ganz.eclipse.gdtk.core.ModuleCore;
import com.ganz.eclipse.gdtk.internal.core.Module;
import com.ganz.eclipse.gdtk.internal.ivy.ResolveRequest;

public class ResolveJob extends Job {
	private static String DEBUG_OPTION_NAME = ModuleCore.PLUGIN_ID + "/debug/ivy/ResolveJob";
	private static boolean VERBOSE = true;

	private static final int FULL_RESOLVE_WORK_UNITS = 1000;
	private static final int MAIN_RESOLVE_WORK_UNITS = 800;
	private static final int POST_RESOLVE_WORK_UNITS = 100;

	private static final int SCHEDULE_DELAY = 1000;

	private static ResolveJob instance;
	private final List<ResolveRequest> queue;
	private final List<Module> queuex;

	private ResolveJob() {
		super("Module Resolve Worker");
		setUser(false);
		setRule(ResourcesPlugin.getWorkspace().getRuleFactory().buildRule());
		queue = new ArrayList<ResolveRequest>();
		queuex = new ArrayList<Module>();
	}

	public static ResolveJob getInstance() {
		if (instance == null) {
			instance = new ResolveJob();
		}
		return instance;
	}

	public void resolve(Module module, IProgressMonitor monitor) {
		synchronized (queuex) {
			queuex.add(module);
		}
		if (monitor != null) {
			run(monitor);
		} else {
			schedule(SCHEDULE_DELAY);
		}
	}

	public void addRequest(ResolveRequest request) {
		synchronized (queue) {
			queue.add(request);
		}
		schedule(SCHEDULE_DELAY);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		return null;
	}

	private IStatus doRun(IProgressMonitor monitor) {
		trace("Starting resolve worker ...");
		List<ResolveRequest> requests;
		synchronized (queue) {
			requests = new ArrayList(queue);
			queue.clear();
		}

		if (requests.isEmpty()) {
			trace("Nothing to resolve.");
			return Status.OK_STATUS;
		}

		trace("Resolving %d module(s).", requests.size());
		monitor.beginTask("Loading module descriptors ...", FULL_RESOLVE_WORK_UNITS);

		final MultiStatus errors = new MultiStatus(ModuleCore.PLUGIN_ID, IStatus.ERROR, "Failed to resolve some modules.", null);

		// ANTE RESOLVE
		int workUnit = (FULL_RESOLVE_WORK_UNITS - MAIN_RESOLVE_WORK_UNITS - POST_RESOLVE_WORK_UNITS) / requests.size();

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(ResolveJob.class.getClassLoader());
		try {
			// try to do something like
			// for(Imodule module : modules) {
			// state = Solution.getPerModuleState(module)
			// state.getResolver().getIvy();
			for (ResolveRequest request : requests) {
				trace("Resolving %s", request);
				// monitor.subTask(name);
				IProject project = request.getResolver().getProject();
				if (project == null) {
					// warn()
					monitor.worked(workUnit);
					continue;
				}
				if (!project.isAccessible()) {
					// trace()
					monitor.worked(workUnit);
					continue;
				}
			}

		} finally {
			Thread.currentThread().setContextClassLoader(cl);
		}

		// POST RESOLVE
		workUnit = POST_RESOLVE_WORK_UNITS / requests.size();
		monitor.setTaskName("Performing post resolve tasks ...");
		for (ResolveRequest request : requests) {
			if (request.getStatus().isOK()) {
				monitor.setTaskName("Performing post resolve tasks ...");
				request.getResolver().postResolve();
			}
			monitor.worked(workUnit);
		}
		return Status.OK_STATUS;
	}

	private void trace(String message, Object... args) {
		if (!VERBOSE) {
			return;
		}
		System.out.printf("[" + this.getClass().getSimpleName() + "] " + message + System.lineSeparator(), args);
	}

}
