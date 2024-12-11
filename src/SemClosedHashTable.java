/**
 * The hash table data structure distinguish each record’s key from the
 * rest of that record’s value. Ideally, it is shielded from the fact that
 * records stored in a memory manager. The hash table design relies on storing
 * the key (one field of the record, i.e., seminar ID) and the value
 * that we use to store in the memory manager, which we call “Handle” to the
 * data record.
 * A handle is the value returned by the memory manager when a request
 * is made to insert a new record into the memory pool. This handle is used to
 * recover the record.
 * 
 * We adopt double hashing technique for improved collision resolution. When
 * hashing key value k; h1(k) = k mod M which is simply the ID mod the table
 * size, for double hashing h2(k) = (((k/M) mod (M/2)) ∗ 2) + 1.
 * 
 * The hash table size will always be a power of two. The hash table must be
 * extensible, in which, we start with a hash table of a certain size (user
 * defined value that passed when the program starts) and this must be a power
 * of two, or the program should immediately terminate with an error message. If
 * the hash table exceeds 50% full, then we replace the array with another that
 * is twice the size, and rehash all of the records from the old array.
 * For example, say that the hash table has 32 slots. Inserting 16 records is
 * OK. When you try to insert the 17th record, you would first re-hash all of
 * the original 16 records into a table of 64 slots and so on.
 * 
 * @author Sadath-Mohammed-msadath
 * @author Emadeldin-Abdrabou-emazied
 * 
 * @version 04-26-2024
 */
public class SemClosedHashTable {

    /** public interface for table entry object */
    public interface Entry {
        /** @return true if it is tombStone node and false if not */
        public boolean isTombStone();


        /** @return key of the entry it is an integer value of the key */
        public int getKey();


        /**
         * @return handle the value of the entry stored in the memory manager
         */
        public SemMemoryManager.Handle getValue();
    }


    /**
     * public nested class of a valid entry that implements the Entry interface
     */
    public class ValidEntry implements Entry {
        private int key;
        private SemMemoryManager.Handle value;

        /**
         * constructor for the valid entry object with values
         * 
         * @param key
         *            integer value of the entry's key
         * @param value
         *            the value of memory handle object for the entry stored in
         *            the memory manager
         */
        public ValidEntry(int key, SemMemoryManager.Handle value) {
            this.key = key;
            this.value = value;
        }


        /** @return true if it is tombStone node and false if not */
        public boolean isTombStone() {
            return false;
        }


        /** @return key of the entry it is an integer value of the key */
        public int getKey() {
            return key;
        }


        /**
         * @return handle the value of the entry stored in the memory manager
         */
        public SemMemoryManager.Handle getValue() {
            return value;
        }
    }


    /** Fly Weight design for the TombStone object instead of null */
    public class TombStone implements Entry {
        /** constructor for dummy entry */
        public TombStone() {
            // do nothing to replace the null object
        }


        /** @return key of the entry it is an integer value of the key */
        public int getKey() {
            return -1;
        }


        /** @return true if it is tombStone node and false if not */
        public boolean isTombStone() {
            return true;
        }


        // to keep consistent with the Entry interface ... it may not be called
        // throughout the application
        /**
         * @return handle the value of the entry stored in the memory manager
         */
        public SemMemoryManager.Handle getValue() {
            return null;
        }
    }

    private Entry[] table;
    private int size;
    private int capacity;
    private Entry tombStone = new TombStone();

    /**
     * Hash Table Constructor
     * 
     * @param initialCapacity
     *            integer value of the initial capacity of the hash table
     */
    public SemClosedHashTable(int initialCapacity) {
        // the check of power of two is already implemented in the main file
        // if (!isPowerOfTwo(initialCapacity)) {
        // System.err.println("Initial capacity must be a power of two.");
        // System.exit(1);
        // }
        this.capacity = initialCapacity;
        this.table = new Entry[capacity];
        this.size = 0;
    }

// // check if the size is correct value (is power of two)
// private boolean isPowerOfTwo(int n) {
// return (n & (n - 1)) == 0 && n != 0;
// }


    /**
     * Get the hash function of the key
     * 
     * @param key
     *            integer value of the entry's key (seminar ID)
     * @return Math.abs(key)%capacity
     *         integer value of hash function of the passed key
     */
    public int hashFunction(int key) {
        return Math.abs(key) % capacity;
    }


    /**
     * Get the hash two function of the key
     * 
     * @param key
     *            integer value of the entry's key (seminar ID)
     * @return (((Math.abs(key) / capacity) % (capacity / 2)) * 2) + 1
     *         integer value of hash function of the passed key
     */
    public int hash2(int key) {
        return (((Math.abs(key) / capacity) % (capacity / 2)) * 2) + 1;
    }


    /**
     * Get the double hash of the key
     * 
     * @param key
     *            integer value of the entry's key (seminar ID)
     * 
     * @param i
     *            integer value of hash 2 factor for double hashing design
     * 
     * @return (hashFunction(key) + i * hash2(key)) % capacity
     *         integer value of double hash function of the passed key and
     *         factor i
     */
    public int doubleHash(int key, int i) {
        return (hashFunction(key) + i * hash2(key)) % capacity;
    }


    /**
     * check if the key already exists in the table
     * 
     * @param key
     *            integer value of the entry's key (seminar ID)
     * 
     * @return true if the key exists and false if not
     * 
     */
    public boolean isDuplicate(int key) {
        int index = hashFunction(key);
        int i = 0;
        while (table[index] != null) {
            if (table[index].getKey() == key) {
                return true;
            }
            index = doubleHash(key, i++);
        }
        return false;
    }


    /**
     * @return the size of the hash table
     */
    public int getSize() {
        return size;
    }


    /**
     * @return the capacity of the hash table
     */
    public int getCapacity() {
        return capacity;
    }


    /**
     * insert method that use the key value and memory manager's handle
     * 
     * @param key
     *            integer data type that stores the key of the entry (seminar
     *            ID)
     * @param value
     *            Handle data type that stores the starting position in memory
     *            and the length of a seminar record
     * 
     */
    public void insert(int key, SemMemoryManager.Handle value) {
        // we moved the following check to the SemDatabase class
// if (size >= capacity / 2) {
// resize();
// }

        int index = hashFunction(key);
        int i = 0;
        // while (table[index] != null) {
        // check duplicate would be called from the SemDatabase insert
        // method
        // if (table[index].getKey() == key) {
        // System.out.println("Duplicates not allowed");
        // return;
        // }
        // index = doubleHash(key, i++);
        // }

        while (true) {
            if (table[index] == null || table[index].getKey() == -1) {
                table[index] = new ValidEntry(key, value);
                size++;
                return;
            }
            else {
                index = doubleHash(key, i++);
            }
        }
    }


    /**
     * search method where pass the key (seminar record ID) and return the
     * handle from memory manager
     *
     * @param key
     *            integer data type that stores the key of the entry (seminar
     *            ID)
     *
     * @return table[index].value, which is the handle of a record stored in
     *         memory manager if a record with the passed key found and null if
     *         not.
     */
    public SemMemoryManager.Handle search(int key) {
        int index = hashFunction(key);
        int i = 0;
        while (table[index] != null) {
            if (table[index].getKey() == key) {
                return table[index].getValue();
            }
            index = doubleHash(key, i++);
        }
        return null;
    }

// /**
// * another search method to move over all table contents
// * where pass the key (seminar record ID) and return the
// * handle from memory manager
// *
// * @param key
// * integer data type that stores the key of the entry (seminar
// * ID)
// *
// * @return table[index].value, which is the handle of a record stored in
// * memory manager if a record with the passed key found and null if
// * not.
// */
// public SemMemoryManager.Handle search(int key) {
// SemMemoryManager.Handle theReturnedHandle = null;
// for (int i = 0; i < capacity; i++) {
// // check the status of every cell in the array
// if (table[i] == null || table[i].getKey() == -1 || table[i]
// .isTombStone()) {
// continue;
// }
// else if (table[i].getKey() == key && table[i].getValue() != null) {
// theReturnedHandle = table[i].getValue();
// break;
// }
// else {
// // do nothing
// }
// }
// return theReturnedHandle;
// }


    /**
     * search method where pass the key (seminar record ID) and return the
     * handle from memory manager
     * 
     * @param handle
     *            memory manager handle data type
     * 
     * @return true if the handle in the hash table and false otherwise
     */
    public boolean contain(SemMemoryManager.Handle handle) {
        boolean handleIsContained = false;
        for (int i = 0; i < capacity; i++) {
            // check the status of every cell in the array
            if (table[i] != null && table[i].getKey() != -1) {
                handleIsContained = table[i].getValue().equals(handle);
            }
        }
        return handleIsContained;
    }


    /** print out hash table contents in human readable format */
    public void dump() {

        System.out.println("Hashtable:");

        for (int i = 0; i < capacity; i++) {
            // check the status of every cell in the array
            if (table[i] == null) {
                continue;
            }
            else {
                if (table[i].getKey() != -1 && !table[i].isTombStone()) {
                    System.out.println(i + ": " + table[i].getKey());
                }
                else {
                    System.out.println(i + ": " + "TOMBSTONE");
                }
            }
        }
        System.out.println("total records: " + size);

    }


    /**
     * delete method where pass the key (seminar record ID) to remove its
     * associated record *
     * 
     * @param key
     *            integer data type that stores the key of the entry (seminar
     *            ID)
     * @return true if the record ID found and successfully deleted
     *         and false otherwise
     */
    public boolean delete(int key) {
        int index = hashFunction(key);
        // int ema = hashFunction(15);
        // System.out.println("Hash function of key 15 is: " + ema);
        int i = 0;
        while (table[index] != null) {
            // System.out.println("The index value to iterate over hash table
            // is: "
            // + index);
            if (table[index].getKey() == key) {
                // table[index] = null; // Deleted by setting to null
                table[index] = tombStone; // to be developed using flyweight
                                          // design
                size--;
                return true;
            }
            index = doubleHash(key, i++);
        }
        return false; // added to check successful deletion in hashtable
    }


    /** resize the hash table size if needed */
    public void resize() {
        int newCapacity = capacity * 2;
        Entry[] newTable = new Entry[newCapacity];
        capacity = newCapacity;
        for (Entry entry : table) {
            if (entry != null && entry != tombStone) {
                int index = hashFunction(entry.getKey());
                int i = 0;
                while (newTable[index] != null) {
                    index = doubleHash(entry.getKey(), i++);
                }
                newTable[index] = entry;
            }
        }
        table = newTable;
        // capacity = newCapacity;
        System.out.println("Hash table expanded to " + capacity + " records");
    }
}
