package org.pm4knime.node.io.processtree.reader;

import java.io.InputStream;

import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.pm4knime.portobject.AbstractJSONPortObject;
import org.pm4knime.portobject.ProcessTreePortObject;
import org.pm4knime.portobject.ProcessTreePortObjectSpec;
import org.pm4knime.util.defaultnode.ReaderNodeModel;
import org.pm4knime.util.defaultnode.ReaderNodeSettings;

public class ProcessTreeReaderNodeModel extends ReaderNodeModel {

	protected ProcessTreePortObject m_ptPort;
	ProcessTreePortObjectSpec m_spec = new ProcessTreePortObjectSpec();

	public ProcessTreeReaderNodeModel(Class<ReaderNodeSettings> class1) {
		super(class1, new String[] { ".ptml" }, new ProcessTreePortObjectSpec(),
				new PortType[] { ProcessTreePortObject.TYPE }, "Process Tree JS View");
	}

	@Override
	protected AbstractJSONPortObject write_file_from_stream(InputStream inputStream) {

		try {
			m_ptPort = new ProcessTreePortObject();
	    	m_ptPort.loadFromDefault(m_spec, inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return m_ptPort;
	}

	@Override
	protected PortObjectSpec[] configureOutSpec() {
		return new PortObjectSpec[] { new ProcessTreePortObjectSpec() };
	}

}