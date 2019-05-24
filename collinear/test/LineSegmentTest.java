import org.junit.Test;
import static org.junit.Assert.*;

public class LineSegmentTest {

    @Test
    public void testBruteForce() {
        Point p1 = new Point(15, 20);
        Point p2 = new Point(17, 20);
        Point p3 = new Point(15, 25);
        Point p4 = new Point(20,25);
        Point p5 = new Point(10,15);
        Point p6 = new Point(5, 10);
        Point[] pointList = {p1, p2, p3, p4, p5, p6};

        BruteCollinearPoints tester = new BruteCollinearPoints(pointList);
    }

}
