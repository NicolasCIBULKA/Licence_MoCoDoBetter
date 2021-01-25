package data;

public class Cardinality {
	/*
	 * Cardinality is the class that contains the cardinalities of the different
	 * associations
	 */

	// Datas

	private int lowValue;
	private int highValue;

	// Methods

	public Cardinality(int lowValue, int highValue) {
		this.highValue = highValue;
		this.lowValue = lowValue;
	}

	public int getLowValue() {
		return lowValue;
	}

	public void setLowValue(int lowValue) {
		this.lowValue = lowValue;
	}

	public int getHighValue() {
		return highValue;
	}

	public void setHighValue(int highValue) {
		this.highValue = highValue;
	}
	
	
	
	
}
