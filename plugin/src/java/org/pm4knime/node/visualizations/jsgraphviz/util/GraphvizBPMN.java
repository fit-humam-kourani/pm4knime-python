package org.pm4knime.node.visualizations.jsgraphviz.util;

import java.awt.Color;

import java.util.ArrayList;

import java.util.Arrays;

import java.util.HashMap;

import java.util.HashSet;

import java.util.List;

import java.util.Set;

import java.util.stream.Collectors;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;

import org.processmining.models.graphbased.directed.bpmn.BPMNNode;

import org.processmining.models.graphbased.directed.bpmn.elements.Activity;
import org.processmining.models.graphbased.directed.bpmn.elements.CallActivity;
import org.processmining.models.graphbased.directed.bpmn.elements.Event;
import org.processmining.models.graphbased.directed.bpmn.elements.Event.EventType;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;

import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway.GatewayType;
import org.processmining.models.graphbased.directed.bpmn.elements.Event;

import org.processmining.plugins.graphviz.dot.Dot;

import org.processmining.plugins.graphviz.dot.Dot.GraphDirection;

import org.processmining.plugins.graphviz.dot.DotNode;

public class GraphvizBPMN {

	public interface EdgeInterface {

		void ConfigureEdge();

	}

	public static Dot convert(BPMNDiagram model) {

		Dot dot = new Dot();

		dot.setDirection(GraphDirection.leftRight);

		convert(dot, model);

		return dot;

	}

	private static class LocalDotNode extends DotNode {

		public LocalDotNode(String label) {

			super(label, null);

		}

		public LocalDotNode(Color color) {

			super("", null);

			setOption("shape", "circle");
		}

	}

	private static void convert(Dot dot, BPMNDiagram diagram) {

		HashMap<BPMNNode, DotNode> nodeMap = new HashMap<BPMNNode, DotNode>();
	
		for (Event event : diagram.getEvents()) {
			String label = "";
			DotNode eventNode = new LocalDotNode(label);
			eventNode.setOption("shape", "circle");
			if (event.getEventType().equals(EventType.START)) {
				eventNode.setOption("style", "filled");
				eventNode.setOption("fillcolor", "#80ff00");
				dot.addNode(eventNode);
				nodeMap.put((BPMNNode) event, eventNode);
			} else if (event.getEventType().equals(EventType.END)) {		
				eventNode.setOption("style", "filled");
				eventNode.setOption("fillcolor", "#FA8072");
                eventNode.setOption("penwidth", "4");
				dot.addNode(eventNode);
				nodeMap.put((BPMNNode) event, eventNode);		
			} else {
				eventNode.setOption("peripheries", "2");
				dot.addNode(eventNode);
				nodeMap.put((BPMNNode) event, eventNode);				
			}				
		}
		
		for (Activity activity : diagram.getActivities()) {

			String label = activity.getLabel();
			if (label == null) {
				label = "";
			}

			DotNode activityNode = new LocalDotNode(label);
			activityNode.setOption("shape", "box");
			dot.addNode(activityNode);
			nodeMap.put((BPMNNode) activity, activityNode);
		}
		
		for (CallActivity activity : diagram.getCallActivities()) {

			String label = activity.getLabel();
			if (label == null) {
				label = "";
			}

			DotNode activityNode = new LocalDotNode(label);
			activityNode.setOption("shape", "box");
			dot.addNode(activityNode);
			nodeMap.put((BPMNNode) activity, activityNode);
		}

		for (Gateway gateway : diagram.getGateways()) {

			String label = "";
			
			if (gateway.getGatewayType() == GatewayType.DATABASED) {
				label = "<<B>✕</B>>"; 
			} else if (gateway.getGatewayType() == GatewayType.PARALLEL) {
				label = "✚";
			} else if (gateway.getGatewayType() == GatewayType.COMPLEX) {
				label = "❋";
			} else if (gateway.getGatewayType() == GatewayType.INCLUSIVE) {
				label = "◯";
			} else if (gateway.getGatewayType() == GatewayType.EVENTBASED) {
				label = "⌾";
			} 
					
			DotNode gatewayNode = new LocalDotNode(label);
			gatewayNode.setOption("shape", "diamond");

			dot.addNode(gatewayNode);
			nodeMap.put((BPMNNode) gateway, gatewayNode);		

		}


		for (Flow flow : diagram.getFlows()) {

			BPMNNode source = flow.getSource();
			BPMNNode target = flow.getTarget();
			

			if (nodeMap.get(source) != null && nodeMap.get(target) != null) {
				String label = flow.getLabel();
				if (label == null) {
					label = "";
				}
				dot.addEdge(nodeMap.get(source), nodeMap.get(target), label);

			}

		}
		
	}

}
