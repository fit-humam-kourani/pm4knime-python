package org.pm4knime.node.conversion.pn2bpmn;

import java.io.File;
import java.io.IOException;
import java.util.Map;


import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectHolder;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.js.core.node.AbstractSVGWizardNodeModel;
import org.mozilla.javascript.Context;
import org.pm4knime.node.visualizations.jsgraphviz.JSGraphVizViewRepresentation;
import org.pm4knime.node.visualizations.jsgraphviz.JSGraphVizViewValue;
import org.pm4knime.portobject.AbstractDotPanelPortObject;

import org.pm4knime.portobject.PetriNetPortObject;
import org.pm4knime.portobject.PetriNetPortObjectSpec;


import org.pm4knime.portobject.ProcessTreePortObject;
import org.pm4knime.portobject.ProcessTreePortObjectSpec;

import org.pm4knime.portobject.BpmnPortObject;
import org.pm4knime.portobject.BpmnPortObjectSpec;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetFactory;



import org.processmining.models.graphbased.directed.bpmn.BPMNDiagram;
import org.processmining.models.graphbased.directed.bpmn.elements.Activity;
import org.processmining.models.graphbased.directed.bpmn.elements.Flow;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetGraph;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.processtree.ProcessTree;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.plugins.converters.PetriNetToBPMNConverterPlugin;
import org.processmining.plugins.cpnet.visualization.Place;
/**
 * <code>NodeModel</code> for the "PN2BPMNConverter" node. It converts a process tree into Petri net.
 * Since the conversion is guaranteed to work, so no need of NodeDialog. 
 *
 * @author Sanjida Islam Ivy
 */
public class PN2BPMNConverterNodeModel extends AbstractSVGWizardNodeModel<JSGraphVizViewRepresentation, JSGraphVizViewValue> implements PortObjectHolder {
	private static final PluginContext contex = null;
	// Define class-level variables
    protected PortObject bpmnPO; // Store the BPMN PortObject
    protected PetriNetPortObject pnPO; // Store the Petri net PortObject
    private PetriNetPortObjectSpec m_inSpec;
    
    
    
    
    /**
     * Constructor for the node model.
     */
    public PN2BPMNConverterNodeModel() {
        // Call the constructor of the parent class with input and output PortTypes and a node name
        super(new PortType[] { PetriNetPortObject.TYPE },
                new PortType[] { BpmnPortObject.TYPE }, "BPMN JS View");
                 
    }

    
    
   // Override the method to create PortObjects during execution
    @Override
    protected PortObject[] performExecuteCreatePortObjects(final PortObject svgImageFromView,
            final PortObject[] inObjects, final ExecutionContext exec) throws Exception {
        return new PortObject[]{bpmnPO};
    }
	
    // Override the method to create the node view during execution
    @Override
    protected void performExecuteCreateView(PortObject[] inObjects, ExecutionContext exec) throws Exception {
    	pnPO = (PetriNetPortObject) inObjects[0]; // Get the Petrinet PortObject from input
    	AcceptingPetriNet  petrinet = pnPO.getANet();// Get the Petrinet from the PortObject
    	
    	// Get the PetrinetGraph from the AcceptingPetriNet
    	PetrinetGraph petrinetGraph = petrinet.getNet();
    	
    	PetriNetToBPMNConverterPlugin converter = new PetriNetToBPMNConverterPlugin(); // Create a converter
    	
    	// Create a ProM PluginContext to use its features
    	
    	
    	// Call the 'convert' method to convert the Petri net to BPMN
    	Object[] conversionResult = converter.convert(this.contex, petrinetGraph);

    	
    	// You can then access the BPMN diagram, transition conversion map, and place conversion map from the result
    	BPMNDiagram bpmnDiagram = (BPMNDiagram) conversionResult[0];
    	Map<String, Activity> transitionConversionMap = (Map<String, Activity>) conversionResult[1];
    	Map<Place, Flow> placeConversionMap = (Map<Place, Flow>) conversionResult[2];

    	
    	
    	
    	// Create a BpmnPortObject from the AcceptingBPMN
    	BpmnPortObject bpmnPO = new BpmnPortObject(bpmnDiagram);
    	
    	String dotstr;
        JSGraphVizViewRepresentation representation = getViewRepresentation(); // Get the view representation
        // Synchronize access to prevent concurrent modification
        synchronized (getLock()) {
            AbstractDotPanelPortObject port_obj = (AbstractDotPanelPortObject) bpmnPO;
            Dot dot = port_obj.getDotPanel().getDot(); // Get DOT representation of the BPMN
            dotstr = dot.toString(); // Convert DOT to a string
        }
        representation.setDotstr(dotstr); // Set the DOT string in the view representation
    }
    
   
   
    
    
    // Configure the node based on input port object specifications
    @Override
    protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {
        // Check if the input is a valid PetriNetPortObjectSpec
        PetriNetPortObjectSpec spec = (PetriNetPortObjectSpec) inSpecs[0];

        if (!spec.getClass().equals(PetriNetPortObjectSpec.class)) {
            throw new InvalidSettingsException("Input is not a valid Petri Net!");
        }

        m_inSpec = spec;

        return new PortObjectSpec[]{null};
    }

    
    // Configure the output PortObject specification
    protected PortObjectSpec[] configureOutSpec(PetriNetPortObjectSpec logSpec) {
        return new PortObjectSpec[]{new PetriNetPortObjectSpec()};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    }
    
        
    @Override
	protected void performReset() {
	}

	@Override
	protected void useCurrentValueAsDefault() {
	}

	
	@Override
    protected boolean generateImage() {
        return false;
    }
	
	
	@Override
	public JSGraphVizViewRepresentation createEmptyViewRepresentation() {
		return new JSGraphVizViewRepresentation();
	}

	@Override
	public JSGraphVizViewValue createEmptyViewValue() {
		return new JSGraphVizViewValue();
	}
	
	@Override
	public boolean isHideInWizard() {
		return false;
	}

	@Override
	public void setHideInWizard(boolean hide) {
	}

	@Override
	public ValidationError validateViewValue(JSGraphVizViewValue viewContent) {
		return null;
	}

	@Override
	public void saveCurrentValue(NodeSettingsWO content) {
	}
	
	
	@Override
	public String getJavascriptObjectID() {
		return "org.pm4knime.node.visualizations.jsgraphviz.component";
	}


	@Override
	public PortObject[] getInternalPortObjects() {
		// TODO Auto-generated method stub
		return new PortObject[] {pnPO};
	}


	@Override
	public void setInternalPortObjects(PortObject[] portObjects) {
		pnPO = (PetriNetPortObject) portObjects[0];
		
	}
	
}

