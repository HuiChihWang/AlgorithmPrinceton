import edu.princeton.cs.algs4.RectHV;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdRandom;

public class KDTreeTest {

    private PointSET expect;
    private KdTree actual;

    private RectHV getRandomRect() {
        double xmin = StdRandom.uniform();
        double ymin = StdRandom.uniform();
        double xmax = StdRandom.uniform();
        double ymax = StdRandom.uniform();
        while (xmax <= xmin || ymax <= ymin) {
            xmax = StdRandom.uniform();
            ymax = StdRandom.uniform();
        }

        return new RectHV(xmin, ymin, xmax, ymax);
    }

    @Before
    public void init() {
        int testNum = 1000;
        expect = new PointSET();
        actual = new KdTree();

        for (int i = 0; i < testNum; ++ i) {
            Point2D p = new Point2D(StdRandom.uniform(), StdRandom.uniform());
            expect.insert(p);
            actual.insert(p);
        }
    }

    @Test
    public void testRange() {
        RectHV rect = getRandomRect();
        assertEquals(expect.range(rect), actual.range(rect));
    }
}
