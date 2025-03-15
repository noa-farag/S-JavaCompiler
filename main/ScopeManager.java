package ex5.main;

import ex5.models.Method;
import ex5.models.Variable;
import ex5.models.InvalidValueException;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * The ScopeManager class is responsible for managing the scopes of the program.
 * It is responsible for managing the scopes of the program, including the global scope and method scopes.
 * @author noa.farag, noya.ashkenazi
 */
public class ScopeManager {
    private static final String SCOPE_ERROR_MESSAGE = "No scope to exit";
    private static final String DOUBLE_DECLARATION_ERROR_MESSAGE =
            "Variable already declared in this scope: ";

    private Stack<Map<String, Variable>> scopes;
    private boolean isInsideMethod;
    private Method currentMethod;
    private int methodScope;

    /**
     * Create a new ScopeManager object.
     * The constructor initializes the global scope and sets isInsideMethod to false.
     */
    public ScopeManager() {
        this.scopes = new Stack<>();
        // push the global scope
        scopes.push(new HashMap<>());
        // init isInsideMethod to false
        this.isInsideMethod = false;
        this.currentMethod = null;
        this.methodScope = -1;
    }

    /**
     * Enter a new scope.
     */
    public void enterScope() {
        scopes.push(new HashMap<>());
    }

    private void exitMethodScope() {
        currentMethod = null;
        isInsideMethod = false;
        methodScope = -1;
        // comm
    }

    /**
     * Get the global scope.
     *
     * @return the global scope.
     */
    public HashMap<String, Variable> getGlobalScope() {
        return (HashMap<String, Variable>) scopes.get(0);
    }

    /**
     * Exit the current scope.
     *
     */
    public void exitScope() throws IncorrectStructureException {
        if (scopes.isEmpty()) {
            throw new IncorrectStructureException(SCOPE_ERROR_MESSAGE);
        }
        scopes.pop();
        if (scopes.size() < methodScope) {
            exitMethodScope();
        }
    }

    /**
     * Lookup a variable in the current scope.
     *
     * @param name the name of the variable to lookup.
     * @return the variable.
     */
    public Variable lookupVariable(String name) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            Map<String, Variable> currentScope = scopes.get(i);
            if (currentScope.containsKey(name)) {
                return currentScope.get(name);
            }

        }
        return null;
    }

    /**
     * Declare a variable in the current scope.
     * If the variable is already declared in the current scope, an exception is thrown.
     *
     * @param name     the name of the variable to declare.
     * @param variable the variable to declare.
     */
    public void declareVariable(String name, Variable variable) throws SyntaxException {
        Map<String, Variable> currentScope = scopes.peek();
        if (currentScope.containsKey(name)) {
            throw new SyntaxException(DOUBLE_DECLARATION_ERROR_MESSAGE + name);
        }
        currentScope.put(name, variable);
    }



    /**
     * Check if the current scope is the global scope.
     *
     * @return true if the current scope is the global scope, false otherwise.
     */
    public boolean isGlobalScope() {
        return scopes.size() == 1;
    }

    /**
     * Get the depth of the current scope.
     *
     * @return the depth of the current scope.
     */
    public int getScopeDepth() {
        return scopes.size();
    }

    /**
     * Get the current method.
     *
     * @return the current method.
     */
    public Method getCurrentMethod() {
        return currentMethod;
    }

    /**
     * Check if the scope manager is inside a method.
     *
     * @return true if the scope manager is inside a method, false otherwise.
     */
    public boolean isInsideMethod() {
        return isInsideMethod;
    }

    /**
     * Enter a method scope, add parameters as variables of the scope.
     *
     * @param method the method to enter.
     */
    public void enterMethod(Method method) throws InvalidValueException, SyntaxException{
        this.currentMethod = method;
        this.isInsideMethod = true;
        this.methodScope = scopes.size();
        for (Variable param : method.getParameters()) {
            declareVariable(param.getName(), param);
            param.setIsInitialized(true); // set parameters as initialized,
            // as they are initialized by the method call
            param.addDefaultValue();

        }
    }

    /**
     * Set the global scope.
     *
     * @param globalVariablesCopy the global scope to set.
     */
    public void setGlobalScope(HashMap<String, Variable> globalVariablesCopy) {
        scopes.set(0, globalVariablesCopy);
    }
}