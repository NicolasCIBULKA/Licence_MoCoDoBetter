package exceptions;

public class InvalidNodeLinkException extends Exception {
	
	/*
	 * Exception thrown when we try to link 2 associations or 2 Entities together
	 * 
	 */
	
	public InvalidNodeLinkException(String errormessage) {
		super(errormessage);
	}
}
