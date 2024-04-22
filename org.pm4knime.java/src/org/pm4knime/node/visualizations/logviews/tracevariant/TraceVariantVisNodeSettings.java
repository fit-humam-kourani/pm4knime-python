package org.pm4knime.node.visualizations.logviews.tracevariant;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings.DialogLayout;


@SuppressWarnings("restriction")
public final class TraceVariantVisNodeSettings implements DefaultNodeSettings {

	@Widget(title = "Trace Classifier", description = "The column to be used as a trace classifier.")
	@Layout(DialogLayout.MainDropdownSection.class)
    @ChoicesWidget(choices = StringColumnChoices.class)
	public String t_classifier;
	
	@Widget(title = "Event Classifier", description = "The column to be used as an event classifier.")
	@Layout(DialogLayout.MainDropdownSection.class)
    @ChoicesWidget(choices = StringColumnChoices.class)
	public String e_classifier;
	
	
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
}