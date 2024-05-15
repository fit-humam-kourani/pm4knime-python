package org.pm4knime.node.visualizations.jsgraphviz;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.knime.base.node.mine.decisiontree2.image.DecTreeToImageNodeFactory;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.js.core.JSONViewContent;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class JSGraphVizViewRepresentation extends JSONViewContent {
	
	

	public final int pseudoIdentifier = (new Random()).nextInt();
	DecTreeToImageNodeFactory f;
	
	private static final String DOT_DATA = "dotstr";
	private static final String PAR_DOT_DATA = "parseddot";
	private String m_dotstr;
	private String parseddot;

	@Override
	public void saveToNodeSettings(NodeSettingsWO settings) {
		try {
			settings.addString(DOT_DATA, m_dotstr);
			settings.addString(PAR_DOT_DATA, parseddot);
	    } catch (Exception ex) {
	        // do nothing
	    }   
	}

	@Override
	public void loadFromNodeSettings(NodeSettingsRO settings) throws InvalidSettingsException {
		try {
			m_dotstr = settings.getString(DOT_DATA);
			parseddot = settings.getString(PAR_DOT_DATA);
	    } catch (Exception ex) {
	        // do nothing
	    }   
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		
		JSGraphVizViewRepresentation other = (JSGraphVizViewRepresentation)obj;
		return new EqualsBuilder()
				.append(m_dotstr, other.m_dotstr)
				.append(parseddot, other.parseddot)
                .isEquals();
	}

	@Override
	public int hashCode() {
		 return new HashCodeBuilder().append(m_dotstr)
				 .append(parseddot)
				 .toHashCode();
	}

	public String getDotstr() {
		return m_dotstr;
	}
	
	public String getParseddot() {
		return parseddot;
	}
	
	public void setJSONString(Map<String, List<?>> json) {
		
		Gson gson = new Gson();
        String jsonData = gson.toJson(json);
		this.parseddot = jsonData;
		
		//System.out.println(parseddot);
		
	}
}

