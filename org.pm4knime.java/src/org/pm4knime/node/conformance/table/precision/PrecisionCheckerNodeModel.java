package org.pm4knime.node.conformance.table.precision;

import java.io.File;
import java.io.IOException;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.pm4knime.portobject.RepResultPortObjectTable;
import org.pm4knime.node.conformance.precision.PrecCheckerInfoAssistant;
import org.pm4knime.portobject.RepResultPortObjectSpecTable;
import org.pm4knime.util.ReplayerUtil;
import org.pm4knime.util.defaultnode.DefaultNodeModel;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.plugins.multietc.reflected.ReflectedLog;
import org.processmining.plugins.multietc.res.MultiETCResult;
import org.processmining.plugins.multietc.sett.MultiETCSettings;
import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;


@SuppressWarnings("restriction")
public class PrecisionCheckerNodeModel extends NodeModel {
	private static final NodeLogger logger = NodeLogger.getLogger(PrecisionCheckerNodeModel.class);
	
//	final static String ALIGN_1 = "1-Align Precision";
//	final static String ALIGN_ALL = "All-Align Precision";
//	final static String ALIGN_REPRE = "Representative-Align Precision";
//	final static String ETC = "ETC Precision (no invisible/duplicates allowed)";
//	
//	private final SettingsModelBoolean m_isOrdered =  new SettingsModelBoolean(
//			MultiETCSettings.REPRESENTATION, true);
//	private final SettingsModelString m_algorithm =  new SettingsModelString(
//			MultiETCSettings.ALGORITHM, ALIGN_1);
	
	protected PrecisionCheckerNodeSettings m_settings;

    private final Class<PrecisionCheckerNodeSettings> m_settingsClass;
	
	
	private RepResultPortObjectTable repResultPO;
    /**
     * Constructor for the node model.
     * @param class1 
     */
    protected PrecisionCheckerNodeModel(Class<PrecisionCheckerNodeSettings> modelSettingsClass) {
    
        // TODO: Specify the amount of input and output ports needed.
    	super(new PortType[] { RepResultPortObjectTable.TYPE }, new PortType[] {
    			BufferedDataTable.TYPE});
    	m_settingsClass = modelSettingsClass;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {
    	logger.info("Start: ETC Precision Checking");

    	repResultPO = (RepResultPortObjectTable) inData[0];
    	
    	PNRepResult repResult = repResultPO.getRepResult();
		AcceptingPetriNet anet = repResultPO.getNet();
    	
		PNMatchInstancesRepResult matchResult = ReplayerUtil.convert2MatchInstances(repResult, exec);
		
		// create reflected log. Due to loading Petri net will change its transition id, 
		// so the new loaded version differs from the transitions from anet
		ReflectedLog refLog = ReplayerUtil.extractRefLog(matchResult, anet);
		// make refLog with the corresponding version in anet
		MultiETCSettings sett = getParameter();
		// check cancellation of node before the precision checking
		// based on match result, get the precision indication
		Object[] result = ReplayerUtil.checkMultiETC(refLog, anet, sett);
		MultiETCResult res = (MultiETCResult) result[0];
		
		// convert result into a table 
		String tableName = "Precision Table with " + sett.getAlgorithm() +"-" + sett.getRepresentation(); 
		DataTableSpec tSpec = PrecCheckerInfoAssistant.createGlobalStatsTableSpec(tableName);
    	BufferedDataContainer tBuf = exec.createDataContainer(tSpec);
    	PrecCheckerInfoAssistant.buildInfoTable(tBuf, res);
    	
    	tBuf.close();
    	
    	logger.info("End: ETC Precision Checking");
    
        return new PortObject[]{tBuf.getTable()};
    }
        
    

    private MultiETCSettings getParameter() {
    	MultiETCSettings sett = new MultiETCSettings();
    	boolean isOrdered = m_settings.m_ignore_ll;
    	if(isOrdered) 
    		sett.put(MultiETCSettings.REPRESENTATION, MultiETCSettings.Representation.ORDERED);
		else 
			sett.put(MultiETCSettings.REPRESENTATION, MultiETCSettings.Representation.UNORDERED);
		
		//Get the Algorithm 
    	String algName = m_settings.m_variant;
		if( algName == PrecisionCheckerNodeSettings.ETC) 
			sett.put(MultiETCSettings.ALGORITHM, MultiETCSettings.Algorithm.ETC);
		else if( algName == PrecisionCheckerNodeSettings.ALIGN_1) 
			sett.put(MultiETCSettings.ALGORITHM, MultiETCSettings.Algorithm.ALIGN_1);
		else if( algName == PrecisionCheckerNodeSettings.ALIGN_REPRE) 
			sett.put(MultiETCSettings.ALGORITHM, MultiETCSettings.Algorithm.ALIGN_REPRE);
		else if( algName == PrecisionCheckerNodeSettings.ALIGN_ALL) 
			sett.put(MultiETCSettings.ALGORITHM, MultiETCSettings.Algorithm.ALIGN_ALL);
    	
    	return sett;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
            throws InvalidSettingsException {
    	if (!inSpecs[0].getClass().equals(RepResultPortObjectSpecTable.class))
			throw new InvalidSettingsException("Input is not a valid replay result!");

        return new PortObjectSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         // TODO: generated method stub
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
    	m_settings = DefaultNodeSettings.loadSettings(settings, m_settingsClass);
    }

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void reset() {
		// TODO Auto-generated method stub
		
	}

 
}

