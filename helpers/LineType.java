package ex5.helpers;

/**
 * An enum class for line types.
 * @auther noa.farag, noya.ashkenazi
 */
public enum LineType {
    /**
     * A variable declaration line type.
     */
    VARIABLE_DECLARATION,
    /**
     * A method call line type.
     */
    METHOD_CALL,
    /**
     * A method declaration line type.
     */
    IF_WHILE_CONDITION,
    /**
     * A method declaration line type.
     */
    METHOD_DECLARATION,
    /**
     * A variable assignment line type.
     */
    VARIABLE_ASSIGNMENT,
    /**
     * A return line type.
     */
    RETURN,
    /**
     * A line type that contains only a semicolon.
     */
    END_OF_SCOPE
}
