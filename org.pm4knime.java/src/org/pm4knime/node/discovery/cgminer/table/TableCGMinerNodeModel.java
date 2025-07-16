package org.pm4knime.node.discovery.cgminer.table;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.pm4knime.node.discovery.defaultminer.DefaultTableMinerNodeModel;
import org.pm4knime.portobject.AbstractJSONPortObject;
import org.pm4knime.portobject.CausalGraphPortObject;
import org.pm4knime.portobject.CausalGraphPortObjectSpec;
import org.processmining.extendedhybridminer.algorithms.HybridCGMiner;
import org.processmining.extendedhybridminer.algorithms.preprocessing.TraceVariantsLog;
import org.processmining.extendedhybridminer.models.causalgraph.ExtendedCausalGraph;
import org.processmining.extendedhybridminer.plugins.HybridCGMinerSettings;


public class TableCGMinerNodeModel extends DefaultTableMinerNodeModel<TableCGMinerNodeSettings> {
	
	private final NodeLogger logger = NodeLogger
            .getLogger(TableCGMinerNodeModel.class);
	
	private ExtendedCausalGraph cg;
	
	protected TableCGMinerNodeModel(Class<TableCGMinerNodeSettings> modelSettingsClass) {
        super(new PortType[] { BufferedDataTable.TYPE }, 
        		new PortType[] { CausalGraphPortObject.TYPE },
        		"Causal Graph JS View", modelSettingsClass);
    }

	
	protected AbstractJSONPortObject mine(BufferedDataTable table, final ExecutionContext exec) throws Exception{
    	logger.info("Begin: Causal Graph Miner (Table)");
    	String tClassifier = m_settings.t_classifier;
    	String eClassifier = m_settings.e_classifier;
    	HybridCGMinerSettings settings = getConfiguration();
		TraceVariantsLog variants = new TraceVariantsTable(table, settings, tClassifier, eClassifier);
		HybridCGMiner miner = new HybridCGMiner(null, null, variants, settings);
		ExtendedCausalGraph cg = miner.mineFCG();
    	CausalGraphPortObject pnPO = new CausalGraphPortObject(cg);
    	
    	logger.info("End: Causal Graph miner");
    	
    	return pnPO;
    }

    public ExtendedCausalGraph getCG() {
    	return cg; 
    }
   
   
    HybridCGMinerSettings getConfiguration() {
		HybridCGMinerSettings settings = new HybridCGMinerSettings();
    	settings.setFilterAcivityThreshold(m_settings.filter_a/100.0);
		settings.setTraceVariantsThreshold(m_settings.filter_t/100.0);
		settings.setSureThreshold(m_settings.t_certain);
		settings.setQuestionMarkThreshold(m_settings.t_uncertain);
		settings.setLongDepThreshold(m_settings.t_longDep);
		settings.setCausalityWeight(m_settings.weight);

		return settings;
	}


    protected PortObjectSpec[] configureOutSpec(DataTableSpec logSpec) {
        return new PortObjectSpec[]{new CausalGraphPortObjectSpec()};
    }

   
}

