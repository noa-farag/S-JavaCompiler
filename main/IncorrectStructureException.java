package ex5.main;

/**
 * An exception class for incorrect structure.
 * @auther noa.farag, noya.ashkenazi
 */
public class IncorrectStructureException extends CompileException {
    /**
     * The constructor of the IncorrectStructureException class.
     *
     * @param string the message of the exception.
     */
    public IncorrectStructureException(String string) {
        super(string);
    }
}
