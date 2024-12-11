// import java.util.Iterator;

/**
 * This class is responsible for interfacing between the command processor and
 * the hash table as well as memory manager. The responsibility of this class is
 * to further interpret
 * variations of commands and do some error checking of those commands. This
 * class further interpreting the command means that the two types of remove
 * will be overloaded methods for if we are inserting, searching, or removing a
 * record.
 * 
 * Many of these methods will simply call the appropriate version of the
 * Hash Table methods after some preparation.
 * 
 * @author Sadath-Mohammed-msadath
 * @author Emadeldin-Abdrabou-emazied
 * 
 * @version 04-26-2024
 * 
 */
public class SemDatabase {
    // this is the Hash Table object that we are using
    // an integer for the seminar ID or record key
    // and handle object for memory manager access
    private SemClosedHashTable hashTable;

    // this is the Memory Manager object that we are using
    // to store a seminar object as bytes array and use handle to exchange
    // information between the memory manager and hash table
    private SemMemoryManager memManager;

    // This is an Iterator object over the hash table to loop through it from
    // to check the existence of the ID and not allowing duplicate ID
    // private Iterator itr1;

    /**
     * The constructor for this class initializes a hash table with its initial
     * capacity and initial memory pool size
     * 
     * @param mSize
     *            integer value of initial memory pool size which is value of
     *            power of two
     * 
     * @param hTCapacity
     *            integer value of initial hash table capacity
     */
    public SemDatabase(int mSize, int hTCapacity) {
        // instantiate hash table object
        hashTable = new SemClosedHashTable(hTCapacity);

        // instantiate memory manager object
        memManager = new SemMemoryManager(mSize);
    }


    /**
     * 01- Insert the seminar object
     * 
     *
     * @param semID
     *            integer value of the unique seminar ID
     * 
     * @param sem
     *            Seminar object
     * @throws Exception
     */
    public void insert(int semID, Seminar sem) throws Exception {
        // The logic of calling insert method in memory manager and hash table
        // goes here

        // 1- call the hashTable is duplicate to check the if the sem ID exists,
        // if yes print failure message and exit
        if (hashTable.isDuplicate(semID)) {
            System.out.println(
                "Insert FAILED - There is already a record with ID " + semID);
            return;
        }
        // otherwise continue with the
        // following step
        // check if we need to resize memory pool
        // 2- serialize the the seminar object to get it in bytes format
        byte[] semSerialized = sem.serialize();
        int semSize = semSerialized.length;
        // System.out.println("Seminar byte array size: " + semSize); // checked

        int currentMemPoolSize = memManager.getPoolSize();
        int currentMemAllocSize = memManager.calculateTotalAllocatedSize();
        int requestedBlockSize = memManager.calculateBlockSize(semSize);

        // System.out.println("current allocated size: " + currentMemAllocSize
        // + " record size: " + semSize + " requested block size: " +
        // requestedBlockSize
        // + " available size: "
        // + (currentMemPoolSize - currentMemAllocSize)); // checked

        if (currentMemAllocSize + requestedBlockSize > currentMemPoolSize) {
            // System.out.println(
            // "Did you check the allocation size to resize the pool"); //
            // checked
            memManager.resizePool();

            // System.out.println("New pool size is: " +
            // memManager.getPoolSize()
            // + " bytes");
        }
        // 3- store the byte format of the seminar object in the memory manager
        // (call insert method of memory manager)
        SemMemoryManager.Handle semHandle = memManager.insert(semSerialized,
            semSize);

        // check the hash table does not have the handle (not same block start
        // is allowed)

// if (hashTable.contain(semHandle)) {
// System.out.println(
// "Insert FAILED - There is already a record with the same "
// + "handle that starts at: " + semHandle.getStart());
// return;
// } // checked
// 4- the returned handle of the memory manager used for hash table
// insertion with the seminar ID, but we need to check hash table size
// before inserting the handle in the hash table
        int currentHashTableSize = hashTable.getSize();
        int currentHashTableCapaicty = hashTable.getCapacity() / 2;
        if (currentHashTableSize >= currentHashTableCapaicty) {
            // resize the hash table
            hashTable.resize();
        }

        // call insert method in the hash table
        hashTable.insert(semID, semHandle);

        // 5- print out the required successful insertion message
        System.out.println("Successfully inserted record with ID " + semID);
        System.out.println(sem.toString());
        System.out.println("Size: " + semSize);
    }


    /**
     * 02- delete a record given its key, i.e., seminar ID
     * 
     * @param semID
     *            integer value of the seminar ID
     */
    public void delete(int semID) {
        // The logic of record deletion goes here
        // 1- find the record ID, if exists continue with the following steps,
        // otherwise return a failure message that the given ID does not exist
        SemMemoryManager.Handle semHandle = hashTable.search(semID);
        if (semHandle == null) {
            System.out.println("Delete FAILED -- There is no record with ID "
                + semID);
        }
        // 2- after finding the record get its handle and remove it from hash
        // table and memory manager by calling the delete method of each data
        // structure
        else {
            // delete the record from the Hash Table
            // hashTable.delete(semID);
            if (hashTable.delete(semID)) {
                // delete the record from the memory manager
                memManager.remove(semHandle);
                System.out.println("Record with ID " + semID
                    + " successfully deleted from the database");
            }
        }
    }


    /**
     * 03- search for a record by a given key, i.e., seminar ID
     * 
     * 
     * @param seminID
     *            integer value of the seminar ID
     * @throws Exception
     * 
     */
    public void search(int seminID) throws Exception {
        // The logic of record search goes here
        // 1- find the record ID, if exists continue with the following steps,
        // otherwise return a failure message that the given ID does not exist
        SemMemoryManager.Handle semHandle = hashTable.search(seminID);
        // // Printing debug
        // if (semHandle != null) {
        // System.out.println("Handle start is: " + semHandle.getStart()
        // + " and handle size is: " + semHandle.getSize());
        // }

        if (semHandle == null) {
            System.out.println("Search FAILED -- There is no record with ID "
                + seminID);
        }
        // 2- after finding the record in the hash table, get its handle and
        // call its contents from memory manager and print appropriate messages
        // Found record with ID 3:
        /*
         * ID: 3, Title: Computing Systems Research at VT
         * Date: 0701250830, Length: 30, X: 30, Y: 10, Cost: 17
         * Description: Seminar about the Computing systems research at VT
         * Keywords: high_performance_computing, grids, VT, computer, science
         */
        else {
            // instantiate a byte array to copy the content of the handle in the
            // memory pool
            byte[] semDeserial = new byte[memManager.length(semHandle)];
            int recordLen = memManager.get(semDeserial, semHandle, memManager
                .length(semHandle));
            if (recordLen == semDeserial.length) {
                System.out.println("Found record with ID " + seminID + ":");
                // convert the byte array to seminar object
                Seminar semi = Seminar.deserialize(semDeserial);
                // print out the contents of seminar record
                System.out.println(semi.toString());
            }
        }
    }


    /**
     * 04- Hash table Dump
     * Prints out a dump of the Hash table contents in the format as described
     * in the project description and sample out files
     */
    public void hashTableDump() {
        // to create the dump method in the hash table data structure and call
        // it here
        hashTable.dump();
    }


    /**
     * 05- Memory blocks Dump
     * Prints out a dump of the memory pool contents in the format as described
     * in the project description and sample out files
     */
    public void memoryBlocksDataDump() {
        // to create the dump method in the memory manager data structure and
        // call it here
        memManager.dump();
    }
}
