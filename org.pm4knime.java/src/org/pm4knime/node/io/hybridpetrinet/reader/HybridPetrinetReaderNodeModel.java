package org.pm4knime.node.io.hybridpetrinet.reader;

import java.io.InputStream;

import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.pm4knime.portobject.AbstractJSONPortObject;
import org.pm4knime.portobject.HybridPetriNetPortObject;
import org.pm4knime.portobject.HybridPetriNetPortObjectSpec;
import org.pm4knime.util.HybridPetriNetUtil;
import org.pm4knime.util.defaultnode.ReaderNodeModel;
import org.pm4knime.util.defaultnode.ReaderNodeSettings;
import org.processmining.extendedhybridminer.models.hybridpetrinet.ExtendedHybridPetrinet;

public class HybridPetrinetReaderNodeModel extends ReaderNodeModel {

	protected HybridPetriNetPortObject hpnObj;

	public HybridPetrinetReaderNodeModel(Class<ReaderNodeSettings> class1) {
		super(class1, new String[] { ".pnml" }, new HybridPetriNetPortObjectSpec(),
				new PortType[] { HybridPetriNetPortObject.TYPE }, "Petri Net JS View");
	}

	@Override
	protected AbstractJSONPortObject write_file_from_stream(InputStream inputStream) {

		try {
			ExtendedHybridPetrinet net = new ExtendedHybridPetrinet("Hybrid Petri Net");
			HybridPetriNetUtil.importHybridPetrinetFromStream(inputStream, net);
			hpnObj = new HybridPetriNetPortObject(net);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return hpnObj;
	}

	@Override
	protected PortObjectSpec[] configureOutSpec() {
		return new PortObjectSpec[] { new HybridPetriNetPortObjectSpec() };
	}
	
}