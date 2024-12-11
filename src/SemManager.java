import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Build a memory management system for Seminar training sessions. Using the
 * system, it will be possible to enter records for training sessions, delete
 * them, and search by session ID.
 * 
 * A memory management package aims to store variable-length records in a large
 * memory space, and use a hash table to access the
 * records by a simple key value.
 * 
 * The records will be a serialized version of the seminar information.
 * 
 */

/**
 * The class containing the main method.
 *
 * @author Sadath-Mohammed-msadath
 * @author Emadeldin-Abdrabou-emazied
 * 
 * @version 04-26-2024
 */

// On my honor:
// - I have not used source code obtained from another current or
// former student, or any other unauthorized source, either
// modified or unmodified.
//
// - All source code and documentation used in my program is
// either my original work, or was derived by me from the
// source code published in the textbook for this course.
//
// - I have not discussed coding details about this project with
// anyone other than my partner (in the case of a joint
// submission), instructor, ACM/UPE tutors or the TAs assigned
// to this course. I understand that I may discuss the concepts
// of this program with other students, and that another student
// may help me debug my program so long as neither of us writes
// anything during the discussion or modifies any computer file
// during the discussion. I have violated neither the spirit nor
// letter of this restriction.

public class SemManager {
    /**
     * @param args
     *            Command line parameters
     * 
     *            java SemManager args[]
     * 
     *            java SemManager {initial-memory-size} {initial-hash-size}
     *            {command-file}
     * 
     *            SemManager is then name of the program.
     * 
     *            args[0] << initial-memory-size >>
     *            is an integer that is the initial size of the memory pool (in
     *            bytes) and is a power of two.
     * 
     *            args[1] << initial-hash-size >>
     *            is an integer that specifies the initial size of the hash
     *            table (in terms of slots) and is a power of two.
     * 
     *            args[2] << command-file >>
     *            is the name of the command file to read.
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // the file containing the commands
        File file = null;

        // check if the memory size and hash table initial capacity arguments
        // are integer
        if (!isInteger(args[0]) || !isInteger(args[1])) {
            // system exit
            System.err.println(
                "Usage: %> java SemManager <{initial-memory-size} (integer)> "
                    + "<{initial-hash-size} (integer)> <{command-file}>");
            // exit with error code 1
            // System.exit(1);
            return;
        }

        // check if the memory size and hash table initial capacity arguments
        // are power of two
        int memoryPoolSize = Integer.parseInt(args[0]);
        int initialHashTableSize = Integer.parseInt(args[1]);

        if (!isPowerOfTwo(memoryPoolSize) || !isPowerOfTwo(
            initialHashTableSize)) {
            // system exit
            System.err.println(
                "Please double check that <initial-memory-size> and "
                    + "<initial-hash-size> "
                    + "arguments are power of two integer values");
            // exit with error code 1
            // System.exit(1);
            return;
        }

        // Attempts to open the file and scan through it
        try {
            // takes the first command line argument and opens that file
            file = new File(args[2]);

            // creates a scanner object
            Scanner scanner = new Scanner(file);

            // creates a command processor object with memory size and initial
            // hash table size
            SemCommandProcessor cmdProc = new SemCommandProcessor(
                memoryPoolSize, initialHashTableSize);

            // reads the entire file and processes the commands
            // line by line
            ArrayList<String> commands = new ArrayList<>();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                // determines if the file has more lines to read
                if (!line.trim().isEmpty()) { // check line is not blank line
                    commands.add(line);
                }
            }

            int i = 0;
            // System.out.println(commands.size());
            while (i < commands.size()) {
                String commandType = commands.get(i).trim().split("\\s{1,}")[0];
                if (commandType.equals("insert")) {
                    String[] insertCommand = { commands.get(i), commands.get(i
                        + 1), commands.get(i + 2), commands.get(i + 3), commands
                            .get(i + 4) };
                    cmdProc.processor(insertCommand);
                    i += 5;
                }
                else if (commandType.equals("search") || commandType.equals(
                    "delete") || commandType.equals("print")) {
                    cmdProc.processor(commands.get(i));
                    i++;
                }
                else {

                    // System.out.println(commands.get(i));
                    i++;
                }
            }
            // closes the scanner
            scanner.close();
        }
        // catches the exception if the file cannot be found
        // and outputs the correct information to the console
        catch (FileNotFoundException e) {
            System.out.println("Invalid file");
            e.printStackTrace();
        }
    }


    // helper function to check for integer data type
    private static boolean isInteger(String intNum) {
        try {
            // Attempt to parse the input string as an integer
            Integer.parseInt(intNum);
            // If parsing succeeds, return true
            return true;

        }
        catch (NumberFormatException e) {
            // If parsing fails (NumberFormatException is thrown), return false
            return false;
        }
    }


    // helper function to check if the memory pool size or hash table size is
    // power of two
    private static boolean isPowerOfTwo(int n) {
        return (n & (n - 1)) == 0 && n != 0;
    }
}
