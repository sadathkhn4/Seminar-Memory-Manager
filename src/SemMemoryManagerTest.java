import student.TestCase;
import java.util.Arrays;

/**
 * This class is used to perform testing on SemMemoryManager.java class
 *
 *
 * @author Sadath-Mohammed-msadath
 * @author Emadeldin-Abdrabou-emazied
 *
 * @version 05-01-20240
 */
public class SemMemoryManagerTest extends TestCase {
/** Test insert and remove simple test */
    public void testInsertAndRemove() {
        SemMemoryManager memoryManager = new SemMemoryManager(64);
        byte[] data = { 1, 2, 3, 4, 5 };
        SemMemoryManager.Handle handle = memoryManager.insert(data,
            data.length);
        assertEquals(8, memoryManager.calculateTotalAllocatedSize());
        memoryManager.remove(handle);
        assertEquals(0, memoryManager.calculateTotalAllocatedSize());
    }

    /** Test insert with resize pool and remove simple test */
    public void testInsertResizeAndRemove() {
        SemMemoryManager memoryManager = new SemMemoryManager(1);
        SemMemoryManager memoryManagerTwo = new SemMemoryManager(4096);
        byte[] data = { 1, 2, 3, 4, 5, 6, 7, 8 };
        byte[] dataTwo = {1};
        SemMemoryManager.Handle handle = memoryManager.insert(data,
            data.length);
        SemMemoryManager.Handle handleTwo = memoryManagerTwo.insert(dataTwo,
            dataTwo.length);
        assertEquals(8, memoryManager.calculateTotalAllocatedSize());
        memoryManager.remove(handle);
        assertEquals(0, memoryManager.calculateTotalAllocatedSize());

        assertEquals(1, memoryManagerTwo.calculateTotalAllocatedSize());
        memoryManager.remove(handleTwo);
        assertEquals(1, memoryManagerTwo.calculateTotalAllocatedSize());
    }

    /** Test get data retrieve */
    public void testGet() {
        SemMemoryManager memoryManager = new SemMemoryManager(128);
        byte[] data = { 1, 2, 3, 4, 5 };
        SemMemoryManager.Handle handle = memoryManager.insert(data,
            data.length);
        byte[] retrievedData = new byte[5];
        memoryManager.get(retrievedData, handle, retrievedData.length);
        assertTrue(Arrays.equals(data, retrievedData));
    }

    /** Test pool expansion */
    public void testPoolExpansion() {
        SemMemoryManager memoryManager = new SemMemoryManager(4);
        byte[] data = { 1, 2, 3, 4, 5, 6, 7, 8 };
        SemMemoryManager.Handle handle = memoryManager.insert(data,
            data.length);
        assertEquals(8, memoryManager.calculateTotalAllocatedSize());
        assertEquals(8, memoryManager.getPoolSize());
    }


    /** Test calculate block size */
    public void testCalculateBlockSize() {
        SemMemoryManager memoryManager = new SemMemoryManager(64);

        // Test a size that requires no rounding up
        int blockSize1 = memoryManager.calculateBlockSize(4);
        assertEquals(4, blockSize1);

        // Test a size that requires rounding up to the next power of 2
        int blockSize2 = memoryManager.calculateBlockSize(5);
        assertEquals(8, blockSize2);

    }

}
