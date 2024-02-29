package org.pm4knime.node.logmanipulation.merge.table;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;


@SuppressWarnings("restriction")
public final class MergeTableNodeSettings implements DefaultNodeSettings {

	
	public static final class StringColumnChoices implements ChoicesProvider {

	    @Override
	    public String[] choices(final DefaultNodeSettingsContext context) {
	        // Assuming context.getDataTableSpecs() returns an array of Object or a more generic type
	        Object specObj = context.getPortObjectSpecs()[0];

	        if (specObj instanceof DataTableSpec) { // Check if the object is an instance of DataTableSpec
	            DataTableSpec specs = (DataTableSpec) specObj;

	            return specs.stream()
	                    .filter(s -> s.getType().isCompatible(StringValue.class))
	                    .map(DataColumnSpec::getName)
	                    .toArray(String[]::new);
	        } else {
	            // Handle the case where specObj is not an instance of DataTableSpec
	            // For example, log an error or throw a more descriptive exception
	            System.err.println("Expected a DataTableSpec but received a different type: " + specObj.getClass().getSimpleName());
	            return new String[0];
	        }
	    }
	}
		
	
	@Widget(title = "Trace Classifier First Table", description = "The column to be used as a trace classifier for the first event table.")
    @ChoicesWidget(choices = StringColumnChoices.class)
	public String t_classifier_0;
	
	@Widget(title = "Trace Classifier Second Table", description = "The column to be used as a trace classifier for the second event table.")
	@ChoicesWidget(choices = StringColumnChoices.class)
	public String t_classifier_1;
	
	public static final String[]  CFG_TRACE_STRATEGY = {"Separate Traces",  "Ignore Second Trace", "Merge Traces"};
	
	public static class StrategyChoicesProvider implements ChoicesProvider {
        @Override
        public String[] choices(final DefaultNodeSettingsContext context) {
            return CFG_TRACE_STRATEGY;
        }
    }
	
	@Widget(title = "Merging Strategy", description = "The merging strategy. The following strategies are available:\r\n"
			+ "        <ul>\r\n"
			+ "        	<li>Separate Traces: traces with the same caseID are added to the merged event table as separate traces.  \r\n"
			+ "        	</li>\r\n"
			+ "        	<li>Ignore Second Trace: for traces with the same caseID, only the traces from the first event table are added to the merged event table.</li>\r\n"
			+ "	        <li>Merge Traces: traces with the same caseID are merge into a single trace containing all events from these traces.\r\n"
			+ "            </li>\r\n"
			+ "        </ul>")
    @ChoicesWidget(choices = StrategyChoicesProvider.class)
	public String m_strategy = CFG_TRACE_STRATEGY[0];

	
}
