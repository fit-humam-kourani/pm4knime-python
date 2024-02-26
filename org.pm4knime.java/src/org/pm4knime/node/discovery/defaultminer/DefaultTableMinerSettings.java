package org.pm4knime.node.discovery.defaultminer;

import java.awt.Composite;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Section;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;


@SuppressWarnings({"restriction"}) 
public class DefaultTableMinerSettings implements DefaultNodeSettings {

	
	public interface DialogLayout {
		 
        @Section(title = "Event Log Classifiers")
        interface MainDropdownSection {
        }
      }
	
	@Widget(title = "Trace Classifier", description = "The column to be used as a trace classifier.")
	@Layout(DialogLayout.MainDropdownSection.class)
    @ChoicesWidget(choices = StringColumnChoices.class)
	String t_classifier;
	
	@Widget(title = "Event Classifier", description = "The column to be used as an event classifier.")
	@Layout(DialogLayout.MainDropdownSection.class)
    @ChoicesWidget(choices = StringColumnChoices.class)
	String e_classifier;
	
	public String getTraceClassifier() {
		return t_classifier;
	}
	
	public String getEventClassifier() {
		return e_classifier;
	}
	
	
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
