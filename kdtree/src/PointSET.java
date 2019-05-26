import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {

    private SET<Point2D> pointSet;

    public PointSET() {
        pointSet = new SET<>();
    }

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    public int size() {
        return pointSet.size();
    }

    public void insert(Point2D point) {
        pointSet.add(point);
    }

    public boolean contains(Point2D point) {
        return pointSet.contains(point);
    }

    public void draw() {
        for (Point2D point: pointSet) {
            point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkObjectNull(rect);

        SET<Point2D> pointsInRectangle = new SET<>();

        for (Point2D pointInSet: pointSet) {
            if (rect.contains(pointInSet)) {
                pointsInRectangle.add(pointInSet);
            }
        }
        return pointsInRectangle;
    }

    public Point2D nearest(Point2D point) {
        checkObjectNull(point);

        double distMin = Double.POSITIVE_INFINITY;
        Point2D pointNearest = null;
        for (Point2D pointInSet: pointSet) {
            if (pointInSet.distanceSquaredTo(point) < distMin) {
                pointNearest = pointInSet;
                distMin = pointInSet.distanceSquaredTo(point);
            }
        }

        return pointNearest;
    }

    private void checkObjectNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }
}
