package ex5.models;

import ex5.main.SyntaxException;

/**
 * An exception class for final variables.
 * @auther noa.farag, noya.ashkenazi
 */
public class FinalVariableException extends SyntaxException {
    /**
     * The constructor of the FinalVariableException class.
     *
     * @param string the message of the exception.
     */
    public FinalVariableException(String string) {
        super(string);
    }
}
