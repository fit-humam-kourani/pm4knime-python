package org.pm4knime.portobject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.knime.core.node.NodeSettings;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectSpecZipInputStream;
import org.knime.core.node.port.PortObjectSpecZipOutputStream;
import org.pm4knime.node.conformance.replayer.table.helper.PNReplayerTableNodeSettings;


public class RepResultPortObjectSpecTable implements PortObjectSpec {
	
	
private static final String ZIP_ENTRY_NAME = "RepResultPortObjectSpecTable";
	

//	private PNReplayerTableNodeSettings replayerSettings;
	
	public RepResultPortObjectSpecTable() {}
//	public RepResultPortObjectSpecTable(PNReplayerTableNodeSettings replayerSettings) {
//		this.replayerSettings = replayerSettings;
//	}
	
	@Override 
	public JComponent[] getViews() {
		// TODO do it later when implementing the whole result
		// for spec, currently, no view, but later should generate a table for it to show the parameter
		return null;
	}
	
	
	public static final class RepResultPortObjectSpecSerializerTable extends PortObjectSpecSerializer<RepResultPortObjectSpecTable> {

		@Override
		public void savePortObjectSpec(RepResultPortObjectSpecTable portObjectSpec, PortObjectSpecZipOutputStream out)
				throws IOException {
			// TODO Auto-generated method stub
//			out.putNextEntry(new ZipEntry(ZIP_ENTRY_NAME));
//			// how to save m_parameter with out, but not the settings?? 
//			
//			PNReplayerTableNodeSettings settings = portObjectSpec.getReplayerSettings();
//			NodeSettings tmp = new NodeSettings(ZIP_ENTRY_NAME + ":" 
//					+ settings.getClass().getSimpleName() + ":");
//			NodeSettingsRO settings_ro = new NodeSettings("node_settings");
//			DefaultNodeSettings.loadSettings(settings_ro, settings.getClass());
//			
//			ObjectOutputStream objOut = new ObjectOutputStream(out);
//			objOut.writeObject(tmp);
			
		}
		

		
		@Override
		public RepResultPortObjectSpecTable loadPortObjectSpec(PortObjectSpecZipInputStream in) throws IOException {
//			// TODO there is no need to store the Spec here..
//			ZipEntry nextEntry = in.getNextEntry();
//			if ((nextEntry == null) || !nextEntry.getName().equals(ZIP_ENTRY_NAME)) {
//				throw new IOException("Expected zip entry '" + ZIP_ENTRY_NAME + "' not present");
//			}
//			
//			ObjectInputStream objIn = new ObjectInputStream(in);
//			try {
//				NodeSettings tmp = (NodeSettings) objIn.readObject();
//				String[] configStringArray = tmp.getKey().split(":");
//				
//				if(configStringArray[1].equals(SMAlignmentReplayParameterTable.class.getSimpleName())) {
//					
//					// how to get the config name and then use it to load the values??
//					PNReplayerTableNodeSettings settings = new SMAlignmentReplayParameterTable(configStringArray[2]);
//					m_parameter.loadSettingsFrom(tmp);
//					return new RepResultPortObjectSpecTable(m_parameter);
//				}else {
//					SMAlignmentReplayerParameterWithCTTable m_parameter = new SMAlignmentReplayerParameterWithCTTable(configStringArray[2]);
//					m_parameter.loadSettingsFrom(tmp);
//					return new RepResultPortObjectSpecTable(m_parameter);
//				}
//				
//			} catch (ClassNotFoundException | InvalidSettingsException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			return new RepResultPortObjectSpecTable();
		}
		
	}



//	public void setReplayerSettings(PNReplayerTableNodeSettings m_modelSettings) {
//		this.replayerSettings = m_modelSettings;
//		
//	}
//	
//	
//	public PNReplayerTableNodeSettings getReplayerSettings() {
//		if(replayerSettings !=null)
//			return replayerSettings;
//		else
//			return new PNReplayerTableNodeSettings();
//		
//	}
	

}
