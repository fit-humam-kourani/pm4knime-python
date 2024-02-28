package org.pm4knime.node.discovery.ilpminer.Table;

import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Section;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.NumberInputWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings;
import org.pm4knime.settingsmodel.SMILPMinerParameter;


@SuppressWarnings("restriction")
public final class ILPMinerTableNodeSettings extends DefaultTableMinerSettings {
	
	public static interface ILPMinerDialogLayout extends DialogLayout{
	    
		
	    @Section(title = "ILP Miner Settings")
        @After(MainDropdownSection.class)
        interface Settings {
	    	
        }   

      
	}

	
	public static class ILPMinerChoicesProvider implements ChoicesProvider {
	    @Override
	    public String[] choices(final DefaultNodeSettingsContext context) {
	        return SMILPMinerParameter.CFG_FILTER_TYPES;
	    }
	}
	
	
	@Widget(title = "Filter Type", description = "The filter type includes the following choices:\r\n"
			+ "            <ul>\r\n"
			+ "            	<li>NONE: no filter.</li>\r\n"
			+ "            	<li>SEQUENCE_ENCODING: the sequence encoding filter specifies at what level a branch should be cut off.</li>\r\n"
			+ "            	<li>SLACK_VAR: the slack variable filter specifies what portion of constraints should be filtered out.</li>\r\n"
			+ "            </ul>")
	@Layout(ILPMinerDialogLayout.Settings.class)
	@ChoicesWidget(choices = ILPMinerChoicesProvider.class)
	String m_filterType = SMILPMinerParameter.CFG_FILTER_TYPES[0];
	
	
	@Widget(title = "Noise Threshold", description = "Threshold for filtering out noise.")
	@Layout(ILPMinerDialogLayout.Settings.class)
	@NumberInputWidget(min = 0.0, max = 1.0)
	double m_filterThreshold = 0.25;
	
	public static class ObjectiveFunctionChoicesProvider implements ChoicesProvider {
	    @Override
	    public String[] choices(final DefaultNodeSettingsContext context) {
	        return SMILPMinerParameter.CFG_LPOBJ_TYPES;
	    }
	}
	
	@Widget(title = "Objective Function", description = "The objective function for ILP. The following options are supported: \r\n"
			+ "      	    <ul>\r\n"
			+ "      	    	<li>WEIGHTED_ABSOLUTE_PARIKH: weighted parikh values, using absolute frequencies.</li>\r\n"
			+ "      	    	<li>WEIGHTED_RELATIVE_PARIKH: weighted parikh values, using relative frequencies.</li>\r\n"
			+ "      	    	<li>UNWEIGHTED_PARIKH: unweighted parikh values.</li>\r\n"
			+ "      	    	<li>MINIMIZE_ARCS: minimize arcs.</li>\r\n"
			+ "      	    </ul>")
	@Layout(ILPMinerDialogLayout.Settings.class)
	@ChoicesWidget(choices = ObjectiveFunctionChoicesProvider.class)
	String m_lpObj = SMILPMinerParameter.CFG_LPOBJ_TYPES[0];
	
	
	public static class VTChoicesProvider implements ChoicesProvider {
	    @Override
	    public String[] choices(final DefaultNodeSettingsContext context) {
	        return SMILPMinerParameter.CFG_LPVAR_TYPES;
	    }
	}
	
	@Widget(title = "Variable Distribution", description = "Setting LP variable. The following options are supported: \r\n"
			+ "            <ul>\r\n"
			+ "            	<li>DUAL: two variables per event.</li>\r\n"
			+ "            	<li>HYBRID: one variable per event, two for an event which is potentially in a self loop.</li>\r\n"
			+ "            	<li>SINGLE: one variable per event.</li>\r\n"
			+ "            </ul>")
	@Layout(ILPMinerDialogLayout.Settings.class)
	@ChoicesWidget(choices = VTChoicesProvider.class)
	String m_lpVar = SMILPMinerParameter.CFG_LPVAR_TYPES[0];
	
	public static class DSChoicesProvider implements ChoicesProvider {
	    @Override
	    public String[] choices(final DefaultNodeSettingsContext context) {
	        return SMILPMinerParameter.CFG_DS_TYPES;
	    }
	}
	
	@Widget(title = "Discovery Strategy", description = "The Discovery Strategy. The following options are supported: \r\n"
			+ "            <ul>\r\n"
			+ "            	<li>CAUSAL_FLEX_HEUR: mine a place per causal relation (flexible heuristics miner).</li>\r\n"
			+ "            	<li>TRANSITION_PAIR: mine a connecting place between each pair of transitions.</li>\r\n"
			+ "            </ul>")
	@Layout(ILPMinerDialogLayout.Settings.class)
	@ChoicesWidget(choices = DSChoicesProvider.class)
	String m_ds = SMILPMinerParameter.CFG_DS_TYPES[0];

}
