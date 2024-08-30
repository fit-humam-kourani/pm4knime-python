package org.pm4knime.node.io.log.reader.MXMLImporter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.EnumSet;
import java.util.Optional;

import org.apache.commons.digester.plugins.PluginContext;
import org.apache.commons.lang3.StringUtils;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XLog;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.context.ports.PortsConfiguration;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.webui.node.dialog.defaultdialog.DefaultNodeSettings;
import org.knime.filehandling.core.connections.FSConnection;
import org.knime.filehandling.core.connections.FSFileSystem;
import org.knime.filehandling.core.connections.FSFiles;
import org.knime.filehandling.core.defaultnodesettings.FileSystemHelper;
import org.knime.filehandling.core.defaultnodesettings.filechooser.reader.ReadPathAccessor;
import org.knime.filehandling.core.defaultnodesettings.filechooser.reader.SettingsModelReaderFileChooser;
import org.knime.filehandling.core.defaultnodesettings.status.NodeModelStatusConsumer;
import org.pm4knime.node.io.log.reader.StreamImport;
import org.pm4knime.node.io.log.reader.XesConvertToXLogAlgorithm;
import org.pm4knime.node.visualizations.jsgraphviz.JSGraphVizViewRepresentation;
import org.pm4knime.portobject.XLogPortObject;
import org.pm4knime.util.connectors.prom.PM4KNIMEGlobalContext;
import org.pm4knime.util.defaultnode.ReaderNodeSettings;
import org.processmining.plugins.log.OpenNaiveLogFilePlugin;
import org.xesstandard.model.XesLog;
import org.xesstandard.xml.XesXmlParserLenient;

import org.knime.filehandling.core.defaultnodesettings.status.StatusMessage.MessageType;
//
//public class MXMLImporterNodeModel extends NodeModel {
//
////	String[] extensions;
//
//	PortObjectSpec m_spec;
//	private final static String[] CFG_METHODS = { "OPEN NAIVE", "IEEE Lenient" };
//
//	private MXMLImporterNodeSettings m_settings;
//
//	private final Class<MXMLImporterNodeSettings> m_settingsClass;
//
//	XLogPortObject m_Port;
//
//	private final SettingsModelReaderFileChooser m_sourceModel;
//	// transport errors / warning to node
//	private final NodeModelStatusConsumer m_statusConsumer = new NodeModelStatusConsumer(
//			EnumSet.of(MessageType.ERROR, MessageType.WARNING));
//
////	public MXMLImporterNodeModel(Class<ReaderNodeSettings> class1, String[] types, PortObjectSpec portObjectSpec,
////			PortType[] portTypes, String view_name) {
////		super(null, portTypes);
////		m_spec = portObjectSpec;
////		extensions = types;
////		this.m_settingsClass = null;
////	}
//	
//	protected MXMLImporterNodeModel(final PortsConfiguration portsConfig) {
//		/**
//		 * Here we specify how many data input and output tables the node should have.
//		 * In this case its one input and one output table.
//		 */
//		super(portsConfig.getInputPorts(), portsConfig.getOutputPorts());
//		this.m_settingsClass = null;
//		m_sourceModel = createSourceModel(portsConfig);
//
//	}
//	
//	static final SettingsModelReaderFileChooser createSourceModel(final PortsConfiguration portsConfig) {
//		return new SettingsModelReaderFileChooser(SOURCE_FILE, portsConfig, MXMLImporterNodeFactory.CONNECTION_INPUT_PORT_GRP_NAME, mode, DEFAULT_FS, new String[]{".mxml", ".mxml.gz"});
//	}
//
//	public MXMLImporterNodeModel(Class<MXMLImporterNodeSettings> class1) {
//		// TODO Auto-generated constructor stub
//		super(new PortType[] {}, new PortType[] { XLogPortObject.TYPE });
//		m_settingsClass = class1;
//		this.m_sourceModel = null;
//	}
//
//	protected XLogPortObject write_file_from_stream(InputStream inputStream) {
//
////		try {
////			ExtendedHybridPetrinet net = new ExtendedHybridPetrinet("Hybrid Petri Net");
////			HybridPetriNetUtil.importHybridPetrinetFromStream(inputStream, net);
////			hpnObj = new HybridPetriNetPortObject(net);
////		} catch (Exception e) {
////			e.printStackTrace();
////		}
//
//		StreamImport streams = new StreamImport();
//
//		final ReadPathAccessor readAccessor = m_sourceModel.createReadPathAccessor();
//		File file = null;
//		try {
//			file = readAccessor.getFSPaths(m_statusConsumer).get(0).toFile();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (InvalidSettingsException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		XLog result = null;
//		if (m_settings.readMethod.equals(CFG_METHODS[0])) {
//			// Open Naive can read multiple types of event log!!
//			PluginContext context = (PluginContext) PM4KNIMEGlobalContext.instance()
//					.getFutureResultAwarePluginContext(OpenNaiveLogFilePlugin.class);
//			try {
//				result = (XLog) streams.importFileStream((org.processmining.framework.plugin.PluginContext) context,
//						inputStream, file.getName(), file.length(), file);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		} else if (m_settings.readMethod.equals(CFG_METHODS[1])) {
//			// this parser imports all extensions in event log.
//			XesXmlParserLenient lenientParser = new XesXmlParserLenient();
//			if (lenientParser.canParse(file)) {
//				XesLog xlog = lenientParser.parse(inputStream);
//				XesConvertToXLogAlgorithm convertor = new XesConvertToXLogAlgorithm();
//				try {
//					result = convertor.convertToLog(xlog, null);
//				} catch (CanceledExecutionException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//
//		XLogPortObject logPO = new XLogPortObject();
//		logPO.setLog(result);
//
//		return logPO;
//	}
//
////	@Override
////	protected PortObjectSpec[] configureOutSpec() {
////		return new PortObjectSpec[] { new XLogPortObjectSpec() };
////	}
//
//	@Override
//	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
//		m_settings = m_settingsClass;
//		validate();
//		return configureOutSpec();
//	}
//
//	protected PortObjectSpec[] configureOutSpec() {
//		return null;
//	}
//
//	protected void performExecuteCreateView(PortObject[] inObjects, ExecutionContext exec) throws Exception {
//
//		exec.checkCanceled();
//		try {
//
//			var fsLocation = m_settings.m_file.getFSLocation();
//			FSConnection connection = FileSystemHelper.retrieveFSConnection(Optional.empty(), fsLocation)
//					.orElseThrow(() -> new IOException("File system is not available"));
//			FSFileSystem<?> fileSystem = connection.getFileSystem();
//			final Path filePath = fileSystem.getPath(fsLocation);
//			InputStream inputStream = FSFiles.newInputStream(filePath);
//
//			m_Port = write_file_from_stream(inputStream);
//			// JSGraphVizViewRepresentation representation = getViewRepresentation();
//			// representation.setJSONString(m_Port.getJSON());
//
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		exec.checkCanceled();
//
//	}
//
//	public void validate() throws InvalidSettingsException {
//
//		if (StringUtils.isEmpty(m_settings.m_file.getFSLocation().getPath())) {
//			throw new InvalidSettingsException("Please specify a path to the file to read!");
//		}
//
//		if (!StringUtils.endsWith(m_settings.m_file.getFSLocation().getPath(), "mxml")) {
//			throw new InvalidSettingsException("Unsupported file type: Please select a " + ".mxml" + " file");
//		}
//
//	}
//
//	@Override
//	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
//			throws IOException, CanceledExecutionException {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
//			throws IOException, CanceledExecutionException {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void saveSettingsTo(NodeSettingsWO settings) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void reset() {
//		// TODO Auto-generated method stub
//
//	}
//
//}



// package org.pm4knime.node.io.log.reader.MXMLImporter;

//import java.io.InputStream;
//
//import org.knime.core.node.port.PortObjectSpec;
//import org.knime.core.node.port.PortType;
//import org.pm4knime.portobject.AbstractJSONPortObject;
//import org.pm4knime.portobject.PetriNetPortObject;
//import org.pm4knime.portobject.PetriNetPortObjectSpec;
//import org.pm4knime.portobject.XLogPortObject;
//import org.pm4knime.portobject.XLogPortObjectSpec;
//import org.pm4knime.util.PetriNetUtil;
//import org.pm4knime.util.defaultnode.LogReaderNodeModel;
//import org.pm4knime.util.defaultnode.ReaderNodeModel;
//import org.pm4knime.util.defaultnode.ReaderNodeSettings;
//
//public class MXMLImporterNodeModel extends LogReaderNodeModel {
//	
//	PortObjectSpec m_spec;
//	private final static String[] CFG_METHODS = { "OPEN NAIVE", "IEEE Lenient" };
//
//	private MXMLImporterNodeSettings m_settings;
//
//
////	public MXMLImporterNodeModel(Class<DefaultNodeSettings> class1) {
////		 super(class1, new String[] { ".mxml" }, new XLogPortObjectSpec(),
////				new PortType[] { XLogPortObject.TYPE }, "JS View");
////		// super(new PortType[]{XLogPortObject.TYPE}, {});
////		// m_settings = class1;
////	}
//	
//
//	public MXMLImporterNodeModel(Class<MXMLImporterNodeSettings> class1) {
//		// TODO Auto-generated constructor stub
//		super(class1, new String[] { ".mxml" }, new XLogPortObjectSpec(), 
//				new PortType[] { XLogPortObject.TYPE }, "JS View");
//	}
//
//	@Override
//	protected XLogPortObject write_log_file_from_stream(InputStream inputStream) {
//		
//		StreamImport streams = new StreamImport();
//
////		final ReadPathAccessor readAccessor = m_sourceModel.createReadPathAccessor();
////		File file = null;
////		try {
////			file = readAccessor.getFSPaths(m_statusConsumer).get(0).toFile();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (InvalidSettingsException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//
//		XLog result = null;
//		long num = 0;
//		if (m_settings.readMethod.equals(CFG_METHODS[0])) {
//			// Open Naive can read multiple types of event log!!
//			PluginContext context = (PluginContext) PM4KNIMEGlobalContext.instance()
//					.getFutureResultAwarePluginContext(OpenNaiveLogFilePlugin.class);
//			try {
//				result = (XLog) streams.importFromStream((org.processmining.framework.plugin.PluginContext) context,
//						inputStream, m_settings.m_file.toString(), num);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
////		} else if (m_settings.readMethod.equals(CFG_METHODS[1])) {
////			// this parser imports all extensions in event log.
////			XesXmlParserLenient lenientParser = new XesXmlParserLenient();
////			if (lenientParser.canParse(m_settings.m_file)) {
////				XesLog xlog = lenientParser.parse(inputStream);
////				XesConvertToXLogAlgorithm convertor = new XesConvertToXLogAlgorithm();
////				try {
////					result = convertor.convertToLog(xlog, null);
////				} catch (CanceledExecutionException e) {
////					// TODO Auto-generated catch block
////					e.printStackTrace();
////				}
////			}
//	
//		
//
//		XLogPortObject logPO = new XLogPortObject();
//		logPO.setLog(result);
//
//		return logPO;
//		}
//
//	@Override
//	protected PortObjectSpec[] configureOutSpec() {
//		return new PortObjectSpec[] { new XLogPortObjectSpec() };
//	}
//
//}

import java.io.File;
import java.io.InputStream;
import java.util.EnumSet;

import org.deckfour.xes.model.XLog;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.context.ports.PortsConfiguration;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.knime.filehandling.core.connections.FSCategory;
import org.knime.filehandling.core.connections.FSFiles;
import org.knime.filehandling.core.connections.FSPath;
import org.knime.filehandling.core.defaultnodesettings.EnumConfig;
import org.knime.filehandling.core.defaultnodesettings.filechooser.reader.ReadPathAccessor;
import org.knime.filehandling.core.defaultnodesettings.filechooser.reader.SettingsModelReaderFileChooser;
import org.knime.filehandling.core.defaultnodesettings.filtermode.SettingsModelFilterMode.FilterMode;
import org.knime.filehandling.core.defaultnodesettings.status.NodeModelStatusConsumer;
import org.knime.filehandling.core.defaultnodesettings.status.StatusMessage.MessageType;
import org.pm4knime.node.io.log.reader.StreamImport;
import org.pm4knime.node.io.log.reader.XesConvertToXLogAlgorithm;
import org.pm4knime.portobject.XLogPortObject;
import org.pm4knime.portobject.XLogPortObjectSpec;
import org.pm4knime.util.connectors.prom.PM4KNIMEGlobalContext;
import org.pm4knime.util.defaultnode.DefaultNodeModel;
import org.processmining.plugins.log.OpenNaiveLogFilePlugin;
import org.xesstandard.model.XesLog;
import org.xesstandard.xml.XesXmlParserLenient;

/**
 * <code>NodeModel</code> for the "MXMLImporter" node.
 *
 * @author tbd
 */
public class MXMLImporterNodeModel extends DefaultNodeModel {

	/**
	 * The logger is used to print info/warning/error messages to the KNIME console
	 * and to the KNIME log file. Retrieve it via 'NodeLogger.getLogger' providing
	 * the class of this node model.
	 */
	private static final NodeLogger LOGGER = NodeLogger.getLogger(MXMLImporterNodeModel.class);
	private static final EnumConfig<FilterMode> mode = EnumConfig.create(FilterMode.FILE);
	private static final EnumSet<FSCategory> DEFAULT_FS = //
			EnumSet.of(FSCategory.LOCAL, FSCategory.MOUNTPOINT, FSCategory.RELATIVE, FSCategory.CUSTOM_URL, FSCategory.HUB_SPACE);

	private final static String[] CFG_METHODS = { "OPEN NAIVE", "IEEE Lenient" };
	// private SettingsModelString m_method = createMethodModel();
	// private SettingsModelString m_fileName = createFileNameModel();
	// private static final String SOURCE_FILE = "sourcefile";
	private final SettingsModelReaderFileChooser m_sourceModel;
	// transport errors / warning to node
	private final NodeModelStatusConsumer m_statusConsumer = new NodeModelStatusConsumer(
			EnumSet.of(MessageType.ERROR, MessageType.WARNING));

	/**
	 * Constructor for the node model.
	 */
	protected MXMLImporterNodeModel(final Class<MXMLImporterNodeSettings> class1) {
		/**
		 * Here we specify how many data input and output tables the node should have.
		 * In this case its one input and one output table.
		 */
		super(new PortType[] { XLogPortObject.TYPE }, null);
		m_sourceModel = null;

	}

	public static SettingsModelString createMethodModel() {
		return new SettingsModelString("Read Method", CFG_METHODS[0]);
	}

	public static SettingsModelString createFileNameModel() {
		return new SettingsModelString("File Name", "");
	}

	@Override
	protected PortObject[] execute(final PortObject[] inObjects, final ExecutionContext exec) throws Exception {
		LOGGER.info("start: import event log");
		// check the reading type and method for it??
		// Are they 1-1 relation?? One method only corresponds to one reading methods??
		// if there is 1 -n , we can choose them!!
		final ReadPathAccessor readAccessor = m_sourceModel.createReadPathAccessor();
		final File file = readAccessor.getFSPaths(m_statusConsumer).get(0).toFile();
		final FSPath inputPath = readAccessor.getFSPaths(m_statusConsumer).get(0);
		m_statusConsumer.setWarningsIfRequired(this::setWarningMessage);
		StreamImport streams = new StreamImport();
		InputStream inputStream = FSFiles.newInputStream(inputPath);
		// the difference of format and then later the read plugin should also change
		XLog result = null;
		if (m_method.getStringValue().equals(CFG_METHODS[0])) {
			// Open Naive can read multiple types of event log!!
			PluginContext context = (org.apache.commons.digester.plugins.PluginContext) PM4KNIMEGlobalContext.instance()
					.getFutureResultAwarePluginContext(OpenNaiveLogFilePlugin.class);
			checkCanceled(exec);
			// result = (XLog) streams.importFileStream(context, inputStream, file.getName(), file.length(), file);

			result = (XLog) streams.importFromStream((org.processmining.framework.plugin.PluginContext) context,
					inputStream, file.getName(), 0);

		} else if (m_method.getStringValue().equals(CFG_METHODS[1])) {
			// this parser imports all extensions in event log.
			XesXmlParserLenient lenientParser = new XesXmlParserLenient();
			if (lenientParser.canParse(file)) {
				XesLog xlog = lenientParser.parse(inputStream);
				XesConvertToXLogAlgorithm convertor = new XesConvertToXLogAlgorithm();
				result = convertor.convertToLog(xlog, exec);
			}
		}

		XLogPortObject logPO = new XLogPortObject();

		logPO.setLog(result);

		// how to use this classifiers??
		LOGGER.info("end: import event log");
		return new PortObject[] { logPO };
	}

	/**
	 * Create source model
	 */
	static final SettingsModelReaderFileChooser createSourceModel(final PortsConfiguration portsConfig) {
		return new SettingsModelReaderFileChooser(SOURCE_FILE, portsConfig, "File System Connection", mode, DEFAULT_FS, new String[]{".mxml", ".mxml.gz"});
	}

	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs) throws InvalidSettingsException {
		XLogPortObjectSpec outSpec = new XLogPortObjectSpec();
		m_sourceModel.configureInModel(inSpecs, m_statusConsumer);

		return new PortObjectSpec[] { outSpec };
	}

	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {
		m_method.saveSettingsTo(settings);
		m_sourceModel.saveSettingsTo(settings);
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		m_method.validateSettings(settings);
		m_sourceModel.validateSettings(settings);

	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {
		m_method.loadSettingsFrom(settings);
		m_sourceModel.loadSettingsFrom(settings);
	}

	public static String[] getCFG_METHODS() {
		return CFG_METHODS;
	}

}

