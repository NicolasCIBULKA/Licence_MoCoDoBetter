package exceptions;

public class ExistingEdgeException extends Exception {
	/*
	 * Exception raised when we try to create an edge between two Nodes But the Edge
	 * already exists
	 */
	public ExistingEdgeException(String errorMessage) {
		super(errorMessage);
	}

}
