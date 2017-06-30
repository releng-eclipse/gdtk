package com.ganz.eclipse.gdtk.internal.listener;

import java.io.File;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.launching.AbstractVMRunner;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.mylyn.tasks.core.ITask;
import org.eclipse.mylyn.tasks.core.ITaskActivationListener;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.progress.UIJob;

import com.ganz.eclipse.gdtk.core.ModuleCore;
import com.ganz.eclipse.gdtk.internal.util.Logger;

public class TaskActivationListener implements ITaskActivationListener {

	
	private class RestartWorkbenchJob extends UIJob {
		private static final String PROP_VM = "eclipse.vm"; //$NON-NLS-1$

		private static final String PROP_VMARGS = "eclipse.vmargs"; //$NON-NLS-1$

		private static final String PROP_COMMANDS = "eclipse.commands"; //$NON-NLS-1$

		private static final String PROP_EXIT_CODE = "eclipse.exitcode"; //$NON-NLS-1$

		private static final String PROP_EXIT_DATA = "eclipse.exitdata"; //$NON-NLS-1$

		private static final String CMD_DATA = "-data"; //$NON-NLS-1$

		private static final String CMD_VMARGS = "-vmargs"; //$NON-NLS-1$

		private static final String NEW_LINE = "\n"; //$NON-NLS-1$
		
		String taskKey;
		
		public RestartWorkbenchJob(ITask task) {
			super("Restart Workbench");
			setSystem(true);
			taskKey = task.getTaskKey();
			IWorkspace ws = ResourcesPlugin.getWorkspace();
			IPath path =ws.getRoot().getLocation();
			File file = path.toFile();
			File wsParent = file.getParentFile();
			File wsTaskDir = new File(wsParent, taskKey.toLowerCase());
			taskKey = wsTaskDir.getAbsolutePath();
		}

		@Override
		public IStatus runInUIThread(IProgressMonitor monitor) {
			Logger.getInstance().info("Restarting workbench");
			restart(taskKey);
			return new Status(IStatus.OK, ModuleCore.PLUGIN_ID, IStatus.OK, "", null);
		}
		
		/**
		 * Restart the workbench using the specified path as the workspace location.
		 *
		 * @param path
		 *            the location
		 * @since 3.3
		 */
		private void restart(String path) {
			String command_line = buildCommandLine(path);
			if (command_line == null) {
				return;
			}
//
//			String command_line = "\"C:\\Program Files\\Java\\jdk1.8.0_71\\bin\\javaw.exe\" -agentlib:jdwp=transport=dt_socket,suspend=y,address=localhost:54920 -Declipse.pde.launch=true -Dfile.encoding=Cp1252 -classpath \"C:\\work\\tool\\eclipse\\.p2\\pool\\plugins\\org.eclipse.equinox.launcher_1.3.201.v20161025-1711.jar\" org.eclipse.equinox.launcher.Main -launcher \"C:\\work\\ganz\\tool\\eclipse\\devel\\neon\\eclipse.exe\" -name Eclipse -showsplash 600 -product org.eclipse.platform.ide -data \"C:\\work\\ganz\\proj\\releng\\eclipse\\master\\..\\runtime\\studio\" -configuration file:C:/work/ganz/proj/releng/eclipse/master/.metadata/.plugins/org.eclipse.pde.core/Studio/ -dev file:C:/work/ganz/proj/releng/eclipse/master/.metadata/.plugins/org.eclipse.pde.core/Studio/dev.properties -os win32 -ws win32 -arch x86_64 -nl en_US -consoleLog -debug";
//			
			System.setProperty(PROP_EXIT_CODE, Integer.toString(24));
			System.setProperty(PROP_EXIT_DATA, command_line);
			PlatformUI.getWorkbench().restart();
		}
		
		/**
		 * Create and return a string with command line options for eclipse.exe that
		 * will launch a new workbench that is the same as the currently running
		 * one, but using the argument directory as its workspace.
		 *
		 * @param workspace
		 *            the directory to use as the new workspace
		 * @return a string of command line options or null on error
		 */
		private String buildCommandLine(String workspace) {
			String property = System.getProperty(PROP_VM);
//			if (property == null) {
//				MessageDialog
//						.openError(
//								window.getShell(),
//								IDEWorkbenchMessages.OpenWorkspaceAction_errorTitle,
//								NLS
//										.bind(
//												IDEWorkbenchMessages.OpenWorkspaceAction_errorMessage,
//												PROP_VM));
//				return null;
//			}

			property = "C:\\Program Files\\Java\\jre1.8.0_71\\bin\\server\\jvm.dll";
			StringBuffer result = new StringBuffer(512);
			result.append("-vm");
			result.append(NEW_LINE);
			result.append(property);
			result.append(NEW_LINE);

			// append the vmargs and commands. Assume that these already end in \n
			String vmargs = System.getProperty(PROP_VMARGS);
			if (vmargs != null) {
				result.append(vmargs);
			}

			// append the rest of the args, replacing or adding -data as required
			property = System.getProperty(PROP_COMMANDS);
			if (property == null) {
				result.append(CMD_DATA);
				result.append(NEW_LINE);
				result.append(workspace);
				result.append(NEW_LINE);
			} else {
				// find the index of the arg to add/replace its value
				int cmd_data_pos = property.lastIndexOf(CMD_DATA);
				if (cmd_data_pos != -1) {
					cmd_data_pos += CMD_DATA.length() + 1;
					result.append(property.substring(0, cmd_data_pos));
					result.append(workspace);
					// append from the next arg
					int nextArg = property.indexOf("\n-", cmd_data_pos - 1); //$NON-NLS-1$
					if (nextArg != -1) {
						result.append(property.substring(nextArg));
					}
				} else {
					result.append(CMD_DATA);
					result.append(NEW_LINE);
					result.append(workspace);
					result.append(NEW_LINE);
					result.append(property);
				}
			}

			// put the vmargs back at the very end (the eclipse.commands property
			// already contains the -vm arg)
			if (vmargs != null) {
				if (result.charAt(result.length() - 1) != '\n') {
					result.append('\n');
				}
				result.append(CMD_VMARGS);
				result.append(NEW_LINE);
				result.append(vmargs);
			}

			return result.toString();
		}
	}
	private ITask task;
	@Override
	public void preTaskActivated(ITask task) {
		// TODO Auto-generated method stub
		this.task = task;
		String taskKey = task.getTaskKey();
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		IPath path =ws.getRoot().getLocation();
		File file = path.toFile();
		File wsParent = file.getParentFile();
		File wsTaskDir = new File(wsParent, taskKey.toLowerCase());
		if(!wsTaskDir.exists()) {
			wsTaskDir.mkdirs();
		}
		
	}

	@Override
	public void preTaskDeactivated(ITask task) {
		// TODO Auto-generated method stub
		this.task = task;
	}

	@Override
	public void taskActivated(ITask task) {
		// TODO Auto-generated method stub
		this.task = task;
		Logger.getInstance().info(String.format("Activating %s task", task.getTaskKey()));
		new RestartWorkbenchJob(task).schedule();
	}

	@Override
	public void taskDeactivated(ITask task) {
		// TODO Auto-generated method stub
		this.task = task;
		Logger.getInstance().info(String.format("Deactivate %s task", task.getTaskKey()));
	}

}
