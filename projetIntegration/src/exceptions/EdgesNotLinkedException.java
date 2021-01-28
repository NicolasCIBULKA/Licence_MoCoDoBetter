package exceptions;

public class EdgesNotLinkedException extends Exception{
	
	/*
	 * Exception raised when we try to delete an edge that does not exists
	 */

	public EdgesNotLinkedException(String errorMessage) {
		super(errorMessage);
	}
}
