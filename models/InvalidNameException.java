package ex5.models;

import ex5.main.SyntaxException;

/**
 * An exception class for invalid names.
 * @auther noa.farag, noya.ashkenazi
 */
public class InvalidNameException extends SyntaxException {
    /**
     * The constructor of the InvalidNameException class.
     *
     * @param message the message of the exception.
     */
    public InvalidNameException(String message) {
        super(message);
    }
}
