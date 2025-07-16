package org.pm4knime.util.defaultnode;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.persistence.api.Persist;
import org.knime.core.webui.node.dialog.defaultdialog.widget.NumberInputWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ValueSwitchWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.validation.NumberInputWidgetValidation.MinValidation.IsNonNegativeValidation;
import org.pm4knime.util.NodeSettingsUtils.ExistingOutputFileHandlingMode;



@SuppressWarnings("restriction")
public class WriterNodeSettings implements DefaultNodeSettings {
   
    @Widget( //
        title = "If output file exists", //
        description = "Specify whether the local output file should be overwritten if it exists or whether this node "
            + "should fail. If a KNIME URL is given, no check is done."//
    )
    @ValueSwitchWidget
	public ExistingOutputFileHandlingMode m_existingFileHandlingMode = ExistingOutputFileHandlingMode.FAIL;

}