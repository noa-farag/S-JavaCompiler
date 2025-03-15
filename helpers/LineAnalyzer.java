package ex5.helpers;

import ex5.main.*;
import ex5.models.Method;
import ex5.models.Variable;
import ex5.models.VariableType;
import ex5.models.InvalidNameException;
import ex5.models.InvalidValueException;
import ex5.models.InvalidVariableTypeException;


import java.util.function.Function;

import static ex5.helpers.RegExConstants.*;

/**
 * A class for analyzing lines of code.
 * @auther noa.farag, noya.ashkenazi
 */
public class LineAnalyzer {
    private static final String INVALID_LINE_MESSAGE = "Invalid line - missing ; or { or }";
    private static final String INVALID_LINE_FORMAT = "Invalid line format";
    private static final String INVALID_METHOD_DECLARATION = "Invalid method declaration";
    private static final String INVALID_PARAMETERS = "Invalid parameters";
    private static final String FINAL = "final";
    private static final String BOOLEAN = "boolean";
    private static final String INVALID_CONDITION = "Invalid condition";
    private static final String TEMP = "temp";
    private static final String VARIABLE_NOT_INITIALIZED =
            "Trying to assign to a variable that is not initialized";
    private static final String INCOMPATIBLE_TYPES = "Trying to assign incompatible types";
    private static final String FINAL_VARIABLE_NOT_INITIALIZED = "final variable must be initialized";
    private static final int LEGAL_VARIABLEDETAILS_LENGTH = 2;
    private static final String VOID_REP_REG = "void";

    private static final int FINAL_DEC_LENGTH = 3;


    /**
     * Analyze a line of code.
     *
     * @param line the line to analyze.
     * @return the analysis of the line. can be one of the following:
     * VARIABLE, METHOD_CALL, IF_WHILE_CONDITION, METHOD_DECLARATION, RETURN
     * @throws InvalidLineSyntaxException if the line is invalid.
     */
    public static LineType analyzeLine(String line) throws  InvalidLineSyntaxException {
        checkLine(line); // check if the end of the line is valid
        if (isMethodDeclaration(line)) {
            return LineType.METHOD_DECLARATION;
        }
        else if (isIfWhileStatement(line)) {
            return LineType.IF_WHILE_CONDITION;
        }
        else if (isReturnStatement(line)) {
            return LineType.RETURN;
        }
        else if (idMethodCall(line)) {
            return LineType.METHOD_CALL;
        }
        else if (isVariableDeclaration(line)) {
            return LineType.VARIABLE_DECLARATION;
        }
        else if (isVariableAssignment(line)) {
            return LineType.VARIABLE_ASSIGNMENT;
        } else if (exitedScope(line)) {
            return LineType.END_OF_SCOPE;
        }
        // if reached here, the line is invalid
        throw new InvalidLineSyntaxException(INVALID_LINE_FORMAT);
    }

    private static boolean isVariableAssignment(String line) {
        //split by ,
        String[] splitLine = line.split(COMMA_REGEX);
        for (String s : splitLine) {
            if (!RegexHelper.regexMatches(VARIABLE_ASSIGNMENT_REGEX, s)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isVariableDeclaration(String line) {
        return RegexHelper.regexMatches(VARIABLE_DECLARATION_REGEX, line);
    }

    private static boolean idMethodCall(String line) {
        return RegexHelper.regexMatches(METHOD_CALL_REGEX, line);
    }

    /**
     * Check if a line is a return statement.
     *
     * @param line the line to check.
     * @return true if the line is a return statement, false otherwise.
     */
    public static boolean isReturnStatement(String line) {
        return RegexHelper.regexMatches(RETURN_REGEX, line);
    }

    private static void checkLine(String line) throws InvalidLineSyntaxException {

        if (!line.endsWith(SEMI_COLON_REGEX) && !line.endsWith(RIGHT_CURLY_BRACE) &&
                !line.endsWith(LEFT_CURLY_BRACE)) {
            // need to catch this exception.
            throw new InvalidLineSyntaxException(INVALID_LINE_MESSAGE);
        }
    }

    private static boolean isIfWhileStatement(String line) {
        return RegexHelper.regexMatches(IF_WHILE_REGEX, line);
    }

    private static boolean isMethodDeclaration(String line) throws InvalidLineSyntaxException {
        if (!RegexHelper.regexFind(VOID_REGEX, line)) {
            return false;
        }
        if (!RegexHelper.regexMatches(METHOD_DECLARATION_REGEX, line)) {
            throw new InvalidLineSyntaxException(INVALID_METHOD_DECLARATION);
        }
        return true;
    }

    /**
     * Check if a line entered a new scope.
     *
     * @param line the line to check.
     * @return true if the line entered a new scope, false otherwise.
     */
    public static boolean enteredScope(String line) {
        String withoutString = line.replaceAll(STRING_EXPRESSION, "");
        return RegexHelper.regexFind(REGEX_LEFT_CURLY_BRACELET, withoutString);
    }

    /**
     * Check if a line exited a scope.
     *
     * @param line the line to check.
     * @return true if the line exited a scope, false otherwise.
     */
    public static boolean exitedScope(String line) {
        return RegexHelper.regexMatches(REGEX_RIGHT_CURLY_BRACELET, line);
    }

    /**
     * Extract variables from a line.
     *
     * @param line the line to extract variables from.
     * @param lookupVariable a function to lookup a variable by name.
     * @return the extracted variables.
     * @throws InvalidNameException if a variable has an invalid name.
     * @throws InvalidVariableTypeException if a variable has an invalid type.
     * @throws InvalidValueException if a variable has an invalid value.
     * @throws InvalidLineSyntaxException if the line has an invalid syntax.
     */
    public static Variable[] extractVariables(String line, Function<String, Variable> lookupVariable)
            throws InvalidNameException, InvalidVariableTypeException,
            InvalidValueException, InvalidLineSyntaxException {

        // extract variables from line
        boolean isFinal;
        // handle final
        String finalRemoved = line.trim();
        if (RegexHelper.regexMatches(FINAL_REGEX, line)) {
            isFinal = true;
            finalRemoved = line.replaceFirst(FINAL_REDUNDANT_REGEX, "");
        } else {
            isFinal = false;
        }
        // handle type
        String type = finalRemoved.split(SPACES_REGEX)[0];
        String typeRemoved = finalRemoved.replaceFirst(type, "");
        typeRemoved = typeRemoved.substring(0, typeRemoved.length() - 1).strip(); // remove semicolon

        // handle names
        String[] variableStatements = typeRemoved.split(COMMA_REGEX, -1);
        Variable[] variables = new Variable[variableStatements.length];
        int i = 0;
        for (String variableStatement : variableStatements) {
            Variable variable;
            variableStatement = variableStatement.trim();
            if (!variableStatement.contains(EQUAL_REGEX)) {
                if (isFinal) {
                    throw new InvalidLineSyntaxException(FINAL_VARIABLE_NOT_INITIALIZED);
                }
                variable = new Variable(variableStatement, type, isFinal);
            } else {
                String[] variableDetails = variableStatement.split(EQUAL_REGEX, -1);
                if (variableDetails.length > LEGAL_VARIABLEDETAILS_LENGTH) {
                    throw new InvalidLineSyntaxException(INVALID_LINE_FORMAT);
                }
                variable = new Variable(variableDetails[0].trim(), type, isFinal);
                String value = variableDetails[1].trim();
                if (lookupVariable.apply(value) != null) {
                    if (!lookupVariable.apply(value).getIsInitialized()) {
                        throw new InvalidLineSyntaxException(VARIABLE_NOT_INITIALIZED);
                    }
                    checkAssignmentType(variable, lookupVariable.apply(value));
                    value = lookupVariable.apply(value).getValue();
                }
                variable.addValue(value);
            }
            variables[i] = variable;
            i++;
        }
        return variables;
    }

    private static void checkAssignmentType(Variable variable, Variable apply)
            throws InvalidLineSyntaxException {
        if (variable.getType() != apply.getType()) {
            if (variable.getType() == VariableType.DOUBLE && apply.getType() == VariableType.INT) {
                return;
            }
            if (variable.getType() == VariableType.BOOLEAN &&
                    (apply.getType() == VariableType.INT|| apply.getType() == VariableType.DOUBLE)) {
                return;
            }
            throw new InvalidLineSyntaxException(INCOMPATIBLE_TYPES);
        }
    }

    /**
     * Extract a method from a line.
     *
     * @param line the line to extract the method from.
     * @return the extracted method.
     * @throws SyntaxException if the method has an invalid syntax.
     */
    public static Method extractMethod(String line) throws SyntaxException {
        Variable[] parameters = extractParameters(line);
        String body = line.replaceFirst(VOID_REP_REG, "").trim();
        String name = body.split(RIGHT_BRACELET_REGEX)[0].trim();
        return new Method(name, parameters);
    }

    private static Variable[] extractParameters(String line) throws InvalidLineSyntaxException,
            InvalidNameException, InvalidVariableTypeException {
        int startIndex = line.indexOf(LEFT_BRACELET) + 1;
        int endIndex = line.indexOf(RIGHT_BRACELET);
        String slicedLine = line.substring(startIndex, endIndex).trim();
        String[] parameters = slicedLine.split(COMMA_REGEX);
        Variable[] variables = new Variable[parameters.length];
        int i = 0;
        if (parameters.length == 1 && parameters[0].equals("")) { // no parameters
            return null;
        }
        for (String parameter : parameters) {
            String[] parameterDetails = parameter.trim().split(SPACES_REGEX);
            if (parameterDetails.length > FINAL_DEC_LENGTH) {
                throw new InvalidLineSyntaxException(INVALID_PARAMETERS);
            }
            boolean isFinal = false;
            String name, type;
            if (parameterDetails.length == FINAL_DEC_LENGTH) {
                if (!parameterDetails[0].equals(FINAL)) {
                        throw new InvalidLineSyntaxException(INVALID_PARAMETERS);
                }
                isFinal = true;
                type = parameterDetails[1];
                name = parameterDetails[FINAL_DEC_LENGTH-1];

            } else {
                if (parameterDetails.length <= 1) {
                    throw new InvalidLineSyntaxException(INVALID_PARAMETERS);
                }
                type = parameterDetails[0];
                name = parameterDetails[1];
            }
            variables[i] = new Variable(name, type, isFinal);
            i++;
        }
        return variables;
    }

    /**
     * Analyze a condition.
     *
     * @param condition the condition to analyze.
     * @param lookupVariable a function to lookup a variable by name.
     * @throws InvalidValueException if the condition has an invalid value.
     * @throws InvalidNameException if the condition has an invalid name.
     * @throws InvalidVariableTypeException if the condition has an invalid type.
     * @throws InvalidLineSyntaxException if the condition has an invalid syntax.
     */
    public static void conditionAnalyzer(String condition, Function<String, Variable> lookupVariable)
            throws InvalidValueException, InvalidNameException, InvalidVariableTypeException,
            InvalidLineSyntaxException {
        // check if the condition is an initialized variable
        Variable variable = lookupVariable.apply(condition);
        if (variable == null) {
            Variable booleanVariable = new Variable(TEMP, BOOLEAN, false);
            booleanVariable.addValue(condition);
            // if no exception was thrown, the condition is valid! :)
            return;
        }

        else if ((variable.getType() == VariableType.BOOLEAN ||
                variable.getType() == VariableType.INT ||
                variable.getType() == VariableType.DOUBLE) && variable.getIsInitialized()) {
            return; //condition is valid! :)
        }
        throw new InvalidLineSyntaxException(INVALID_CONDITION);
    }
}
