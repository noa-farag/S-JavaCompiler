package ex5.helpers;

public class RegExConstants {
    // line analysis
    public static final String VARIABLE_ASSIGNMENT_REGEX = "^\\s*[A-Za-z_]+\\w*\\s*=\\s*[^\\s]+";
    public static final String COMMA_REGEX = ",";
    public static final String VARIABLE_DECLARATION_REGEX =
            "^\\s*(final\\s+)?(int|double|String|boolean|char)\\s+.+;$";
    public static final String METHOD_CALL_REGEX = "^\\s*[A-Za-z]+\\w*\\s*\\(.*\\)\\s*;\\s*";
    public static final String RETURN_REGEX = "^\\s*return\\s*;\\s*";
    public static final String SEMI_COLON_REGEX = ";";
    public static final String RIGHT_CURLY_BRACE = "}";
    public static final String LEFT_CURLY_BRACE = "{";
    public static final String IF_WHILE_REGEX = "^\\s*(if|while)\\s*\\(.*\\)\\s*\\{\\s*";
    public static final String VOID_REGEX = "^\\s*void\\s+";
    public static final String METHOD_DECLARATION_REGEX =
            "^\\s*void\\s+[A-Za-z]+\\w*\\s*\\([\\w\\s\\,]*\\)\\s*\\{\\s*";
    public static final String FINAL_REGEX = "^\\s*final\\s+.*";
    public static final String STRING_EXPRESSION = "\"([^\"]*)\"";
    public static final String REGEX_LEFT_CURLY_BRACELET = "\\{\\s*$";
    public static final String REGEX_RIGHT_CURLY_BRACELET = "^\\s*\\}\\s*$";
    public static final String FINAL_REDUNDANT_REGEX = "^\\s*final\\s+";
    public static final String SPACES_REGEX = "\\s+";
    public static final String EQUAL_REGEX = "=";
    public static final String RIGHT_BRACELET_REGEX = "\\(";
    public static final char RIGHT_BRACELET = ')';
    public static final char LEFT_BRACELET = '(';

    public static final String ONLY_SPACES = "^\\s+$";
    public static final String EMPTY_LINE = "//";
    public static final String OR_CONDITION_REGEX = "\\|\\|";
    public static final String AND_CONDITION_REGEX = "&&";
    public static final String METHOD_NAME_REGEX = "^[A-z]+\\w*";
    public static final String VARIABLE_NAME_REGEX = "^[A-Za-z]+\\w*$";
    public static final String VARIABLE_NAME_REGEX__ = "^_[A-Za-z0-9]+\\w*$";
    public static final String TRUE_FALSE_REGEX = "^(true|false)$";
    public static final String DOUBLE_DOT_IN_BEGINNING_REGEX = "^[+-]?(\\d+|\\.[0-9]+)$";
    public static final String DOUBLE_DOT_IN_MIDDLE_REGEX = "^[+-]?[0-9]+\\.$";
    public static final String DOUBLE_DOT_IN_END_REGEX = "^[+-]?[0-9]+\\.[0-9]+$";
    public static final String STRING_REGEX = "^\"([^,'\"\\\\])*\"$";
    public static final String CHAR_REGEX = "^\\'.{1}\\'$";
    public static final String INT_REGEX = "^[0-9]+$";
    public static final String POSITIVE_INT_REGEX = "^\\+[0-9]+$";
    public static final String NEGATIVE_INT_REGEX = "^\\-[0-9]+$";
}
