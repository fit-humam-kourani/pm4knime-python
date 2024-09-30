package org.pm4knime.node.io.log.reader.XesImporter;

import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.setting.filechooser.FileChooser;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;

@SuppressWarnings("restriction")
public class XesImporterNodeSettings implements DefaultNodeSettings {

	@Widget(title = "File Location", description = "Path to the file to read.")
	public FileChooser m_file = new FileChooser();

	@Widget(title = "Read Method", description = "The method to read an event log. "
			+ "There are two methods available: Open Naive and IEEE Lenient. "
			+ "Open Naive opens XES using the standard reading method. "
			+ "IEEE Lenient can read a lenient IEEE XES log from a plain XML serialization.")
	@ChoicesWidget(choices = readMethodChoicesProvider.class)
	String readMethod = readMethodList[0];

	public static final String[] readMethodList = XesImporterNodeModel.CFG_METHODS;

	public static class readMethodChoicesProvider implements ChoicesProvider {
		@Override
		public String[] choices(final DefaultNodeSettingsContext context) {
			return readMethodList;
		}
	}
}
