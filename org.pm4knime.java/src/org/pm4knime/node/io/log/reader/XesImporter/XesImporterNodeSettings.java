package org.pm4knime.node.io.log.reader.XesImporter;

import java.util.Arrays;
import java.util.List;

import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.setting.fileselection.FileSelection;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.choices.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.choices.StringChoicesProvider;

@SuppressWarnings("restriction")
public class XesImporterNodeSettings implements DefaultNodeSettings {

	@Widget(title = "File Location", description = "Path to the file to read.")
	public FileSelection m_file = new FileSelection();

	public static final List<String> readMethodList = Arrays.asList(XesImporterNodeModel.CFG_METHODS);
	
	@Widget(title = "Read Method", description = "The method to read an event log. "
			+ "There are two methods available: Open Naive and IEEE Lenient. "
			+ "Open Naive opens XES using the standard reading method. "
			+ "IEEE Lenient can read a lenient IEEE XES log from a plain XML serialization.")
	@ChoicesProvider(value = readMethodChoicesProvider.class)
	String readMethod = readMethodList.get(0);

	public static class readMethodChoicesProvider implements StringChoicesProvider {
		@Override
		public List<String> choices(final DefaultNodeSettingsContext context) {
			return readMethodList;
		}
	}
}
