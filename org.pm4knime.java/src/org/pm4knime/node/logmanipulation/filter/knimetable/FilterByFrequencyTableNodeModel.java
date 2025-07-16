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
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.pm4knime.util.defaultnode.DefaultTableNodeModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;



public class FilterByFrequencyTableNodeModel extends DefaultTableNodeModel<FilterByFrequencyTableNodeSettings> {
	private static final NodeLogger logger = NodeLogger.getLogger(FilterByFrequencyTableNodeFactory.class);
	
	
	 protected FilterByFrequencyTableNodeModel(Class<FilterByFrequencyTableNodeSettings> class1) {
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
    	
    	System.out.println(m_settings.m_threshold);
    		
    	List<String> idAndTime =  Arrays.asList(m_settings.t_classifier, m_settings.time_classifier);
    	boolean[] sort_asc = new boolean[2];
    	sort_asc[0] = true;
    	sort_asc[1] = true;
    	BufferedDataTableSorter sorted_log = new BufferedDataTableSorter(log, idAndTime , sort_asc);
    	
    	log = sorted_log.sort(exec);
    	
    	
   
    	
		String curr_traceID = "";
		String trace = "";
		int totalTraces = 0;
		HashMap<String, Integer> freq = new HashMap<String, Integer>(); // activity sequence to freq 
		HashMap<String, ArrayList<String>> trace_array = new HashMap<String, ArrayList<String>>(); // id to activities
		HashMap<String, ArrayList<String>> tracetoIds = new HashMap<String, ArrayList<String>>(); // activity sequence to ids
		
		System.out.println("Start mapping");
		// Create Mappings
    	for (DataRow row : log) {
	    	
	    	DataCell activity = row.getCell(log.getDataTableSpec().findColumnIndex(m_settings.e_classifier));
	    	DataCell traceID = row.getCell(log.getDataTableSpec().findColumnIndex(m_settings.t_classifier));
	
	    	String traceIDStr = traceID.toString();
	    	if (!traceIDStr.equals(curr_traceID)) {
	    		
	    		if(!curr_traceID.equals("")) {
		    		
		    		if (freq.containsKey(trace)) {
		    			totalTraces++;
		    			freq.put(trace, freq.get(trace) + 1);
		    		}else {
		    			freq.put(trace, 1);
		    			totalTraces++;
		    		}
		    		
		    		if (tracetoIds.containsKey(trace)) {
		    			
		    			tracetoIds.get(trace).add(curr_traceID);
		    		}else {
		    			ArrayList<String> traceid_list = new ArrayList<String>();
			    		traceid_list.add(curr_traceID);
			    		tracetoIds.put(trace, traceid_list);
		    		}
	    		}
	    		
	    		
	    		curr_traceID = traceIDStr;
	    		
	    		
	    		ArrayList<String> activity_list = new ArrayList<String>();
	    		activity_list.add(activity.toString());
	    		trace_array.put(curr_traceID, activity_list);
	    		trace = activity.toString();
	    	} else {
	    		//freq.put(curr_traceID, freq.get(curr_traceID) + 1);
	    		trace_array.get(curr_traceID).add(activity.toString());
	    		trace = trace + "," + activity.toString();
	    	}
	    	
    	}
    	
    	if (freq.containsKey(trace)) {
			totalTraces++;
			freq.put(trace, freq.get(trace) + 1);
		}else {
			freq.put(trace, 1);
			totalTraces++;
		}
		
		if (tracetoIds.containsKey(trace)) {
			
			tracetoIds.get(trace).add(curr_traceID);
		}else {
			ArrayList<String> traceid_list = new ArrayList<String>();
    		traceid_list.add(curr_traceID);
    		tracetoIds.put(trace, traceid_list);
		}
		
		System.out.println("End mapping");
		
		System.out.println("Start freq");
		//Sort 
		ArrayList<ArrayList<String>> listOfCaseId = new ArrayList<ArrayList<String>>(tracetoIds.values());
		Comparator<ArrayList<String>> sizeComparator = new Comparator<ArrayList<String>>()
	    {
	        @Override
	        public int compare(ArrayList<String> o1, ArrayList<String> o2)
	        {
	            return Integer.compare(o1.size(), o2.size());
	        }
	    };
		
	    Collections.sort(listOfCaseId, sizeComparator);
	    Collections.reverse(listOfCaseId);
	    

	    
	    // Create new BufferedDataTable without filtered rows
	    
	    HashMap<String, Boolean> containIDs = getContainedCases(listOfCaseId, trace_array, totalTraces);

	    
	    BufferedDataContainer buf = exec.createDataContainer(log.getDataTableSpec(), false);
	    

	    for (DataRow row : log) {
	    	
	    	DataCell traceID = row.getCell(log.getDataTableSpec().findColumnIndex(m_settings.t_classifier));

	    	if (containIDs.containsKey(traceID.toString())) {
		    	buf.addRowToTable(row);

	    	}
	  
	    }
	    buf.close();



    	logger.info("End: filter log by trace frequency");
        return new BufferedDataTable[]{buf.getTable()};
    }
    
     
    protected HashMap<String, Boolean> getContainedCases(ArrayList<ArrayList<String>> listOfValues, 
    														HashMap<String, ArrayList<String>> trace_array, int totalTraces) {
    	
    
    	
    	int iThreshold = 0;
    	if(m_settings.m_threshold < 1) {
    		iThreshold = (int) (m_settings.m_threshold * totalTraces);
    	}else {
    		// we can deal with it with the listener 
    		iThreshold = (int) m_settings.m_threshold;
    		
    	}
    	
    	System.out.print("2Threshold - Knime - ");
    	System.out.println(iThreshold);
    	
    	HashMap<String, Boolean> containIDs = new HashMap<String, Boolean>();
	    
	    
	    if (m_settings.m_isForSingleTV){
	    	
			if(m_settings.m_isKeep) {
				for(ArrayList<String> variant : listOfValues) {
		    		
		    		if(variant.size() >= iThreshold) {
		    			for(String event : variant) {
		    				containIDs.put(event, true);
		    			}
		    			
		    		}
		    	}
			}else {
				for(ArrayList<String> variant : listOfValues) {
		    		if(variant.size() < iThreshold) {
		    			for(String event : variant) {
		    				containIDs.put(event, true);
		    			}
		    		}
		    	}
			}
	    	
	    }else {
	    	
	    	
	    	int sum = 0;
	    	if(m_settings.m_isKeep) {
	    		for(ArrayList<String> variant : listOfValues) {

	        		if(sum <= iThreshold) {
		    			for(String event : variant) {
		    				containIDs.put(event, true);
		    			}
	        		}else
	        			break;
	        		sum += variant.size();
	    		}
	    		
	    	}else {
	    		for(ArrayList<String> variant : listOfValues) {

	        		if(sum <= iThreshold) {
	        			sum += variant.size();
	        			continue ;
	        		}else
		    			for(String event : variant) {
		    				containIDs.put(event, true);
		    			}
	    		}
	    	}
	    	 
	    }
    	
	    return containIDs;
    }


	@Override
	protected PortObjectSpec[] configureOutSpec(DataTableSpec logSpec) {
		DataTableSpec[] m_outSpecs = new DataTableSpec[getNrOutPorts()];
    	m_outSpecs[0] = logSpec;
		
		return new PortObjectSpec[] {m_outSpecs[0]};
	}


	@Override
	protected void validateSpecificSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		// TODO Auto-generated method stub
		
	}
   

}

