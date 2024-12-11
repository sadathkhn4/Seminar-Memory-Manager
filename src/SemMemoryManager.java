import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;

/**
 *
 *
 * @author Sadath-Mohammed-msadath
 * @author Emadeldin-Abdrabou-emazied
 * 
 * @version 04-26-2024
 */
public class SemMemoryManager {
    private byte[] memoryPool;
    private boolean[] isAllocated;
    private int poolSize;
    private int totalAllocatedSize;
    private int freeBlocksArraySize;
    private LinkedList<FreeBlock>[] freeBlocks;

    /**
     * constructor
     * 
     * @param poolSize
     *            integer value of the memory pool size (bytes)
     */
    @SuppressWarnings("unchecked")
    public SemMemoryManager(int poolSize) {
        this.poolSize = poolSize;
        memoryPool = new byte[poolSize];
        isAllocated = new boolean[poolSize];
        totalAllocatedSize = 0;
        // free blocks initial size
        freeBlocksArraySize = getIndexOfBlock(poolSize);

        freeBlocks = new LinkedList[freeBlocksArraySize + 1];

        // System.out.println("Free Block array length is: " +
        // freeBlocks.length);

        for (int i = 0; i <= freeBlocksArraySize; i++) {
            freeBlocks[i] = new LinkedList<>();
        }

        freeBlocks[freeBlocksArraySize].add(new FreeBlock(0, poolSize - 1));
    }


    /*********************************/

    /**
     * insert method
     * 
     * @param space
     *            byte[] the serialized version of seminar object as byte array
     * @param size
     *            the length of the seminar object, i.e., how many bytes in the
     *            object
     * @return Handle handle object that identifies the starting position in
     *         memory pool and the size of the handle
     */
    public Handle insert(byte[] space, int size) {
        // Check first if there is sufficient memory for new insertion after
        // calculating the required blockSize
        int blockSize = calculateBlockSize(size);

        while (poolSize < totalAllocatedSize + blockSize) {
            resizePool();
        }

        FreeBlock block = findFreeBlock(blockSize);

        allocateBlock(block, blockSize);

        System.arraycopy(space, 0, memoryPool, block.start, size);

        totalAllocatedSize += blockSize;

        return new Handle(block.start, size);
    }


    /*******************************/
    // Helper methods for insertion
    /*******************************/

    // method to get the index of a free block in the free blocks array
    private int getIndexOfBlock(int bSize) {
        return (int)(Math.log(bSize) / Math.log(2));
    }


    // resize pool
    /** Resize the pool size for insertion that needs more memory space */
    @SuppressWarnings("unchecked")
    public void resizePool() {
        int newPoolSize = poolSize * 2;
        byte[] newMemoryPool = new byte[newPoolSize];
        boolean[] newIsAllocated = new boolean[newPoolSize];

        // first copy the old free blocks to the new free blocks
        // then clear the freeBlocks list freeBlocks.clear();
        LinkedList<FreeBlock>[] oldFreeBlocks =
            new LinkedList[freeBlocks.length];

        for (int i = 0; i < freeBlocks.length; i++) {
            oldFreeBlocks[i] = new LinkedList<>();
            oldFreeBlocks[i].addAll(freeBlocks[i]);

            // clear the linked list in the free block cell
            freeBlocks[i].clear();
        }

        // free blocks new size
        int freeBlocksNewSize = getIndexOfBlock(newPoolSize);

        freeBlocks = new LinkedList[freeBlocksNewSize + 1];

        // initialize each cell in the free blocks array with an empty linked
        // list
        for (int k = 0; k <= freeBlocksNewSize; k++) {
            freeBlocks[k] = new LinkedList<>();
        }

        // copy back the old contents of free block lists before resizing it
        for (int j = 0; j < oldFreeBlocks.length; j++) {
            freeBlocks[j].addAll(oldFreeBlocks[j]);
            // clear all linked lists in the old free block cell
            oldFreeBlocks[j].clear();
        }
        // instantiate new object to be merged later
        FreeBlock newExpandedBlock = new FreeBlock(poolSize, newPoolSize - 1);

        // System.out.println("Free Block array length is: " +
        // freeBlocks.length);
        // Copy existing memory pool and allocation status
        System.arraycopy(memoryPool, 0, newMemoryPool, 0, poolSize);
        System.arraycopy(isAllocated, 0, newIsAllocated, 0, poolSize);

        // Update class variables
        memoryPool = newMemoryPool;
        isAllocated = newIsAllocated;
        poolSize = newPoolSize;
        freeBlocksArraySize = freeBlocksNewSize;

        // add the expanded memory block to the free block list
        // the new block size should exist in the earlier cell
        // System.out.println("The new expanded block size is: "
        // + newExpandedBlock.size);
        mergeFreeBlocks(newExpandedBlock);

        System.out.println("Memory pool expanded to " + poolSize + " bytes");
    }


    /**
     * to calculate the current total size of the memory pool
     * 
     * @return totalAllocatedSize integer value of the current total allocated
     *         size in the pool
     */
    public int calculateTotalAllocatedSize() {
        return totalAllocatedSize;
    }


    /**
     * calculate memory block size
     * 
     * @param size
     *            integer record size stored in the handle
     * 
     * @return blockSize
     *         integer value of the power of base two number
     */
    public int calculateBlockSize(int size) {
        int blockSize = 1;
        while (blockSize < size) {
            blockSize *= 2;
        }
        return blockSize;
    }


    // Method to find a free block of given size ... in this method we need to
    // pick up the best fit block that
    // has size is close to the requested size. Otherwise, split a larger block
    // if necessary
    private FreeBlock findFreeBlock(int recordBlockSize) {
        // System.out.println("First I entered here to find a free block");
        FreeBlock blockToBeReturned = null;
// System.out.println("The free block array lenght is: "
// + freeBlocks.length);
        for (int i = 0; i < freeBlocks.length; i++) {
// System.out.println("Did you enter the loop with: "
// + freeBlocks.length + " iterations?");
            if (!freeBlocks[i].isEmpty()) {
// System.out.println(
// "Did you check the empty condition at cell number: " + i
// + " in the free block list?"
// + "The element size in this cell is " + freeBlocks[i]
// .get(0).size);
                if (freeBlocks[i].get(0).size == recordBlockSize) {
// System.out.println(
// "I entered here to find perfect matched block size");
                    blockToBeReturned = freeBlocks[i].get(0);
                    break;
                }
                else if (freeBlocks[i].get(0).size > recordBlockSize) {
                    blockToBeReturned = splitBlock(freeBlocks[i].get(0),
                        recordBlockSize);
// System.out.println(
// "If perfect matched block size not found, I got a return from split block
// which is: "
// + blockToBeReturned);
// System.out.println(
// "The output block size of split block is: "
// + blockToBeReturned.size);
// this.dump();
                    break;
                }
                else {
                    // do nothing and continue with the loop
                }
            } // end of if (freeBlocks[i].size() > 0)

        } // end of the for loop

        return blockToBeReturned;
    }


    // Method to split a larger block into smaller ones that needs to be split
    // this method works only when the requested
    // block size is less than the passed block's size
    private FreeBlock splitBlock(FreeBlock block, int reqBlockSize) {
        boolean blockRemoved = false;
        // find the index of the current passed block
        // to remove it from the free blocks array
        // find the block and remove it
        for (int i = 0; i < freeBlocks.length; i++) {
            if (freeBlocks[i].contains(block)) {
                blockRemoved = freeBlocks[i].remove(block);
                break;
            }
        }
        // divide the large block into two blocks
        FreeBlock tempBlock = new FreeBlock(block.start, block.start
            + reqBlockSize - 1);
        int startOfNextBlock = tempBlock.end + 1;
        int bSize = reqBlockSize;
        while (bSize < block.size) {
            FreeBlock newBlock = new FreeBlock(startOfNextBlock,
                startOfNextBlock + bSize - 1);
            mergeFreeBlocks(newBlock);
            startOfNextBlock = newBlock.end + 1;
            bSize *= 2;
        }
        return tempBlock;
    }


    // Method to allocate a block
    private void allocateBlock(FreeBlock block, int blockSize) {
        if (block.size == blockSize) {
            // find the block and remove it from free blocks
            for (int i = 0; i < freeBlocks.length; i++) {
                if (freeBlocks[i].contains(block)) {
                    isAllocated[block.start] = freeBlocks[i].remove(block);
                    break;
                }
            }
        }

    }


    /*******************************/
    // Helper methods for removal
    /******************************/
    // Method to deallocate a block
    private void deallocateBlock(int start, int size) {
        int blockSize = calculateBlockSize(size);
        isAllocated[start] = false;

        // Add the deallocated block to free block
        FreeBlock block = new FreeBlock(start, start + blockSize - 1);

        // assume that we will add the block to free block at position zero
        mergeFreeBlocks(block);
    }


    private boolean isBuddy(FreeBlock blockA, FreeBlock blockB) {

        return (blockA.start | blockA.size) == (blockB.start | blockB.size);

    }


    private FreeBlock mergeTwoBlocks(FreeBlock blockA, FreeBlock blockB) {
        if (blockA.end + 1 == blockB.start) {
            return (new FreeBlock(blockA.start, blockB.end));
        }

        if (blockB.end + 1 == blockA.start) {
            return (new FreeBlock(blockB.start, blockA.end));
        }

        return null;
    }


    private FreeBlock theBuddyBlock(FreeBlock bloc) {
        // variable declarations
        int blocIndex = getIndexOfBlock(bloc.size);

        for (FreeBlock b : freeBlocks[blocIndex]) {
            if (isBuddy(b, bloc)) {
                // remove separate blocks and return the merged one
                freeBlocks[blocIndex].remove(b);
                freeBlocks[blocIndex].remove(bloc);
                // merge blocks
                return mergeTwoBlocks(b, bloc);
            }
        }
        return null;
    }


    // Method to merge adjacent free blocks
    private void mergeFreeBlocks(FreeBlock blk) {
        // variable declaration
        int indexOfTheBlock = getIndexOfBlock(blk.size);

        if (freeBlocks[indexOfTheBlock].isEmpty()) {
            freeBlocks[indexOfTheBlock].add(blk);
            return;
        }

        FreeBlock buddyMergedBlock = theBuddyBlock(blk);
        if (buddyMergedBlock != null) {
            mergeFreeBlocks(buddyMergedBlock);
        }
        // add the block to the freeBlocksArray and sort it based on start
        // values
        else {
            freeBlocks[indexOfTheBlock].add(blk);
            // then sort to make sure the first element is the first start
            Collections.sort(freeBlocks[indexOfTheBlock], (fb1, fb2) -> Integer
                .compare(fb1.start, fb2.start));
            return;
        }

    } // end of the mergeBlocks method


    /*********************************/

    /**
     * Remove the memory handle of a record
     * 
     * @param theHandle
     *            Handle object of a stored record in the memory pool
     */
    public void remove(Handle theHandle) {
        int deallocatedBlockSize = calculateBlockSize(theHandle.size);
        totalAllocatedSize -= deallocatedBlockSize;
        deallocateBlock(theHandle.start, theHandle.size);
    }

    /************************************************/


    /*********************/
    // Getter methods
    /********************/
    /**
     * Get the length of the memory handle
     * 
     * @param theHandle
     *            Handle object of a stored record in the memory pool
     * 
     * @return theHandle.size
     *         size of the memory handle for a record
     */
    public int length(Handle theHandle) {
        return theHandle.size;
    }


    /**
     * get the current memory pool size
     * 
     * @return the poolSize
     */
    public int getPoolSize() {
        return poolSize;
    }


    /**
     * Get the content of a handle for search method
     * 
     * @param space
     *            byte[] A byte array that is used to store a copy of stored
     *            bytes for a given memory handle of a record
     * 
     * @param theHandle
     *            Handle object the has the start position in the memory pool
     *            and the size of a record
     * 
     * @param size
     *            integer value of the handle size, i.e., how many bytes for a
     *            stored record in the memory
     *
     * @return bytesToCopy
     *         integer value of the size of array bytes that are
     *         copied from memory pool to the byte array space parameter
     */
    public int get(byte[] space, Handle theHandle, int size) {
        int bytesToCopy = Math.min(size, theHandle.size);
        System.arraycopy(memoryPool, theHandle.start, space, 0, bytesToCopy);
        return bytesToCopy;
    }


    /*********************************/

    /** printing the content of free block list in human readable format */
    public void dump() {
        System.out.println("Freeblock List:");
        // Print the sorted list
        // declare list of readings
        ArrayList<String> listOfReadings = new ArrayList<>();
        boolean isEmptyFreeBlocks = true;

        for (int i = 0; i < freeBlocks.length; i++) {
            if (!freeBlocks[i].isEmpty()) {
                if (freeBlocks[i].size() > 0) {
                    isEmptyFreeBlocks = false;
                    // sort the freeBlocks list based on its start value of each
                    // object
                    Collections.sort(freeBlocks[i], (fb1, fb2) -> Integer
                        .compare(fb1.start, fb2.start));
                    int l = (int)Math.pow(2, i);
                    String newReading = l + ":";
                    for (FreeBlock block : freeBlocks[i]) {
                        if (block.size == l) {
                            newReading += " " + block.start;
                        }
                    }
                    listOfReadings.add(newReading);
                }
            }
        }
        for (String newRead : listOfReadings) {
            System.out.println(newRead);
        }

        if (isEmptyFreeBlocks) {
            System.out.println("There are no freeblocks in the memory pool");
        }
    }

    /*********************************/

    /*********** Nested classes ***************/
    // private nested class FreeBlock
    private static class FreeBlock {
        private int start;
        private int end;
        private int size;

        FreeBlock(int start, int end) {
            this.start = start;
            this.end = end;
            this.size = end - start + 1;
        }
    } /// close nested class FreeBlock


    /**
     * This public nested class that keeps track of record handles
     * 
     * @author Sadath-Mohammed-msadath
     * @author Emadeldin-Abdrabou-emazied
     * 
     * @version 04-20-2024
     */
    public class Handle {
        private int start;
        private int size;

        /**
         * constructor
         * 
         * @param start
         *            integer value that refers to the start position of the
         *            handle stored in the memory block
         * @param size
         *            integer value of the handle size, i.e., size of byte array
         *            that store a record contents
         */
        Handle(int start, int size) {
            this.start = start;
            this.size = size;
        }
    } /// close nested class Handle

} /// close the outer class SemMemoryManager
/*********************************/
