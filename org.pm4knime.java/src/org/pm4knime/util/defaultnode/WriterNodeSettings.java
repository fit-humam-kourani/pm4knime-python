package org.pm4knime.util.defaultnode;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.context.NodeCreationConfiguration;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.persistence.field.FieldNodeSettingsPersistor;
import org.knime.core.webui.node.dialog.defaultdialog.persistence.field.Persist;
import org.knime.core.webui.node.dialog.defaultdialog.widget.LocalFileWriterWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.NumberInputWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ValueSwitchWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.pm4knime.util.NodeSettingsUtils;
import org.pm4knime.util.NodeSettingsUtils.ExistingOutputFileHandlingMode;



@SuppressWarnings("restriction")
public class WriterNodeSettings implements DefaultNodeSettings {
	
	static final int DEFAULT_TIMEOUT_SECONDS = 30;
    
    @Widget( //
        title = "If output file exists", //
        description = "Specify whether the local output file should be overwritten if it exists or whether this node "
            + "should fail. If a KNIME URL is given, no check is done."//
    )
    @ValueSwitchWidget
	public
    ExistingOutputFileHandlingMode m_existingFileHandlingMode = ExistingOutputFileHandlingMode.FAIL;

    @Widget(title = "Report file generation timeout (seconds)",
            description = "If you run into timeouts when generating large files, try increasing the timeout.",
            advanced = true)
        @NumberInputWidget(min = 0)
        @Persist(optional = true, customPersistor = Timeout.class)
		public int m_timeoutSeconds = DEFAULT_TIMEOUT_SECONDS;

        private static final class Timeout implements FieldNodeSettingsPersistor<Integer> {

            private static final String KEY_TIMEOUT = "timeoutSeconds";

            // while in Labs, the node used this key
            private static final String LEGACY_KEY_TIMEOUT = "cefTimeoutSeconds";

            @Override
            public Integer load(final NodeSettingsRO settings) throws InvalidSettingsException {
                try {
                    return settings.getInt(LEGACY_KEY_TIMEOUT);
                } catch (final InvalidSettingsException e) { // NOSONAR
                    return settings.getInt(KEY_TIMEOUT, DEFAULT_TIMEOUT_SECONDS);
                }
            }

            @Override
            public void save(final Integer timeout, final NodeSettingsWO settings) {
                settings.addInt(KEY_TIMEOUT, timeout);
            }

            @Override
            public String[] getConfigKeys() {
                return new String[]{KEY_TIMEOUT};
            }
        }
}