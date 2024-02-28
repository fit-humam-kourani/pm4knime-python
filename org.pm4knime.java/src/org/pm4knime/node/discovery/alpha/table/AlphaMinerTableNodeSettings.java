package org.pm4knime.node.discovery.alpha.table;

import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Section;
import org.knime.core.webui.node.dialog.defaultdialog.widget.NumberInputWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings;
import org.processmining.alphaminer.parameters.AlphaVersion;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;


@SuppressWarnings("restriction")
public final class AlphaMinerTableNodeSettings extends DefaultTableMinerSettings {



		 
		public static interface AlphaDialogLayout extends DialogLayout{
		    
			
		    @Section(title = "Alpha Miner Variant")
	        @After(MainDropdownSection.class)
	        interface Variants {
		    	
	        }   
		    
		    @Section(title = "Parameters for AlphaR")
	        @After(Variants.class)
	        interface AlphaRSection {
	        }

	        @Section(title = "Parameters for Alpha+")
	        @After(AlphaRSection.class)
	        interface AlphaPlus {
	        }

	      
		 }
		 
		public static final String[] variantList = {AlphaVersion.CLASSIC.toString() , AlphaVersion.PLUS.toString(),
					AlphaVersion.PLUS_PLUS.toString(), AlphaVersion.SHARP.toString(), AlphaVersion.ROBUST.toString()};
	
	    
	    public static class AlphaVersionChoicesProvider implements ChoicesProvider {
	        @Override
	        public String[] choices(final DefaultNodeSettingsContext context) {
	            return variantList;
	        }
	    }
	
		
	    @Widget(title = "Alpha Variant", description = "The Alpha miner variant to be applied. Five variants are supported: Alpha, Alpha+, Alpha++, Alpha#, and AlphaR.")
	    @Layout(AlphaDialogLayout.Variants.class)
	    @ChoicesWidget(choices = AlphaVersionChoicesProvider.class)
	    String m_variant = variantList[0];
	   
		
	    @Widget(title = "Noise Threshold for Least Frequency", description = "This parameter sets the least frequency noise threshold for the AlphaR variant.")
	    @Layout(AlphaDialogLayout.AlphaRSection.class)
		@NumberInputWidget(min = 0.0, max = 100)
		double m_noiseTLF = 0.0;
		
	    @Widget(title = "Noise Threshold for Most Frequency", description = "This parameter sets the most frequency noise threshold for the AlphaR variant.")
	    @Layout(AlphaDialogLayout.AlphaRSection.class)
		@NumberInputWidget(min = 0.0, max = 100)
		double m_noiseTMF = 0.0;
	    
	    @Widget(title = "Casual Threshold", description = "This parameter sets the casual threshold for the AlphaR variant.")
	    @Layout(AlphaDialogLayout.AlphaRSection.class)
		@NumberInputWidget(min = 0.0, max = 100)
		double m_casualTH = 0.0;
		 
	    
	    
	    @Widget(title = "Ignore the length of the loops", description = " This parameter sets whether the Alpha+ variant ignores loop length or not.")
	    @Layout(AlphaDialogLayout.AlphaPlus.class)
	    boolean m_ignore_ll = false;
	    

 
	}
