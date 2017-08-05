package com.ganz.eclipse.gtdk.internal.ui.editor;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;

public class Page extends FormPage {
	private Control lastFocusControl;

	public Page(FormEditor editor, String id, String title) {
		super(editor, id, title);
		lastFocusControl = null;
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		// dynamically add focus listeners to all form's children so that last
		// control to obtain focus can be tracked
		IManagedForm managedForm = getManagedForm();
		if (managedForm != null) {
			addLastFocusListeners(managedForm.getForm());
		}
	}

	@Override
	public void dispose() {
		Control control = getPartControl();
		if (control != null && !control.isDisposed()) {
			Menu menu = control.getMenu();
			if (menu != null) {
				resetMenu(menu, control);
			}
		}
		super.dispose();
	}

	protected void createFormContent(IManagedForm managedForm) {
		// TODO
	}

	protected void createFormError(IManagedForm managedForm, String errorTitle, String errorMessage, Exception e) {
		// TODO
	}

	private void addLastFocusListeners(final Control control) {
		// TODO
	}

	private void resetMenu(Menu menu, Control control) {
		if (control instanceof Composite) {
			Control[] children = ((Composite) control).getChildren();
			for (Control child : children) {
				resetMenu(menu, child);
			}
		}
		Menu ctrlMenu = control.getMenu();
		if (ctrlMenu != null && ctrlMenu.equals(menu)) {
			control.setMenu(null);
		}
	}
}
