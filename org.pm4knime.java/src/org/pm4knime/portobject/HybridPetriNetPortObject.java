package org.pm4knime.portobject;


import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
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
import org.pm4knime.node.visualizations.jsgraphviz.util.GraphvizHybridPetriNet;
import org.pm4knime.util.HybridPetriNetUtil;
import org.processmining.extendedhybridminer.models.hybridpetrinet.ExtendedHybridPetrinet;
import org.processmining.models.graphbased.directed.DirectedGraphEdge;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.graphviz.visualisation.DotPanel;


public class HybridPetriNetPortObject extends AbstractJSONPortObject {

	/**
	 * Define port type of objects of this class when used as PortObjects.
	 */
	public static final PortType TYPE = PortTypeRegistry.getInstance().getPortType(HybridPetriNetPortObject.class);
	public static final PortType TYPE_OPTIONAL =
			PortTypeRegistry.getInstance().getPortType(HybridPetriNetPortObject.class, true);
	
	private static final String ZIP_ENTRY_NAME = "HybridPetriNetPortObject";
	
	public static final String HYBRID_PETRI_NET_TEXT = "A Hybrid Petri net is a directed graph used to model processes. "
			+ "It consists of places, transitions, and directed arcs connecting them. A place is enabled if it it contains at least one token. "
			+ "A transition can only fire if all incoming places are enabled. After firing a transition, a token is consumed from all of its incoming places, "
			+ "and a token is produced in all of its outgoing places. The initial marking indicates the initial state of the Hybrid Petri net. "
			+ "Places that belong to the initial marking are marked by green tokens inside them. The final marking denotes the final state of the Hybrid Petri net. "
			+ "Places within the final marking are highlighted with a heavier border. A Hybrid Petri net can also contain arcs directly connecting transitions to indicate informal dependencies between them. "
			+ "We distinguish three types of informal arcs: (1) strong dependencies are represented by blue solid arcs (certain arcs); "
			+ "(2) weak dependencies are represented by red dotted arcs (uncertain arcs); "
			+ "(3) long-term dependencies are represented by orange solid arcs.";
	
	static ExtendedHybridPetrinet pn ;
	HybridPetriNetPortObjectSpec m_spec;
	
	public HybridPetriNetPortObject() {}
	
	public HybridPetriNetPortObject(ExtendedHybridPetrinet pn) {
		HybridPetriNetPortObject.pn = pn;
	}
	
	
	public ExtendedHybridPetrinet getPN() {
		return pn;
	}

	public void setPN(ExtendedHybridPetrinet net) {
		pn = net;
	}

	@Override
	public String getSummary() {
		return "Transitions: " + pn.getTransitions().size() + ", Places: " + pn.getPlaces().size();
	}

	public boolean equals(Object o) {
		return pn.equals(o);
	}
	
	
	@Override
	public HybridPetriNetPortObjectSpec getSpec() {
		if(m_spec!=null)
			return m_spec;
		return new HybridPetriNetPortObjectSpec();
	}

	public void setSpec(PortObjectSpec spec) {
		m_spec = (HybridPetriNetPortObjectSpec) spec;
	}
	
	@Override
	public JComponent[] getViews() {
//		if (pn != null) {
//			
//			PluginContext context = PM4KNIMEGlobalContext.instance().getPluginContext();
//			JComponent view = null;
//			try {
//				view = HybridPetrinetVisualizer.visualize(context, pn);
//				int n = view.getComponents().length - 1;
//				view.remove(n);
//				view.remove(n-1);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			view.setName("Hybrid Petri Net");
//			return new JComponent[] {view};
//		}
		
		return new JComponent[] {};
	}
	
    public DotPanel getDotPanel() {
		
		if(pn != null) {
			
			DotPanel navDot;
			navDot = new DotPanel(GraphvizHybridPetriNet.convert(pn));
			navDot.setName("Generated hybrid petri net");
			return navDot;
			
		}
		return null;
		
	}

	
	@Override
	protected void save(PortObjectZipOutputStream out, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		
		out.putNextEntry(new ZipEntry(ZIP_ENTRY_NAME));
		final ObjectOutputStream objOut = new ObjectOutputStream(out);
		
		objOut.writeInt(pn.getSurePlaceColor().getRGB());
		objOut.writeInt(pn.getSureColor().getRGB());
		objOut.writeInt(pn.getUnsureColor().getRGB());
		objOut.writeInt(pn.getLDColor().getRGB());
		
		HybridPetriNetUtil.exportHybridPetrinetToFile(objOut, pn);
		objOut.close();
		out.close();
	}

	@Override
	protected void load(PortObjectZipInputStream in, PortObjectSpec spec, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {
		final ZipEntry entry = in.getNextEntry();
		if (!ZIP_ENTRY_NAME.equals(entry.getName())) {
			throw new IOException("Failed to load Causal Graph port object. " + "Invalid zip entry name '" + entry.getName()
					+ "', expected '" + ZIP_ENTRY_NAME + "'.");
		}
		final ObjectInputStream objIn = new ObjectInputStream(in);
		try {
			setSpec((HybridPetriNetPortObjectSpec) spec);

			Color color1 = new Color(objIn.readInt());
			Color color2 = new Color(objIn.readInt());
			Color color3 = new Color(objIn.readInt());	
			Color color4 = new Color(objIn.readInt());	
						
			ExtendedHybridPetrinet net = new ExtendedHybridPetrinet("Hybrid Petri Net");
			HybridPetriNetUtil.importHybridPetrinetFromStream(objIn, net);
			
			net.updateSurePlaceColor(color1);
			net.updateSureColor(color2);
			net.updateUnsureColor(color3);
			net.updateLDColor(color4);
			setPN(net);
		} catch (Exception e) {
			e.printStackTrace();
		}

		in.close();
	}

	public static class HybridPetriNetPortObjectSerializer
			extends AbstractPortObject.AbstractPortObjectSerializer<HybridPetriNetPortObject> {

	}

	public Map<String, List<?>> getJSON() {
		Map<String, List<?>> result = new HashMap<>();
		
		Set<Place> finalMarkingPlaces = new TreeSet<Place>();
		for (Marking setMarkings : pn.setFinalMarkings())
			finalMarkingPlaces.addAll(setMarkings);	
		
		Marking init_marking = pn.setInitialMarking();
		
		List<Node> nodes = new ArrayList<>();
		
		for(Place place : pn.getPlaces()) {
			if(init_marking.contains(place))
				nodes.add(new PlaceNode(place.getId().toString(), "place", "", true, false));
			else if (finalMarkingPlaces.contains(place))
				nodes.add(new PlaceNode(place.getId().toString(), "place", "", false, true));
			else
				nodes.add(new PlaceNode(place.getId().toString(), "place", "", false, false));
		}
		
		for (Transition transition : pn.getTransitions())
		{
			String label = transition.getLabel();
			if (transition.isInvisible())
				nodes.add(new Node(transition.getId().toString(), "transition", ""));
			else 
				nodes.add(new Node(transition.getId().toString(), "transition", label));
		}
		
		result.put("nodes", nodes);
		
		List<Link> links = new ArrayList<>();
		
		for (DirectedGraphEdge<?, ?> edge : pn.getEdges())
		{
			String source = edge.getSource().getId().toString();
			String target = edge.getTarget().getId().toString();
			links.add(new Link(source, target, edge.getClass().getSimpleName()));
		}

		result.put("links", links);
		
		return result;
		
	}
}
