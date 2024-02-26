package org.pm4knime.node.discovery.alpha.table;

import java.util.Set;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerNodeModel;
import org.pm4knime.node.discovery.defaultminer.TraceVariantRep;
import org.pm4knime.portobject.CausalGraphPortObject;
import org.pm4knime.portobject.PetriNetPortObject;
import org.pm4knime.portobject.PetriNetPortObjectSpec;
import org.pm4knime.util.PetriNetUtil;
import org.pm4knime.util.connectors.prom.PM4KNIMEGlobalContext;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetImpl;
import org.processmining.alphaminer.parameters.AlphaMinerParameters;
import org.processmining.alphaminer.parameters.AlphaPlusMinerParameters;
import org.processmining.alphaminer.parameters.AlphaRobustMinerParameters;
import org.processmining.alphaminer.parameters.AlphaVersion;
import org.processmining.alphaminer.plugins.AlphaMinerPlugin;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.semantics.petrinet.Marking;



public class AlphaMinerTableNodeModel extends DefaultTableMinerNodeModel<AlphaMinerTableNodeSettings> {

	private static final NodeLogger logger = NodeLogger.getLogger(AlphaMinerTableNodeModel.class);
	
	protected PortObject pnPO;
	protected CausalGraphPortObject cgPO;
	
	
	
	protected AlphaMinerTableNodeModel(final Class<AlphaMinerTableNodeSettings> modelSettingsClass) {
		super( new PortType[]{BufferedDataTable.TYPE } ,
				new PortType[] { PetriNetPortObject.TYPE }, "Petri Net JS View", modelSettingsClass);
	}

	
	
	



	protected PortObject mine(BufferedDataTable table, final ExecutionContext exec) throws Exception {
		// TODO Auto-generated method stub
		logger.info("Start: Alpha Miner");
		AlphaMinerParameters alphaParams = null;
		
		
		if(m_settings.m_variant.equals(AlphaVersion.CLASSIC.toString()))
			alphaParams = new AlphaMinerParameters(AlphaVersion.CLASSIC);
		else if(m_settings.m_variant.equals(AlphaVersion.PLUS.toString())) {
			alphaParams = new AlphaPlusMinerParameters(AlphaVersion.PLUS,m_settings.m_ignore_ll);
			alphaParams.setVersion(AlphaVersion.PLUS);
		}
		else if(m_settings.m_variant.equals(AlphaVersion.PLUS_PLUS.toString()))
			alphaParams = new AlphaMinerParameters(AlphaVersion.PLUS_PLUS);
		else if(m_settings.m_variant.equals(AlphaVersion.SHARP.toString()))
			alphaParams = new AlphaMinerParameters(AlphaVersion.SHARP);
		else if(m_settings.m_variant.equals(AlphaVersion.ROBUST.toString())) {
			alphaParams = new AlphaRobustMinerParameters(m_settings.m_casualTH,m_settings.m_noiseTLF, m_settings.m_noiseTMF);
			alphaParams.setVersion(AlphaVersion.ROBUST);
		}
		PluginContext context = PM4KNIMEGlobalContext.instance().getFutureResultAwarePluginContext(AlphaMinerPlugin.class);
		
		
		TraceVariantRep variants = new TraceVariantRep(table, m_settings.getTraceClassifier(), m_settings.getEventClassifier());
		Object[] result = AlphaAbstraction.apply(context, variants, m_settings.getEventClassifier(), alphaParams);

		// when there is no finalMarking available, we set the finalMarking automatically
		Set<Marking> fmSet = PetriNetUtil.guessFinalMarking((Petrinet) result[0]); // new HashMap();
		
		AcceptingPetriNet anet = new AcceptingPetriNetImpl((Petrinet) result[0], (Marking) result[1], fmSet);
		PetriNetPortObject pnPO = new PetriNetPortObject(anet);
		logger.info("End: Alpha Miner");
		return pnPO;
		
	}

	
	
	protected PortObjectSpec[] configureOutSpec(DataTableSpec logSpec) {
		// TODO Auto-generated method stub
		PetriNetPortObjectSpec ptSpec = new PetriNetPortObjectSpec();
		return new PortObjectSpec[] { ptSpec };
	}


	
}

