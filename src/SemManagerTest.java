import student.TestCase;

/**
 * @author Sadath-Mohammed-msadath
 * @author Emadeldin-Abdrabou-emazied
 * 
 * @version 04-26-20240
 */
public class SemManagerTest extends TestCase {
    /**
     * Sets up the tests that follow. In general, used for initialization
     */
    public void setUp() {
        // Nothing here
    }


    /**
     * Get code coverage of the class declaration.
     */
    public void testMInitx() {
        SemManager sem = new SemManager();
        assertNotNull(sem);
        // SemManager.main(null);
    }


    /**
     * Test invalid file case
     * 
     * @throws Exception
     */
    public void testInvalidFile() throws Exception {
        SemManager sem = new SemManager();
        String[] args = { "256", "256", "invalid-commands.txt" };
        sem.main(args);
        assertTrue(systemOut().getHistory().contains("Invalid file"));
    }


    /**
     * Test second integer arguments
     * 
     * @throws Exception
     */
    public void testInt1() throws Exception {
        SemManager sem = new SemManager();
        String[] args = { "8", "a", "commands.txt" };
        sem.main(args);
        assertTrue(systemErr().getHistory().contains(
            "Usage: %> java SemManager"));
    }


    /**
     * Test first integer arguments
     * 
     * @throws Exception
     */
    public void testInt2() throws Exception {
        SemManager sem = new SemManager();
        String[] args = { "b", "8", "commands.txt" };
        sem.main(args);
        assertTrue(systemErr().getHistory().contains(
            "Usage: %> java SemManager"));
    }


    /**
     * Test valid command
     * 
     * @throws Exception
     */
    public void testValid() throws Exception {
        SemManager sem = new SemManager();
        String[] args = { "256", "256", "commands.txt" };
        sem.main(args);
        assertTrue(systemOut().getHistory().contains(
            "Successfully inserted record with ID 1"));
    }


    /**
     * Test invalid power of two integer number 1
     * 
     * @throws Exception
     */
    public void testNonPowerOfTwo() throws Exception {
        SemManager sem = new SemManager();
        String[] args = { "253", "256", "commands.txt" };
        sem.main(args);
        assertTrue(systemErr().getHistory().contains(
            "Please double check that <initial-memory-size> and "
                + "<initial-hash-size> arguments are "
                + "power of two integer values"));
    }


    /**
     * Test invalid power of two integer number 2
     * 
     * @throws Exception
     */
    public void testNonPowerOfTwoB() throws Exception {
        SemManager sem = new SemManager();
        String[] args = { "256", "253", "commands.txt" };
        sem.main(args);
        assertTrue(systemErr().getHistory().contains(
            "Please double check that <initial-memory-size> and "
                + "<initial-hash-size> arguments are "
                + "power of two integer values"));
    }


    /**
     * Test invalid power of two integer number with zero case 1
     * 
     * @throws Exception
     */
    public void testPowerOfTwoZero() throws Exception {
        SemManager sem = new SemManager();
        String[] args = { "256", "0", "commands.txt" };
        sem.main(args);
        assertTrue(systemErr().getHistory().contains(
            "Please double check that <initial-memory-size> and "
                + "<initial-hash-size> arguments are "
                + "power of two integer values"));
    }


    /**
     * Test invalid power of two integer number with zero case 2
     * 
     * @throws Exception
     */
    public void testPowerOfTwoZero2() throws Exception {
        SemManager sem = new SemManager();
        String[] args = { "0", "256", "commands.txt" };
        sem.main(args);
        assertTrue(systemErr().getHistory().contains(
            "Please double check that <initial-memory-size> and "
                + "<initial-hash-size> arguments are "
                + "power of two integer values"));
    }


    /**
     * Test valid input command
     * 
     * @throws Exception
     */
    public void testValidInput() throws Exception {
        SemManager sem = new SemManager();
        String[] args = { "256", "256", "commands.txt" };
        sem.main(args);
        assertTrue(systemOut().getHistory().contains(
            "Successfully inserted record with ID 1"));
        assertTrue(systemOut().getHistory().contains(
            "ID: 1, Title: Seminar Title"));
        assertTrue(systemOut().getHistory().contains(
            "Date: 2024-04-26, Length: 2, X: 100, Y: 200, Cost: 50"));
        assertTrue(systemOut().getHistory().contains(
            "Keywords: keyword1, keyword2, keyword3"));
        assertTrue(systemOut().getHistory().contains(
            "Description: This is a description for the seminar."));
        assertTrue(systemOut().getHistory().contains(
            "Delete FAILED -- There is no record with ID 2"));
        assertTrue(systemOut().getHistory().contains(
            "Search FAILED -- There is no record with ID 3"));
        assertTrue(systemOut().getHistory().contains("Hashtable:"));
        assertFalse(systemOut().getHistory().contains(": TOMBSTONE"));
        assertTrue(systemOut().getHistory().contains("Freeblock List:"));
    }
    // After 'insert'
    // size 128 working
    // size 134 working
    // size 129-133 NOT working

}
