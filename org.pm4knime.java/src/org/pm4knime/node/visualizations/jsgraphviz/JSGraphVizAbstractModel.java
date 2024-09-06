package org.pm4knime.node.visualizations.jsgraphviz;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectHolder;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.web.ValidationError;
import org.knime.core.node.port.image.ImagePortObject;
import org.knime.core.node.port.image.ImagePortObjectSpec;
import org.knime.base.data.xml.SvgCell;
import org.pm4knime.node.visualizations.jsgraphviz.util.WebUIJSViewNodeModel;
import org.pm4knime.portobject.AbstractJSONPortObject;
import org.pm4knime.util.defaultnode.EmptyNodeSettings;


public class JSGraphVizAbstractModel extends WebUIJSViewNodeModel<EmptyNodeSettings, JSGraphVizViewRepresentation, JSGraphVizViewValue> implements PortObjectHolder {
	
	private static PortType[] OUT_TYPES = {ImagePortObject.TYPE};
	private static PortType IN_TYPE;
	AbstractJSONPortObject port_obj;
	

	public JSGraphVizAbstractModel(PortType[] in_types, String view_name, Class<EmptyNodeSettings> modelSettingsClass) {
		// TODO Auto-generated constructor stub
		super(in_types, OUT_TYPES, view_name, modelSettingsClass);
		IN_TYPE = in_types[0];	
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
	public String getJavascriptObjectID() {
		return "org.pm4knime.node.visualizations.jsgraphviz.component";
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
	protected PortObjectSpec[] configure(PortObjectSpec[] inSpecs, final EmptyNodeSettings modelSettings) throws InvalidSettingsException {
		
		PortObjectSpec imageSpec = new ImagePortObjectSpec(SvgCell.TYPE);
        
        return new PortObjectSpec[]{imageSpec};
	}

	@Override
	protected void performExecuteCreateView(PortObject[] inObjects, ExecutionContext exec) throws Exception {
		final String dotstr;
		
		JSGraphVizViewRepresentation representation = getViewRepresentation();

		synchronized (getLock()) {
			
			port_obj = (AbstractJSONPortObject) inObjects[0];
			representation.setJSONString(port_obj.getJSON());
		}

	}

	@Override
	protected void performReset() {
	}

	@Override
	protected void useCurrentValueAsDefault() {
	}

	
	public PortObject[] getInternalPortObjects() {
		return new PortObject[] {port_obj};
	}

	
	public void setInternalPortObjects(PortObject[] portObjects) {
		port_obj = (AbstractJSONPortObject) portObjects[0];
	}
	
	@Override
    protected boolean generateImage() {
        return true;
    }
	
	@Override
    protected PortObject[] performExecuteCreatePortObjects(final PortObject svgImageFromView,
        final PortObject[] inObjects, final ExecutionContext exec) throws Exception {
        return new PortObject[]{svgImageFromView};
    }
	
}