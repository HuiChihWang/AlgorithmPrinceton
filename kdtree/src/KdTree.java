import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

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
            root = new KDTreeNode(point, true);
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
            prev.left = new KDTreeNode(point, !prev.isCompareX);
        } else {
            prev.right = new KDTreeNode(point, !prev.isCompareX);
        }

        size += 1;
    }

    public boolean contains(Point2D point) {
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
        return null;
    }

    private void searchPointInRange(KDTreeNode currentNode, RectHV rect, SET<Point2D> pInRange) {
        if (currentNode == null) {
            return;
        }

        if (rect.contains(currentNode.point)) {
            pInRange.add(currentNode.point);
        }

        if (currentNode.findLeft(rect)) {
            searchPointInRange(currentNode.left, rect, pInRange);
        }

        if (currentNode.findRight(rect)) {
            searchPointInRange(currentNode.right, rect, pInRange);
        }

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


        KDTreeNode(Point2D point, boolean isCompareX) {
            this.point = point;
            this.isCompareX = isCompareX;
        }

        boolean goLeft(Point2D pointInput) {
            if (isCompareX) {
                return Point2D.X_ORDER.compare(pointInput, this.point) < 0;
            }

            return Point2D.Y_ORDER.compare(pointInput, this.point) < 0;
        }

        boolean findRight(RectHV rect) {
            return isCompareX ? rect.xmax() > point.x() : rect.ymax() > point.y();
        }

        boolean findLeft(RectHV rect) {
            return isCompareX ? rect.xmin() < point.x() : rect.ymin() < point.y();
        }
    }
}
