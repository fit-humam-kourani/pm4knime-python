package org.pm4knime.node.conversion.table2log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XLog;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.sort.BufferedDataTableSorter;
import org.knime.core.data.time.localdatetime.LocalDateTimeCellFactory;
import org.knime.core.data.time.zoneddatetime.ZonedDateTimeCellFactory;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.pm4knime.portobject.XLogPortObject;
import org.pm4knime.portobject.XLogPortObjectSpec;
import org.pm4knime.settingsmodel.SMTable2XLogConfig;

/**
 * <code>NodeModel</code> for the "CVS2XLogConverter" node. This node is coded without ProM plugin. 
 * The reasons are : 
 * Input:  DataTable 
 * Output: XLogPortObject
 * Parameters: a lot ! But in two kinds:
 *    1. to assign the caseId and eventID, timestamp according to the Spec from the cvs
 *    2. to choose the storage format of XLogPortObject
 * 
 * For later use, if we want to convert event log into cvs file, and then store them, what to do then?? 
 * By using the prom Plugin, we have the exporter and importer available. And then after this, we get the 
 * @author Kefang Ding
 */

@SuppressWarnings("restriction")
public class Table2XLogConverterNodeModel extends NodeModel {
    
	private static final NodeLogger logger = NodeLogger.getLogger(Table2XLogConverterNodeModel.class);
	// here we have optional item, but now just this two
//	public static final String CFG_KEY_CONFIG = "Table to event log converter config";
//	SMTable2XLogConfig m_config =  new SMTable2XLogConfig(CFG_KEY_CONFIG);
	XLogPortObject logPO;
	
	protected Table2XLogConverterNodeSettings m_settings = new Table2XLogConverterNodeSettings();

    private final Class<Table2XLogConverterNodeSettings> m_settingsClass;

    /**
     * Constructor for the node model.
     */
//    protected Table2XLogConverterNodeModel() {
//    
//        // TODO: Specify the amount of input and output ports needed.
//     
//    }

    public Table2XLogConverterNodeModel(Class<Table2XLogConverterNodeSettings> modelSettingsClass) {
		// TODO Auto-generated constructor stub
       super(new PortType[]{BufferedDataTable.TYPE}, new PortType[]{XLogPortObject.TYPE});
       m_settingsClass = modelSettingsClass;
	}

	/**
     * they must portOBject to PortObject, else not working.
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {
    	logger.info("Start : Convert DataTable to Event Log" );
        // TODO: accept input DataTable, use the configuration columnNames, 
    	// convert the data into XLog
    	// how to check the type for this?
    	BufferedDataTable tData = (BufferedDataTable) inData[0];
    	
    	// sort the table w.r.t. caseID column
    	List<String> m_inclList = new ArrayList<String>();
    	//m_inclList.add(m_config.getMCaseID().getStringValue());
    	m_inclList.add(m_settings.case_id);
    	// here we might need to make sure they mean this
    	boolean[] m_sortOrder = {true};
    	boolean m_missingToEnd = false;
    	boolean m_sortInMemory = false;
    	BufferedDataTableSorter sorter = new BufferedDataTableSorter(
    			tData, m_inclList, m_sortOrder, m_missingToEnd);
    	
        sorter.setSortInMemory(m_sortInMemory);
        BufferedDataTable sortedTable = sorter.sort(exec);
    	
        checkCanceled();
    	// convert the string to date and sort them according to caseID? So we can read them easier for rows
    	// it creates the corresponding column spec and create another DataTable for it.
    	// one thing to remember, it is not so important to have order of timestamp. 
    	ToXLogConverter handler = new ToXLogConverter();

    	//handler.setConfig(m_config);
    	handler.setLogger(logger);
    	
    	handler.convertDataTable2Log(sortedTable, m_settings, exec);
    	XLog log = handler.getXLog();
    	
    	checkCanceled();
    	logPO = new XLogPortObject(log);
    	
    	logger.info("End : Convert DataTable to Event Log" );
        return new PortObject[]{logPO};
    }

    private void checkCanceled() {
		// TODO Auto-generated method stub
		
	}

	/**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO: generated method stub
    	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
            throws InvalidSettingsException {
    	
        // Input PortObject is a DataTableSpec, output Spec is XLogPortObject
    	if(!inSpecs[0].getClass().equals(DataTableSpec.class)) 
    		throw new InvalidSettingsException("Input is not a CSV File!");
    	// here check the options from the exlcuded list from traceAttrSet and event Attr set
    	// to make sure, the columns in the first panel must be included in the lists
    	// check if the type of this column is LocalDateTime or ZonedDateTime type
    	//String tsName = m_config.getMTimeStamp().getStringValue();
    	
    	String tsName = m_settings.time_stamp;
    	DataTableSpec spec  = (DataTableSpec) inSpecs[0];
    	
    	if(!tsName.equals(SMTable2XLogConfig.CFG_NO_OPTION)&&!spec.getColumnSpec(tsName).getType().equals(LocalDateTimeCellFactory.TYPE) &&
    			!spec.getColumnSpec(tsName).getType().equals(ZonedDateTimeCellFactory.TYPE))
    		throw new InvalidSettingsException("The time stamp doesn't have the required format in LocalDateTime or ZonedDateTime");
    	    	
    	String[] all_columns = spec.getColumnNames();
		
		String[] traceColumns = m_settings.m_columnFilterTrace.filterFromFullSpec(spec);
		List<String> traceList = Arrays.asList(traceColumns);
		Set<String> traceSet = new HashSet<>(traceList);
		
		List<String> eventList = new ArrayList<>();
		for (String col : all_columns) {
		    if (!traceSet.contains(col)) {
		        eventList.add(col);
		    }
		}
    	
    	if(traceList.contains(m_settings.case_id)) 
    		if(eventList.contains(m_settings.event_class))
    			if(eventList.contains(m_settings.life_cycle)
    					|| m_settings.life_cycle.equals(SMTable2XLogConfig.CFG_NO_OPTION))
    				if(eventList.contains(m_settings.time_stamp) 
    						|| m_settings.time_stamp.equals(SMTable2XLogConfig.CFG_NO_OPTION))
    					return new PortObjectSpec[]{new XLogPortObjectSpec()};
   	
    	if(!traceList.contains(m_settings.case_id)) 
    		throw new InvalidSettingsException("Please ensure that Case Identifier is a trace attribute.");
    	if(!eventList.contains(m_settings.event_class))
    		throw new InvalidSettingsException("Please ensure that Event Identifier is an event attribute.");
    	if(!(eventList.contains(m_settings.life_cycle) 
    			|| m_settings.life_cycle.equals(SMTable2XLogConfig.CFG_NO_OPTION)))
    		throw new InvalidSettingsException("Please ensure that Life Cycle Identifier is either set to MISSING or is an event attribute.");
    	if(!(eventList.contains(m_settings.time_stamp) 
    			|| m_settings.time_stamp.equals(SMTable2XLogConfig.CFG_NO_OPTION)))
    		throw new InvalidSettingsException("Please ensure that Timestamp Identifier is either set to MISSING or is an event attribute.");
    	throw new InvalidSettingsException("Default error message");
    	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         // TODO: generated method stub
    	// we can add additional info into settings
    	//settings.addStringArray("Column Names", m_possibleColumns);
    	if (m_settings != null) {
            DefaultNodeSettings.saveSettings(m_settingsClass, m_settings, settings);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    	
    	// in this way, we get the values here, but do we need those values ??
    	// not necessary if we have m_caseID and m_eventID already
    	// m_possibleColumns = settings.getStringArray("Column Names");
    	
    	m_settings = DefaultNodeSettings.loadSettings(settings, m_settingsClass);
    	
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }

	public XLogPortObject getLogPO() {
		// TODO Auto-generated method stub
		return logPO;
	}

}