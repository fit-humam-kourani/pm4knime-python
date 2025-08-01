package org.pm4knime.node.logmanipulation.sample.knimetable;

import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Section;
import org.knime.core.webui.node.dialog.defaultdialog.widget.NumberInputWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.validation.NumberInputWidgetValidation.MinValidation.IsNonNegativeValidation;
import org.pm4knime.util.defaultnode.DefaultTableNodeSettings;


@SuppressWarnings("restriction")
public final class SampleLogTableNodeSettings extends DefaultTableNodeSettings {



	 
	public static interface ExtendedDialogLayout extends DialogLayoutWithTime {
	    
		
	    @Section(title = "Partitioning Settings")
        @After(EventLogClassifiers.class)
        interface Settings {
	    	
        }   
	     
	 }
	
	
	@Widget(title = "Use Percentage", description = "If checked, the sampling number is used as the percentage of traces to be kept. \r\n"
			+ "        Otherwise, the sampling number is used as the number of traces to be kept.")
	@Layout(ExtendedDialogLayout.Settings.class)
	boolean m_samplePref = true;
	
	@Widget(title = "Sampling Number", description = "If 'Use Percentage' is checked, the sampling number must be between 0.0 and 1.0 to set the percentage of traces to be kept. Otherwise, the sampling number must be a non-negative integer to set the number of traces to be kept.")
	@Layout(ExtendedDialogLayout.Settings.class)
	@NumberInputWidget(minValidation=IsNonNegativeValidation.class)
	double m_samplePercentage = 0.3;
	

}
