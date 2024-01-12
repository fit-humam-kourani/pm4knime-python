package org.pm4knime.node.conversion.pn2table;

import java.util.List;

import org.knime.core.data.DataValue;

public interface PetriNetValue extends DataValue {
    
	String getPetriNetString();
    List<String> getPlaces();
    List<String> getTransitions();
    List<String> getEdges();
    String getInitialMarking();
    List<String> getFinalMarking();
}