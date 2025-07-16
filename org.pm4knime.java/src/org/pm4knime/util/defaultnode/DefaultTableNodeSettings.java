package org.pm4knime.util.defaultnode;

import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Section;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.choices.ChoicesProvider;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings.StringCellColumnsProvider;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings.TimeColumnsProvider;


@SuppressWarnings({"restriction"}) 
public class DefaultTableNodeSettings implements DefaultNodeSettings {

	
	public interface DialogLayoutWithTime {
		 
        @Section(title = "Event Log Classifiers")
        interface EventLogClassifiers {
        }
      }

	
	@Layout(DialogLayoutWithTime.EventLogClassifiers.class)
	@Widget(title = "Case ID", description = "The column that contains the case/trace identifiers.")
    @ChoicesProvider(value = StringCellColumnsProvider.class)
	public String t_classifier;
	
	@Layout(DialogLayoutWithTime.EventLogClassifiers.class)
    @Widget(title = "Activity", description = "The column that contains the activity/event identifiers.")
	@ChoicesProvider(value = StringCellColumnsProvider.class)
	public String e_classifier;
	
	@Layout(DialogLayoutWithTime.EventLogClassifiers.class)
	@Widget(title = "Timestamp", description = "The column that contains the timestamps.")
	@ChoicesProvider(value = TimeColumnsProvider.class)
	public String time_classifier;

	
}
