package org.pm4knime.node.conversion.pn2table;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataCellDataInput;
import org.knime.core.data.DataCellDataOutput;
import org.knime.core.data.DataCellSerializer;
import org.knime.core.data.DataValue;
import org.knime.core.node.CanceledExecutionException;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;

@SuppressWarnings("serial")
public class PetriNetCell extends DataCell implements PetriNetValue {
	
	
    String pnString;
    List<String> places;
    List<String> transitions;
    List<String> edges;
	String iMarking;
	List<String> fMarking;

    public static final class PetriNetSerializer implements DataCellSerializer<PetriNetCell> {
        /**
         * {@inheritDoc}
         */
        @Override
        public void serialize(final PetriNetCell cell, final DataCellDataOutput output) throws IOException {
            try {
                output.writeUTF(cell.pnString);
                output.writeInt(cell.places.size());
                for (int i = 0; i < cell.places.size(); i++) {
                	output.writeUTF(cell.places.get(i));
                }
                output.writeInt(cell.transitions.size());
                for (int i = 0; i < cell.transitions.size(); i++) {
                	output.writeUTF(cell.transitions.get(i));
                }
                output.writeInt(cell.edges.size());
                for (int i = 0; i < cell.edges.size(); i++) {
                	output.writeUTF(cell.edges.get(i));
                }
                output.writeUTF(cell.iMarking);
                output.writeInt(cell.fMarking.size());
                for (int i = 0; i < cell.fMarking.size(); i++) {
                	output.writeUTF(cell.fMarking.get(i));
                }
            } catch (IOException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new IOException("Could not serialize Petri Net", ex);
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public PetriNetCell deserialize(final DataCellDataInput input) throws IOException {
            String pnString = input.readUTF();
            int num_places = input.readInt();
            List<String> places = new ArrayList<String>();
    		for (int i = 0; i < num_places; i++) {
    			places.add(input.readUTF());
    		}
    		int num_transitions = input.readInt();
    		List<String> transitions = new ArrayList<String>();
    		for (int i = 0; i < num_transitions; i++) {
    			transitions.add(input.readUTF());
    		}
    		int num_edges = input.readInt();
    		List<String> edges = new ArrayList<String>();
    		for (int i = 0; i < num_edges; i++) {
    			edges.add(input.readUTF());
    		}
    		String iM = input.readUTF();
     		int num_fM = input.readInt();
             List<String> fM = new ArrayList<String>();
     		for (int i = 0; i < num_fM; i++) {
     			fM.add(input.readUTF());
     		}
            return new PetriNetCell(pnString, places, transitions, edges, iM, fM);
        }
    }

	

    public static DataCellSerializer<PetriNetCell> getCellSerializer() {
        return new PetriNetSerializer();
    }

    
    public PetriNetCell(AcceptingPetriNet anet) {
    	try {
    		pnString = PN2XmlConverter.convert(anet);
    		places = new ArrayList<String>();
    		Iterator<Place> it = anet.getNet().getPlaces().iterator();
    		for (int i = 0; i < anet.getNet().getPlaces().size(); i++) {
    			places.add(it.next().getLabel());
    		}
    		transitions = new ArrayList<String>();
    		Iterator<Transition> it_2 = anet.getNet().getTransitions().iterator();
    		for (int i = 0; i < anet.getNet().getTransitions().size(); i++) {
    			transitions.add(it_2.next().getLabel());
    		}
    		edges = new ArrayList<String>();
    		Iterator<PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode>> it_edges = anet.getNet().getEdges().iterator();
    		for (int i = 0; i < anet.getNet().getEdges().size(); i++) {
    			PetrinetEdge<? extends PetrinetNode, ? extends PetrinetNode> e = it_edges.next();
    			edges.add(e.getSource().getLabel() + " --> " + e.getTarget().getLabel());
    		}
    		iMarking = anet.getInitialMarking().toString();
    		fMarking = new ArrayList<String>();
    		Iterator<Marking> it_fm = anet.getFinalMarkings().iterator();
    		for (int i = 0; i < anet.getFinalMarkings().size(); i++) {
    			fMarking.add(it_fm.next().toString());
    		}
    		Collections.sort(places);
    		Collections.sort(transitions);
    		Collections.sort(edges);
    		Collections.sort(fMarking);
    		
		} catch (CanceledExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public PetriNetCell(String pnString2, List<String> placeList, List<String> transitionList, List<String> edgeList, String iM, List<String> fM) {
		pnString = pnString2;
		places = placeList;
		transitions = transitionList;
		edges = edgeList;
		iMarking = iM;
		fMarking = fM;
		Collections.sort(places);
		Collections.sort(transitions);
		Collections.sort(edges);
		Collections.sort(fMarking);
	}

	public String toString() {
		return pnString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean equalsDataCell(final DataCell cell) {
    	return this.equalContent(cell);  
    }

    /**
     * {@inheritDoc}
     */
    protected boolean equalContent(final DataValue otherValue) {
        if (otherValue instanceof PetriNetValue) {
            PetriNetValue petriNetValue = (PetriNetValue) otherValue;
            return places.equals(petriNetValue.getPlaces()) && 
                   transitions.equals(petriNetValue.getTransitions()) && 
                   edges.equals(petriNetValue.getEdges()) && 
                   iMarking.equals(petriNetValue.getInitialMarking()) && 
                   fMarking.equals(petriNetValue.getFinalMarking());
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
    	return places.hashCode() + transitions.hashCode() + edges.hashCode() + iMarking.hashCode() + fMarking.hashCode();
    }

	
	@Override
	public String getPetriNetString() {
	    return this.pnString;
	}


	@Override
	public List<String> getPlaces() {
		// TODO Auto-generated method stub
		return this.places;
	}


	@Override
	public List<String> getTransitions() {
		// TODO Auto-generated method stub
		return this.transitions;
	}


	@Override
	public List<String> getEdges() {
		// TODO Auto-generated method stub
		return this.edges;
	}


	@Override
	public String getInitialMarking() {
		// TODO Auto-generated method stub
		return this.iMarking;
	}


	@Override
	public List<String> getFinalMarking() {
		// TODO Auto-generated method stub
		return this.fMarking;
	}

	

    
}
