package exceptions;

public class NotTheRightFileFormatException extends Exception {
	/*
	 * Exception raised when the xml file is not compatible with the application
	 */

	public NotTheRightFileFormatException(String errorMessage) {
		super(errorMessage);
	}
}
