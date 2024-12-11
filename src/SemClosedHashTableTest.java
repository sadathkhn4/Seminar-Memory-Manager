import student.TestCase;

/**
 * This class is used to perform testing on SemClosedHashTable.java class
 * 
 * 
 * @author Sadath-Mohammed-msadath
 * @author Emadeldin-Abdrabou-emazied
 * 
 * @version 04-26-20240
 */

public class SemClosedHashTableTest extends TestCase {

    /**
     * Creates an object of SemClosedHashTable class
     */

    public void testHash2() {
        SemClosedHashTable ht = new SemClosedHashTable(16);
        assertEquals(ht.hash2(2), 1);
        assertEquals(ht.hash2(32), 5);
    }


    /**
     * Creates an object of SemClosedHashTable class and test hash 2 function
     */
    public void testHash2a() {
        SemClosedHashTable ht = new SemClosedHashTable(2);
        assertEquals(ht.hash2(16), 1);
        assertEquals(ht.doubleHash(16, 3), 1);
    }


    /**
     * Creates an object of SemClosedHashTable class and test different hash
     * functions
     */
    public void testHash2b() {
        SemClosedHashTable ht = new SemClosedHashTable(8);
        assertEquals(ht.hash2(41), 3);
        assertEquals(ht.doubleHash(41, 4), 5);

        assertEquals(ht.hash2(40), 3);
        assertEquals(ht.doubleHash(40, 3), 1);

        assertEquals(ht.hash2(39), 1);
        assertEquals(ht.doubleHash(39, 7), 6);

        assertEquals(ht.hash2(59), 7);
        assertEquals(ht.doubleHash(59, 11), 0);
    }

// public void testSize() {
// SemClosedHashTable ht = new SemClosedHashTable(8);
//
// }

}
