package org.pm4knime.node.conversion.hpn2table;

import java.util.ArrayList;

import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.TextInputWidget;

@SuppressWarnings("restriction")
public final class HybridPetriNet2TableConverterNodeSettings implements DefaultNodeSettings {
	 
	public static interface HybridPetriNet2TableDialogLayout {
	    		
	    interface RowIdentifier {
	    	
        }
	    
	    @After(RowIdentifier.class)
	    interface ColumnName {

	    }
	}
	
	public static class RowIdentifierChoicesProvider implements ChoicesProvider {
        @Override
        public String[] choices(final DefaultNodeSettingsContext context) {
        	HybridPetriNet2TableConverterNodeModel node = new HybridPetriNet2TableConverterNodeModel(HybridPetriNet2TableConverterNodeSettings.class);
        	ArrayList<String> rowList = new ArrayList<>(1);
            rowList.add(node.DEFAULT_ROWKEY.toString());
            String rowListArray[] = rowList.toArray(new String[rowList.size()]);
            return rowListArray;
        }
    }

	@Widget(title = "Row Identifier", description = "Identifier for the row generated")
	@Layout(HybridPetriNet2TableDialogLayout.RowIdentifier.class)
	@ChoicesWidget(choices = RowIdentifierChoicesProvider.class)
	String m_row_identifier;
	
	@Widget(title = "Column Name", description = "Name of the column generated")
	@Layout(HybridPetriNet2TableDialogLayout.ColumnName.class)
	@TextInputWidget
	String m_column_name = "Hybrid Petri Net";
}