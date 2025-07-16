package org.pm4knime.node.visualizations.logviews.tracevariant;

import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.choices.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.choices.column.CompatibleColumnsProvider.StringColumnsProvider;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings.DialogLayout;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings.TimeColumnsProvider;


@SuppressWarnings("restriction")
public final class TraceVariantVisNodeSettings implements DefaultNodeSettings {

	@Widget(title = "Case ID", description = "The column that contains the case/trace identifiers.")
	@Layout(DialogLayout.MainDropdownSection.class)
	@ChoicesProvider(value = StringColumnsProvider.class)
	public String t_classifier;
	
	@Widget(title = "Activity", description = "The column that contains the activity/event identifiers.")
	@Layout(DialogLayout.MainDropdownSection.class)
	@ChoicesProvider(value = StringColumnsProvider.class)
	public String e_classifier;

	@Layout(DialogLayout.MainDropdownSection.class)
	@Widget(title = "Timestamp", description = "The column that contains the timestamps.")
	@ChoicesProvider(value = TimeColumnsProvider.class)
	String time_classifier;
	
}