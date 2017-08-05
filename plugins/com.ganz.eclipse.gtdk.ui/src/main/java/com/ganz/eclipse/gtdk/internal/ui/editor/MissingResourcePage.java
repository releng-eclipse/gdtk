package com.ganz.eclipse.gtdk.internal.ui.editor;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class MissingResourcePage extends Page {
	MissingResourcePage(MultiPageEditor editor) {
		super(editor, Messages.MissingResourcePage_0, Messages.MissingResourcePage_title);
	}

	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		Composite composite = managedForm.getToolkit().createComposite(form);
		composite.setLayout(new GridLayout());
		IPersistableElement persistable = getEditorInput().getPersistable();
		String text;
		// if (persistable instanceof IFileEditorInput) {
		// IFile file = ((IFileEditorInput) persistable).getFile();
		// } else {
		text = Messages.MissingResourcePage_unableToOpen;
		// }
		form.setText(text);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

}
