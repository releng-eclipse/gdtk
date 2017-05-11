package com.ganz.eclipse.gtdk.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ModuleUI extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.ganz.eclipse.gdtk.ui"; //$NON-NLS-1$

	// The shared instance
	private static ModuleUI plugin;

	/**
	 * The constructor
	 */
	public ModuleUI() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static ModuleUI getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
