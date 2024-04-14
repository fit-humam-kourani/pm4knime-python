package org.pm4knime.node.conversion.table2log;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.StringValue;
import org.knime.core.node.util.ColumnFilter;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.core.webui.node.dialog.defaultdialog.layout.Layout;

//import org.knime.core.webui.node.dialog.defaultdialog.layout.Inside;

import org.knime.core.webui.node.dialog.defaultdialog.layout.After;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesProvider;
import org.knime.core.webui.node.dialog.defaultdialog.widget.ChoicesWidget;
import org.knime.core.webui.node.dialog.defaultdialog.widget.Widget;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerSettings.DialogLayout;

@SuppressWarnings("restriction")
public final class Table2XLogConverterNodeSettings implements DefaultNodeSettings {
	
	public static interface Table2XLogDialogLayout extends DialogLayout {
		
		interface StandardOptions {
			
			interface CaseID {
			
			}
		
			@After(CaseID.class)
			interface EventClass {
				
			}
		
			@After(EventClass.class)
			interface LifeCycle {
			
			}
		
			@After(LifeCycle.class)
			interface TimeStamp {
			
			}
		}
		
		interface ChooseAttributesSet {
			
			interface TraceAttributes {
				
			}
		
			@After(TraceAttributes.class)
			interface EventAttributes {
				
			}	
		}
		
		interface ExpertChoice {
		
			interface XFactory {
				
			}
		
			@After(XFactory.class)
			interface ErrorHandling {
				
			}
			
			@After(ErrorHandling.class)
			interface SparseLog {
				
			}
		}
			
	}
	
	public static final class StringColumnChoices implements ChoicesProvider {

        @Override
        public String[] choices(final DefaultNodeSettingsContext context) {

            final DataTableSpec specs = context.getDataTableSpecs()[0];

            if (specs == null) {

                return new String[0];

            } else {

                return specs.stream() //

                    .filter(s -> s.getType().isCompatible(StringValue.class)) //

                    .map(DataColumnSpec::getName) //

                    .toArray(String[]::new);

            }

        }
	 }
	 
	@Widget(title = "Case ID", description = "Column to be used as a caseID in the event log")
	@Layout(DialogLayout.MainDropdownSection.class)
    @ChoicesWidget(choices = StringColumnChoices.class)
	public String case_id;
	
	@Widget(title = "Eventclass", description = "Column to be used as an eventID in the event log")
	@Layout(DialogLayout.MainDropdownSection.class)
    @ChoicesWidget(choices = StringColumnChoices.class)
	public String event_class;
	
	@Widget(title = "Life Cycle", description = "Column to be used for the life cycle attribute in the event log.")
	@Layout(DialogLayout.MainDropdownSection.class)
    @ChoicesWidget(choices = StringColumnChoices.class)
	public String life_cycle;
	
	@Widget(title = "Time Stamp", description = "Column to be used for the time stamp attribute in the event log. It should be in format of ZonedDateTime or DateTime; otherwise, an error will be thrown.")
	@Layout(DialogLayout.MainDropdownSection.class)
    @ChoicesWidget(choices = StringColumnChoices.class)
	public String time_stamp;
	
	@Widget(title = "Trace Attributes", description = "Table columns to be used as trace attributes. Please make sure the columns chosen in the \"Standard Options\" dialog window are included; \r\n"
			+ "            otherwise, an error will be thrown.")
	@Layout(DialogLayout.MainDropdownSection.class)
    public String trace_attributes;
	
	@Widget(title = "Event Attributes", description = "Table columns to be used as event attributes. Please make sure the columns chosen in the \"Standard Options\" dialog window are included; \r\n"
			+ "            otherwise, an error will be thrown.")
	@Layout(DialogLayout.MainDropdownSection.class)
    @ChoicesWidget(choices = StringColumnChoices.class)
	public String event_attributes;
	
	@Widget(title = "XFactory", description = "XFactory implementation that is used to create the log.")
	@Layout(DialogLayout.MainDropdownSection.class)
    @ChoicesWidget(choices = StringColumnChoices.class)
	public String xfactory;
	
	@Widget(title = "Error Handling", description = "The strategy for handling errors.")
	@Layout(DialogLayout.MainDropdownSection.class)
    @ChoicesWidget(choices = StringColumnChoices.class)
	public String error_handling;
	
	@Widget(title = "Sparse / Dense Log", description = "This affects how empty cells in the table are handled. \r\n"
			+ "          Some plug-ins require the log to be dense, i.e., all attributes are defined for each event. \r\n"
			+ "          In other cases, it might be more efficient or even required to only add attributes to events if the attributes actually contain data.")
	@Layout(DialogLayout.MainDropdownSection.class)
    @ChoicesWidget(choices = StringColumnChoices.class)
	public String sparse_log;
	
}