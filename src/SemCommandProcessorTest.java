import student.TestCase;

/**
 * This class is used to perform testing on CommandProcessor.java class
 * 
 * 
 * @author Sadath-Mohammed-msadath
 * @author Emadeldin-Abdrabou-emazied
 * 
 * @version 04-27-20240
 */

public class SemCommandProcessorTest extends TestCase {

    // Creates an object of CommandProcessor class
    private SemCommandProcessor cmd = new SemCommandProcessor(256, 2);

    /**
     * Test valid insert method
     * 
     * @throws Exception
     * @throws NumberFormatException
     */
    public void testValidInsert() throws NumberFormatException, Exception {

        String[] commands = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 90 10 10 45", "HCI Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of HCI research at VT" };

        cmd.processor(commands);
        assertTrue(systemOut().getHistory().contains(
            "Successfully inserted record with ID 1"));

    }


    /**
     * Test duplicate insert
     * 
     * @throws Exception
     * @throws NumberFormatException
     */
    public void testDuplicateInsert() throws NumberFormatException, Exception {

        String[] commands = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 90 10 10 45", "HCI Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of HCI research at VT" };
        String[] commands2 = { "insert 1", "Overviewsss of HCI Research at VT",
            "0610051600 80 20 50 85",
            "HCIssssss Computer_Science VT Virginia_Tech",
            "This sssssss will present an overview of HCI research at VT" };

        cmd.processor(commands);
        cmd.processor(commands2);
        assertTrue(systemOut().getHistory().contains(
            "Successfully inserted record with ID 1"));
        assertTrue(systemOut().getHistory().contains(
            "Insert FAILED - There is already a record with ID 1"));
    }

// public void testCount() {
// String[] line = {"abcdef ghij", "afsaf"};
// assertEquals(cmd.countCharacters(line), 16);
// }


    /**
     * Test invalid command
     * 
     * @throws Exception
     * 
     */
    public void testInvalidCommand() throws Exception {
        String[] commands = { "invalid 1", "Overview of HCI Research at VT",
            "0610051600 90 10 10 45", "HCI Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of HCI research at VT" };

        cmd.processor(commands);
        assertTrue(systemOut().getHistory().contains("Usage: %>"));
    }


    /**
     * Test insert method with invalid title length
     * 
     * @throws Exception
     * 
     */
    public void testTitleTooLong() throws Exception {
        String[] commands = { "insert 1",
            "This is a very long title that exceeds the maximum length of 80 "
                + "characters because it contains more than 80 characters",
            "0610051600 90 10 10 45", "HCI Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of HCI research at VT" };

        cmd.processor(commands);

        assertTrue(systemOut().getHistory().contains("Usage: %>"));
    }

// public void testDateTimeFormatInvalid() throws Exception {
// String[] commands = {
// "insert 1",
// "Overview of HCI Research at VT",
// "invalid_datetime_format 90 10 10 45",
// "HCI Computer_Science VT Virginia_Tech",
// "This seminar will present an overview of HCI research at VT"
// };
//
// cmd.processor(commands);
//
// assertTrue(systemOut().getHistory().contains("Usage: %>"));
// }


    /**
     * Test insert method with invalid cost data type
     * 
     * @throws Exception
     * 
     */
    public void testInvalidCost() throws Exception {
        String[] commands = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 90 10 10 invalid_cost",
            "HCI Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of HCI research at VT" };

        cmd.processor(commands);

        assertTrue(systemOut().getHistory().contains("Usage: %>"));
    }


    /**
     * Test insert method with invalid keywords
     * 
     * @throws Exception
     * 
     */
    public void testKeywordListTooLong() throws Exception {
        String[] commands = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 90 10 10 45",
            "This keyword list contains a lot of keywords which exceed "
                + "the maximum length of 80 characters "
                + "in order to produce test failure",
            "This seminar will present an overview of HCI research at VT" };

        cmd.processor(commands);

        assertTrue(systemOut().getHistory().contains("Usage: %>"));
    }


    /**
     * Test insert method with invalid description length
     * 
     * @throws Exception
     * 
     */
    public void testDescriptionTooLong() throws Exception {
        String[] commands = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 90 10 10 45",
            "This keyword list contains a lot of keywords",
            "This seminar will present an overview of HCI research at VT "
                + "which exceed the maximum length of 80 characters "
                + "in order to produce test failure" };

        cmd.processor(commands);

        assertTrue(systemOut().getHistory().contains("Usage: %>"));
    }


    /**
     * Test insert method with invalid array list length of first array arr1
     * 
     * @throws Exception
     * 
     */
    public void testArr1ListTooLong() throws Exception {
        String[] commands = { "insert 1 2", "Overview of HCI Research at VT",
            "0610051600 90 10 10 45", "HCI Computer_Science VT Virginia_Tech",
            "This description list contains description" };

        cmd.processor(commands);

        assertTrue(systemOut().getHistory().contains("Usage: %>"));
    }


    /**
     * Test insert method with invalid array list length of third array arr3
     * 
     * @throws Exception
     * 
     */
    public void testArr3ListTooLong() throws Exception {
        String[] commands = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 90 10 10 45 55",
            "HCI Computer_Science VT Virginia_Tech",
            "This description list contains description" };

        cmd.processor(commands);

        assertTrue(systemOut().getHistory().contains("Usage: %>"));
    }


    /**
     * Test insert method with x coordinates
     * 
     * @throws Exception
     * 
     */
    public void testXInvalid() throws Exception {
        String[] commands = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 90 a 10 45", "HCI Computer_Science VT Virginia_Tech",
            "This description list contains description" };

        cmd.processor(commands);

        assertTrue(systemOut().getHistory().contains("Usage: %>"));
    }


    /**
     * Test insert method with y coordinates
     * 
     * @throws Exception
     * 
     */
    public void testYInvalid() throws Exception {
        String[] commands = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 90 12 b 45", "HCI Computer_Science VT Virginia_Tech",
            "This description list contains description" };

        cmd.processor(commands);

        assertTrue(systemOut().getHistory().contains("Usage: %>"));
    }


    /**
     * Test insert method with invalid test ID number
     * 
     * @throws Exception
     * 
     */
    public void testIDNotInteger() throws Exception {
        String[] commands = { "insert invalid_id",
            "Overview of HCI Research at VT", "0610051600 90 10 10 45",
            "HCI Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of HCI research at VT" };

        cmd.processor(commands);

        assertTrue(systemOut().getHistory().contains("Usage: %>"));
    }


    /**
     * Test insert method with invalid x coordinates
     * 
     * @throws Exception
     * 
     */
    public void testInvalidXCoord() throws Exception {
        String[] commands = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 invalid_xcoord 10 10 45",
            "HCI Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of HCI research at VT" };

        cmd.processor(commands);

        assertTrue(systemOut().getHistory().contains("Usage: %>"));
    }


    /**
     * Test insert method with invalid y coordinate
     * 
     * @throws Exception
     * 
     */
    public void testInvalidYCoord() throws Exception {
        String[] commands = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 90 10 invalid_ycoord 45",
            "HCI Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of HCI research at VT" };

        cmd.processor(commands);

        assertTrue(systemOut().getHistory().contains("Usage: %>"));
    }


    /**
     * Test insert method with invalid length
     * 
     * @throws Exception
     * 
     */
    public void testInvalidLength() throws Exception {
        String[] commands = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 invalid_length 10 10 45",
            "HCI Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of HCI research at VT" };

        cmd.processor(commands);

        assertTrue(systemOut().getHistory().contains("Usage: %>"));
    }


    /**
     * Test delete method
     * 
     * @throws Exception
     * 
     * @throws NumberFormatException
     */
    public void testValidDelete() throws NumberFormatException, Exception {

        String[] commands = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 90 10 10 45", "HCI Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of HCI research at VT" };

        cmd.processor("delete 4");
        assertTrue(systemOut().getHistory().contains(
            "Delete FAILED -- There is no record with ID 4"));
        cmd.processor("print hashtable");
        assertFalse(systemOut().getHistory().contains(": TOMBSTONE"));

        systemOut().clearHistory();
        cmd.processor(commands);
        cmd.processor("delete 1");
        assertTrue(systemOut().getHistory().contains(
            "Record with ID 1 successfully deleted from the database"));

        cmd.processor("print hashtable");
        assertTrue(systemOut().getHistory().contains("total records: 0"));
    }


    /**
     * Test search method
     * 
     * @throws Exception
     * 
     * @throws NumberFormatException
     */
    public void testValidSearch() throws NumberFormatException, Exception {

        String[] commands = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 90 10 10 45", "HCI Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of HCI research at VT" };

        String[] commands2 = { "insert 2",
            "Overview of Software Research at VT", "0610051600 90 10 10 45",
            "Software Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of "
                + "Software research at VT" };

        cmd.processor(commands);
        cmd.processor(commands2);
        cmd.processor("search 2");
        cmd.processor("search 4");

        assertTrue(systemOut().getHistory().contains(
            "Found record with ID 2:"));
        assertTrue(systemOut().getHistory().contains(
            "ID: 2, Title: Overview of Software Research at VT"));
        assertTrue(systemOut().getHistory().contains(
            "Search FAILED -- There is no record with ID 4"));
    }


    /**
     * Test dump method
     * 
     * @throws Exception
     * 
     * @throws NumberFormatException
     */
    public void testValidTableDump() throws NumberFormatException, Exception {

        String[] commands1 = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 90 10 10 45", "HCI Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of HCI research at VT" };
        String[] commands2 = { "insert 2", "Overview of ML Research at VT",
            "0610051600 90 10 10 45", "ML Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of ML research at VT" };

        String[] commands3 = { "insert 3", "Overview of SWE Research at VT",
            "0610051600 90 10 10 45", "SWE Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of SWE research at VT" };

        String[] commands10 = { "insert 10", "Overview of Arch Research at VT",
            "0610051600 90 10 10 45", "Arch Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of Arch research at VT" };

        cmd.processor(commands1);
        cmd.processor(commands2);
        cmd.processor(commands3);
        cmd.processor(commands10);
        cmd.processor("print hashtable");

// cmd.processor("delete 1");
// cmd.processor("delete 4");

        assertTrue(systemOut().getHistory().contains(": 1"));
        assertTrue(systemOut().getHistory().contains(": 2"));
        assertTrue(systemOut().getHistory().contains(": 3"));
        assertTrue(systemOut().getHistory().contains(": 10"));

        systemOut().clearHistory();
        cmd.processor("delete 2");
        cmd.processor("print hashtable");
        assertTrue(systemOut().getHistory().contains("total records: 3"));
        assertFalse(systemOut().getHistory().contains(": 2"));
        assertTrue(systemOut().getHistory().contains(": TOMBSTONE"));

        systemOut().clearHistory();
        cmd.processor("delete 2");
        cmd.processor("print hashtable");
        assertTrue(systemOut().getHistory().contains("total records: 3"));

        systemOut().clearHistory();
        cmd.processor(commands2);
        cmd.processor("print hashtable");
        assertTrue(systemOut().getHistory().contains(": 2"));
        assertFalse(systemOut().getHistory().contains(": TOMBSTONE"));

        systemOut().clearHistory();
        cmd.processor("delete 10");
        cmd.processor("print hashtable");
        assertFalse(systemOut().getHistory().contains(": 10"));
        assertTrue(systemOut().getHistory().contains(": TOMBSTONE"));

        systemOut().clearHistory();
        cmd.processor("print invalid_hashtable");
        assertFalse(systemOut().getHistory().contains(": TOMBSTONE"));

        systemOut().clearHistory();
        cmd.processor("invalid_print hashtable");
        assertFalse(systemOut().getHistory().contains(": TOMBSTONE"));

        systemOut().clearHistory();
        cmd.processor("invalid_print blocks");
        assertFalse(systemOut().getHistory().contains("Freeblock List:"));

        systemOut().clearHistory();
        cmd.processor("print blocks");
        assertTrue(systemOut().getHistory().contains("Freeblock List:"));

        systemOut().clearHistory();
        cmd.processor("print invalid_blocks");
        assertFalse(systemOut().getHistory().contains("Freeblock List:"));

    }


    /**
     * Test resize method
     * 
     * @throws Exception
     * 
     * @throws NumberFormatException
     */
    public void testResize() throws NumberFormatException, Exception {

        String[] commands1 = { "insert 1", "Overview of HCI Research at VT",
            "0610051600 90 10 10 45", "HCI Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of HCI research at VT" };
        String[] commands2 = { "insert 2", "Overview of ML Research at VT",
            "0610051600 90 10 10 45", "ML Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of ML research at VT" };

        String[] commands3 = { "insert 3", "Overview of SWE Research at VT",
            "0610051600 90 10 10 45", "SWE Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of SWE research at VT" };

        String[] commands10 = { "insert 10", "Overview of Arch Research at VT",
            "0610051600 90 10 10 45", "Arch Computer_Science VT Virginia_Tech",
            "This seminar will present an overview of Arch research at VT" };
        String[] commands7 = { "insert 7", "7 Overview of HCI Research at VT",
            "0610051600 90 10 10 45", "7 HCI Computer_Science VT Virginia_Tech",
            "7 This seminar will present an overview of HCI research at VT" };
        String[] commands6 = { "insert 6", "6 Overview of HCI Research at VT",
            "0610051600 90 10 10 45", "6 HCI Computer_Science VT Virginia_Tech",
            "6 This seminar will present an overview of HCI research at VT" };

        cmd.processor(commands1);
        cmd.processor("print hashtable");
        cmd.processor("print blocks");
        assertTrue(systemOut().getHistory().contains(
            "There are no freeblocks in the memory pool"));

        systemOut().clearHistory();
        cmd.processor(commands2);
        assertTrue(systemOut().getHistory().contains(
            "Hash table expanded to 4 records"));
        cmd.processor("print hashtable");
        cmd.processor("print blocks");

        systemOut().clearHistory();
        cmd.processor(commands3);
        assertTrue(systemOut().getHistory().contains(
            "Hash table expanded to 8 records"));
        cmd.processor("print hashtable");
        cmd.processor("print blocks");

        systemOut().clearHistory();
        cmd.processor(commands10);
        cmd.processor("print hashtable");
        cmd.processor("print blocks");

        systemOut().clearHistory();
        // cmd.processor(commands6);
        // assertTrue(systemOut().getHistory().contains("Hash table expanded to
        // 16 records"));
        cmd.processor("print hashtable");
        cmd.processor("print blocks");

        systemOut().clearHistory();
        // cmd.processor(commands7);
        cmd.processor("print hashtable");
        cmd.processor("print blocks");
    }

}
