package com.ganz.eclipse.gdtk.internal.util;

import java.text.MessageFormat;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.ganz.eclipse.gdtk.core.ModuleCore;

public class Logger {
	static private Logger inst;
	private ILog log;

	protected Logger() {
		log = ModuleCore.getDefault().getLog();
	}

	static public Logger getLog() {
		if (Logger.inst == null) {
			Logger.inst = new Logger();
		}
		return Logger.inst;
	}

	public static void log(IStatus status) {

	}

	public void info(String message) {
		info("{0}", message);

	}

	public void info(String pattern, Object... args) {
		String message;
		Status status;
		message = MessageFormat.format(pattern, args);
		status = new Status(Status.INFO, ModuleCore.PLUGIN_ID, message);
		log.log(status);
		status = null;

	}

	public void error(String message) {
		Status status;
		status = new Status(Status.ERROR, ModuleCore.PLUGIN_ID, message);
		log.log(status);
		status = null;
	}

	public void error(String message, Throwable t) {
		Status status;
		status = new Status(Status.ERROR, ModuleCore.PLUGIN_ID, message, t);
		log.log(status);
		status = null;
	}

	public void error(String pattern, Object... args) {
		String message;
		Status status;
		message = MessageFormat.format(pattern, args);
		status = new Status(Status.INFO, ModuleCore.PLUGIN_ID, message);
		log.log(status);
		status = null;
	}
}
