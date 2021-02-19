package data;

import java.util.Vector;

public class Attribute {
	/*
	 * Data class of the attributes, ie the columns in the tables resulting of the
	 * queries on databases
	 */
	
	// DATA
	
	private String name;
	private String type;
	private boolean isNullable;
	private boolean isPrimaryKey;
	private boolean isUnique;

	// Methods
	
	public Attribute(String name, String type, boolean isNullable, boolean isPrimaryKey, boolean isUnique) {
		super();
		this.name = name;
		this.type = type;
		this.isNullable = isNullable;
		this.isPrimaryKey = isPrimaryKey;
		this.isUnique = isUnique;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}

	public boolean isPrimaryKey() {
		return isPrimaryKey;
	}

	public void setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
	}

	public boolean isUnique() {
		return isUnique;
	}

	public void setUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}
	
	/**
	 * Generates a vector composed of all attribute's datas.
	 * 
	 * @return attributeVector
	 */
	public Vector<Object> toVector(){
		Vector<Object> attributeVector = new Vector<Object>();
		
		attributeVector.add(name);
		attributeVector.add(type);
		attributeVector.add(isNullable);
		attributeVector.add(isPrimaryKey);
		attributeVector.add(isUnique);
		
		return attributeVector;
	}
	
	
}
