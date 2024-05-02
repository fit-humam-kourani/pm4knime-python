package org.pm4knime.node.conversion.pn2table;

import java.util.ArrayList;

import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.TextInputWidget;

@SuppressWarnings("restriction")
public final class PetriNet2TableConverterNodeSettings implements DefaultNodeSettings {
	 
	public static interface PetriNet2TableDialogLayout {
	    		
	    interface RowIdentifier {
	    	
        }
	    
	    @After(RowIdentifier.class)
	    interface ColumnName {

	    }
	}
	
//	public static class RowIdentifierChoicesProvider implements ChoicesProvider {
//        @Override
//        public String[] choices(final DefaultNodeSettingsContext context) {
//        	PetriNet2TableConverterNodeModel node = new PetriNet2TableConverterNodeModel(PetriNet2TableConverterNodeSettings.class);
//        	ArrayList<String> rowList = new ArrayList<>();
//            rowList.add(node.DEFAULT_ROWKEY.toString());
//            String rowListArray[] = rowList.toArray(new String[rowList.size()]);
//            return rowListArray;
//        }
//    }

	@Widget(title = "Row Identifier", description = "Identifier for the row generated")
	@Layout(PetriNet2TableDialogLayout.RowIdentifier.class)
//	@ChoicesWidget(choices = RowIdentifierChoicesProvider.class)
	@TextInputWidget
	String m_row_identifier = "row 0";
	
	@Widget(title = "Column Name", description = "Name of the column generated")
	@Layout(PetriNet2TableDialogLayout.ColumnName.class)
	@TextInputWidget
	String m_column_name = "Petri Net";
}