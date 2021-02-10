package exceptions;

public class SQLTranscriptionException extends Exception{

	/**
	 * Exception raised when we have an error while translating MLD to SQL File
	 */
	public SQLTranscriptionException(String errormessage) {
		super(errormessage);
	}
}
