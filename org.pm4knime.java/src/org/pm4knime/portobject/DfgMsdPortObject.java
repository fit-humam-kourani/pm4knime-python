package org.pm4knime.portobject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;

import javax.swing.JComponent;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortObjectZipInputStream;
import org.knime.core.node.port.PortObjectZipOutputStream;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.processmining.models.graphbased.NodeID;
import org.processmining.plugins.graphviz.visualisation.DotPanel;
import org.processmining.plugins.inductiveminer2.helperclasses.graphs.IntGraph;
import org.processmining.plugins.inductiveminer2.plugins.DfgMsdImportPlugin;
import org.processmining.plugins.inductiveminer2.plugins.DfgMsdVisualisationPlugin;
import org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd.DfgMsd;

public class DfgMsdPortObject extends AbstractJSONPortObject {

	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(DfgMsdPortObject.class);
	private static final String ZIP_ENTRY_NAME = "DfgMsdPortObject";
	private static final int BUFFER_SIZE = 8192 * 4;
	private static final String CHARSET = Charset.defaultCharset().name();
	DfgMsd dfm;
	DfgMsdPortObjectSpec m_spec;
	public DfgMsdPortObject() {
	}

	public DfgMsdPortObject(DfgMsd dfm) {
		this.dfm = dfm;
	}

	public DfgMsd getDfgMsd() {
		return dfm;
	}

	public void setDfgMsd(DfgMsd dfm) {
		this.dfm = dfm;
	}

	@Override
	public String getSummary() {
		// TODO Auto-generated method stub
		return "Nodes: " + dfm.getDirectlyFollowsGraph().getNumberOfNodes();
	}

	@Override
	public PortObjectSpec getSpec() {
		// TODO Auto-generated method stub
		if(m_spec!=null)
			return m_spec;
		return new DfgMsdPortObjectSpec();
	}

	public void setSpec(PortObjectSpec spec) {
		m_spec = (DfgMsdPortObjectSpec) spec;
	}
	@Override
	public JComponent[] getViews() {
		// TODO it has view which is
		// we need to change the steps..
		//JComponent viewPanel = DfgMsdVisualisationPlugin.fancy((DirectlyFollowsGraph) dfm);
		//		JComponent viewPanel = getDotPanel();
		//		viewPanel.setName("Directly Follows Model");
		//		return new JComponent[] { viewPanel };
		return new JComponent[] { };
	}

	public DotPanel getDotPanel() {

		if(dfm != null) {
			DotPanel navDot = DfgMsdVisualisationPlugin.fancy(dfm);

			navDot.setName("Generated DFG Model");
			return navDot;

		}


		return null;

	}

	@Override
	protected void save(PortObjectZipOutputStream out, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		// TODO create own way to save the PortObject due to simplicity
		// we use ObjectOutputStream to save this object
		out.putNextEntry(new ZipEntry(ZIP_ENTRY_NAME));
		/*result.writeInt(dfm.getNumberOfActivities());
		for (String e : dfm.getAllActivities()) {
			result.writeUTF(e + "\n");
		}

		result.writeInt(dfm.getStartActivities().setSize());
		for (int activityIndex : dfm.getStartActivities()) {
			result.writeUTF(activityIndex + "x" + dfm.getStartActivities().getCardinalityOf(activityIndex) + "\n");
		}

		result.writeInt(dfm.getEndActivities().setSize());
		for (int activityIndex : dfm.getEndActivities()) {
			result.writeUTF(activityIndex + "x" + dfm.getEndActivities().getCardinalityOf(activityIndex) + "\n");
		}

		//dfg-edges
		{
			IntGraph g = dfm.getDirectlyFollowsGraph();
			long edges = 0;
			for (Iterator<Long> iterator = g.getEdges().iterator(); iterator.hasNext();) {
				iterator.next();
				edges++;
			}
			result.writeLong(edges);
			for (long edge : g.getEdges()) {
				long v = g.getEdgeWeight(edge);
				if (v > 0) {
					int source = g.getEdgeSource(edge);
					int target = g.getEdgeTarget(edge);
					result.writeUTF(source + ">");
					result.writeUTF(target + "x");
					result.writeLong(v);
				}
			}
		}

		//msd-edges
		if (dfm instanceof DfgMsd) {
			IntGraph g = ((DfgMsd) dfm).getMinimumSelfDistanceGraph();
			long edges = 0;
			for (Iterator<Long> iterator = g.getEdges().iterator(); iterator.hasNext();) {
				iterator.next();
				edges++;
			}
			result.writeLong(edges);
			for (long edge : g.getEdges()) {
				long v = g.getEdgeWeight(edge);
				if (v > 0) {
					int source = g.getEdgeSource(edge);
					int target = g.getEdgeTarget(edge);
					result.writeUTF(source + ">");
					result.writeUTF(target + "x");
					result.writeLong(v);
				}
			}
		}
		result.close();*/
		BufferedWriter result = new BufferedWriter(new OutputStreamWriter(out, CHARSET), BUFFER_SIZE);
		result.append(dfm.getNumberOfActivities() + "\n");
		for (String e : dfm.getAllActivities()) {
			result.append(e + "\n");
		}

		result.append(dfm.getStartActivities().setSize() + "\n");
		for (int activityIndex : dfm.getStartActivities()) {
			result.append(activityIndex + "x" + dfm.getStartActivities().getCardinalityOf(activityIndex) + "\n");
		}

		result.append(dfm.getEndActivities().setSize() + "\n");
		for (int activityIndex : dfm.getEndActivities()) {
			result.append(activityIndex + "x" + dfm.getEndActivities().getCardinalityOf(activityIndex) + "\n");
		}

		//dfg-edges
		{
			IntGraph g = dfm.getDirectlyFollowsGraph();
			long edges = 0;
			for (Iterator<Long> iterator = g.getEdges().iterator(); iterator.hasNext();) {
				iterator.next();
				edges++;
			}
			result.append(edges + "\n");
			for (long edge : g.getEdges()) {
				long v = g.getEdgeWeight(edge);
				if (v > 0) {
					int source = g.getEdgeSource(edge);
					int target = g.getEdgeTarget(edge);
					result.append(source + ">");
					result.append(target + "x");
					result.append(v + "\n");
				}
			}
		}

		//msd-edges
		if (dfm instanceof DfgMsd) {
			IntGraph g = ((DfgMsd) dfm).getMinimumSelfDistanceGraph();
			long edges = 0;
			for (Iterator<Long> iterator = g.getEdges().iterator(); iterator.hasNext();) {
				iterator.next();
				edges++;
			}
			result.append(edges + "\n");
			for (long edge : g.getEdges()) {
				long v = g.getEdgeWeight(edge);
				if (v > 0) {
					int source = g.getEdgeSource(edge);
					int target = g.getEdgeTarget(edge);
					result.append(source + ">");
					result.append(target + "x");
					result.append(v + "\n");
				}
			}
		}

		result.flush();
		result.close();
	}

	@Override
	protected void load(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		ZipEntry nextEntry = in.getNextEntry();
		if ((nextEntry == null) || !nextEntry.getName().equals(ZIP_ENTRY_NAME)) {
			throw new IOException("Expected zip entry '" + ZIP_ENTRY_NAME + "' not present");
		}

		//DfgMsdPortObject result = null;
		try {
			// they put layout information into context, if we want to show the them, 
			// we need to keep the context the same in load and save program. But how to do this??
			// that's why there is context in portObject. If we also save the context, what can be done??
			DfgMsd dfg = DfgMsdImportPlugin.readFile(in);
			//result = new DfgMsdPortObject(dfg);
			//result.setSpec(spec);
			dfm = dfg;
			setSpec(spec);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		in.close();

	}

	public static final class DfgMsdPortObjectSerializer extends AbstractPortObjectSerializer<DfgMsdPortObject> {
	}

	@Override
	public Map<String, List<?>> getJSON() {
		
		Map<String, List<?>> result = new HashMap<>();	
		Map<Integer, String> vertexToIDMapping = new HashMap<>();
		
		int numActivities = dfm.getNumberOfActivities();
		int startVertex = numActivities;
		int endVertex = numActivities + 1;
				
		vertexToIDMapping.put(startVertex, new NodeID().toString());
		vertexToIDMapping.put(endVertex, new NodeID().toString());
		
		List<Node> nodes = new ArrayList<>();
					
		for(int node : dfm.getDirectlyFollowsGraph().getNodes()) {
			NodeID id = new NodeID();
			vertexToIDMapping.put(node, id.toString());
			nodes.add(new PlaceNode(id.toString(), "activity", dfm.getActivityOfIndex(node), false, false));
		}
		
		nodes.add(new PlaceNode(vertexToIDMapping.get(startVertex), "artificial start", "start", true, false));
		nodes.add(new PlaceNode(vertexToIDMapping.get(endVertex), "artificial end", "end", false, true));
		
		result.put("nodes", nodes);

		List<LinkWithFrequency> links = new ArrayList<>();
		
		for (long edge : dfm.getDirectlyFollowsGraph().getEdges()) {
			int source = dfm.getDirectlyFollowsGraph().getEdgeSource(edge);
			int target = dfm.getDirectlyFollowsGraph().getEdgeTarget(edge);
			int weight = (int)dfm.getDirectlyFollowsGraph().getEdgeWeight(edge);
			links.add(new LinkWithFrequency(vertexToIDMapping.get(source), vertexToIDMapping.get(target), weight));
		}
				
		for (int start : dfm.getStartActivities()) {
			int source = startVertex;
			int target = start;
			int weight = (int) dfm.getStartActivities().getCardinalityOf(start);
			links.add(new LinkWithFrequency(vertexToIDMapping.get(source), vertexToIDMapping.get(target), weight));
		}
		
		for (int end : dfm.getEndActivities()) {
			int source = end;
			int target = endVertex;
			int weight = (int) dfm.getEndActivities().getCardinalityOf(end);
			links.add(new LinkWithFrequency(vertexToIDMapping.get(source), vertexToIDMapping.get(target), weight));
		}
				
		result.put("links", links);
		
		return result;
	}
}
