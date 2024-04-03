package org.pm4knime.node.conformance.table.fitness;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * This is an example implementation of the node factory of the
 * "ConformanceChecker" node.
 *
 * @author 
 */
public class OLD_FitnessCheckerNodeFactory 
        extends NodeFactory<OLD_FitnessCheckerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public OLD_FitnessCheckerNodeModel createNodeModel() {
		// Create and return a new node model.
        return new OLD_FitnessCheckerNodeModel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
		return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OLD_FitnessCheckerNodeView createNodeView(final int viewIndex,
            final OLD_FitnessCheckerNodeModel nodeModel) {
		JPanel viewPanel = new JPanel();
    	viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));
    	viewPanel.setName("Fitness Projection Panel");
		return new OLD_FitnessCheckerNodeView(nodeModel, viewPanel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
		// Indication whether the node has a dialog or not.
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
		// This example node has a dialog, hence we create and return it here. Also see "hasDialog()".
        return null;
    }

}

