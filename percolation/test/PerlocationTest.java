import static org.junit.Assert.*;
import org.junit.Test;

public class PerlocationTest {
    private PercolationFactory createPerlocation = new PercolationFactory();

    /* test initial state*/
    @Test
    public void testPerlocationInitialize(){
        Percolation pTester = createPerlocation.make(5);

        assertFalse(pTester.isOpen(3,4));
        assertFalse(pTester.isOpen(2,2));
        assertFalse(pTester.isFull(0,1));
    }

    @Test
    public void testPerlocationOpen(){
        Percolation pTester = createPerlocation.make(10);

        pTester.open(5,5);
        pTester.open(5,6);
        assertTrue(pTester.isOpen(5,5));
        assertTrue(pTester.isOpen(5,6));

        pTester.open(0,1);
        assertTrue(pTester.isOpen(0,1));
        assertTrue(pTester.isFull(0,1));

        assertFalse(pTester.isFull(0,0));

        pTester.open(0,1);
        pTester.open(5,5);
        pTester.open(4,4);
        assertEquals(4,pTester.numberOfOpenSites());

    }

    @Test
    public void testPerlocate(){
        Percolation pTester = createPerlocation.make(4);

        pTester.open(1,1);
        assertFalse(pTester.percolates());

        pTester.open(0,0);
        pTester.open(0,1);
        assertFalse(pTester.percolates());

        pTester.open(0,2);
        pTester.open(2,1);
        pTester.open(3,1);
        assertTrue(pTester.percolates());

        pTester.open(3,3);
        assertTrue(pTester.isOpen(3,3));
        assertFalse(pTester.isFull(3,3));

    }

    @Test
    public void testPercolateAdvance(){
        Percolation pTester = createPerlocation.make(4);
        pTester.open(0,1);
        pTester.open(3,1);
        pTester.open(2,1);
        pTester.open(1,1);

        assertTrue(pTester.percolates());
    }

    @Test
    public void testPerlocateNEqualtoOne(){
        Percolation pTester = createPerlocation.make(1);
        assertFalse(pTester.percolates());
        pTester.open(0,0);
        assertTrue(pTester.percolates());
    }
}
