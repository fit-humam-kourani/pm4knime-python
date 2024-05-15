//package org.pm4knime.node.visualizations.jsgraphviz.util;
//
//
//import org.processmining.models.graphbased.directed.DirectedGraphEdge;
//import org.processmining.models.graphbased.directed.petrinet.elements.Place;
//import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
//import org.processmining.models.semantics.petrinet.Marking;
//import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.TreeSet;
//
//public class DotStringParser {
//
//    public static class Node {
//        String id;
//        String type;
//        String label; 
//        boolean initial_marking;
//        boolean final_marking;
//
//        public Node(String id, String type, String label, boolean initial_marking, boolean final_marking) {
//            this.id = id;
//            this.type = type;
//            this.label = label; 
//            this.initial_marking = initial_marking;
//            this.final_marking = final_marking;
//        }
//    }
//
//    public static class Link {
//        String source;
//        String target;
//
//        public Link(String source, String target) {
//            this.source = source;
//            this.target = target;
//        }
//    }
//
//    public static Map<String, List<?>> parseDotString(String dotstring) {
//    	List<Node> nodes = new ArrayList<>();
//    	
//    	String nodePattern = "\"(.+?)\" \\[(.*?)\\]";
//    	Pattern pattern = Pattern.compile(nodePattern);
//    	Matcher matcher = pattern.matcher(dotstring);
//
//    	while (matcher.find()) {
//    	    String attributes = matcher.group(2);
//
//    	    String[] attrs = attributes.split(",\\s*");
//    	    Map<String, String> attrMap = new HashMap<>();
//    	    for (String attr : attrs) {
//    	        String[] keyValue = attr.split("=");
//    	        if (keyValue.length == 2) {
//    	            attrMap.put(keyValue[0], keyValue[1].replaceAll("\"", ""));
//    	        }
//    	    }
//
//    	    String label = attrMap.getOrDefault("label", "");
//    	    String id = attrMap.getOrDefault("id", "");
//    	    String fillcolor = attrMap.getOrDefault("fillcolor", "");
//    	    String shape = attrMap.getOrDefault("shape", "");
//    	    String peripheries = attrMap.getOrDefault("peripheries", "");
//    	    
//    	    String type;
//        	boolean i_marking = false;
//        	boolean f_marking = false;
//        	
//        	if (shape.equals("box")) {
//        		type = "transition";
//        	} else if (shape.equals("circle")) {
//        		type = "place";
//        		if (fillcolor.equals("#80ff00")) {
//        			i_marking = true;
//        		}
//        		if (peripheries.equals("2")) {
//        			f_marking = true;
//        		}
//        	} else {
//        		continue;
//        	}
//        	nodes.add(new Node(id, type, label, i_marking, f_marking));
//  	         
//        }
//
//        Map<String, List<?>> result = new HashMap<>();
//        result.put("nodes", nodes);
//        
//        String edgePattern = "\"(.+?)\" -> \"(.+?)\".*?\\]";
//        Pattern pEdges = Pattern.compile(edgePattern);
//        Matcher mEdges = pEdges.matcher(dotstring);
//
//        List<Link> links = new ArrayList<>();
//        while (mEdges.find()) {
//            links.add(new Link(mEdges.group(1), mEdges.group(2)));
//        }
//
//        result.put("links", links);
//
//        return result;
//   }
//}