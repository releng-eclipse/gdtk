package com.ganz.eclipse.gtdk.internal.ui.editor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

import com.ganz.eclipse.gtdk.ui.ModuleUI;

public abstract class MultiPageEditor extends FormEditor {
	protected boolean error;

	@Override
	protected void addPages() {
		// error = getAggregateModel() == null;
		// if (error) {
		try {
			addPage(new MissingResourcePage(this));
		} catch (PartInitException e) {
			ModuleUI.log(e);
		}
		// }
		// addEditorPages();
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	//////////////////////////////////////////////////////
	protected abstract void addEditorPages();

	public Object getAggregateModel() {
		return null;
	}
}
