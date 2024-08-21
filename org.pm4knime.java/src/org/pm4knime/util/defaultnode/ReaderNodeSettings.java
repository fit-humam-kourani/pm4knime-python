package org.pm4knime.util.defaultnode;

import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.setting.filechooser.FileChooser;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;

@SuppressWarnings("restriction")
public class ReaderNodeSettings implements DefaultNodeSettings {
	
    @Widget(title = "File Location", description = "Path to the file to read.")
    public FileChooser m_file = new FileChooser();

}
