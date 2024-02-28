package org.pm4knime.node.discovery.cgminer.table;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.NodeFactory.NodeType;
import org.knime.core.node.wizard.WizardNodeFactoryExtension;
import org.knime.core.webui.node.impl.WebUINodeConfiguration;
import org.knime.core.webui.node.impl.WebUINodeFactory;
import org.pm4knime.node.visualizations.jsgraphviz.JSGraphVizViewRepresentation;
import org.pm4knime.node.visualizations.jsgraphviz.JSGraphVizViewValue;
import org.pm4knime.portobject.CausalGraphPortObject;


@SuppressWarnings("restriction")
public class TableCGMinerNodeFactory extends WebUINodeFactory<TableCGMinerNodeModel> implements WizardNodeFactoryExtension<TableCGMinerNodeModel, JSGraphVizViewRepresentation, JSGraphVizViewValue> {
	TableCGMinerNodeModel node;

	public static final WebUINodeConfiguration CONFIG = WebUINodeConfiguration.builder()
			.name("Causal Graph Miner")
			.icon("../../category-discovery.png")
			.shortDescription("This node implements the first step of the Hybrid Miner to discover a causal graph from an event table.")
			.fullDescription("This node implements the first step of the Hybrid Miner to discover a causal graph from an event table. \r\n"
					+ "			A causal graph consists of nodes representing activities and two types of directed edges connecting nodes. \r\n"
					+ "			Certain edges represent strong causal dependencies and uncertain edges represent weak dependencies. \r\n"
					+ "			A third type of edges is used to represent long-term dependencies.") 
			.modelSettingsClass(TableCGMinerNodeSettings.class)//
			.addInputPort("Table", BufferedDataTable.TYPE ,"an event table")//
			.addOutputPort("Causal Graph", CausalGraphPortObject.TYPE, "a causal graph")//
			.nodeType(NodeType.Learner)
			.build();


	public TableCGMinerNodeFactory() {
		super(CONFIG);
	}


	protected TableCGMinerNodeFactory(final WebUINodeConfiguration configuration) {
		super(configuration);
	}


	@Override
	public TableCGMinerNodeModel createNodeModel() {
		node = new TableCGMinerNodeModel(TableCGMinerNodeSettings.class);
		return node;
	}

}

