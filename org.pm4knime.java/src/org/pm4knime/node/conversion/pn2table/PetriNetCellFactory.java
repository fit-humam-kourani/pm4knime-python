package org.pm4knime.node.conversion.pn2table;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.knime.core.data.DataCell;
import org.knime.core.data.DataCellFactory.FromComplexString;
import org.knime.core.data.DataCellFactory.FromInputStream;
import org.knime.core.data.DataType;
import org.knime.core.data.convert.DataCellFactoryMethod;
import org.pm4knime.util.PetriNetUtil;
import org.knime.core.data.v2.ReadValue;
import org.knime.core.data.v2.ValueFactory;
import org.knime.core.data.v2.WriteValue;
import org.knime.core.table.access.StringAccess.StringReadAccess;
import org.knime.core.table.access.StringAccess.StringWriteAccess;
import org.knime.core.table.schema.DataSpec;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;

public final class PetriNetCellFactory implements ValueFactory<StringReadAccess, StringWriteAccess>, FromComplexString, FromInputStream {

    public static final DataType TYPE = DataType.getType(PetriNetCell.class);

    @Override
    @DataCellFactoryMethod(name = "String (Petri Net)")
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
    	AcceptingPetriNet anet = PetriNetUtil.importFromStream(input);
    	return new PetriNetCell(anet);
    }

	
	@Override
	public ReadValue createReadValue(final StringReadAccess access) {
	    return new ReadValue() {
	        @Override
	        public DataCell getDataCell() {
	            String stringValue = access.getStringValue();
	            PetriNetCellFactory f = new PetriNetCellFactory();
	            return f.createCell(stringValue);
	        }
	    };
	}

	@Override
	public WriteValue<PetriNetValue> createWriteValue(StringWriteAccess access) {
		// TODO Auto-generated method stub
		return new PetriNetWriteValue(access);
	}

	@Override
	public DataSpec getSpec() {
		// TODO Auto-generated method stub
		return DataSpec.stringSpec();
	}
	
//	public static class PetriNetReadValue implements ReadValue {
//
//		private String string_value;
//
//		PetriNetReadValue(final StringReadAccess structAccess) {
//			string_value = structAccess.getStringValue();
//		}
//
//        @Override
//		public DataCell getDataCell() {
//			PetriNetCellFactory f = new PetriNetCellFactory();
//			return f.createCell(string_value);
//		}
//	}
	
	public static final class PetriNetWriteValue implements WriteValue<PetriNetValue> {
		private StringWriteAccess string_access;

		PetriNetWriteValue(final StringWriteAccess structAccess) {
			string_access = structAccess;
		}

		@Override
		public void setValue(final PetriNetValue value) {
			string_access.setStringValue(value.getPetriNetString());
		}
	}
	
}
