package org.pm4knime.node.conformance.table.precision;

import java.util.Arrays;
import java.util.List;

import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Section;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.choices.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.choices.StringChoicesProvider;
import org.processmining.plugins.multietc.sett.MultiETCSettings;

@SuppressWarnings("restriction")
public final class PrecisionCheckerNodeSettings implements DefaultNodeSettings {
	
	public static interface PrecisionDialogLayout {
	    
		
	    @Section(title = "OrderedPresentation")
        interface OrderedPresentation {
	    	
        }   
	    
	    @Section(title = "Algorithm")
        @After(OrderedPresentation.class)
        interface Algorithm {
        }
      
	 }
	
	final static String ALIGN_1 = "1-Align Precision";
	final static String ALIGN_ALL = "All-Align Precision";
	final static String ALIGN_REPRE = "Representative-Align Precision";
	final static String ETC = "ETC Precision (no invisible/duplicates allowed)";
	
	public static final List<String> algorithmList = Arrays.asList(MultiETCSettings.ALGORITHM.toString() , ALIGN_1,
			ALIGN_ALL, ALIGN_REPRE, ETC);
	 
	public static class AlgorithmChoicesProvider implements StringChoicesProvider {
        @Override
        public List<String> choices(final DefaultNodeSettingsContext context) {
            return algorithmList;
        }
    }

    
	@Widget(title = "Ordered Representation", description = "The representation of the information in the event log (ordered sequences or multi-sets). This option is enabled by default.")
	@Layout(PrecisionDialogLayout.OrderedPresentation.class)
	boolean m_ignore_ll = true;

    @Widget(title = "Algorithm", description = "The algorithm to calculate the precision. The following options are available:\r\n"
    		+ "			<ul>\r\n"
    		+ "				<li>1-Align Precision.</li>\r\n"
    		+ "				<li>All-Align Precision.</li>\r\n"
    		+ "				<li>Representative-Align Precision.</li>\r\n"
    		+ "				<li>ETC Precision (no invisible/duplicates allowed). </li>\r\n"
    		+ "				</ul>")
    @Layout(PrecisionDialogLayout.Algorithm.class)
    @ChoicesProvider(value = AlgorithmChoicesProvider.class)
    String m_variant = ALIGN_1;

}