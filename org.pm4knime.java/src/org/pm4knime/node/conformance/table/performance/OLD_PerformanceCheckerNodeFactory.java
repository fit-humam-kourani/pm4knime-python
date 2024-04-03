//package org.pm4knime.node.conformance.table.performance;
//
//import javax.swing.BoxLayout;
//import javax.swing.JPanel;
//
//import org.knime.core.node.NodeDialogPane;
//import org.knime.core.node.NodeFactory;
//import org.knime.core.node.NodeView;
//
///**
// * This is an example implementation of the node factory of the
// * "PerformanceChecker" node.
// *
// * @author Kefang Ding
// */
//public class OLD_PerformanceCheckerNodeFactory 
//        extends NodeFactory<OLD_PerformanceCheckerNodeModel> {
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public OLD_PerformanceCheckerNodeModel createNodeModel() {
//		// Create and return a new node model.
//        return new OLD_PerformanceCheckerNodeModel();
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public int getNrNodeViews() {
//		// The number of views the node should have, in this cases there is none.
//        return 1;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public NodeView<OLD_PerformanceCheckerNodeModel> createNodeView(final int viewIndex,
//            final OLD_PerformanceCheckerNodeModel nodeModel) {
//		// We return null as this example node does not provide a view. Also see "getNrNodeViews()".
//    	JPanel viewPanel = new JPanel();
//    	viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.Y_AXIS));
//    	viewPanel.setName("Performance Projection Panel");
//		return new OLD_PerformanceCheckerNodeView(nodeModel, viewPanel);
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public boolean hasDialog() {
//		// Indication whether the node has a dialog or not.
//        return true;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public NodeDialogPane createNodeDialogPane() {
//		// This example node has a dialog, hence we create and return it here. Also see "hasDialog()".
//        return new OLD_PerformanceCheckerNodeDialog();
//    }
//
//}
//
