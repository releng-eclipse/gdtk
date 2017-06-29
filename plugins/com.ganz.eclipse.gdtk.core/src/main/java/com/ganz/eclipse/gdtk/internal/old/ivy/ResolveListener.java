package com.ganz.eclipse.gdtk.internal.old.ivy;

import org.apache.ivy.core.event.IvyEvent;
import org.apache.ivy.core.event.IvyListener;
import org.apache.ivy.plugins.repository.TransferEvent;
import org.apache.ivy.plugins.repository.TransferListener;
import org.eclipse.core.runtime.IProgressMonitor;

public class ResolveListener implements IvyListener, TransferListener {

	public ResolveListener(final IProgressMonitor monitor, int step) {

	}

	@Override
	public void transferProgress(TransferEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void progress(IvyEvent event) {
		// TODO Auto-generated method stub

	}

}
