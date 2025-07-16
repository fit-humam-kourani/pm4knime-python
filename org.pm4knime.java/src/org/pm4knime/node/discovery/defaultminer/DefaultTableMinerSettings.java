package org.pm4knime.node.discovery.defaultminer;

import java.util.Arrays;
import java.util.Collection;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataValue;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.time.localdatetime.LocalDateTimeValue;
import org.knime.core.data.time.zoneddatetime.ZonedDateTimeValue;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Section;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.choices.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.choices.column.CompatibleColumnsProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.choices.column.CompatibleColumnsProvider.StringColumnsProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.choices.column.FilteredInputTableColumnsProvider;


@SuppressWarnings({"restriction"}) 
public class DefaultTableMinerSettings implements DefaultNodeSettings {	
	
	
	public interface DialogLayout {
		 
        @Section(title = "Event Log Classifiers")
        interface MainDropdownSection {
        }
      }
	
	@Widget(title = "Case ID", description = "The column that contains the case/trace identifiers.")
	@Layout(DialogLayout.MainDropdownSection.class)
	@ChoicesProvider(value = StringColumnsProvider.class)
	public String t_classifier;
	
	@Widget(title = "Activity", description = "The column that contains the activity/event identifiers.")
	@Layout(DialogLayout.MainDropdownSection.class)
	@ChoicesProvider(value = StringColumnsProvider.class)
	public String e_classifier;
	
	@Layout(DialogLayout.MainDropdownSection.class)
	@Widget(title = "Timestamp", description = "The column that contains the timestamps.")
	@ChoicesProvider(value = TimeColumnsProvider.class)
	public String time_classifier;
		
		
    public static class TimeColumnsProvider extends CompatibleColumnsProvider {

    	private static final Collection<Class<? extends DataValue>> TIME_TYPES = Arrays.asList(
    			ZonedDateTimeValue.class, LocalDateTimeValue.class
		);
    	
    	protected TimeColumnsProvider() {
			super(TIME_TYPES);
		}

    }
    
    public static class StringCellColumnsProvider implements FilteredInputTableColumnsProvider {

        @Override
        public boolean isIncluded(final DataColumnSpec col) {
            final var colType = col.getType();
            final var cellClass = colType.getCellClass();
            return cellClass.isAssignableFrom(StringCell.class);
        }
    }

}
