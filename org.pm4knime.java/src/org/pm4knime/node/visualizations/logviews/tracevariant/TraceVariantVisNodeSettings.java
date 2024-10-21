package org.pm4knime.node.visualizations.logviews.tracevariant;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.data.time.localdatetime.LocalDateTimeCellFactory;
import org.knime.core.data.time.zoneddatetime.ZonedDateTimeCellFactory;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings.DialogLayout;


@SuppressWarnings("restriction")
public final class TraceVariantVisNodeSettings implements DefaultNodeSettings {

	@Widget(title = "Case ID", description = "The column that contains the case/trace identifiers.")
	@Layout(DialogLayout.MainDropdownSection.class)
    @ChoicesWidget(choices = StringColumnChoices.class)
	public String t_classifier;
	
	@Widget(title = "Activity", description = "The column that contains the activity/event identifiers.")
	@Layout(DialogLayout.MainDropdownSection.class)
    @ChoicesWidget(choices = StringColumnChoices.class)
	public String e_classifier;

	@Layout(DialogLayout.MainDropdownSection.class)
	@Widget(title = "Timestamp", description = "The column that contains the timestamps.")
	@ChoicesWidget(choices = TimeColumnChoices.class)
	String time_classifier;
	
	
	public static final class StringColumnChoices implements ChoicesProvider {

        @Override
        public String[] choices(final DefaultNodeSettingsContext context) {

            final DataTableSpec specs = context.getDataTableSpecs()[0];

            if (specs == null) {

                return new String[0];

            } else {

                return specs.stream() //

                    .filter(s -> s.getType().isCompatible(StringValue.class)) //

                    .map(DataColumnSpec::getName) //

                    .toArray(String[]::new);

            }

        }
	 }
	
	public static final class TimeColumnChoices implements ChoicesProvider {

        @Override
        public String[] choices(final DefaultNodeSettingsContext context) {

        	Object specObj = context.getPortObjectSpecs()[0];

        	if (specObj instanceof DataTableSpec) { // Check if the object is an instance of DataTableSpec
	            DataTableSpec specs = (DataTableSpec) specObj;
	            return specs.stream() //

	                    .filter(s -> s.getType().equals(ZonedDateTimeCellFactory.TYPE) || s.getType().equals(LocalDateTimeCellFactory.TYPE)) //

	                    .map(DataColumnSpec::getName) //

	                    .toArray(String[]::new);
	            
            } else {

            	System.err.println("Expected a DataTableSpec but received a different type: " + specObj.getClass().getSimpleName());
	            return new String[0];

            }

        }
	 }
}