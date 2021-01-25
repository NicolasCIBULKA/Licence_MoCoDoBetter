package data;

import java.util.ArrayList;

public abstract class Node {
	/*
	 * Node is the general type for the nodes of the graph contained in 
	 * 
	 * MCD Class.
	 */
	
	// DATA
	
	private String name;
	private ArrayList<Attribute> listAttribute;
	
	// Methods
	
	public Node(String name, ArrayList<Attribute> listAttribute) {
		super();
		this.name = name;
		this.listAttribute = listAttribute;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Attribute> getListAttribute() {
		return listAttribute;
	}

	public void setListAttribute(ArrayList<Attribute> listAttribute) {
		this.listAttribute = listAttribute;
	}
	
	
}
