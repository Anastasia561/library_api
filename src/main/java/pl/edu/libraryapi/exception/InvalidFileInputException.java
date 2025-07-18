package pl.edu.libraryapi.exception;

public class InvalidFileInputException extends RuntimeException {
    public InvalidFileInputException(String message) {
        super(message);
    }
}
