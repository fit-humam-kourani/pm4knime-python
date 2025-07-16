package org.pm4knime.node.logmanipulation.merge.table;

import java.util.Arrays;
import java.util.List;

import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.choices.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.choices.StringChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.choices.column.CompatibleColumnsProvider.StringColumnsProvider;


@SuppressWarnings("restriction")
public final class MergeTableNodeSettings implements DefaultNodeSettings {
		
	
	@Widget(title = "Trace Classifier First Table", description = "The column to be used as a trace classifier for the first event table.")
	@ChoicesProvider(value = StringColumnsProvider.class)
	public String t_classifier_0;
	
	@Widget(title = "Trace Classifier Second Table", description = "The column to be used as a trace classifier for the second event table.")
	@ChoicesProvider(value = StringColumnsProvider.class)
	public String t_classifier_1;
	
	public static final List<String> CFG_TRACE_STRATEGY = Arrays.asList("Separate Traces",
			"Ignore Second Trace",
			"Merge Traces");
	
	public static class StrategyChoicesProvider implements StringChoicesProvider {
        @Override
        public List<String> choices(final DefaultNodeSettingsContext context) {
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
    @ChoicesProvider(value = StrategyChoicesProvider.class)
	public String m_strategy = CFG_TRACE_STRATEGY.get(0);

	
}
