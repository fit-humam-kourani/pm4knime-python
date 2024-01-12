package org.pm4knime.node.conversion.hpn2table;

import java.util.List;

import org.knime.core.data.DataValue;

public interface HybridPetriNetValue extends DataValue {
    
	String getHybridPetriNetString();
    List<String> getPlaces();
    List<String> getTransitions();
    List<String> getCertainEdges();
    List<String> getUncertainEdges();
    List<String> getPlaceEdges();
    List<String> getLongDepEdges();
    String getInitialMarking();
    List<String> getFinalMarking();
}
