import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class KdTree {
    private KDTreeNode root;
    private int size;

    public KdTree() {
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D point) {
        checkObjectNull(point);

        if (contains(point)) {
            return;
        }

        if (isEmpty()) {
            RectHV rect = new RectHV(0., 0., 1., 1.);
            root = new KDTreeNode(point, true, rect);
            size += 1;
            return;
        }

        KDTreeNode start = root;
        KDTreeNode prev = null;
        boolean fromLeft = false;

        while (start != null) {
            prev = start;

            if (start.goLeft(point)) {
                start = start.left;
                fromLeft = true;
            } else {
                start = start.right;
                fromLeft = false;
            }
        }

        if (fromLeft) {
            prev.left = new KDTreeNode(point, !prev.isCompareX, prev.getLowerRegion());
        } else {
            prev.right = new KDTreeNode(point, !prev.isCompareX, prev.getUpperRegion());
        }

        size += 1;
    }

    public boolean contains(Point2D point) {
        checkObjectNull(point);
        KDTreeNode start = root;

        while (start != null) {
            if (start.point.equals(point)) {
                return true;
            }

            if (start.goLeft(point)) {
                start = start.left;
            } else {
                start = start.right;
            }
        }

        return false;
    }

    public void draw() {
        drawPointsPreOrder(root);
    }

    public Iterable<Point2D> range(RectHV rect) {
        checkObjectNull(rect);
        SET<Point2D> pointsInRange = new SET<>();
        searchPointInRange(root, rect, pointsInRange);
        return pointsInRange;
    }

    public Point2D nearest(Point2D point) {
        checkObjectNull(point);

        if (isEmpty()) {
            return null;
        }

        return searchNearestPoint(root, point, root.point);
    }

    private void searchPointInRange(KDTreeNode currentNode, RectHV rect, SET<Point2D> pInRange) {
        if (currentNode == null) {
            return;
        }

        if (!currentNode.intersect(rect)) {
            return;
        }

        if (rect.contains(currentNode.point)) {
            pInRange.add(currentNode.point);
        }

        searchPointInRange(currentNode.left, rect, pInRange);
        searchPointInRange(currentNode.right, rect, pInRange);
    }

    private Point2D searchNearestPoint(KDTreeNode currentNode, Point2D input, Point2D currentNear) {

        if (currentNode == null) {
            return currentNear;
        }

        if (currentNode.axisRect.distanceSquaredTo(input) >= currentNear.distanceSquaredTo(input)) {
            return currentNear;
        }

        if (currentNode.point.distanceSquaredTo(input) < currentNear.distanceSquaredTo(input)) {
            currentNear = currentNode.point;
        }

        if (currentNode.goLeft(input)) {
            currentNear = searchNearestPoint(currentNode.left, input, currentNear);
            currentNear = searchNearestPoint(currentNode.right, input, currentNear);
        } else {
            currentNear = searchNearestPoint(currentNode.right, input, currentNear);
            currentNear = searchNearestPoint(currentNode.left, input, currentNear);
        }

        return currentNear;
    }

    private void checkObjectNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    private void drawPointsPreOrder(KDTreeNode current) {
        if (current == null) {
            return;
        }

        current.point.draw();
        drawPointsPreOrder(current.left);
        drawPointsPreOrder(current.right);
    }


    private static class KDTreeNode {

        Point2D point;
        KDTreeNode left;
        KDTreeNode right;
        RectHV axisRect;
        boolean isCompareX;


        KDTreeNode(Point2D point, boolean isCompareX, RectHV rect) {
            this.point = point;
            this.isCompareX = isCompareX;
            this.axisRect = rect;
        }

        boolean goLeft(Point2D pointInput) {
            if (isCompareX) {
                return Point2D.X_ORDER.compare(pointInput, this.point) < 0;
            }

            return Point2D.Y_ORDER.compare(pointInput, this.point) < 0;
        }

        RectHV getUpperRegion() {
            if (isCompareX) {
                return new RectHV(point.x(), axisRect.ymin(), axisRect.xmax(), axisRect.ymax());
            }

            return new RectHV(axisRect.xmin(), point.y(), axisRect.xmax(), axisRect.ymax());
        }

        RectHV getLowerRegion() {
            if (isCompareX) {
                return new RectHV(axisRect.xmin(), axisRect.ymin(), point.x(), axisRect.ymax());
            }

            return new RectHV(axisRect.xmin(), axisRect.ymin(), axisRect.xmax(), point.y());
        }

        boolean intersect(RectHV rect) {
            return axisRect.intersects(rect);
        }
    }
}
