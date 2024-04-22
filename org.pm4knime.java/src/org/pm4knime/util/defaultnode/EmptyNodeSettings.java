package org.pm4knime.util.defaultnode;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Section;
import org.knime.core.webui.node.dialog.defaultdialog.rule.Effect;
import org.knime.core.webui.node.dialog.defaultdialog.rule.Effect.EffectType;
import org.knime.core.webui.node.dialog.defaultdialog.rule.Signal;
import org.knime.core.webui.node.dialog.defaultdialog.rule.TrueCondition;


@SuppressWarnings("restriction")
public final class EmptyNodeSettings implements DefaultNodeSettings {
	
	interface NodeSignal {}
	
	public static interface EmptyLayout {
				
	    @Section(title = "This node has no settings!")
        interface Text {
	    	
        }   
	      
	 }
		
	@Layout(EmptyLayout.Text.class)
	@Signal(id = NodeSignal.class, condition = TrueCondition.class)
	@Effect(signals = NodeSignal.class, type = EffectType.HIDE)
	public static boolean dummy_var = true;
		
}