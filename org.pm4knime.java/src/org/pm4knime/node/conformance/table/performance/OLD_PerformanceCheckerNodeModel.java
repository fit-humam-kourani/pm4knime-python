//package org.pm4knime.node.conformance.table.performance;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//
//import org.knime.core.data.DataTableSpec;
//import org.knime.core.data.DataType;
//import org.knime.core.data.def.StringCell;
//import org.knime.core.node.BufferedDataContainer;
//import org.knime.core.node.BufferedDataTable;
//import org.knime.core.node.CanceledExecutionException;
//import org.knime.core.node.ExecutionContext;
//import org.knime.core.node.ExecutionMonitor;
//import org.knime.core.node.InvalidSettingsException;
//import org.knime.core.node.NodeLogger;
//import org.knime.core.node.NodeSettingsRO;
//import org.knime.core.node.NodeSettingsWO;
//import org.knime.core.node.port.PortObject;
//import org.knime.core.node.port.PortObjectHolder;
//import org.knime.core.node.port.PortObjectSpec;
//import org.knime.core.node.port.PortType;
//import org.pm4knime.node.conformance.performance.PerfCheckerInfoAssistant;
//import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.PNManifestFlattenerTable;
//import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.PNManifestReplayerParameterTable;
//import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.PerfCounterTable;
//import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.ReliablePerfCounterTable;
//import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.SMAlignmentReplayParameterTable;
//import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.TableEventLog;
//import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.ManifestEvClassPatternTable;
//import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.ManifestFactoryTable;
//import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.ManifestTable;
//import org.pm4knime.node.conformance.replayer.table.helper.tableLibs.ManifestWithSerializerTable;
////import org.pm4knime.portobject.ManifestWithSerializer;
//import org.pm4knime.portobject.RepResultPortObjectTable;
//import org.pm4knime.portobject.RepResultPortObjectSpecTable;
//import org.pm4knime.settingsmodel.SMPerformanceParameter;
//import org.pm4knime.util.defaultnode.DefaultNodeModel;
//import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
//import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
//import org.processmining.models.semantics.petrinet.Marking;
////import org.processmining.plugins.manifestanalysis.visualization.performance.PerfCounter;
////import org.processmining.plugins.manifestanalysis.visualization.performance.ReliablePerfCounter;
//
//import org.processmining.plugins.petrinet.replayresult.PNRepResult;
//import org.processmining.plugins.replayer.replayresult.SyncReplayResult;
//
///**
// * <code>NodeModel</code> for the "PerformanceChecker" node. Input: one
// * XLogPortObject + PetriNetPortObject Output: -- Alignment PortObject but it
// * doesn't matter actually -- statistics information in three output tables, one
// * for the global, -- @reference org.processmining.plugins.manifestanalysis.
// * visualization.performance.ManifestCaseStatPanel#showAllStats
// * 
// * one is for transitions, -- @reference
// * org.processmining.plugins.manifestanalysis.
// * visualization.performance.ManifestElementStatPanel#showTransStats one for
// * source -- @reference org.processmining.plugins.manifestanalysis.
// * visualization.performance.ManifestElementStatPanel#showPlaceStats -- One view
// * to show the Analysis result -- No need to show it here:: one view to show the
// * time between transitions But only the views there, or do we need another
// * table to output it here??
// * 
// * Process: following the ones like ConformanceChecking and get the information
// * there; but one stuff, we don't want to popup too many things. avoid it if we
// * can
// * 
// * @author Kefang Ding
// * @reference https://svn.win.tue.nl/repos/prom/Packages/PNetReplayer/Trunk/src/org/processmining/plugins/petrinet/manifestreplayer/PNManifestReplayer.java
// *            +
// *            https://github.com/rapidprom/rapidprom-source/blob/master/src/main/java/org/rapidprom/operators/conformance/PerformanceConformanceAnalysisOperator.java
// */
//public class OLD_PerformanceCheckerNodeModel extends DefaultNodeModel implements PortObjectHolder {
//	private static final NodeLogger logger = NodeLogger.getLogger(OLD_PerformanceCheckerNodeModel.class);
//
//	private static final String CFG_MC_MANIFEST = "Model Content for Manifest";
//
//
//	// we create a similar nodeSetting like Conformance Checking?
//	SMPerformanceParameter m_parameter;
//	RepResultPortObjectSpecTable m_rSpec;
//	private ManifestTable mResult;
//	private PerfCounterTable counter;
//	RepResultPortObjectTable repResultPO;
//
//	private File file;
//
//	private ExecutionMonitor exe;
//
//	protected OLD_PerformanceCheckerNodeModel() {
//
//		// TODO: Specify the amount of input and output ports needed.
//		super(new PortType[] { RepResultPortObjectTable.TYPE },
//				new PortType[] { BufferedDataTable.TYPE, BufferedDataTable.TYPE, BufferedDataTable.TYPE });
//		m_parameter = new SMPerformanceParameter("Performance Parameter");
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	protected PortObject[] execute(final PortObject[] inData, final ExecutionContext exec) throws Exception {
//
//		// TODO: Return a BufferedDataTable for each output port
//		logger.info("Start: ManifestReplayer Performance Checking");
//		
//		repResultPO = (RepResultPortObjectTable) inData[0];
//		TableEventLog log = repResultPO.getLog();
//		AcceptingPetriNet anet = repResultPO.getNet();
//
//		PNRepResult repResult = repResultPO.getRepResult();
//
//		// pass the values of specParameter to m_parameter
//		// we can do it when it is in configuration state, or here??
//		SMAlignmentReplayParameterTable specParameter = m_rSpec.getMParameter();
//		// for the values specParameter has, we set the needed parameter there
//		m_parameter.setClassifierSet(specParameter.getClassifierSet().getStringArrayValue());
//
//		// we can also get the event classifer by checking the map from rSpec, it takes
//		// more time..
////		XEventClassifier eventClassifier = XLogUtil.getEventClassifier(log,
////				specParameter.getMClassifierName().getStringValue());
//		PNManifestReplayerParameterTable manifestParameters = specParameter.getPerfParameter(log, anet);
//		PNManifestFlattenerTable flattener = new PNManifestFlattenerTable(anet.getNet(), manifestParameters);
//		
//// check cancellation of node before sync
//    	checkCanceled(null, exec);
//		sync(repResult, flattener, exec);
//
//// check cancellation of node before replaying
//		checkCanceled(null, exec);
//		mResult = ManifestFactoryTable.construct(anet.getNet(), anet.getInitialMarking(),
//				anet.getFinalMarkings().toArray(new Marking[0]), log, flattener, repResult, manifestParameters.getMapping());
//		//		mResult = ManifestFactory.construct(flattener.getNet(), flattener.getInitMarking(),
////				flattener.getFinalMarkings(), log, flattener, repResult, manifestParameters.getMapping());
//		checkCanceled(null, exec);
//		// global statistics information. It includes all the performance info, the
//		// whole process
//		// we need one view to show the result here
//		if (m_parameter.isMWithSynMove().getBooleanValue()) {
//			counter = new ReliablePerfCounterTable();
//		} else
//			counter = new PerfCounterTable();
//		PerfCheckerInfoAssistantTable infoAssistant = new PerfCheckerInfoAssistantTable(m_parameter, mResult, counter);
//
//		DataTableSpec gSpec = createGlobalStatsTableSpec();
//		BufferedDataContainer gBuf = exec.createDataContainer(gSpec);
//		// here to fill the values from result to gBuf
//		infoAssistant.fillGlobalData(gBuf);
//
//		// create one for transition, one for place there
//		DataTableSpec tSpec = createElemenentStatsTableSpec("Transition");
//		BufferedDataContainer tBuf = exec.createDataContainer(tSpec);
//		infoAssistant.fillTransitionData(tBuf, anet.getNet().getTransitions());
//
//		DataTableSpec pSpec = createElemenentStatsTableSpec("Place");
//		BufferedDataContainer pBuf = exec.createDataContainer(pSpec);
//		infoAssistant.fillPlaceData(pBuf, anet.getNet().getPlaces());
//
//		gBuf.close();
//		tBuf.close();
//		pBuf.close();
//// check cancellation of node after replaying
//		
//		logger.info("End: ManifestReplayer Performance Evaluation");
//		return new PortObject[] { gBuf.getTable(), tBuf.getTable(), pBuf.getTable() };
//	}
//
//	private void sync(PNRepResult repResult, PNManifestFlattenerTable flattener, ExecutionContext exec) throws CanceledExecutionException {
//		// TODO make the transition in the same transition ids here
//		// set a map here to record the connection?? Or, we can reload the nodes by
//		// making the transition the same
//		
//		for (SyncReplayResult alignment : repResult) {
//			checkCanceled(null, exec);
//			
//			List<Object> nodeInstances = alignment.getNodeInstance();
//			for (int idx = 0; idx < nodeInstances.size(); idx++) {
//				Object node = nodeInstances.get(idx);
//				if (node instanceof Transition) {
//					Transition tInResult = (Transition) node;
//					// here make the wrong match for silent transition only with label
//					// to include silent transition, we need to have one order of the transition..
//					// because it is from the accpeting petri net, so there should be some order to match it 
//					// it can't work out because flattener net changes its structure by adding more transitions
//					// the thing is to convert the performance calculation information there. 
//					// so go to further about the structure there, no need to repeat the use of them.
//					
//					int tIdx = flattener.getOrigTrans2Int().get(tInResult);
//					Transition tValue = flattener.getFlatTransArr()[tIdx];
//					
//					nodeInstances.set(idx, tValue);
//				}
//			}
//		}
//
//	}
//
//	public ManifestTable getMainfestResult() {
//		return mResult;
//	}
//
//	public SMPerformanceParameter getMParameter() {
//		return m_parameter;
//	}
//
//	/**
//	 * this method create a table for the element statistics info. It can be used
//	 * for transitions, but also for the places. But how to get this?? We should
//	 * have columnClassifier
//	 * 
//	 * From the parameters, we could create a table spec from it
//	 * 
//	 * @return
//	 */
//	private DataTableSpec createElemenentStatsTableSpec(String itemColName) {
//		// here we need to change the table spec according to the places
//		String[] columnNames = { itemColName, "Property", "Min.", "Max.", "Avg.", "Std. Dev", "Freq." };
//		DataType[] columnTypes = { StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE, StringCell.TYPE,
//				StringCell.TYPE, StringCell.TYPE };
//		DataTableSpec tSpec = new DataTableSpec(itemColName + " Performance Table", columnNames, columnTypes);
//		return tSpec;
//	}
//
//	/**
//	 * there is one global table for this, so
//	 */
//	private DataTableSpec createGlobalStatsTableSpec() {
//		String[] columnNames = { "Case Property", "Value" };
//		DataType[] columnTypes = { StringCell.TYPE, StringCell.TYPE };
//		DataTableSpec tSpec = new DataTableSpec("Global Performance Statistics Table", columnNames, columnTypes);
//		return tSpec;
//	}
//
//	
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
//
//		if (!inSpecs[0].getClass().equals(RepResultPortObjectSpecTable.class))
//			throw new InvalidSettingsException("Input is not a valid replay result!");
//
//		// TODO : assign the table spec here
//		//if(m_parameter.getMTimeStamp().getStringValue().isEmpty())
//		//m_parameter.setMTimeStamp(mResult.pa);
//			//throw new InvalidSettingsException("The timestamp attribute is not set!");
//		
//		m_rSpec = (RepResultPortObjectSpecTable) inSpecs[0];
//		return new PortObjectSpec[] { null, null, null };
//
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	protected void saveSettingsTo(final NodeSettingsWO settings) {
//		// TODO: generated method stub
//		m_parameter.saveSettingsTo(settings);
//	}
//
//	/**
//	 * {@inheritDoc}
//	 */
//	@Override
//	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings) throws InvalidSettingsException {
//		// TODO: generated method stub
//		m_parameter.loadSettingsFrom(settings);
//	}
//
//	@Override
//	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
//			throws IOException, CanceledExecutionException {
//		// TODO deserialize the manifest and other related data for view
//		file = nodeInternDir;
//		exe = exec;
//		
//	}
//	
//
//	@Override
//	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
//			throws IOException, CanceledExecutionException {
//		// TODO serialise the manifest and other related data for view
//		// manifest, reliableResultSymbol, timeAttribute, if with Synmove to generate counter.[pure one is ok]
//		// to serialize manifest with a dir
//		File manifestDir = new File(nodeInternDir, CFG_MC_MANIFEST);
//		manifestDir.mkdirs();
//		ManifestWithSerializerTable.saveTo((ManifestEvClassPatternTable) mResult, manifestDir , exec);
//		
//	}
//	
//
//	@Override
//	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
//		// TODO Auto-generated method stub
//		m_parameter.validateSettings(settings);
//	}
//	
//	public RepResultPortObjectTable getRepResultPO() {
//		// TODO Auto-generated method stub
//		return repResultPO;
//	}
//	
//	@Override
//	public void setInternalPortObjects(PortObject[] portObjects) {
//		repResultPO = (RepResultPortObjectTable)portObjects[0];
//		try {
//			File manifestDir = new File(file, CFG_MC_MANIFEST);
//			TableEventLog log = repResultPO.getLog();
//			try {
//				mResult = ManifestWithSerializerTable.loadFrom(manifestDir, exe, log);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (CanceledExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (InvalidSettingsException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public PortObject[] getInternalPortObjects() {
//		// TODO Auto-generated method stub
//		return new PortObject[] {repResultPO};
//	
//	}
//	
//}
