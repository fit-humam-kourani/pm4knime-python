package org.pm4knime.node.discovery.heuritsicsminer.table;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerNodeModel;
import org.pm4knime.node.discovery.heuritsicsminer.table.util.TableFlexibleHeuristicsMiner;
import org.pm4knime.portobject.AbstractJSONPortObject;
import org.pm4knime.portobject.PetriNetPortObject;
import org.pm4knime.portobject.PetriNetPortObjectSpec;
import org.pm4knime.util.connectors.prom.PM4KNIMEGlobalContext;
import org.pm4knime.util.defaultnode.TraceVariantRepresentation;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetImpl;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.heuristics.HeuristicsNet;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.heuristicsnet.miner.heuristics.converter.HeuristicsNetToPetriNetConverter;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.FlexibleHeuristicsMinerPlugin;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.settings.HeuristicsMinerSettings;


public class HeuristicsMinerTableNodeModel extends DefaultTableMinerNodeModel<HeuristicsMinerTableNodeSettings> {
	
	private static final NodeLogger logger = NodeLogger
            .getLogger(HeuristicsMinerTableNodeModel.class);
	
	
	
	private HeuristicsNet hnet;

    protected HeuristicsMinerTableNodeModel(Class<HeuristicsMinerTableNodeSettings> class1) {
    
        // TODO: Specify the amount of input and output ports needed.
        super(new PortType[] { BufferedDataTable.TYPE }, 
        		new PortType[] { PetriNetPortObject.TYPE }, "Petri Net JS View", class1);
    }

    
    @Override
	protected AbstractJSONPortObject mine(BufferedDataTable log, final ExecutionContext exec) throws Exception{
    	logger.info("Begin: Heuristic Miner");
    	
    	PluginContext pluginContext = PM4KNIMEGlobalContext.instance()
				.getFutureResultAwarePluginContext(FlexibleHeuristicsMinerPlugin.class);
    	
    	TraceVariantRepresentation variants = new TraceVariantRepresentation(log, m_settings.t_classifier, m_settings.e_classifier);
    	
    	HeuristicsMinerSettings heuristicsMinerSettings = getConfiguration();
    	
    	TableFlexibleHeuristicsMiner fhm = new TableFlexibleHeuristicsMiner(pluginContext, variants, heuristicsMinerSettings);

    	hnet = fhm.mine();
    	
    	Object[] result = HeuristicsNetToPetriNetConverter.converter(pluginContext, hnet);
    	
    	AcceptingPetriNet anet = new AcceptingPetriNetImpl((Petrinet) result[0], (Marking) result[1]);
    	
    	PetriNetPortObject pnPO = new PetriNetPortObject(anet);
    	
    	logger.info("End: Heuristics miner");
    	return pnPO;
    }

    public HeuristicsNet getHNet() {
    	return hnet; 
    }
    
    
    HeuristicsMinerSettings getConfiguration() {
    	HeuristicsMinerSettings heuristicsMinerSettings = new HeuristicsMinerSettings();
    	
    	heuristicsMinerSettings.setRelativeToBestThreshold(m_settings.m_r2b);
		heuristicsMinerSettings.setDependencyThreshold(m_settings.m_dependency);
		heuristicsMinerSettings.setL1lThreshold(m_settings.m_length1Loop);
		heuristicsMinerSettings.setL2lThreshold(m_settings.m_length2Loop);
		heuristicsMinerSettings.setLongDistanceThreshold(m_settings.m_longDistance);
		heuristicsMinerSettings.setUseAllConnectedHeuristics(m_settings.m_allConnected);
		heuristicsMinerSettings.setUseLongDistanceDependency(m_settings.m_withLT);
		heuristicsMinerSettings.setCheckBestAgainstL2L(false);
		heuristicsMinerSettings.setAndThreshold(Double.NaN);
		
		return heuristicsMinerSettings;
	}



	@Override
	protected PortObjectSpec[] configureOutSpec(DataTableSpec tableSpec) {
		// TODO Auto-generated method stub
		PetriNetPortObjectSpec pnSpec = new PetriNetPortObjectSpec();
        return new PortObjectSpec[]{pnSpec};
	}
     
   
}

