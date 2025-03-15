package ex5.models;

import ex5.main.SyntaxException;

/**
 * An exception class for invalid variable types.
 * @auther noa.farag, noya.ashkenazi
 */
public class InvalidVariableTypeException extends SyntaxException {
    /**
     * The constructor of the InvalidVariableTypeException class.
     *
     * @param message the message of the exception.
     */
    public InvalidVariableTypeException(String message) {
        super(message);
    }
}
