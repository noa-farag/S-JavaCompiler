package ex5.main;

/**
 * An exception class for compile errors.
 * @auther noa.farag, noya.ashkenazi
 */
public class CompileException extends Exception {
    /**
     * The constructor of the CompileException class.
     *
     * @param message the message of the exception.
     */
    public CompileException(String message) {
        super(message);
    }
}
