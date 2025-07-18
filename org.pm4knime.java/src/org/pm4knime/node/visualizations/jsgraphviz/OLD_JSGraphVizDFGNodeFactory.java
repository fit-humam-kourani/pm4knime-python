//package org.pm4knime.node.visualizations.jsgraphviz;
//
//import org.knime.core.node.wizard.WizardNodeFactoryExtension;
//import org.pm4knime.portobject.DfgMsdPortObject;
//import org.pm4knime.portobject.PetriNetPortObject;
//import org.knime.core.node.NodeDialogPane;
//import org.knime.core.node.NodeFactory;
//import org.knime.core.node.NodeView;
//import org.knime.core.node.port.PortType;
//
///**
// * This is an example implementation of the node factory of the
// * "JSGraphViz" node.
// *
// * @author 
// */
//public class JSGraphVizDFGNodeFactory 
//        extends NodeFactory<JSGraphVizAbstractModel> implements WizardNodeFactoryExtension<JSGraphVizAbstractModel, JSGraphVizViewRepresentation, JSGraphVizViewValue> {
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public JSGraphVizAbstractModel createNodeModel() {
//    	
//    	PortType[] IN_TYPES = {DfgMsdPortObject.TYPE};
//        return new JSGraphVizAbstractModel(IN_TYPES, "DFG JS View");
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public int getNrNodeViews() {
//		// The number of views the node should have, in this cases there is none.
//        return 0;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public NodeView<JSGraphVizAbstractModel> createNodeView(final int viewIndex,
//            final JSGraphVizAbstractModel nodeModel) {
//		// We return null as this example node does not provide a view. Also see "getNrNodeViews()".
//		return null;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public boolean hasDialog() {
//		// Indication whether the node has a dialog or not.
//        return false;
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    public NodeDialogPane createNodeDialogPane() {
//		// This example node has a dialog, hence we create and return it here. Also see "hasDialog()".
//        return null;
//    }
//
//}