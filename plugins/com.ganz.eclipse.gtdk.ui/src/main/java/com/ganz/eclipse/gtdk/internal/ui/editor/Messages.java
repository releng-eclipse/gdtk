package com.ganz.eclipse.gtdk.internal.ui.editor;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.ganz.eclipse.gtdk.internal.ui.editor.messages"; //$NON-NLS-1$
	public static String MissingResourcePage_0;
	public static String MissingResourcePage_title;
	public static String MissingResourcePage_unableToOpen;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
