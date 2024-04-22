package org.pm4knime.node.conversion.log2table;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.persistence.field.Persist;
import org.knime.core.webui.node.dialog.defaultdialog.setting.columnfilter.LegacyColumnFilterPersistor;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ColumnChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings.DialogLayout;
import java.util.stream.Stream;

@SuppressWarnings("restriction")
public final class XLog2TableConverterNodeSettings implements DefaultNodeSettings {	
	
    @Persist(configKey = "column-filter", customPersistor = LegacyColumnFilterPersistor.class)
    @Widget(title = "Trace attribute set", description = "Choose the trace attributes to be included in the table.")
    @ChoicesWidget(choices = AllColumns.class)
    public String[] m_columnFilterTrace = new String[0];
    
    @Persist(configKey = "column-filter", customPersistor = LegacyColumnFilterPersistor.class)
    @Widget(title = "Event attribute set", description = "Choose the event attributes to be included in the table.")
    @ChoicesWidget(choices = AllColumns.class)
    public String[] m_columnFilterEvent = new String[0];
    
    static final class AllColumns implements ColumnChoicesProvider {

        @Override
        public DataColumnSpec[] columnChoices(final DefaultNodeSettingsContext context) {
        	return context.getDataTableSpec(0).map(DataTableSpec::stream)//
                .orElseGet(Stream::empty)//
                .toArray(DataColumnSpec[]::new);
        }
        
     }
    
    
}
  
