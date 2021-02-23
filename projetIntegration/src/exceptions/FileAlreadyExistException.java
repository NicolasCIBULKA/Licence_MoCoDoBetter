package exceptions;

public class FileAlreadyExistException extends Exception {
	//class for the exception in case of saving in a file that already exist in save as.
	public FileAlreadyExistException(String errormessage) {
		super(errormessage);
	}
}
