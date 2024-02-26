package org.pm4knime.node.conversion.pn2bpmn;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.wizard.WizardNodeFactoryExtension;
import org.pm4knime.node.visualizations.jsgraphviz.JSGraphVizViewRepresentation;
import org.pm4knime.node.visualizations.jsgraphviz.JSGraphVizViewValue;

/**
 * This is an example implementation of the node factory of the
 * "PN2BPMNConverter" node.
 *
 * @author Sanjida Islam Ivy
 */
public class PN2BPMNConverterNodeFactory extends NodeFactory<PN2BPMNConverterNodeModel> implements WizardNodeFactoryExtension<PN2BPMNConverterNodeModel, JSGraphVizViewRepresentation, JSGraphVizViewValue> {

    
	PN2BPMNConverterNodeModel node;
	
	/**
     * {@inheritDoc}
     */
    @Override
    public PN2BPMNConverterNodeModel createNodeModel() {
    	node = new PN2BPMNConverterNodeModel();
		return node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
		// The number of views the node should have, in this cases there is none.
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<PN2BPMNConverterNodeModel> createNodeView(final int viewIndex,
            final PN2BPMNConverterNodeModel nodeModel) {
		// We return null as this example node does not provide a view. Also see "getNrNodeViews()".
		return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
		return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
        return null;
    }

}

