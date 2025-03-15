package ex5.helpers;

import ex5.main.SyntaxException;

/**
 * An exception class for invalid line syntax.
 * @auther noa.farag, noya.ashkenazi
 */
public class InvalidLineSyntaxException extends SyntaxException {
    /**
     * The constructor of the InvalidLineSyntaxException class.
     *
     * @param message the message of the exception.
     */
    public InvalidLineSyntaxException(String message) {
        super(message);
    }
}
