package org.pm4knime.node.discovery.defaultminer;

import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectHolder;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.pm4knime.node.visualizations.jsgraphviz.JSGraphVizViewRepresentation;
import org.pm4knime.node.visualizations.jsgraphviz.JSGraphVizViewValue;
import org.pm4knime.node.visualizations.jsgraphviz.util.WebUIJSViewNodeModel;
import org.pm4knime.portobject.AbstractDotPanelPortObject;
import org.processmining.plugins.graphviz.dot.Dot;


public abstract class DefaultTableMinerNodeModel<S extends DefaultTableMinerSettings> extends WebUIJSViewNodeModel<S, JSGraphVizViewRepresentation, JSGraphVizViewValue> implements PortObjectHolder {

	protected DefaultTableMinerNodeModel(PortType[] inPortTypes, PortType[] outPortTypes, String view_name, Class<S> modelSettingsClass) {
		super(inPortTypes, outPortTypes, view_name, modelSettingsClass);
	}

	
	protected BufferedDataTable logPO;
	protected PortObject pmPO;
	protected S m_settings;
	
	
	@Override
    protected PortObject[] performExecuteCreatePortObjects(final PortObject svgImageFromView,
        final PortObject[] inObjects, final ExecutionContext exec) throws Exception {
        return new PortObject[]{pmPO};
    }
	
	@Override
	protected void performExecuteCreateView(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		
		logPO = (BufferedDataTable)inObjects[0];
		pmPO = mine(logPO, exec);
		
		final String dotstr;
		JSGraphVizViewRepresentation representation = getViewRepresentation();

		synchronized (getLock()) {
			AbstractDotPanelPortObject port_obj = (AbstractDotPanelPortObject) pmPO;
			Dot dot =  port_obj.getDotPanel().getDot();
			dotstr = dot.toString();
		}
		representation.setDotstr(dotstr);

	}
	
	
	protected abstract PortObject mine(BufferedDataTable log, final ExecutionContext exec) throws Exception; 
	
	
	@Override
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs, final S modelSettings) throws InvalidSettingsException {

		m_settings = modelSettings;
		if (!inSpecs[0].getClass().equals(DataTableSpec.class))
			throw new InvalidSettingsException("Input is not a valid Table!");
		DataTableSpec logSpec = (DataTableSpec) inSpecs[0];
		if(modelSettings.e_classifier == null || modelSettings.t_classifier == null)
			throw new InvalidSettingsException("Classifiers are not set! Please open the dialog and configure the node!");
		return configureOutSpec(logSpec);
	}


	protected abstract PortObjectSpec[] configureOutSpec(DataTableSpec logSpec);	
		
	
	public PortObject[] getInternalPortObjects() {
		return new PortObject[] {logPO};
	}

	
	public void setInternalPortObjects(PortObject[] portObjects) {
		logPO = (BufferedDataTable) portObjects[0];
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
	
}
