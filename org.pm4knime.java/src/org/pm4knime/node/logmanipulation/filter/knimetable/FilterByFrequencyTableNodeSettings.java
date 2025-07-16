package org.pm4knime.node.logmanipulation.filter.knimetable;

import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Section;
import org.knime.core.webui.node.dialog.defaultdialog.widget.NumberInputWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.validation.NumberInputWidgetValidation.MinValidation.IsNonNegativeValidation;
import org.pm4knime.util.defaultnode.DefaultTableNodeSettings;


@SuppressWarnings("restriction")
public final class FilterByFrequencyTableNodeSettings extends DefaultTableNodeSettings {

	 
	public static interface ExtendedDialogLayout extends DialogLayoutWithTime {
	    
		
	    @Section(title = "Filtering Settings")
        @After(EventLogClassifiers.class)
        interface Settings {
	    	
        }   
	     
	 }
	
	
	@Widget(title = "Keep", description = "If checked, the traces/trace variants that meet the filtering threshold will be kept.\r\n"
			+ "        Otherwise, the traces/trace variants that meet the filtering threshold will removed.")
	@Layout(ExtendedDialogLayout.Settings.class)
	boolean m_isKeep = true;
	
	@Widget(title = "Trace Variant Filtering", description = "If checked, the filtering will be applied on the trace variant level; i.e., trace variants that meet the filtering threshold will be kept/removed.\r\n"
			+ "        If this option is disabled, the filtering threshold is used to set the percentage of traces to be kept/removed.")
	@Layout(ExtendedDialogLayout.Settings.class)
	boolean m_isForSingleTV = true;
	
	@Widget(title = "Filtering Threshold", description = "The filtering threshold. It accepts both relative and absolute values (relative values between 0 and 1.0 and absolute integers greater than 1).")
	@Layout(ExtendedDialogLayout.Settings.class)
	@NumberInputWidget(minValidation=IsNonNegativeValidation.class)
	double m_threshold = 0.2;
	

}
