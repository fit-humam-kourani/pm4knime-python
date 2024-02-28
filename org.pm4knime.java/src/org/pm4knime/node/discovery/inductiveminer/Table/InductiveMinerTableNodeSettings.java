package org.pm4knime.node.discovery.inductiveminer.Table;

import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Section;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.NumberInputWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings;

@SuppressWarnings("restriction")
public final class InductiveMinerTableNodeSettings extends DefaultTableMinerSettings {
	
	public static interface InductiveMinerDialogLayout extends DialogLayout{
	    
		
	    @Section(title = "Inductive Miner Settings")
        @After(MainDropdownSection.class)
        interface Settings {
	    	
        }   

      
	}
	 
	public static final String[] variantList = {"Inductive Miner - Base", 
			"Inductive Miner - Infrequent", 
			"Inductive Miner - Incompleteness", //
			"Inductive Miner - Life cycle" 
	};
	
	
	public static class InductiveMinerChoicesProvider implements ChoicesProvider {
	    @Override
	    public String[] choices(final DefaultNodeSettingsContext context) {
	        return variantList;
	    }
	}
	
	
	@Widget(title = "Inductive Miner Variant", description = "The variant of the Inductive Miner to be used.")
	@Layout(InductiveMinerDialogLayout.Settings.class)
	@ChoicesWidget(choices = InductiveMinerChoicesProvider.class)
	String m_variant = variantList[1];
	
	
	@Widget(title = "Noise Threshold for Least Frequency", description = "Threshold for filtering out noise. Accepted values: between 0.0 and 1.0.")
	@Layout(InductiveMinerDialogLayout.Settings.class)
	@NumberInputWidget(min = 0.0, max = 1.0)
	double m_noise = 0.2;


}
