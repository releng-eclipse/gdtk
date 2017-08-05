package com.ganz.eclipse.gtdk.internal.ui.editor.module;

import org.eclipse.ui.PartInitException;

import com.ganz.eclipse.gtdk.internal.ui.editor.MultiPageEditor;
import com.ganz.eclipse.gtdk.ui.ModuleUI;

public class Editor extends MultiPageEditor {

	protected void addEditorPages() {
		try {
			addPage(new OverviewPage(this));
		} catch (PartInitException e) {
			ModuleUI.log(e);
		}
		// addSourcePage
	}

}
