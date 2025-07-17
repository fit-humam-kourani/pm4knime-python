package org.pm4knime.util.defaultnode;

import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.setting.fileselection.FileSelection;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;

@SuppressWarnings("restriction")
public class ReaderNodeSettings implements DefaultNodeSettings {
	
    @Widget(title = "File Location", description = "Path to the file to read.")
    public FileSelection m_file = new FileSelection();

}
