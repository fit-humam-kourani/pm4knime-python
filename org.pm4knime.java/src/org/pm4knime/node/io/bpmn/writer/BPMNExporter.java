package org.pm4knime.node.io.bpmn.writer;

import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.elements.Activity;
import org.processmining.models.graphbased.directed.bpmn.elements.Event;
import org.processmining.models.graphbased.directed.bpmn.elements.Gateway;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BPMNExporter {
	
	public static String generateValidXmlId() {
	    return "id" + UUID.randomUUID().toString().replace("-", "");
	}

    public static String convertToXML(BPMNDiagram diagram) {
        StringBuilder xml = new StringBuilder();
        Map<String, StringBuilder> elementConnections = new HashMap<>();


        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<bpmn:definitions xmlns:bpmn=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" ")
           .append("xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" ")
           .append("xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" ")
           .append("xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\" ")
           .append("xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" ")
           .append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" ")
           .append("targetNamespace=\"http://www.example.com/bpmn20\" ")
           .append("expressionLanguage=\"http://www.w3.org/1999/XPath\" ")
           .append("typeLanguage=\"http://www.w3.org/2001/XMLSchema\">\n");

        String processId = generateValidXmlId();
        xml.append("\t<bpmn:process id=\"").append(processId).append("\" isExecutable=\"false\">\n");

        for (Flow flow : diagram.getFlows()) {
            String sourceId = flow.getSource().getId().toString();
            String targetId = flow.getTarget().getId().toString();

            elementConnections.computeIfAbsent(sourceId, k -> new StringBuilder())
                              .append("\t\t\t<bpmn:outgoing>").append(flow.getEdgeID()).append("</bpmn:outgoing>\n");
            elementConnections.computeIfAbsent(targetId, k -> new StringBuilder())
                              .append("\t\t\t<bpmn:incoming>").append(flow.getEdgeID()).append("</bpmn:incoming>\n");
        }

        for (Activity activity : diagram.getActivities()) {
            xml.append("\t\t<bpmn:task id=\"").append(activity.getId())
               .append("\" name=\"").append(escapeXML(activity.getLabel())).append("\">\n")
               .append(elementConnections.getOrDefault(activity.getId().toString(), new StringBuilder()))
               .append("\t\t</bpmn:task>\n");
        }
        
        for (Event event : diagram.getEvents()) {
            if (event.getEventType() == Event.EventType.START) {
                xml.append("\t\t<bpmn:startEvent id=\"").append(event.getId())
                   .append("\" name=\"\">\n")
                   .append(elementConnections.getOrDefault(event.getId().toString(), new StringBuilder()))
                   .append("\t\t</bpmn:startEvent>\n");
            } else if (event.getEventType() == Event.EventType.END) {
                xml.append("\t\t<bpmn:endEvent id=\"").append(event.getId())
                   .append("\" name=\"\">\n")
                   .append(elementConnections.getOrDefault(event.getId().toString(), new StringBuilder()))
                   .append("\t\t</bpmn:endEvent>\n");
            }
        }
        
        for (Gateway gateway : diagram.getGateways()) {
        	if (gateway.getGatewayType() == Gateway.GatewayType.PARALLEL) {
                xml.append("\t\t<bpmn:parallelGateway id=\"").append(gateway.getId())
                   .append("\" name=\"\">\n")
                   .append(elementConnections.getOrDefault(gateway.getId().toString(), new StringBuilder()))
                   .append("\t\t</bpmn:parallelGateway>\n");
        	} else if (gateway.getGatewayType() == Gateway.GatewayType.DATABASED) {
                xml.append("\t\t<bpmn:exclusiveGateway id=\"").append(gateway.getId())
                   .append("\" name=\"\">\n")
                   .append(elementConnections.getOrDefault(gateway.getId().toString(), new StringBuilder()))
                   .append("\t\t</bpmn:exclusiveGateway>\n");
            }  
        }
        
        for (Flow flow : diagram.getFlows()) {
            xml.append("\t\t<bpmn:sequenceFlow id=\"").append(flow.getEdgeID())
               .append("\" name=\"").append(escapeXML(flow.getLabel()))
               .append("\" sourceRef=\"").append(flow.getSource().getId())
               .append("\" targetRef=\"").append(flow.getTarget().getId()).append("\"/>\n");
        }

      
        xml.append("\t</bpmn:process>\n");
        
        
        xml.append("\t<bpmndi:BPMNDiagram id=\"").append(generateValidXmlId()).append("\" name=\"Diagram\">\n");
        xml.append("\t\t<bpmndi:BPMNPlane bpmnElement=\"").append(processId).append("\" id=\"").append(generateValidXmlId()).append("\">\n");

        for (Activity activity : diagram.getActivities()) {
            xml.append("\t\t\t<bpmndi:BPMNShape bpmnElement=\"").append(activity.getId())
               .append("\" id=\"").append(activity.getId()).append("_gui\">\n")
               .append("\t\t\t\t<omgdc:Bounds height=\"0\" width=\"0\" x=\"0\" y=\"0\"/>\n")
               .append("\t\t\t</bpmndi:BPMNShape>\n");
        }
        
        for (Event event : diagram.getEvents()) {
            if (event.getEventType() == Event.EventType.START || event.getEventType() == Event.EventType.END) {
            	xml.append("\t\t\t<bpmndi:BPMNShape bpmnElement=\"").append(event.getId())
                .append("\" id=\"").append(event.getId()).append("_gui\">\n")
                .append("\t\t\t\t<omgdc:Bounds height=\"0\" width=\"0\" x=\"0\" y=\"0\"/>\n")
                .append("\t\t\t</bpmndi:BPMNShape>\n");
            } 
        }
        
        for (Gateway gateway : diagram.getGateways()) {
            if (gateway.getGatewayType() == Gateway.GatewayType.DATABASED || gateway.getGatewayType() == Gateway.GatewayType.PARALLEL) {
            	xml.append("\t\t\t<bpmndi:BPMNShape bpmnElement=\"").append(gateway.getId())
                .append("\" id=\"").append(gateway.getId()).append("_gui\">\n")
                .append("\t\t\t\t<omgdc:Bounds height=\"0\" width=\"0\" x=\"0\" y=\"0\"/>\n")
                .append("\t\t\t</bpmndi:BPMNShape>\n");
            } 
        }
        
        for (Flow flow : diagram.getFlows()) {
            xml.append("\t\t\t<bpmndi:BPMNEdge bpmnElement=\"").append(flow.getEdgeID())
               .append("\" id=\"").append(flow.getEdgeID()).append("_gui\">\n")
               .append("\t\t\t\t<omgdi:waypoint x=\"0\" y=\"0\"/>\n")  
               .append("\t\t\t\t<omgdi:waypoint x=\"0\" y=\"0\"/>\n")  
               .append("\t\t\t</bpmndi:BPMNEdge>\n");
        }
        
        xml.append("\t\t</bpmndi:BPMNPlane>\n");
        xml.append("\t</bpmndi:BPMNDiagram>\n");

        xml.append("</bpmn:definitions>\n");

        return xml.toString().replaceAll("node ", "node_");
    }

    private static String escapeXML(String data) {
        return data.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                   .replace("\"", "&quot;").replace("'", "&apos;");
    }

}