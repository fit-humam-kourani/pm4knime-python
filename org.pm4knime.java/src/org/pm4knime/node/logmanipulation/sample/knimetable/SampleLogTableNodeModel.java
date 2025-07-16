package org.pm4knime.node.logmanipulation.sample.knimetable;

import org.knime.core.data.DataTableSpec;
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



public class SampleLogTableNodeModel extends DefaultTableNodeModel<SampleLogTableNodeSettings> {
	private static final NodeLogger logger = NodeLogger
            .getLogger(SampleLogTableNodeModel.class);
	
    protected SampleLogTableNodeModel(Class<SampleLogTableNodeSettings> class1) {
    
    	super(new PortType[] { BufferedDataTable.TYPE },
				new PortType[] { BufferedDataTable.TYPE, BufferedDataTable.TYPE }, class1);
    	}


    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {


    	logger.info("Begin: Sample the Event Log");

    	BufferedDataTable log = (BufferedDataTable) inData[0];
    	BufferedDataTable[] logs;
    	

		if(m_settings.m_samplePref) {
			double percentage = m_settings.m_samplePercentage;
			logs = SampleUtil.sampleLog(log, percentage, m_settings.t_classifier, exec);
			
		} else {
			int num =  (int) m_settings.m_samplePercentage;
			logs = SampleUtil.sampleLog(log, num, m_settings.t_classifier, exec);
		}
		
		logger.info("End : Sample the Event Log");
        return new PortObject[]{logs[0], logs[1]};
    }



    @Override
    protected PortObjectSpec[] configureOutSpec(final DataTableSpec inSpecs) {

    	DataTableSpec[] m_outSpecs = new DataTableSpec[getNrOutPorts()];
    	m_outSpecs[0] = inSpecs;
		m_outSpecs[1] = inSpecs;
		
		return new PortObjectSpec[] {m_outSpecs[0], m_outSpecs[1]};
        
    }



    @Override
    protected void validateSpecificSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {

    	
    	double value = m_settings.m_samplePercentage;
		if(m_settings.m_samplePref) { 
    		if(value > 1.0) {
    			throw new InvalidSettingsException("The current percentage " + value + 
    					"  is out of bounds. Please give value between 0 - 1.0 ");
    		}
    		
    	}else {
    		if (!(value == Math.floor(value) && !Double.isInfinite(value))) {
    			throw new InvalidSettingsException("The current value  " + value + 
    					"  is not an integer. Please give an integer value");
    		}
    	}
    }



}

