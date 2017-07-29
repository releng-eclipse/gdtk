package com.ganz.eclipse.gtdk.internal.ui.view.dependencies;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

public class View extends ViewPart implements IZoomableWorkbenchPart, ISelectionListener {

	private GraphViewer viewer;

	public void init(IViewSite site) throws PartInitException {
		super.init(site);
		ISelectionService selectionService = site.getService(ISelectionService.class);
		selectionService.addSelectionListener(this);
		// site.getSelectionProvider().addSelectionChangedListener(this);
		// this.getSite().getService(api)
	}

	@Override
	public void createPartControl(Composite parent) {
		viewer = new GraphViewer(parent, SWT.BORDER);
		viewer.setContentProvider(new ContentProvider());
		viewer.setLabelProvider(new LabelProvider());
		viewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING));
		// FIXME
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("sample");
		Node node = null;
		try {
			node = NodeAdapter.adapt(project);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewer.setInput(node);

		fillToolBar();
	}

	@Override
	public void setFocus() {
		// TODO
	}

	private void fillToolBar() {
		ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(this);
		IActionBars bars = getViewSite().getActionBars();
		bars.getMenuManager().add(toolbarZoomContributionViewItem);
	}
	////////////////////////////////////////////////////////////
	// IZoomableWorkbenchPart

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}

	// @Override
	// public void selectionChanged(SelectionChangedEvent event) {
	// SelectionChangedEvent e = event;
	// SelectionChangedEvent t = e;
	// // TODO Auto-generated method stub
	//
	// }

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if (!part.getTitle().equals("Navigator")) {
			return;
		}
		if (!(selection instanceof IStructuredSelection) || selection.isEmpty()) {
			return;

		}
		Object selected = ((IStructuredSelection) selection).getFirstElement();
		if (selected instanceof IProject) {

			try {
				Node node = NodeAdapter.adapt((IProject) selected);
				viewer.setInput(node);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String x = "";

		// TODO Auto-generated method stub

	}

}
