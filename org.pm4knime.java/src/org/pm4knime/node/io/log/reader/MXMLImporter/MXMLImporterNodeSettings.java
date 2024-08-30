package org.pm4knime.node.io.log.reader.MXMLImporter;

import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.setting.filechooser.FileChooser;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.pm4knime.node.io.log.reader.XesImporter.XesImporterNodeModel;

@SuppressWarnings("restriction")
public class MXMLImporterNodeSettings implements DefaultNodeSettings {

	@Widget(title = "File Location", description = "Path to the file to read.")
	public FileChooser m_file = new FileChooser();

	@Widget(title = "Read Method", description = "Method to read the file.")
	@ChoicesWidget(choices = readMethodChoicesProvider.class)
	String readMethod = readMethodList[0];

	public static final String[] readMethodList = XesImporterNodeModel.getCFG_METHODS();
	
	public static class readMethodChoicesProvider implements ChoicesProvider {
		@Override
		public String[] choices(final DefaultNodeSettingsContext context) {
			return readMethodList;
		}
	}
}
