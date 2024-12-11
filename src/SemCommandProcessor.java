/**
 * The purpose of this class is to parse a single line from the command text
 * file according to the format specified in the project specs.
 * 
 * @author Sadath-Mohammed-msadath
 * @author Emadeldin-Abdrabou-emazied
 * 
 * @version 04-26-2024
 */
public class SemCommandProcessor {
    // the database object to manipulate the
    // commands that the command processor
    // feeds to it
    private SemDatabase data;

    /**
     * The constructor for the command processor requires a database instance to
     * exist, so the only constructor takes a database class object to feed
     * commands to.
     * 
     * Data member in this class to be initialized in the constructor is data,
     * which is the Database object to manipulate
     * 
     * @param memorySize
     *            integer value of initial memory pool size
     * 
     * @param hashTableInitCap
     *            integer value of initial hash table size
     */
    public SemCommandProcessor(int memorySize, int hashTableInitCap) {
        data = new SemDatabase(memorySize, hashTableInitCap);
    }


    /**
     * This method parses keywords in the line and calls methods in the
     * database as required. Each line command will be specified by one of the
     * keywords to perform the actions.
     * These actions are performed on specified objects and include insert,
     * delete, search, and dump. If the command in the file line is not
     * one of these, an appropriate message will be written in the console.
     * This processor method is called for each line in the file. Note that the
     * methods called will themselves write to the console, this method does
     * not, only calling methods that do.
     * 
     * line
     * a single line from the text file
     */

    // helper function to count characters in input array of strings
    private int countCharacters(String[] l) {
        int count = 0;
        for (int i = 0; i < l.length; i++) {
            count += l[i].length();
        }
        return count;
    }


    // helper function to check for integer data type
    private boolean isInteger(String intNum) {
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


    // helper function to check for short integer data type
    private boolean isShort(String shortNum) {
        try {
            // Attempt to parse the input string as an integer
            Short.parseShort(shortNum);
            // If parsing succeeds, return true
            return true;

        }
        catch (NumberFormatException e) {
            // If parsing fails (NumberFormatException is thrown), return false
            return false;
        }
    }


    // overload method for the processor with the insert command
    /**
     * Processor method to read the insert command
     * 
     * @param insertCommandDetails
     *            String array of insert command lines size of 5 lines
     * 
     * @throws Exception
     */
    public void processor(String[] insertCommandDetails) throws Exception {
        // Trim the leading and trailing whitespace before splitting command
        // and ID
        String[] arr1 = insertCommandDetails[0].trim().split("\\s{1,}");
        // keep intermediate spaces
        String arr2 = insertCommandDetails[1].trim();

        // date/time, length, xCoord, yCoord, cost
        String[] arr3 = insertCommandDetails[2].trim().split("\\s{1,}");

        // keyword list
        String[] arr4 = insertCommandDetails[3].trim().split("\\s{1,}");
        // keep intermediate spaces
        String arr5 = insertCommandDetails[4].trim();

        // double check that it reads insert command and each line character
        // counts without predefined limits
        if (!arr1[0].equals("insert") || arr1.length > 2 || arr2.length() > 80
            || arr3.length > 5 || countCharacters(arr4) > 80 || arr5
                .length() > 80 || !isInteger(arr1[1]) || !isInteger(arr3[1])
            || !isInteger(arr3[4]) || !isShort(arr3[2]) || !isShort(arr3[3])) {
            // just print error message and return without doing anything
            System.out.println("Usage: %> insert <valid integer ID> \n "
                + "<Valid title with lenght at most 80 characters> \n "
                + "<date and time (string)> <length (integer)> <x coordinate "
                + "(short integer)> <y coordinate (short integer)> "
                + "<cost (integer)> \n"
                + "<Valid keywords with lenght at most 80 characters> \n"
                + "<Valid description list with lenght "
                + "at most 80 characters>");
            // Exit
            return;
        }

        int seminarID = Integer.parseInt(arr1[1]);

        String seminarTitle = arr2;

        // store third line and its argument
        String seminarDate = arr3[0];
        int seminarRecordLength = Integer.parseInt(arr3[1]);
        short seminarCoordX = Short.parseShort(arr3[2]);
        short seminarCoordY = Short.parseShort(arr3[3]);
        int seminarCost = Integer.parseInt(arr3[4]);

        // store fourth line
        String[] seminarKeyWords = arr4;

        String seminarDiscription = arr5;

        // This is the main file for the program.
        Seminar sem = new Seminar(seminarID, seminarTitle, seminarDate,
            seminarRecordLength, seminarCoordX, seminarCoordY, seminarCost,
            seminarKeyWords, seminarDiscription);

        // we here expect that the insert method and data base should take the
        // seminar and serialize it, store bytes in memory and get the handle
        // with the associated ID
        data.insert(seminarID, sem);
    }


    /**
     * @param line
     *            read a string line from the file. This processor method is
     *            called in the main file
     * @throws Exception
     * @throws NumberFormatException
     */
    public void processor(String line) throws NumberFormatException, Exception {
        // Trim the leading and trailing whitespace before splitting
        String[] arr = line.trim().split("\\s{1,}");

        // the command will be the first of these elements
        String command = arr[0];

        // calls the appropriate remove method based on the
        // number of white space delimited strings in the line
        if (command.equals("delete")) {
            // // convert the format of the entered seminar ID
            // int semID = Integer.parseInt(arr[1]);

            // Calls delete by ID
            data.delete(Integer.parseInt(arr[1]));
        }
        else if (command.equals("search")) {
            // calls the search method for a name of object
            data.search(Integer.parseInt(arr[1]));
        }
        else if (command.equals("print") && arr[1].equals("hashtable")) {
            // calls the dump method for the database, takes no parameters
            // (see the dump() JavaDoc in the Database class for more
            // information)
            data.hashTableDump();
        }
        else if (command.equals("print") && arr[1].equals("blocks")) {
            // calls the dump method for the database, takes no parameters
            // (see the dump() JavaDoc in the Database class for more
            // information)
            data.memoryBlocksDataDump();
        }
        else { // we expect that case would never be called
               // the first white space delimited string in the line is not
               // one of the commands which can manipulate the database,
               // a message will be written to the console
               // do nothing or print an empty message
               // System.out.println("Unrecognized command.");
               // do nothing
        }
    }
}
