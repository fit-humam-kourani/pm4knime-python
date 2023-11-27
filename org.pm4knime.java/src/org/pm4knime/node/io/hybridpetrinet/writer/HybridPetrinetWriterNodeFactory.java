package org.pm4knime.node.io.hybridpetrinet.writer;

import java.util.Optional;

import org.knime.core.node.ConfigurableNodeFactory;
import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeView;
import org.knime.core.node.context.NodeCreationConfiguration;
import org.knime.filehandling.core.port.FileSystemPortObject;
import org.pm4knime.portobject.HybridPetriNetPortObject;

/**
 * <code>NodeFactory</code> for the "PetrinetWriter" Node.
 * Write Petri net into file to implement the serialization.
 *
 * @author 
 */
public class HybridPetrinetWriterNodeFactory 
        extends ConfigurableNodeFactory<HybridPetrinetWriterNodeModel> {


	    private HybridPetrinetWriterNodeModel model;
	    public static final String CONNECTION_INPUT_PORT_GRP_NAME = "File System Connection";
	    static final String INPUT_PORT_GRP_NAME = "Hybrid Petri Net";


    /**
     * {@inheritDoc}
     */
    @Override
    public int getNrNodeViews() {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NodeView<HybridPetrinetWriterNodeModel> createNodeView(final int viewIndex,
            final HybridPetrinetWriterNodeModel nodeModel) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDialog() {
        return true;
    }

    @Override
	protected Optional<PortsConfigurationBuilder> createPortsConfigBuilder() {
		final PortsConfigurationBuilder builder = new PortsConfigurationBuilder();
        builder.addOptionalInputPortGroup(CONNECTION_INPUT_PORT_GRP_NAME, FileSystemPortObject.TYPE);
        builder.addFixedInputPortGroup(INPUT_PORT_GRP_NAME, HybridPetriNetPortObject.TYPE);
        return Optional.of(builder);
	}

	@Override
	protected HybridPetrinetWriterNodeModel createNodeModel(NodeCreationConfiguration creationConfig) {
		this.model = new HybridPetrinetWriterNodeModel(creationConfig);
		return this.model;
	}
	

	@Override
	protected NodeDialogPane createNodeDialogPane(NodeCreationConfiguration creationConfig) {
		return new HybridPetrinetWriterNodeDialog(creationConfig, this.model);
	}

}

