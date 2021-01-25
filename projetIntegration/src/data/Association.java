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

	private HashMap<String, Cardinality> cardinalityMap;

	// Methods
	public Association(String name, ArrayList<Attribute> listAttribute, HashMap<String, Cardinality> cardinalityMap) {
		super(name, listAttribute);
		this.cardinalityMap = cardinalityMap;
	}

	public HashMap<String, Cardinality> getCardinalityMap() {
		return cardinalityMap;
	}

	public void setCardinalityMap(HashMap<String, Cardinality> cardinalityMap) {
		this.cardinalityMap = cardinalityMap;
	}

}
