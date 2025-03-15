package ex5.main;


import ex5.helpers.InvalidLineSyntaxException;
import ex5.models.InvalidNameException;
import ex5.models.InvalidValueException;
import ex5.models.InvalidVariableTypeException;

import java.io.FileNotFoundException;

import java.io.IOException;


/**
 * The ex5.main class of the Sjavac program.
 *
 * @author noa.farag, noya.ashkenazi
 */
public class Sjavac {


    private static final String INVALID_NUMBER_OF_ARGUMENTS = "Invalid number of arguments";
    private static final String PASSED_ALL_TESTS = "0";
    private static final String FOUND_ERRORS = "1";
    private static final String GENERAL_ERROR = "2";
    private static final String FILE_NOT_FOUND_ERROR = "File not found";
    private static final String AN_ERROR_OCCURRED = "An error occurred";
    private static final String FILE_FORMAT = ".sjava";
    private static final String INVALID_FILE_NAME = "Invalid file name";

    private String file;

    /**
     * The constructor of the Sjavac class.
     *
     * @param file the scanner to read the file.
     */
    public Sjavac(String file) {
        this.file = file;
    }

    /**
     * The method that runs the Sjavac program.
     *
     * @throws IOException if an error occurs while reading the file.
     */
    public void run() throws IOException, InvalidVariableTypeException, InvalidNameException,
            InvalidValueException, InvalidLineSyntaxException, CompileException {
        ScopeManager scopeManager = new ScopeManager();
        GeneralScanner generalScanner = new GeneralScanner(scopeManager, file);
        generalScanner.firstFileScan();
        generalScanner.finalFileScan();
    }


    /**
     * The ex5.main method of the Sjavac program.
     *
     * @param args the arguments given to the program.
     */
    public static void main(String[] args) throws IOException {
        try {


            if (args.length != 1) {
                //need to catch this exception.
                throw new IOException(INVALID_NUMBER_OF_ARGUMENTS);
            }
            String filePath = args[0];
            checkSjavaFileName(filePath);

            Sjavac sjavacManager = new Sjavac(filePath);
            sjavacManager.run();

            // if successful, print 0
            System.out.println(PASSED_ALL_TESTS);


        }
        catch (InvalidLineSyntaxException | InvalidNameException | InvalidValueException |
               InvalidVariableTypeException e) {
            System.err.println(e.getMessage());
            System.out.println(FOUND_ERRORS);
        }
        catch (SyntaxException e) {
            System.err.println(e.getMessage());
            System.out.println(FOUND_ERRORS);
        }
        catch (CompileException e) {
            System.err.println(e.getMessage());
            System.out.println(FOUND_ERRORS);
        }
        catch (FileNotFoundException e) {
            System.err.println(FILE_NOT_FOUND_ERROR);
            System.out.println(GENERAL_ERROR);

        }
        catch (IOException e) {
            System.err.println(e.getMessage());
            System.out.println(GENERAL_ERROR);

        }
        catch (Exception e) {
            System.err.println(AN_ERROR_OCCURRED);
            System.out.println(GENERAL_ERROR);
        }

    }

    private static void checkSjavaFileName(String filePath) throws IOException {
        if (!filePath.endsWith(FILE_FORMAT)) {
            throw new IOException(INVALID_FILE_NAME);
        }
    }
}

