package ex5.main;

import ex5.helpers.InvalidLineSyntaxException;
import ex5.helpers.LineAnalyzer;
import ex5.helpers.LineType;
import ex5.helpers.RegexHelper;
import ex5.models.*;
import ex5.models.InvalidValueException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static ex5.helpers.RegExConstants.*;

/**
 * The GeneralScanner class is responsible for scanning the file for the first time and the second time.
 * The first scan is for mapping global variables and methods.
 * The second scan is for checking variable assignments and method calls.
 * The class uses the ScopeManager to keep track of the scope and the methods map to
 * keep track of the methods.
 * The class also uses the LineAnalyzer to analyze the lines of the file.
 * The class throws exceptions if there are syntax errors or logic errors.
 * @author noa.farag, noya.ashkenazi
 */
public class GeneralScanner {

    private static final String METHOD_NAME_ALREADY_EXISTS = "method name already exists";
    private static final String SCOPE_NOT_CLOSED = "there is a scope that was not closed";
    private static final int EXPRESSION_LENGTH = 2;
    private static final String INVALID_ASSIGNMENT = "invalid assignment expression";
    private static final String VARIABLE_NOT_DECLARED = "variable not declared";
    private static final String CANNOT_ASSIGN_FINAL = "cannot assign to final variable";
    private static final String VARIABLE_NOT_INITIATED = "variable not initialized";
    private static final String NESTED_METHODS_ERROR = "nested methods are not allowed";
    private static final String NO_RETURN_STATEMENT = "method has no return statement";
    private static final String METHOD_DOES_NOT_EXIST = "trying to call a method that does not exist";
    private static final String CONDITION_NOT_VALID = "invalid condition";
    private static final String IF_WHILE_CANNOT_BE_IN_GLOBAL_SCOPE = "if and while statements cannot" +
            " be declared in global scope";
    private static final String INVALID_LINE_IN_GLOBAL_SCOPE = "Invalid statement in global scope";

    private final ScopeManager scopeManager;
    private final String file;
    private Map<String, Method> methods;


    /**
     * The constructor of the GeneralScanner class.
     *
     * @param scopeManager the scope manager to keep track of the scope.
     * @param file         the file to scan.
     */
    public GeneralScanner(ScopeManager scopeManager, String file) {
        this.file = file;
        this.scopeManager = scopeManager;
        this.methods = new HashMap<>();
    }

    /**
     * Scan a file for the first time.
     * mapping global variables and methods.
     * in the end of the scan, we should have a map of all global variables and methods.
     * syntax errors should be caught here.
     *
     * @throws FileNotFoundException if the file is not found.
     * @throws InvalidVariableTypeException if the variable type is invalid.
     * @throws InvalidNameException if the variable name is invalid.
     * @throws InvalidValueException if the variable value is invalid.
     * @throws InvalidLineSyntaxException if the line syntax is invalid.
     * @throws CompileException if there is a compile error.
     * @throws IncorrectStructureException if the structure of the file is incorrect.
     * @throws SyntaxException if there is a syntax error.
     *
     */
    public void firstFileScan() throws IOException, InvalidVariableTypeException, InvalidNameException,
            InvalidValueException, InvalidLineSyntaxException, CompileException {
        curlyBraceHandler();
        // scan file, move line by line
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty() || line.startsWith(EMPTY_LINE) ||
                        RegexHelper.regexMatches(ONLY_SPACES, line)) { // skip empty lines and comments
                    continue;
                }
                line = line.trim();

                // keep tab of scope
                if (LineAnalyzer.enteredScope(line)) {
                    scopeManager.enterScope();
                }
                if (LineAnalyzer.exitedScope(line)) {
                    scopeManager.exitScope();
                }


                // analyze line type
                LineType type = LineAnalyzer.analyzeLine(line);
                if ((type == LineType.IF_WHILE_CONDITION )&& scopeManager.getScopeDepth()==2) {
                    throw new IncorrectStructureException(IF_WHILE_CANNOT_BE_IN_GLOBAL_SCOPE);
                }
                if ((type == LineType.METHOD_CALL || type==LineType.RETURN) && scopeManager.isGlobalScope()) {
                    throw new IncorrectStructureException(INVALID_LINE_IN_GLOBAL_SCOPE);
                }
                if (type == LineType.METHOD_DECLARATION) {
                    // create method object
                    Method method = LineAnalyzer.extractMethod(line);
                    // add method to methods map
                    if (methods.containsKey(method.getName())) {
                        throw new IncorrectStructureException(METHOD_NAME_ALREADY_EXISTS);
                    }
                    methods.put(method.getName(), method);
                } else if (type == LineType.VARIABLE_DECLARATION && scopeManager.isGlobalScope()) {
                    // extract variable
                    Variable[] variables = LineAnalyzer.extractVariables(line, scopeManager::lookupVariable);
                    // add them to the scope
                    for (Variable variable : variables) {
                        scopeManager.declareVariable(variable.getName(), variable);
                    }
                } else if (type == LineType.VARIABLE_ASSIGNMENT && scopeManager.isGlobalScope()) {
                    checkVariableAssignment(line);

                }

            }
            if (scopeManager.getScopeDepth() > 1) { // check if scopes are closed
                throw new IncorrectStructureException(SCOPE_NOT_CLOSED);
            }
        }

    }

    private void checkVariableAssignment(String line) throws InvalidValueException, SyntaxException {
        line = line.trim().substring(0, line.length() - 1);
        String[] assignments = line.split(COMMA_REGEX);
        for (String assignment : assignments) {
            String[] parts = assignment.split(EQUAL_REGEX);
            if (parts.length != EXPRESSION_LENGTH) {
                throw new SyntaxException(INVALID_ASSIGNMENT);
            }
            String name = parts[0].trim();
            String value = parts[1].trim();
            Variable variable = scopeManager.lookupVariable(name);
            if (variable == null) {
                throw new SyntaxException(VARIABLE_NOT_DECLARED);
            }
            if (variable.isFinal()) {
                throw new SyntaxException(CANNOT_ASSIGN_FINAL);
            }
            if (scopeManager.lookupVariable(value) != null) {
                if (!scopeManager.lookupVariable(value).getIsInitialized()) {
                    throw new SyntaxException(VARIABLE_NOT_INITIATED);
                }
                value = scopeManager.lookupVariable(value).getValue();
            }
            variable.changeValue(value);
        }
    }


    private void scanMethod(BufferedReader bufferedReader, Method method) throws IOException,
            InvalidVariableTypeException, InvalidNameException, InvalidValueException,
            InvalidLineSyntaxException, CompileException {
        int methodScope = scopeManager.getScopeDepth();
        String line;
        String previousLine = "";
        String prePreLine = "";
        HashMap<String, Variable> globalVariablesCopy = new HashMap<>();
        for (Map.Entry<String, Variable> entry : scopeManager.getGlobalScope().entrySet()) {
            Variable variable = new Variable(entry.getValue());
            globalVariablesCopy.put(entry.getKey(), variable);
        }
        while (scopeManager.getScopeDepth() >= methodScope && (line = bufferedReader.readLine()) != null) {
            if (line.isEmpty() || line.startsWith(EMPTY_LINE) ||
                    RegexHelper.regexMatches(ONLY_SPACES, line)) { // skip empty lines and comments
                continue;
            }
            line = line.trim();
            // keep tab of scope
            if (LineAnalyzer.enteredScope(line)) {
                scopeManager.enterScope();
            }
            if (LineAnalyzer.exitedScope(line)) {
                scopeManager.exitScope();
            }

            LineType type = LineAnalyzer.analyzeLine(line);
            if (type == LineType.METHOD_DECLARATION) {
                throw new IncorrectStructureException(NESTED_METHODS_ERROR);
                // custom exception
            } else if (type == LineType.IF_WHILE_CONDITION) {
                checkIfWhileStatement(line);
            } else if (type == LineType.METHOD_CALL) {
                checkMethodCall(line);
            } else if (type == LineType.RETURN) {


            } else if (type == LineType.VARIABLE_ASSIGNMENT) {
                checkVariableAssignment(line);

            } else if (type == LineType.VARIABLE_DECLARATION) {
                // extract variable
                Variable[] variables = LineAnalyzer.extractVariables(line, scopeManager::lookupVariable);
                // add them to the scope
                for (Variable variable : variables) {
                    scopeManager.declareVariable(variable.getName(), variable);
                }
            }
            prePreLine = previousLine;
            previousLine = line;
        }
        if (!LineAnalyzer.isReturnStatement(prePreLine)) {
            // if we reached here, the method has no return statement
            throw new IncorrectStructureException(NO_RETURN_STATEMENT);
        }
        // reset global variables
        scopeManager.setGlobalScope(globalVariablesCopy);

    }

    private void checkMethodCall(String line) throws CompileException {
        // extract method name
        String name = line.split(RIGHT_BRACELET_REGEX )[0].trim();
        // check if method exists
        if (!methods.containsKey(name)) {
            throw new SyntaxException(METHOD_DOES_NOT_EXIST);

        }
        // extract method arguments
        String[] arguments = line.substring(line.indexOf(LEFT_BRACELET) + 1,
                line.lastIndexOf(RIGHT_BRACELET)).trim().split(COMMA_REGEX);
        for (int i = 0; i < arguments.length; i++) {
            arguments[i] = arguments[i].trim();
        }
        if (arguments.length == 1 && arguments[0].isEmpty()) {
            arguments = new String[0];
        }
        // assert the arguments match the method signature
        Method method = methods.get(name);
        method.assertArguments(arguments, scopeManager::lookupVariable);
    }

    private void checkIfWhileStatement(String line) throws SyntaxException, InvalidValueException {
        int startIndex = line.indexOf(LEFT_BRACELET);
        int endIndex = line.lastIndexOf(RIGHT_BRACELET);
        String condition = line.substring(startIndex + 1, endIndex);
        String[] conditionParts = condition.split(OR_CONDITION_REGEX);
        for (String part : conditionParts) {
            String[] subParts = part.split(AND_CONDITION_REGEX);
            for (String subPart : subParts) {
                if (subPart.isEmpty()) {
                    throw new SyntaxException(CONDITION_NOT_VALID);
                } else {
                    subPart = subPart.trim();
                    LineAnalyzer.conditionAnalyzer(subPart, scopeManager::lookupVariable);
                }
            }
        }
    }

    /**
     * Scan a file for the second time.
     * this time, check for variable assignments and method calls.
     * logic errors should be caught here.
     *
     * @throws FileNotFoundException if the file is not found.
     */
    public void finalFileScan() throws IOException, CompileException {
        try (BufferedReader bufferedReader = new BufferedReader((new FileReader(file)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.isEmpty() || line.startsWith(EMPTY_LINE) ||
                        RegexHelper.regexMatches(ONLY_SPACES, line)) { // skip empty lines and comments
                    continue;
                }
                line = line.trim();
                if (LineAnalyzer.enteredScope(line)) {
                    scopeManager.enterScope();
                }
                if (LineAnalyzer.exitedScope(line)) {
                    scopeManager.exitScope();
                }
                // analyze line type
                LineType type = LineAnalyzer.analyzeLine(line);
                if (type == LineType.METHOD_DECLARATION) {

                    Method method = LineAnalyzer.extractMethod(line);
                    scopeManager.enterMethod(method);
                    scanMethod(bufferedReader, method);

                }
            }
        }

    }

    private void curlyBraceHandler() throws IOException, IncorrectStructureException {
        int curlyBraceCount = 0;
        // scan file, move line by line
        try (BufferedReader bufferedReader = new BufferedReader((new FileReader(file)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                line = line.replaceAll(STRING_EXPRESSION, "");
                for (char c : line.toCharArray()) {
                    if (c == LEFT_CURLY_BRACE.charAt(0)) {
                        curlyBraceCount++;
                    }
                    if (c == RIGHT_CURLY_BRACE.charAt(0)) {
                        curlyBraceCount--;
                    }
                }
            }
            if (curlyBraceCount != 0) {
                throw new IncorrectStructureException(SCOPE_NOT_CLOSED);

            }
        }

    }
}
