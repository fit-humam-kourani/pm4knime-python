package org.pm4knime.node.io.petrinet.reader;

import java.io.InputStream;

import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.pm4knime.portobject.AbstractJSONPortObject;
import org.pm4knime.portobject.PetriNetPortObject;
import org.pm4knime.portobject.PetriNetPortObjectSpec;
import org.pm4knime.util.PetriNetUtil;
import org.pm4knime.util.defaultnode.ReaderNodeModel;
import org.pm4knime.util.defaultnode.ReaderNodeSettings;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;


public class PetrinetReaderNodeModel extends ReaderNodeModel {
    
    
    
	
    public PetrinetReaderNodeModel(Class<ReaderNodeSettings> class1) {
        super(class1, new String[] {".pnml"}, new PetriNetPortObjectSpec(), new PortType[] {PetriNetPortObject.TYPE}, "Petri Net JS View");
    }

	@Override
	protected AbstractJSONPortObject write_file_from_stream(InputStream inputStream) {
		AcceptingPetriNet anet = PetriNetUtil.importFromStream(inputStream);
		PetriNetPortObject pn_po = new PetriNetPortObject(anet);
		return pn_po;
	}

	@Override
	protected PortObjectSpec[] configureOutSpec() {
		return new PortObjectSpec[]{new PetriNetPortObjectSpec()};
	}

	
}

