package data;

import java.util.ArrayList;
import java.util.HashMap;

public class Association extends Node {

	/*
	 * This class has the role of the association, and contains all the lemnts of
	 * the entity, adding the Cardinality of the association for each entity connected to the
	 * association
	 */

	// Data
	private ArrayList<Cardinality> cardinalityList;

	// Methods
	public Association(String name, ArrayList<Attribute> listAttribute, ArrayList<Cardinality> cardinalityList) {
		super(name, listAttribute);
		this.cardinalityList = cardinalityList;
	}

	public ArrayList<Cardinality> getCardinalityList() {
		return cardinalityList;
	}

	public void setCardinalityMap(ArrayList<Cardinality> cardinalityList) {
		this.cardinalityList = cardinalityList;
	}

}
