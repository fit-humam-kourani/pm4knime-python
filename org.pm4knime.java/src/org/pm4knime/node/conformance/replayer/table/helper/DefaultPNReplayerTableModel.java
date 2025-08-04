package org.pm4knime.node.conformance.replayer.table.helper;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.webui.node.dialog.defaultdialog.NodeParametersUtil;
import org.pm4knime.node.conformance.replayer.table.helper.DefaultPNReplayerTableUtil.ParameterGenerator;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.CostBasedCompleteManifestParamTable;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.IPNReplayAlgorithmTable;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.PNLogReplayerTable;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.PNManifestFlattenerTable;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.PNManifestReplayerParameterTable;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.PetrinetReplayerILPRestrictedMoveModelTable;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.PetrinetReplayerWithILPTable;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.PetrinetReplayerWithoutILPTable;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.TableEventLog;
import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.TransEvClassMappingTable;
import org.pm4knime.portobject.PetriNetPortObject;
import org.pm4knime.portobject.PetriNetPortObjectSpec;
import org.pm4knime.portobject.RepResultPortObjectSpecTable;
import org.pm4knime.portobject.RepResultPortObjectTable;
import org.pm4knime.util.PetriNetUtil;
import org.pm4knime.util.ReplayerUtil;
import org.pm4knime.util.connectors.prom.PM4KNIMEGlobalContext;
import org.pm4knime.util.defaultnode.DefaultNodeModel;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.plugins.petrinet.manifestreplayer.PNManifestReplayer;
import org.processmining.plugins.petrinet.replayer.PNLogReplayer;
import org.processmining.plugins.petrinet.replayer.algorithms.IPNReplayParameter;
import org.processmining.plugins.petrinet.replayresult.PNRepResult;



@SuppressWarnings({"restriction", "rawtypes" })
public class DefaultPNReplayerTableModel extends DefaultNodeModel {
	private static final NodeLogger logger = NodeLogger.getLogger(DefaultPNReplayerTableModel.class);
	private static final  String message  = "Replayer With Cost Tables";	
	

	public static String CFG_PARAMETER_NAME = "Parameter In " + message;
	
	protected static final int INPORT_LOG = 0;
	protected static final int INPORT_PETRINET = 1;
	private PNReplayerTableNodeSettings m_modelSettings;
    private final Class<PNReplayerTableNodeSettings> m_modelSettingsClass;
//    SMAlignmentReplayParameterTable m_parameter;

	
//	SMAlignmentReplayParameterTable m_parameter;
	// it can't belong to this class
	String evClassDummy;
	
	RepResultPortObjectTable repResultPO;
	RepResultPortObjectSpecTable m_rSpec ;
    /**
     * Constructor for the node model.
     */
    protected DefaultPNReplayerTableModel(final Class<PNReplayerTableNodeSettings> modelSettingsClass) {
    
        // TODO: Specify the amount of input and output ports needed.
    	super(new PortType[] { BufferedDataTable.TYPE, PetriNetPortObject.TYPE }, new PortType[] {RepResultPortObjectTable.TYPE });
    	evClassDummy = "dummy";
    	// need to initialize the parameters later, because it has different types there.
//    	initializeParameter();
    	m_modelSettingsClass = modelSettingsClass;  	
    }
    
//    protected void initializeParameter() {
//    	m_parameter = new SMAlignmentReplayParameterTable(CFG_PARAMETER_NAME);
//    	
//    }

   
    @Override
    protected PortObject[] execute(final PortObject[] inData,
            final ExecutionContext exec) throws Exception {

    	logger.info("Start: " + message);
    	String strategyName = m_modelSettings.strategy;
    	
    	executeWithoutLogger(inData, exec, strategyName);
    	// in greed to output the strategy for replay
		logger.info("End: " + message + " for "+ strategyName);
		return new PortObject[]{repResultPO};
    }
    

    protected void executeWithoutLogger(final PortObject[] inData,
            final ExecutionContext exec, String strategyName) throws Exception{
      	// extract one method here to allow logger record its current class info
    	// check cancellation of node
    	checkCanceled(exec);
    	
    	BufferedDataTable logPO = (BufferedDataTable) inData[INPORT_LOG];
    	PetriNetPortObject netPO = (PetriNetPortObject) inData[INPORT_PETRINET];
    	String eventClassifier = m_modelSettings.e_classifier;
    	String traceClassifier = m_modelSettings.t_classifier;
    	String timeClassifier = m_modelSettings.time_classifier;
    	TableEventLog log = new TableEventLog(logPO, eventClassifier, traceClassifier, timeClassifier); 
    	AcceptingPetriNet anet = netPO.getANet();
    	
    	// here to change the operation on the classifier

    	PNRepResult repResult = null;
    	IPNReplayAlgorithmTable replayAlgorithm = null ;
    	int[] move_costs = {m_modelSettings.log_move_cost, m_modelSettings.model_move_cost, m_modelSettings.sync_move_cost};
    	Map<String, Integer>[] move_cost_maps = m_modelSettings.getCostMaps();
    	
    	// for performance only
    	if(strategyName.equals(ReplayerUtil.strategyList[2])) {
    		//change the calculation with ILP from nonILP mining.
    		replayAlgorithm = new PetrinetReplayerILPRestrictedMoveModelTable();
//    		m_parameter.setMClassifierTrace(null);
    		// different parameters need different get parameter methods. We need to go back to replayer node
//    		this.m_modelSettings.set_parameters_from_settings(m_parameter);
    		PNManifestReplayerParameterTable manifestParameters = ParameterGenerator.getPerfParameter(log, anet, move_costs, move_cost_maps);
    		
    		PNManifestFlattenerTable flattener = new PNManifestFlattenerTable(anet.getNet(), manifestParameters);
    		CostBasedCompleteManifestParamTable parameter = new CostBasedCompleteManifestParamTable(flattener.getMapEvClass2Cost(),
    				flattener.getMapTrans2Cost(), flattener.getMapSync2Cost(),
    				flattener.getInitMarking(), flattener.getFinalMarkings(), manifestParameters.getMaxNumOfStates(),
    				flattener.getFragmentTrans());
    				
    		parameter.setGUIMode(false);
    		parameter.setCreateConn(false);
    		
    		PluginContext pluginContext = PM4KNIMEGlobalContext.instance()
    				.getFutureResultAwarePluginContext(PNManifestReplayer.class);
    		// check cancellation of node before replaying the result
    		checkCanceled(pluginContext, exec);
    		PNLogReplayerTable replayer = new PNLogReplayerTable();
    		repResult = replayer.replayLog(pluginContext, flattener.getNet(), log, flattener.getMap(),
    				replayAlgorithm, parameter);
    		
    	}else {
    		// for conformance 
	    	if(strategyName.equals(ReplayerUtil.strategyList[0]) ) {
	    		replayAlgorithm = new PetrinetReplayerWithILPTable();
	    	}else if(strategyName.equals(ReplayerUtil.strategyList[1])) {
	    		replayAlgorithm = new PetrinetReplayerWithoutILPTable();
	    	}
	    	
	    	TransEvClassMappingTable mapping = PetriNetUtil.constructMapping(log, anet.getNet(), eventClassifier, evClassDummy);
	    	IPNReplayParameter parameters =  ParameterGenerator.getConfParameter(log, anet, evClassDummy, move_costs, move_cost_maps);
	    	PluginContext pluginContext = PM4KNIMEGlobalContext.instance()
					.getFutureResultAwarePluginContext(PNLogReplayer.class);
	    	// check cancellation of node before replaying the result
	    	checkCanceled(pluginContext, exec);
	    	repResult = replayAlgorithm.replayLog(pluginContext, anet.getNet(), log, mapping, parameters);
    	}
    	
    	// put the dummy event class and event classifier in the info table for reuse
    	//repResult.addInfo(XLogUtil.CFG_DUMMY_ECNAME, XLogUtil.serializeEventClass(evClassDummy));
    	//repResult.addInfo(XLogUtil.CFG_EVENTCLASSIFIER_NAME, m_parameter.getMClassifierName().getStringValue());
    	
    	// check cancellation of node after replaying the result
    	checkCanceled(exec);
    	
		repResultPO = new RepResultPortObjectTable(repResult, log, logPO, anet);
		repResultPO.setDefaultMoveCosts(move_costs);
		repResultPO.setMoveCostMaps(move_cost_maps);
		repResultPO.setSpec(m_rSpec);
    }
  
    public RepResultPortObjectTable getRepResultPO() {
		return repResultPO;
	}
    
  
    /**
     * {@inheritDoc}
     */
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
            throws InvalidSettingsException {
    	
    	try {
            m_modelSettings = m_modelSettingsClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Could not instantiate settings class: " + m_modelSettingsClass.getName(), e);
        }
    	
    	if (!inSpecs[INPORT_LOG].getClass().equals(DataTableSpec.class))
			throw new InvalidSettingsException("Input is not a valid table log!");
    	

		if (!inSpecs[INPORT_PETRINET].getClass().equals(PetriNetPortObjectSpec.class))
			throw new InvalidSettingsException("Input is not a valid Petri net!");
		
		Field[] fields = m_modelSettings.getClass().getDeclaredFields();
	    
	    ArrayList<String> nullFields = new ArrayList<String>();
	    
	    for (Field field : fields) {
	        field.setAccessible(true); 
	        try {
	            Object value = field.get(m_modelSettings);
	            if (value == null) {
	            	nullFields.add(field.getName());
	            }
	        } catch (IllegalAccessException e) {
	            // Handle potential exception here if needed
	        }
	    }
	    
	    if (nullFields.size() > 0) {
//	    	throw new InvalidSettingsException("The following fields are not set: " + nullFields.toString() + ". Please open the dialog and configure the node!");
	        throw new InvalidSettingsException("No value is selected for " + nullFields.size() + " fields! Please open the dialog and configure the node!");
	    }

		
		m_rSpec = new RepResultPortObjectSpecTable();
		
		// one question, how to add the type information here to make them valid at first step??
		return new PortObjectSpec[]{ m_rSpec};
    }

    
    @Override
    protected final void saveSettingsTo(final NodeSettingsWO settings) {
        if (m_modelSettings != null) {
        	NodeParametersUtil.saveSettings(m_modelSettingsClass, m_modelSettings, settings);
        }
    }


    @Override
    protected final void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
        m_modelSettings = NodeParametersUtil.loadSettings(settings, m_modelSettingsClass);
    }
    

}
