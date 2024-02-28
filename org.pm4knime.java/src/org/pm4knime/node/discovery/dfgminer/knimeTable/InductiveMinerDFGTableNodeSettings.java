package org.pm4knime.node.discovery.dfgminer.knimeTable;

import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.widget.NumberInputWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;


@SuppressWarnings({"restriction"}) 
public final class InductiveMinerDFGTableNodeSettings implements DefaultNodeSettings {

	 
	 @Widget(title = "Noise Threshold", description = "Threshold for filtering out noise.")
	 @NumberInputWidget(min = 0.0, max = 1.0)
	 double m_noiseThreshold = 0.8;	 

}

