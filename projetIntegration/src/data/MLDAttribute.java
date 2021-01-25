package data;

public class MLDAttribute extends Attribute {

	/*
	 * Class that contains the attributes for the MLD entities. THis is a classical Attribute
	 * 
	 * with the foreign keys in order to create the MLD scheme
	 */
	
	// DATA 
	
	
	private boolean isForeignKey;
	private Node referenceNode;
	private Attribute referenceAttribute;
	
	// Methods
	
	public MLDAttribute(String name, String type, boolean isNullable, boolean isPrimaryKey, boolean isUnique,
			boolean isForeignKey, Node referenceNode, Attribute referenceAttribute) {
		super(name, type, isNullable, isPrimaryKey, isUnique);
		this.isForeignKey = isForeignKey;
		this.referenceNode = referenceNode;
		this.referenceAttribute = referenceAttribute;
	}
	
	public boolean isForeignKey() {
		return isForeignKey;
	}
	
	public void setForeignKey(boolean isForeignKey) {
		this.isForeignKey = isForeignKey;
	}
	public Node getReferenceNode() {
		return referenceNode;
	}
	public void setReferenceNode(Node referenceNode) {
		this.referenceNode = referenceNode;
	}
	public Attribute getReferenceAttribute() {
		return referenceAttribute;
	}
	public void setReferenceAttribute(Attribute referenceAttribute) {
		this.referenceAttribute = referenceAttribute;
	}
	
}
