package ex5.models;

import ex5.helpers.RegexHelper;

import java.util.*;
import static ex5.helpers.RegExConstants.*;
import java.util.function.Function;

/**
 * A class for methods.
 * @auther noa.farag, noya.ashkenazi
 */
public class Method {

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
    private static final String MEMTHOD_NAME_EXCEPTION = "Invalid method name";
    private static final String METHOD_NAME_IS_KEYWORD = "Invalid method name, name is a keyword";
    private static final String INVALID_NUMBER_OF_PARAMETERS = "Invalid number of parameters";
    private static final String INVALID_PARAMETER_VALUE = "Invalid parameter value";
    private static final String PARAMETER_IS_NOT_INITIALIZED = "Parameter is not initialized";

    private String name;
    private List<Variable> parameters;
    private final static Set<String> savedVarWords = new HashSet<>(Set.of(INT, STRING, BOOLEAN, CHAR,
            DOUBLE));
    private final static Set<String> savedKeyWords = new HashSet<>(Set.of(IF,
            WHILE, RETURN, VOID, FINAL, FALSE, TRUE));


    /**
     * A constructor for the Method class.
     * @param name       the name of the method.
     * @param parameters the parameters of the method (after split with ,).
     */
    public Method(String name, Variable[] parameters) throws InvalidNameException {
        this.name = name;
        checkMethodName();
        this.parameters = new ArrayList<>();
        if (parameters != null) {
            this.parameters.addAll(Arrays.asList(parameters));
        }
    }


    /**
     * A method to check if the method name is valid.
     * @return true if the method name is valid, false otherwise.
     */
    public boolean checkMethodName() throws InvalidNameException {
        String methodNameRegex = METHOD_NAME_REGEX;
        if (this.name.length() <= 0) {
            throw new InvalidNameException(MEMTHOD_NAME_EXCEPTION);
        }
        if (savedVarWords.contains(this.name) || savedKeyWords.contains(this.name)) {
            throw new InvalidNameException(METHOD_NAME_IS_KEYWORD);
        }
        if (RegexHelper.regexMatches(methodNameRegex, this.name)) {
            return true;
        }
        throw new InvalidNameException(MEMTHOD_NAME_EXCEPTION);
    }


    /**
     * A method to add a parameter to the method.
     * @String method name
     */
    public String getName() {
        return this.name;
    }

    /**
     * A method to add a parameter to the method.
     * @Varivable[] all the method parameters
     */
    public Variable[] getParameters() {
        return this.parameters.toArray(new Variable[0]);
    }



    /**
     * A method to add a parameter to the method.
     */
    public void assertArguments(String[] arguments, Function<String, Variable> lookupVariable)
            throws InvalidValueException {
        if (arguments.length != this.parameters.size()) {
            throw new InvalidValueException(INVALID_NUMBER_OF_PARAMETERS);
        }
        for (int i = 0; i < arguments.length; i++) {
            if (arguments[i] == null || parameters.get(i) == null) {
                throw new InvalidValueException(INVALID_PARAMETER_VALUE);
            }
            if (lookupVariable != null) {
                Variable variable = lookupVariable.apply(arguments[i]);
                if (variable != null) {
                    if (!variable.getIsInitialized()) {
                        throw new InvalidValueException(PARAMETER_IS_NOT_INITIALIZED);
                    }
                    arguments[i] = variable.getValue();
                }
            }
            if (!this.parameters.get(i).isValueValid(arguments[i])) {
                throw new InvalidValueException(INVALID_PARAMETER_VALUE);
            }
        }
    }
}