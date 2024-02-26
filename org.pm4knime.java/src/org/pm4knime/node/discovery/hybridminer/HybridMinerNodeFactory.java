package org.pm4knime.node.discovery.hybridminer;

import org.knime.core.node.wizard.WizardNodeFactoryExtension;
import org.knime.core.webui.node.impl.WebUINodeConfiguration;
import org.knime.core.webui.node.impl.WebUINodeFactory;
import org.pm4knime.node.visualizations.jsgraphviz.JSGraphVizViewRepresentation;
import org.pm4knime.node.visualizations.jsgraphviz.JSGraphVizViewValue;
import org.pm4knime.portobject.CausalGraphPortObject;
import org.pm4knime.portobject.HybridPetriNetPortObject;

@SuppressWarnings("restriction")
public final class HybridMinerNodeFactory extends WebUINodeFactory<HybridMinerNodeModel> implements WizardNodeFactoryExtension<HybridMinerNodeModel, JSGraphVizViewRepresentation, JSGraphVizViewValue> {

	HybridMinerNodeModel node;

	private static final WebUINodeConfiguration CONFIG = WebUINodeConfiguration.builder()
			.name("Hybrid Petri Net Miner")
			.icon("../category-discovery.png")
			.shortDescription("This node implements the second step of the Hybrid Miner to discover a hybrid Petri net from a causal graph.")
			.fullDescription("This node implements the second step of the Hybrid Miner to discover a hybrid Petri net from a causal graph. The hybrid miner combines the best of formal and informal modeling notations by discovering hybrid Petri nets. The hybrid miner converts the edges of the input causal graph into formal places if there is enough evidence in the data justifying adding formal constraints. For vague structures where formal constraints cannot be justified, causal relations are depicted in the final hybrid Petri net as informal edges.")
			.modelSettingsClass(HybridMinerNodeSettings.class)//
			.addInputPort("Causal Graph", CausalGraphPortObject.TYPE ,"a causal graph")//
			.addOutputPort("Hybrid Petri Net", HybridPetriNetPortObject.TYPE, "a hybrid Petri net")//
			.nodeType(NodeType.Learner)
			.build();


	public HybridMinerNodeFactory() {
		super(CONFIG);
	}


	protected HybridMinerNodeFactory(final WebUINodeConfiguration configuration) {
		super(configuration);
	}


	@Override
	public HybridMinerNodeModel createNodeModel() {
		node = new HybridMinerNodeModel(HybridMinerNodeSettings.class);
		return node;
	}

}

