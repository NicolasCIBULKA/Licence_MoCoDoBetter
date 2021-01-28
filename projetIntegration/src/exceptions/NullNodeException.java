package exceptions;

public class NullNodeException extends Exception {
	/*
	 * Exception raised when the node does not exists in MCDManaging
	 */
	public NullNodeException(String errorMessage) {
		super(errorMessage);
	}
}
