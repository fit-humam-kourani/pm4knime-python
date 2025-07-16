package org.pm4knime.node.logmanipulation.filter.knimetable;


import org.knime.core.data.DataCell;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.sort.BufferedDataTableSorter;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;

import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;

import org.pm4knime.util.defaultnode.DefaultTableNodeModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FilterByLengthTableNodeModel extends DefaultTableNodeModel<FilterByLengthTableNodeSettings> {
	private static final NodeLogger logger = NodeLogger.getLogger(FilterByLengthTableNodeFactory.class);
	
	

	protected FilterByLengthTableNodeModel(Class<FilterByLengthTableNodeSettings> class1) {
    	 super(new PortType[] {BufferedDataTable.TYPE }, new PortType[] { BufferedDataTable.TYPE }, class1);
    }


    @Override
    protected BufferedDataTable[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {
    	logger.info("Begin: filter log by trace frequency");
    	BufferedDataTable log = ((BufferedDataTable) inData[0]);
    	if(log.size() == 0) {
    		logger.warn("This event log is empty, the original log will be returned");
    		return new BufferedDataTable[]{(BufferedDataTable) inData[0]};
    	}
    
    	List<String> idAndTime =  Arrays.asList(m_settings.t_classifier);
    	boolean[] sort_asc = new boolean[1];
    	sort_asc[0] = true;
    	BufferedDataTableSorter sorted_log = new BufferedDataTableSorter(log, idAndTime , sort_asc);
    	log = sorted_log.sort(exec);
    	
    	
		String curr_traceID = "";
		int trace_length = 0;
		ArrayList<DataRow> trace_datarow = new ArrayList<DataRow>();
	    BufferedDataContainer buf = exec.createDataContainer(log.getDataTableSpec(), false);	
		
    	for (DataRow row : log) {
	    	
	    	DataCell traceID = row.getCell(log.getDataTableSpec().findColumnIndex(m_settings.t_classifier));
	
	    	String traceIDStr = traceID.toString();
	    	if (!traceIDStr.equals(curr_traceID)) {
	    		exec.checkCanceled();
	    		
	    		if(!curr_traceID.equals("")) {
	    			if(m_settings.m_isKeep == 
	    					(trace_length >= m_settings.m_minLength 
	    						&& trace_length <= m_settings.m_maxLength)) {
	    							for (DataRow trace_row : trace_datarow) {
	    								buf.addRowToTable(trace_row);
	    							}
	    			}
	    		}
	    		
	    		curr_traceID = traceIDStr;
	    		trace_length = 1;
	    		trace_datarow = new ArrayList<DataRow>();
	    	} else {
	    		trace_length++;
	    	}
	    	
	    	trace_datarow.add(row);
	    	
    	}
    	
    	if(!curr_traceID.equals("")) {
			if(m_settings.m_isKeep == 
					(trace_length >= m_settings.m_minLength 
						&& trace_length <= m_settings.m_maxLength)) {
							for (DataRow trace_row : trace_datarow) {
								buf.addRowToTable(trace_row);
							}
			}
		}
    	buf.close();

    	logger.info("End: filter log by trace frequency");
        return new BufferedDataTable[]{buf.getTable()};
    }

   

    @Override
    protected PortObjectSpec[] configureOutSpec(DataTableSpec tableSpecs) {
        
        return new PortObjectSpec[]{tableSpecs};
    }
    
     
   
	@Override
	protected void validateSpecificSettings(NodeSettingsRO settings) throws InvalidSettingsException {           
    	if (m_settings.m_minLength > m_settings.m_maxLength) {              
    		throw new InvalidSettingsException("The maximum length cannot be smaller than the minimum length!");  
    	}
		
	}


    

}

