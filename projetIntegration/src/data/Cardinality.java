package data;

public class Cardinality {
	/*
	 * Cardinality is the class that contains the cardinalities of the different
	 * associations
	 */

	// Datas
	private String nomEntity;
	private String lowValue;
	private String highValue;

	// Methods

	public Cardinality(String lowValue, String highValue, String nowEntity) {
		this.highValue = highValue;
		this.lowValue = lowValue;
		this.nomEntity=nowEntity;
	}

	public String getLowValue() {
		return lowValue;
	}

	public void setLowValue(String lowValue) {
		this.lowValue = lowValue;
	}

	public String getHighValue() {
		return highValue;
	}

	public void setHighValue(String highValue) {
		this.highValue = highValue;
	}
	
	public String getNomEntity() {
		return nomEntity;
	}
	
	public void setNomEntity(String nomEntity) {
		this.nomEntity=nomEntity;
	}
	
	
	
}
