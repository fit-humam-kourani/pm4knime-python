package org.pm4knime.node.discovery.heuritsicsminer;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;

/**
 * This is an example implementation of the node factory of the
 * "HeuristicsMiner" node.
 *
 * @author Kefang Ding
 */
public class HeuristicsMinerNodeFactory 
        extends NodeFactory<HeuristicsMinerNodeModel> {

    /**
     * {@inheritDoc}
     */
    @Override
    public HeuristicsMinerNodeModel createNodeModel() {
		// Create and return a new node model.
        return new HeuristicsMinerNodeModel();
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
    public NodeView<HeuristicsMinerNodeModel> createNodeView(final int viewIndex,
            final HeuristicsMinerNodeModel nodeModel) {
		// We return null as this example node does not provide a view. Also see "getNrNodeViews()".
//    	JPanel viewPanel = new JPanel();
//		viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));
//		viewPanel.setPreferredSize(new Dimension(1000,600));
//		viewPanel.setName("Heuristics Net");
//    	return new HeuristicsMinerNodeView(nodeModel, viewPanel);
    	return null;
		
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
		// Indication whether the node has a dialog or not.
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeDialogPane createNodeDialogPane() {
		// This example node has a dialog, hence we create and return it here. Also see "hasDialog()".
        return new HeuristicsMinerNodeDialog();
    }

}

