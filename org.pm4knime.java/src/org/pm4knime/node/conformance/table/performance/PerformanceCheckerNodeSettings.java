package org.pm4knime.node.conformance.table.performance;

import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;

@SuppressWarnings("restriction")
public final class PerformanceCheckerNodeSettings implements DefaultNodeSettings {
	
	public static interface PerformanceDialogLayout {
	    
        interface SynMoves {
	    	
        }   
	    
	    @After(SynMoves.class)
        interface UnreliableReplay {
        }
      
	 }

    
	@Widget(title = "Consider performance in syn moves", description = "Choose whether to consider syn moves. This option is enabled by default.")
	@Layout(PerformanceDialogLayout.SynMoves.class)
	boolean m_withSynMoveComp = true;
	
	@Widget(title = "Include unreliable replay results", description = "Choose whether to include unreliable replay results. This option is enabled by default.")
	@Layout(PerformanceDialogLayout.UnreliableReplay.class)
	boolean m_withUnreliableResultComp = true;

}    