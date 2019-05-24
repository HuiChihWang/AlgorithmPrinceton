import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class PointTest {
    @Test
    public void testPointCompareTo() {
        Point p1 = new Point(15, 20);
        Point p2 = new Point(15, 20);
        Point p3 = new Point(13, 22);
        Point p4 = new Point(25,18);
        Point p5 = new Point(7,22);

        assertEquals(1, p1.compareTo(p4));
        assertEquals(0, p1.compareTo(p2));
        assertEquals(-1, p4.compareTo(p3));
        assertEquals(-1, p5.compareTo(p3));
    }

    @Test
    public void testSortPoint() {
        Point p1 = new Point(15, 20);
        Point p2 = new Point(10, 20);
        Point p3 = new Point(15, 12);
        Point p4 = new Point(25,37);
        Point p5 = new Point(10,18);

        Point[] pointSet = { p1, p2, p3, p4, p5 };
        Arrays.sort(pointSet);

        Point[] pointSortExpect = {p3, p5, p2, p1, p4};
        assertArrayEquals(pointSortExpect, pointSet);

    }

    @Test
    public void testSlope() {
        Point p1 = new Point(15, 20);
        Point p2 = new Point(15, 20);
        Point p3 = new Point(15, 25);
        Point p4 = new Point(25,30);
        Point p5 = new Point(7,20);

        assertEquals(Double.NEGATIVE_INFINITY, p1.slopeTo(p2), 0.0001);
        assertEquals(Double.POSITIVE_INFINITY, p2.slopeTo(p3), 0.0001);
        assertEquals(0.5, p3.slopeTo(p4), 0.0001);
        assertEquals(0.5, p4.slopeTo(p3), 0.0001);
        assertEquals(0., p5.slopeTo(p2), 0.0001);
    }

    @Test
    public void testComparator() {
        Point p1 = new Point(15, 20);
        Point p2 = new Point(17, 20);
        Point p4 = new Point(20,25);
        Point p5 = new Point(10,15);

        assertEquals(0, p1.slopeOrder().compare(p4, p5));
        assertEquals(0, p4.slopeOrder().compare(p1, p5));
        assertEquals(0, p5.slopeOrder().compare(p4, p1));

        assertEquals(1, p1.slopeOrder().compare(p4, p2));
        assertEquals(-1, p4.slopeOrder().compare(p1, p2));

        assertEquals(0, p1.slopeOrder().compare(p1, p1));
    }





}