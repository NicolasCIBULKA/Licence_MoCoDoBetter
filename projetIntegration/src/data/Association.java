package data;

import java.util.ArrayList;

public class Association extends Node {

	/*
	 * This class has the role of the association, and contains all the elemnts of
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
	
	public Association(String name, ArrayList<Attribute> listAttribute) {
		super(name, listAttribute);
		this.cardinalityList = new ArrayList<>();
	}

	public ArrayList<Cardinality> getCardinalityList() {
		return cardinalityList;
	}

	public void setCardinalityList(ArrayList<Cardinality> cardinalityList) {
		this.cardinalityList = cardinalityList;
	}

}
