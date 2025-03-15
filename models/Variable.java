package ex5.models;

import ex5.helpers.RegexHelper;


import java.util.HashSet;
import java.util.Set;

import static ex5.helpers.RegExConstants.*;


/**
 * A class that represents a variable.
 * get all the data without spaces
 * @auther noa.farag, noya.ashkenazi
 */
public class Variable {
    private static final String DEFAULT_INT_TYPE = "0";
    private static final String INT = "int";
    private static final String STRING = "String";
    private static final String BOOLEAN = "boolean";
    private static final String CHAR = "char";
    private static final String DOUBLE = "double";
    private static final String IF = "if";
    private static final String WHILE = "while";
    private static final String RETURN = "return";
    private static final String VOID = "void";
    private static final String FINAL = "final";
    private static final String FALSE = "false";
    private static final String TRUE = "true";
    private static final String INVALID_VARIABLE_TYPE = "Invalid variable type";
    private static final String INVALID_PARAMETER_NAME = "Invalid parameter name";
    private static final String PARAMETER_NAME_IS_KEYWORD = "Invalid parameter name, name is a keyword";
    private static final String FINAL_VAVRIABLE_EXCEPTION = "Final variable cannot be assigned a new value";
    private static final String INVALID_VALUE_ASSIGNMENT = "Invalid variable value assignment";
    private static final String INVALIS_BOLEAN_VALUE_EXCEPTION = "Invalid boolean value";
    private static final String STRING_VALUR_EXCEPTION = "Invalid string value";
    private static final String INVALID_CHAR_EXCEPTION = "Invalid char value";
    private static final String INVALID_DOUBLE_EXCEPTION = "Invalid double value";
    private static final String INVALID_INT_VALUE_EXCEPTION = "Invalid int value";
    private static final String DEFAULT_CHAR_TYPE = "'\u0000'";
    private static final String DEFAULT_STRING_TYPE = "\"\"";


    private final String name;
    private VariableType type;
    private boolean isInitialized;
    private final boolean isFinal;
    private String value;
    private final static Set<String> savedVarWords = new HashSet<>(Set.of(INT, STRING, BOOLEAN, CHAR,
            DOUBLE));
    private final static Set<String> savedKeyWords = new HashSet<>(Set.of(IF,
            WHILE, RETURN, VOID, FINAL, FALSE, TRUE));


    /**
     * A constructor for the Variable class if the variable isn't initialized.
     * @param name the name of the variable.
     * @param type the type of the variable.
     */
    public Variable(String name, String type, boolean isFinal) throws InvalidNameException,
            InvalidVariableTypeException {
        this.name = name;
        this.isNameValid();
        fillType(type);
        this.isInitialized = false;
        this.isFinal = isFinal;
    }

    /**
     * A constructor for the Variable class if the variable is initialized.
     */
    public Variable(Variable other) {
        this.name = other.name;
        this.type = other.type;
        this.isInitialized = other.isInitialized;
        this.isFinal = other.isFinal;
        this.value = other.value;
    }


    private void fillType(String type) throws InvalidVariableTypeException {
        switch (type) {
            case STRING -> this.type = VariableType.STRING;
            case INT -> this.type = VariableType.INT;
            case DOUBLE -> this.type = VariableType.DOUBLE;
            case CHAR -> this.type = VariableType.CHAR;
            case BOOLEAN -> this.type = VariableType.BOOLEAN;
            default -> {
                throw new InvalidVariableTypeException(INVALID_VARIABLE_TYPE);
            }

        }
    }


    /**
     * A method that returns the name of the variable.
     * DOES NOT CHECK SPACES AFTER NAME ONLY THE NAME
     * @return the name of the variable.
     */
    public boolean isNameValid() throws  InvalidNameException {
        if (this.name.length() <= 0) { //check if the name is empty
            throw new InvalidNameException(INVALID_PARAMETER_NAME);
        }
        if (savedVarWords.contains(this.name) || savedKeyWords.contains(this.name)) {
            throw new InvalidNameException(PARAMETER_NAME_IS_KEYWORD);
        }
        if (RegexHelper.regexMatches(VARIABLE_NAME_REGEX, this.name) ||
                RegexHelper.regexMatches(VARIABLE_NAME_REGEX__, this.name)) {
            return true;
        }
        throw new InvalidNameException(INVALID_PARAMETER_NAME); //name doesn't match the regex
    }


    /**
     * A method that returns the name of the variable.
     * @return the name of the variable.
     */
    public boolean addValue(String value) throws InvalidValueException {
        if(isValueValid(value)) {
            this.value = value;
            this.isInitialized = true;
            return true;
        }
        return false;
    }


    /**
     * A method that returns the name of the variable.
     */
    public void changeValue(String newValue) throws FinalVariableException, InvalidValueException {
        if (this.isFinal) {
            throw new FinalVariableException(FINAL_VAVRIABLE_EXCEPTION);
        }
        if (!this.isInitialized) {
            addValue(newValue);
        }
        String prevVal = this.value;
        if (!addValue(newValue)) {
            this.value = prevVal;
            throw new InvalidValueException(INVALID_VALUE_ASSIGNMENT);
        }
    }


    /**
     * A method that returns the name of the variable.
     * @return the name of the variable.
     */
    public boolean isValueValid(String value) throws  InvalidValueException {
        switch (this.type) {
            case INT -> {
                return intValid(value);
            }
            case DOUBLE -> {
                return doubleValid(value);
            }
            case CHAR -> {
                return charValid(value);
            }
            case STRING -> {
                return stringValid(value);
            }
            case BOOLEAN -> {
                return booleanValid(value);
            }
        }
        return false;
    }


    private boolean booleanValid(String value) throws  InvalidValueException {
        if (RegexHelper.regexMatches(TRUE_FALSE_REGEX, value) ||
                RegexHelper.regexMatches(DOUBLE_DOT_IN_BEGINNING_REGEX, value) ||
                RegexHelper.regexMatches(DOUBLE_DOT_IN_MIDDLE_REGEX, value) ||
                RegexHelper.regexMatches(DOUBLE_DOT_IN_END_REGEX, value)) {
            return true;
        }
        throw new InvalidValueException(INVALIS_BOLEAN_VALUE_EXCEPTION);
    }


    private boolean stringValid(String value) throws InvalidValueException {
        if (RegexHelper.regexMatches(STRING_REGEX, value)) {
            this.isInitialized = false;
            return true;
        }
        this.isInitialized = false;
        throw new InvalidValueException(STRING_VALUR_EXCEPTION);
    }


    private boolean charValid(String value) throws InvalidValueException{
        if (RegexHelper.regexMatches(CHAR_REGEX, value)) {
            return true;
        }
        throw new InvalidValueException(INVALID_CHAR_EXCEPTION);
    }


    private boolean doubleValid(String value) throws InvalidValueException {
        if (RegexHelper.regexMatches(DOUBLE_DOT_IN_BEGINNING_REGEX, value) ||
                RegexHelper.regexMatches(DOUBLE_DOT_IN_MIDDLE_REGEX, value) ||
                RegexHelper.regexMatches(DOUBLE_DOT_IN_END_REGEX, value)) {
            return true;
        }
        throw new InvalidValueException(INVALID_DOUBLE_EXCEPTION);
    }


    private boolean intValid(String value) throws InvalidValueException {
        if (RegexHelper.regexMatches(INT_REGEX, value) ||
                RegexHelper.regexMatches(POSITIVE_INT_REGEX, value) ||
                RegexHelper.regexMatches(NEGATIVE_INT_REGEX, value)) {
            return true;
        }
        throw new InvalidValueException(INVALID_INT_VALUE_EXCEPTION);
    }

    /**
     * A method that returns the name of the variable.
     * @return the name of the variable.
     */
    public boolean isFinal() {
        return this.isFinal;
    }

    /**
     * A method that returns the name of the variable.
     * @return the name of the variable.
     */
    public String getName() {
        return this.name;
    }

    /**
     * A method that returns the name of the variable.
     * @return the name of the variable.
     */
    public VariableType getType() {
        return this.type;
    }

    /**
     * A method that returns the name of the variable.
     * @return the name of the variable.
     */
    public boolean getIsInitialized() {
        return this.isInitialized;
    }

    /**
     * A method that returns the name of the variable.
     * @return the name of the variable.
     */
    public String getValue() {
        return this.value;
    }


    /**
     * A method that returns the name of the variable.
     */
    public void setIsInitialized(boolean b) {
        this.isInitialized = b;
    }

    /**
     * A method that returns the name of the variable.
     */
    public void addDefaultValue() {
        switch (this.type) {
            case INT, DOUBLE, BOOLEAN -> {
                this.value = DEFAULT_INT_TYPE;
            }
            case CHAR -> {
                this.value = DEFAULT_CHAR_TYPE;
            }
            case STRING -> {
                this.value = DEFAULT_STRING_TYPE;
            }
        }
    }
}
