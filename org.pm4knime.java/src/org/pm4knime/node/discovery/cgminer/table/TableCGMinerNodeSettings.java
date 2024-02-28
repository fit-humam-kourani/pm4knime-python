package org.pm4knime.node.discovery.cgminer.table;

import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings.DefaultNodeSettingsContext;
import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Section;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.NumberInputWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.pm4knime.node.discovery.cgminer.CGMinerNodeModel;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings.DialogLayout;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings.DialogLayout.MainDropdownSection;


@SuppressWarnings("restriction")
public final class TableCGMinerNodeSettings extends DefaultTableMinerSettings {
	 
	public static interface CGMinerDialogLayout extends DialogLayout{
	    		
	    @Section(title = "Causal Graph Miner Settings")
        @After(MainDropdownSection.class)
        interface Settings {
	    	
        }        
	}

	@Widget(title = "Minimal activity frequency", description = "An activity will be included if it occurs in at least x% of cases; set to 0 to include all activities.")
	@Layout(CGMinerDialogLayout.Settings.class)
	@NumberInputWidget(min = 0, max = 100)
	int filter_a = 0;
	
	@Widget(title = "Minimal trace variant frequency", description = "A trace variant will be included if it covers at least x% of cases; set to 0 to include all trace variants.")
	@Layout(CGMinerDialogLayout.Settings.class)
	@NumberInputWidget(min = 0, max = 100)
	int filter_t = 0;
	
	@Widget(title = "Strong causality threshold", description = "Lower bound for a strong causality between two activities.")
	@Layout(CGMinerDialogLayout.Settings.class)
	@NumberInputWidget(min = 0.0, max = 1.0)
	double t_certain = 0.4;
	
	@Widget(title = "Weak causality threshold", description = "Lower bound for a weak causality between two activities; >set to 100% to avoid uncertain edges.")
	@Layout(CGMinerDialogLayout.Settings.class)
	@NumberInputWidget(min = 0.0, max = 1.0)
	double t_uncertain = 0.3;
	
	@Widget(title = "Long-term dependency threshold", description = "Lower bound for a strong long-term causality between two activities.")
	@Layout(CGMinerDialogLayout.Settings.class)
	@NumberInputWidget(min = 0.0, max = 1.0)
	double t_longDep = 0.8;
	
	@Widget(title = "Causality weight threshold", description = "High values mean more emphasis on the split and join behavior of activities; low values mean more emphasis on the detection of concurrency and loops.")
	@Layout(CGMinerDialogLayout.Settings.class)
	@NumberInputWidget(min = 0.0, max = 1.0)
	double weight = 0.5;
	

}

