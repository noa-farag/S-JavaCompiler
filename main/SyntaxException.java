package ex5.main;

/**
 * An exception class for syntax errors.
 * @auther noa.farag, noya.ashkenazi
 */
public class SyntaxException extends CompileException {
    /**
     * The constructor of the SyntaxException class.
     *
     * @param message the message of the exception.
     */
    public SyntaxException(String message) {
        super(message);
    }
}
