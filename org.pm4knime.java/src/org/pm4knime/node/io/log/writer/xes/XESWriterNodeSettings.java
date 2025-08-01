package org.pm4knime.node.io.log.writer.xes;

import org.knime.core.webui.node.dialog.defaultdialog.widget.FileWriterWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.pm4knime.util.NodeSettingsUtils;
import org.pm4knime.util.defaultnode.WriterNodeSettings;

@SuppressWarnings("restriction")
public final class XESWriterNodeSettings extends WriterNodeSettings {

	@Widget( 
        title = "Output location (path and file name)", 
        description = """
                Specify the full path to where the file shall be written. The location can be either an operating system-dependent path on the local machine or a KNIME URL.
                The folder or workflow group in which the output file shall be written has to exist.
                """ 
    )
	@FileWriterWidget()
	String m_outputFile = NodeSettingsUtils.getPathInUserHomeDir("xes_file.xes");
	
	@Widget(title = "Compress output file (gz)", description = "Choose whether to compress the output file or not. This option is disabled by default.")
	boolean m_compressWithGzipChecker = false;

	public String getExtension() {
		return "";
	}
	
}

