package org.pm4knime.node.conversion.pn2table;

import java.util.ArrayList;

import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings.DefaultNodeSettingsContext;
import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.TextInputWidget;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings.DialogLayout;

@SuppressWarnings("restriction")
public final class PetriNet2TableConverterNodeSettings implements DefaultNodeSettings {
	 
	public static interface PetriNet2TableDialogLayout {
	    		
	    interface RowIdentifier {
	    	
        }
	    
	    @After(RowIdentifier.class)
	    interface ColumnName {

	    }
	}
	
	public static class RowIdentifierChoicesProvider implements ChoicesProvider {
        @Override
        public String[] choices(final DefaultNodeSettingsContext context) {
        	PetriNet2TableConverterNodeModel node = new PetriNet2TableConverterNodeModel(PetriNet2TableConverterNodeSettings.class);
        	ArrayList<String> rowList = new ArrayList<>(1);
            rowList.add(node.DEFAULT_ROWKEY.toString());
            String rowListArray[] = rowList.toArray(new String[rowList.size()]);
            return rowListArray;
        }
    }

	@Widget(title = "Row Identifier")
	@Layout(PetriNet2TableDialogLayout.RowIdentifier.class)
	@ChoicesWidget(choices = RowIdentifierChoicesProvider.class)
	String m_row_identifier = "Row0";
	
	@Widget(title = "Column Name")
	@Layout(PetriNet2TableDialogLayout.ColumnName.class)
	@TextInputWidget
	String m_column_name = "Petri Net";
}