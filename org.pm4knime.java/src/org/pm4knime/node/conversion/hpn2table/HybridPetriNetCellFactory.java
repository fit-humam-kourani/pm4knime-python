package org.pm4knime.node.conversion.hpn2table;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataCellFactory.FromComplexString;
import org.knime.core.data.DataCellFactory.FromInputStream;
import org.knime.core.data.DataType;
import org.knime.core.data.convert.DataCellFactoryMethod;
import org.pm4knime.util.HybridPetriNetUtil;
import org.knime.core.data.v2.ReadValue;
import org.knime.core.data.v2.ValueFactory;
import org.knime.core.data.v2.WriteValue;
import org.knime.core.table.access.StringAccess.StringReadAccess;
import org.knime.core.table.access.StringAccess.StringWriteAccess;
import org.knime.core.table.schema.DataSpec;
import org.processmining.extendedhybridminer.models.hybridpetrinet.ExtendedHybridPetrinet;

public final class HybridPetriNetCellFactory implements ValueFactory<StringReadAccess, StringWriteAccess>, FromComplexString, FromInputStream {

    public static final DataType TYPE = DataType.getType(HybridPetriNetCell.class);

    @Override
    @DataCellFactoryMethod(name = "String (Hybrid Petri Net)")
    public DataCell createCell(final String input) {
    	InputStream stream = new ByteArrayInputStream(input.getBytes());
        try {
        	return createCell(stream);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    @Override
    public DataType getDataType() {
        return TYPE;
    }

    @Override
    @DataCellFactoryMethod(name = "InputStream (SVG)")
    public DataCell createCell(final InputStream input) throws IOException {
    	ExtendedHybridPetrinet net = new ExtendedHybridPetrinet("Hybrid Petri Net");
    	try {
			HybridPetriNetUtil.importHybridPetrinetFromStream(input, net);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return new HybridPetriNetCell(net);
    }


	@Override
	public ReadValue createReadValue(StringReadAccess access) {
		// TODO Auto-generated method stub
		return new HybridPetriNetReadValue(access);
	}

	@Override
	public WriteValue<HybridPetriNetValue> createWriteValue(StringWriteAccess access) {
		// TODO Auto-generated method stub
		return new HybridPetriNetWriteValue(access);
	}

	@Override
	public DataSpec getSpec() {
		// TODO Auto-generated method stub
		return DataSpec.stringSpec();
	}
	
	public static class HybridPetriNetReadValue implements ReadValue {

		private String string_value;

		HybridPetriNetReadValue(final StringReadAccess structAccess) {
			string_value = structAccess.getStringValue();
		}

        @Override
		public DataCell getDataCell() {
			//				return m_factory.createGeoCell(getWKB(), getReferenceSystem());
			HybridPetriNetCellFactory f = new HybridPetriNetCellFactory();
			return f.createCell(string_value);
		}
	}
	
	public static final class HybridPetriNetWriteValue implements WriteValue<HybridPetriNetValue> {
		
		private StringWriteAccess string_access;

		HybridPetriNetWriteValue(final StringWriteAccess structAccess) {
			string_access = structAccess;
		}

		@Override
		public void setValue(final HybridPetriNetValue value) {
			string_access.setStringValue(value.getHybridPetriNetString());
		}
	}
	
}
