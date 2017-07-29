package com.ganz.eclipse.gdtk.internal.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ivy.Ivy;
import org.apache.ivy.core.module.descriptor.ModuleDescriptor;
import org.apache.ivy.core.module.id.ModuleRevisionId;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.ganz.eclipse.gdtk.core.IModule;
import com.ganz.eclipse.gdtk.core.ModuleCore;
import com.ganz.eclipse.gdtk.core.ModuleException;
import com.ganz.eclipse.gdtk.internal.core.ModuleModelManager.PerProjectInfo;
import com.ganz.eclipse.gdtk.internal.core.ModuleProject;
import com.ganz.eclipse.gdtk.internal.ivy.Proxy;
import com.ganz.eclipse.gdtk.internal.util.Logger;

public class ResolveJob extends Job {
	private static String NAME = "Resolve Job";
	private static boolean VERBOSE = true;
	private static String DEBUG_OPTION_NAME = ModuleCore.PLUGIN_ID + "debug/resolve";
	private static int SCHEDULE_DELAY = 1000;
	private static int LOAD = 100;

	private final Map<String, ModuleProject> queue;

	private static ResolveJob instance;

	public ResolveJob() {
		super(NAME);
		queue = new HashMap<String, ModuleProject>();
		setUser(false);
		setRule(ResourcesPlugin.getWorkspace().getRuleFactory().buildRule());
	}

	public static ResolveJob getInstance() {
		if (instance == null) {
			instance = new ResolveJob();
		}
		return instance;
	}

	public void addModule(ModuleProject moduleProject) {
		synchronized (queue) {
			queue.put(moduleProject.getProject().getName(), moduleProject);
		}
		schedule(SCHEDULE_DELAY);
		// this.run(new NullProgressMonitor());

	}

	public void addModule(IModule module) {
		addModule((ModuleProject) module);
	}

	protected IStatus run(IProgressMonitor monitor) {
		try {
			doResolve(monitor);
			return Status.OK_STATUS;
		} catch (OperationCanceledException e) {
			return Status.CANCEL_STATUS;
		} catch (ModuleException e) {
			return e.getStatus();
		}
	}

	private void doResolve(IProgressMonitor monitor) throws ModuleException, OperationCanceledException {
		List<ModuleProject> modules;
		synchronized (this.queue) {
			modules = new ArrayList<ModuleProject>(queue.values());
			this.queue.clear();
		}
		if (modules.isEmpty()) {
			return;
		}
		final MultiStatus errors = new MultiStatus(ModuleCore.PLUGIN_ID, IStatus.ERROR,
				"Some projects failed to resolve.", null);

		// the unit of work burned to parse each module descriptor
		int work = LOAD / modules.size();

		// ivy will use the SaxParserFactory which will require to instantiate
		// the xerces parser, one of the dependencies of this plugin and as
		// such available in the current class loader
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(ResolveJob.class.getClassLoader());
		try {
			for (ModuleProject module : modules) {
				if (!module.getProject().isAccessible()) {
					trace("Skipping closed or missing project '%s'", module.getProject());
					monitor.worked(work);
					continue;
				}
				PerProjectInfo info = module.getPerProjectInfo();
				Ivy ivy;
				try {
					ivy = info.getIvyInstance();
				} catch (ModuleException e) {
					Status error = new Status(IStatus.ERROR, ModuleCore.PLUGIN_ID,
							"Failed to initialize Ivy for '" + module.getProject() + "' project.", e);
					errors.add(error);
					Logger.log(error);
					monitor.worked(work);
					continue;
				}
				try {
					ModuleDescriptor md = info.getModuleDescriptor();
					ModuleRevisionId mrid = md.getModuleRevisionId();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} finally {
			Thread.currentThread().setContextClassLoader(cl);
		}

		for (ModuleProject module : modules) {
			launchResolveThread(module, monitor);
		}

	}

	private void launchResolveThread(ModuleProject moduleProject, IProgressMonitor monitor) {
		final Proxy resolver = new Proxy();
		Runnable thread = new Runnable() {
			public void run() {
				resolver.resolve(moduleProject, monitor);
			}
		};

		Executor executor = new Executor();
		executor.execute(thread, moduleProject, monitor);

	}

	private void trace(String message, Object... args) {
		if (!VERBOSE) {
			return;
		}
		System.out.printf("[" + this.getClass().getSimpleName() + "] " + message + System.lineSeparator(), args);
	}

	class Executor {
		public void execute(Runnable runnable, ModuleProject project, IProgressMonitor monitor) {
			Ivy ivy = null;
			try {
				ivy = project.getPerProjectInfo().getIvyInstance();
			} catch (ModuleException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Thread thread = new Thread(runnable);
			thread.setName("some name");
			thread.start();

			while (true) {
				try {
					thread.join(100);
				} catch (InterruptedException e) {
					ivy.interrupt(thread);
				}
				if (!thread.isAlive()) {
					return;
				}
				if (monitor.isCanceled()) {
					ivy.interrupt(thread);

				}
			}
		}
	}

}
