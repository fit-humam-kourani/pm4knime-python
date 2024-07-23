package org.pm4knime.util.defaultnode;

import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.apache.commons.lang3.StringUtils;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.webui.node.dialog.defaultdialog.setting.filechooser.FileChooser;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;


@SuppressWarnings("restriction")
public class ReaderNodeSettings implements DefaultNodeSettings {
	

    @Widget(title = "File Location", description = "Path to the file to read.")
    public FileChooser m_file = new FileChooser();


    public void validate() throws InvalidSettingsException {
    	if (StringUtils.isEmpty(m_file.getFSLocation().getPath())) {
            throw new InvalidSettingsException("Please specify a path to the file to read!");
        }
    }
}
