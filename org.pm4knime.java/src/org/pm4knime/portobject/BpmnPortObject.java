package org.pm4knime.portobject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.AbstractPortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.pm4knime.node.visualizations.jsgraphviz.util.GraphvizBPMN;
import org.processmining.models.graphbased.EdgeID;
import org.processmining.models.graphbased.NodeID;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.BPMNDiagramFactory;
import org.processmining.models.graphbased.directed.bpmn.BPMNEdge;
import org.processmining.models.graphbased.directed.bpmn.BPMNNode;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.models.graphbased.directed.bpmn.elements.Swimlane;
import org.processmining.plugins.bpmn.Bpmn;
import org.processmining.plugins.bpmn.parameters.BpmnSelectDiagramParameters;
import org.processmining.plugins.graphviz.visualisation.DotPanel;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;


import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.plugins.bpmn.BpmnDefinitions;
import javax.swing.SwingUtilities;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import org.processmining.contexts.uitopia.UIContext;

public class BpmnPortObject extends AbstractJSONPortObject {

	/**
	 * Define port type of objects of this class when used as PortObjects.
	 */
	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(BpmnPortObject.class);
	public static final PortType TYPE_OPTIONAL = PortTypeRegistry.getInstance().getPortType(BpmnPortObject.class, true);

	private static final String ZIP_ENTRY_NAME = "BpmnPortObject";

	static BPMNDiagram model;
	BpmnPortObjectSpec m_spec;

	public BpmnPortObject() {
	}

	public BpmnPortObject(BPMNDiagram bpmn) {
		this.model = bpmn;
	}

	public BPMNDiagram getBPMN() {
		return this.model;
	}

	public void setBPMN(BPMNDiagram model) {
		this.model = model;
	}

	@Override
	public String getSummary() {
		return this.model.toString();
	}

	public boolean equals(Object o) {
		return this.model.equals(o);
	}

	@Override
	public BpmnPortObjectSpec getSpec() {
		if (m_spec != null)
			return m_spec;
		return new BpmnPortObjectSpec();
	}

	public void setSpec(PortObjectSpec spec) {
		m_spec = (BpmnPortObjectSpec) spec;
	}

	@Override
	public JComponent[] getViews() {

		return new JComponent[] {};
	}

	public DotPanel getDotPanel() {

		if (this.model != null) {

			DotPanel navDot;
			navDot = new DotPanel(GraphvizBPMN.convert(this.model));
			navDot.setName("Generated BPMN Model");
			return navDot;

		}
		return null;

	}

	@Override
	protected void save(PortObjectZipOutputStream out, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {

		out.putNextEntry(new ZipEntry(ZIP_ENTRY_NAME));
		final ObjectOutputStream objOut = new ObjectOutputStream(out);
		// Export the BPMNDiagram object to an XML string.
		String xml;
		try {
			xml = exportBPMNDiagram(getBPMN());
			// Write the XML string to the ObjectOutputStream object.
			objOut.writeUTF(xml);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		objOut.close();
		out.close();
	}

	public static String exportBPMNDiagram(final BPMNDiagram diagram) throws Exception {
		   
		final UIContext context = new UIContext();
		final UIPluginContext uiPluginContext = context.getMainPluginContext();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(new MetalLookAndFeel());
				} catch (UnsupportedLookAndFeelException e) {
					throw new RuntimeException(e);
				}
			}
		});
		final BpmnDefinitions.BpmnDefinitionsBuilder definitionsBuilder = new BpmnDefinitions.BpmnDefinitionsBuilder(
				(PluginContext) uiPluginContext, diagram);
		final BpmnDefinitions definitions = new BpmnDefinitions("definitions", definitionsBuilder);
		final StringBuilder sb = new StringBuilder();
		sb.append(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"\n xmlns:dc=\"http://www.omg.org/spec/DD/20100524/DC\"\n xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\"\n xmlns:di=\"http://www.omg.org/spec/DD/20100524/DI\"\n xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n targetNamespace=\"http://www.omg.org/bpmn20\"\n xsi:schemaLocation=\"http://www.omg.org/spec/BPMN/20100524/MODEL BPMN20.xsd\">");
		sb.append(definitions.exportElements());
		sb.append("</definitions>");
		String result = sb.toString();
		result = result.replaceAll("\n", "&#10;");
		result = result.replaceAll(">&#10;", ">\n");
		result = result.replaceAll("\"&#10;", "\"\n");
		result = result.replaceFirst("<bpmndi:BPMNDiagram>.*</bpmndi:BPMNDiagram>", "");
		result = result.replaceAll("<[a-zA-Z]+:[a-zA-Z]+/>", "");
		
		
		List<String> tags = Arrays.asList("task", "endEvent", "startEvent"); 
		String enhancedXML = add_missing_connections(result, tags);

		System.out.println("enhancedXML");
		System.out.println(enhancedXML);
		

		return enhancedXML;
	}

	public static String add_missing_connections(String originalXML, List<String> tags) {
		    
		
		Map<String, List<String>> incomingConnections = new HashMap<>();
	    Map<String, List<String>> outgoingConnections = new HashMap<>();
		
	    Pattern pattern0 = Pattern.compile("<sequenceFlow\\s+(?:[^>]*?\\s+)?id=\"([^\"]+)\"(?:\\s+[^>]*?)?name=\"([^\\\"]*)\"(?:\\s+[^>]*?)?sourceRef=\"([^\\\"]+)\"(?:\\s+[^>]*?)?targetRef=\"([^\\\"]+)\"\\s*\\/\\>");
        Matcher matcher0 = pattern0.matcher(originalXML);

        while (matcher0.find()) {
        	String id = matcher0.group(1);
            String sourceRef = matcher0.group(3);
            String targetRef = matcher0.group(4);

            outgoingConnections.computeIfAbsent(sourceRef, k -> new ArrayList<>()).add(id);
	        incomingConnections.computeIfAbsent(targetRef, k -> new ArrayList<>()).add(id);
        }
		
        
        String res = originalXML;
        
        for (String tag : tags) {
        	String regex = "<"+ tag + "\\s+([^>]*?)id=\"(node_[a-f0-9\\-]+)\"([^>]*?)\\/>";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(res);
            StringBuffer enhancedXML = new StringBuffer();
      
        	while (matcher.find()) {
                String beforeId = matcher.group(1);
                String nodeId = matcher.group(2);
                String afterId = matcher.group(3);

                // Constructing incoming and outgoing connection tags
                StringBuilder connections = new StringBuilder();
                List<String> outgoings = outgoingConnections.getOrDefault(nodeId, List.of());
                List<String> incomings = incomingConnections.getOrDefault(nodeId, List.of());
                incomings.forEach(incoming -> connections.append("<incoming>").append(incoming).append("</incoming>"));
                outgoings.forEach(outgoing -> connections.append("<outgoing>").append(outgoing).append("</outgoing>"));

                // Reconstruct the task element with the new connection details
                String replacement = "<"+ tag + " " + beforeId + "id=\"" + nodeId + "\"" + afterId + ">"
                                     + connections + "</"+ tag + ">";
                matcher.appendReplacement(enhancedXML, replacement);
            }

            matcher.appendTail(enhancedXML);
            res = enhancedXML.toString();
        }
        

        return res;
    }
	

	@Override
	protected void load(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// Get the next entry in the zip file.
		final ZipEntry entry = in.getNextEntry();

		// Check if the entry name is correct.
		if (!ZIP_ENTRY_NAME.equals(entry.getName())) {
			// Throw an exception if the entry name is incorrect.
			throw new IOException("Failed to load BPMN port object. " + "Invalid zip entry name '" + entry.getName()
					+ "', expected '" + ZIP_ENTRY_NAME + "'.");
		}
		// Try to load the BPMN port object.
		try {
			// Set the port object spec.
			setSpec((BpmnPortObjectSpec) spec);

			// Import the BPMN diagram from the zip file.
			BPMNDiagram bpmn = importBPMNDiagram(in);

			// Set the BPMN diagram for the port object.
			setBPMN(bpmn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static BPMNDiagram importBPMNDiagram(InputStream inputStream) throws Exception {
		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		factory.setNamespaceAware(true);
		XmlPullParser xpp = factory.newPullParser();
		xpp.setInput(inputStream, null);
		int eventType = xpp.getEventType();
		Bpmn bpmn = new Bpmn();

		while (eventType != XmlPullParser.START_TAG) {
			eventType = xpp.next();
		}

		if (xpp.getName().equals(bpmn.tag)) {
			bpmn.importElement(xpp, bpmn);
		} else {
			bpmn.log(bpmn.tag, xpp.getLineNumber(), "Expected " + bpmn.tag + ", got " + xpp.getName());
		}

		// Create a new BPMN select diagram parameters object.
		final BpmnSelectDiagramParameters parameters = new BpmnSelectDiagramParameters();
		// Create a new BPMN diagram object.
		final BPMNDiagram bpmnDiagram = BPMNDiagramFactory.newBPMNDiagram("");
		// Create a map to store the mapping of node IDs to nodes.
		final Map<String, BPMNNode> id2node = new HashMap<String, BPMNNode>();
		// Create a map to store the mapping of lane IDs to lanes.
		final Map<String, Swimlane> id2lane = new HashMap<String, Swimlane>();

		// If the diagram parameter is set to NODIAGRAM, unmarshall the BPMN diagram
		// without a diagram.
		if (parameters.getDiagram() == BpmnSelectDiagramParameters.NODIAGRAM) {
			bpmn.unmarshall(bpmnDiagram, (Map) id2node, (Map) id2lane);
		} else {
			// Get the collection of elements to include in the diagram.
			final Collection<String> elements = (Collection<String>) parameters.getDiagram().getElements();
			// Unmarshall the BPMN diagram with the specified elements.
			bpmn.unmarshall(bpmnDiagram, (Collection) elements, (Map) id2node, (Map) id2lane);
		}
		return bpmnDiagram;
	}

	public static class BpmnPortObjectSerializer
			extends AbstractPortObject.AbstractPortObjectSerializer<BpmnPortObject> {

	}

	public static void exportBPMNDiagramToFile(OutputStream outStream, BPMNDiagram bpmn) throws Exception {
		// TODO Auto-generated method stub
		String result = BpmnPortObject.exportBPMNDiagram(bpmn);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outStream));
		System.out.println("================================================");
		System.out.println(result);
		bw.write(result);
		bw.close();

	}
	
	@Override
	public Map<String, List<?>> getJSON() {
		
		Map<String, List<?>> result = new HashMap<>();
		
		try {
			String value = BpmnPortObject.exportBPMNDiagram(model);
			String key = "xml"; 
			result.put(key, Collections.singletonList(value));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;	
		
	}
}
