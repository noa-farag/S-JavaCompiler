package ex5.models;

import ex5.main.CompileException;

/**
 * An exception class for invalid values.
 * @auther noa.farag, noya.ashkenazi
 */
public class InvalidValueException extends CompileException {
    /**
     * The constructor of the InvalidValueException class.
     *
     * @param message the message of the exception.
     */
    public InvalidValueException(String message) {
        super(message);
    }
}
