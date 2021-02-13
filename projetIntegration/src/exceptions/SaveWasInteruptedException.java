package exceptions;

public class SaveWasInteruptedException extends Exception {
	/*
	 * Exception raised when there is an issue while saving the MCD
	 */

	public SaveWasInteruptedException(String errorMessage) {
		super(errorMessage);
	}
}
